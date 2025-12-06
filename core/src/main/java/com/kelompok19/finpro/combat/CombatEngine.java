package com.kelompok19.finpro.combat;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.units.Skill;
import com.kelompok19.finpro.units.Stats;
import com.kelompok19.finpro.units.Unit;

public class CombatEngine {
    private static final int DOUBLING_THRESHOLD = 5;
    private static final double DEX_MULTIPLIER = 1.5;
    private static final double LUCK_MULTIPLIER = 0.5;

    public static CombatPreview calculateCombat(Unit attacker, Unit defender, BattleContext context) {
        CombatPreview preview = new CombatPreview();

        Skill.CombatBonusContainer aBonuses = getSkillBonuses(attacker, defender, context);
        Skill.CombatBonusContainer dBonuses = getSkillBonuses(defender, attacker, context);

        preview.attackerName = attacker.getName();
        preview.aHp = attacker.getCurrentHp();
        preview.aMaxHp = attacker.getStats().hp;

        preview.defenderName = defender.getName();
        preview.dHp = defender.getCurrentHp();
        preview.dMaxHp = defender.getStats().hp;

        int aBaseDmg = calculateBaseDamage(attacker, defender, context.map);
        preview.aDmg = Math.max(0, aBaseDmg + aBonuses.damageDealt + dBonuses.damageTaken);

        int aBaseHit = calculateBaseHit(attacker, defender, context.map);
        preview.aHit = Math.max(0, Math.min(100, aBaseHit + aBonuses.hit - dBonuses.avoid));

        int aBaseCrit = calculateBaseCrit(attacker);

        int dBaseDodge = calculateBaseDodge(defender);
        preview.aCrit = Math.max(0, aBaseCrit + aBonuses.crit - dBaseDodge - dBonuses.dodge);

        int followUp = determineFollowUp(attacker, defender);
        preview.aHits = (followUp == 1) ? 2 : 1;

        Weapon dWeapon = defender.getWeapon();
        int dist = Math.abs(attacker.getX() - defender.getX()) + Math.abs(attacker.getY() - defender.getY());

        if (dWeapon != null && dist >= dWeapon.rangeMin && dist <= dWeapon.rangeMax) {
            preview.defenderCanCounter = true;

            int dBaseDmg = calculateBaseDamage(defender, attacker, context.map);
            preview.dDmg = Math.max(0, dBaseDmg + dBonuses.damageDealt + aBonuses.damageTaken);

            int dBaseHit = calculateBaseHit(defender, attacker, context.map);
            preview.dHit = Math.max(0, Math.min(100, dBaseHit + dBonuses.hit - aBonuses.avoid));

            int dBaseCrit = calculateBaseCrit(defender);
            int aBaseDodge = calculateBaseDodge(attacker);
            preview.dCrit = Math.max(0, dBaseCrit + dBonuses.crit - aBaseDodge - aBonuses.dodge);

            preview.dHits = (followUp == 2) ? 2 : 1;
        }

        else {
            preview.defenderCanCounter = false;
        }

        return preview;
    }

    private static Skill.CombatBonusContainer getSkillBonuses(Unit me, Unit enemy, BattleContext context) {
        Skill.CombatBonusContainer bonuses = new Skill.CombatBonusContainer();

        for (Unit u : context.unitManager.getAllUnits()) {
            if (u == me) continue;

            int dist = Math.abs(me.getX() - u.getX()) + Math.abs(me.getY() - u.getY());

            for (Skill s : u.getSkills()) {
                bonuses.damageDealt += s.getAuraDamageDealt(u, me, dist);
                bonuses.damageTaken += s.getAuraDamageTaken(u, me, dist);
                bonuses.avoid += s.getAuraAvoid(u, me, dist);
                bonuses.dodge += s.getAuraDodge(u, me, dist);
            }
        }

        for (Skill s : me.getSkills()) {
            s.applySelfBonuses(me, enemy, context, bonuses);
        }

        return bonuses;
    }

    public static int determineFollowUp(Unit a, Unit b) {
        int speedA = a.getEffectiveStats().speed;
        int speedB = b.getEffectiveStats().speed;

        if (speedA >= speedB + DOUBLING_THRESHOLD) {
            return 1;
        }

        if (speedB >= speedA + DOUBLING_THRESHOLD) {
            return 2;
        }

        return 0;
    }

    private static int calculateBaseHit(Unit attacker, Unit defender, GameMap map) {
        Stats aStats = attacker.getEffectiveStats();
        Weapon aWeapon = attacker.getWeapon();
        double attackerHit = (aStats.dexterity * DEX_MULTIPLIER) + (aStats.luck * LUCK_MULTIPLIER) + aWeapon.hit;
        int defenderAvoid = calculateBaseAvoid(defender, map);
        return Math.max(0, Math.min(100, (int) Math.round(attackerHit) - defenderAvoid));
    }

    public static int calculateBaseAvoid(Unit unit, GameMap map) {
        Stats stats = unit.getEffectiveStats();
        Weapon w = unit.getWeapon();
        int baseAvoid = ((stats.speed * 3) + stats.luck) / 2;
        int weaponAvoid = (w != null) ? w.avoid : 0;
        int terrainAvoid = (map != null) ? map.getTile(unit.getX(), unit.getY()).getType().avoid : 0;
        return baseAvoid + weaponAvoid + terrainAvoid;
    }

    public static int calculateBaseDodge(Unit unit) {
        Stats stats = unit.getEffectiveStats();
        Weapon w = unit.getWeapon();
        return (stats.luck / 2) + (w != null ? w.dodge : 0);
    }

    public static int calculateBaseCrit(Unit attacker) {
        Stats aStats = attacker.getEffectiveStats();
        Weapon aWeapon = attacker.getWeapon();
        int critFromDex = (int) Math.floor((aStats.dexterity - 4) / 2.0);
        return Math.max(0, aWeapon.crit + critFromDex);
    }

    private static int calculateBaseDamage(Unit attacker, Unit defender, GameMap map) {
        Stats aStats = attacker.getEffectiveStats();
        Weapon aWeapon = attacker.getWeapon();
        Stats dStats = defender.getEffectiveStats();

        int attackStat = Math.max(aStats.strength, aStats.magic);
        int attackPower = attackStat + aWeapon.might;

        int defenseStat = (aStats.magic > aStats.strength) ? dStats.resistance : dStats.defense;

        if (map != null) {
            defenseStat += map.getTile(defender.getX(), defender.getY()).getType().defense;
        }

        return Math.max(0, attackPower - defenseStat);
    }
}
