package com.kelompok19.finpro.combat;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.units.Stats;
import com.kelompok19.finpro.units.Unit;

public class CombatEngine {

    private static final int DOUBLING_THRESHOLD = 5;
    private static final double DEX_MULTIPLIER = 1.5;
    private static final double LUCK_MULTIPLIER = 0.5;

    public static CombatPreview calculateCombat(Unit attacker, Unit defender, GameMap map) {
        CombatPreview preview = new CombatPreview();

        preview.attackerName = attacker.getName();
        preview.aHp = attacker.getCurrentHp();
        preview.aMaxHp = attacker.getStats().hp;

        preview.defenderName = defender.getName();
        preview.dHp = defender.getCurrentHp();
        preview.dMaxHp = defender.getStats().hp;

        preview.aDmg = calculateDamage(attacker, defender, map);
        preview.aHit = calculateHitRate(attacker, defender, map);
        preview.aCrit = calculateCritRate(attacker);

        int followUp = determineFollowUp(attacker, defender);
        preview.aHits = (followUp == 1) ? 2 : 1;

        Weapon dWeapon = defender.getWeapon();
        int dist = Math.abs(attacker.getX() - defender.getX()) + Math.abs(attacker.getY() - defender.getY());

        if (dWeapon != null && dist >= dWeapon.rangeMin && dist <= dWeapon.rangeMax) {
            preview.defenderCanCounter = true;
            preview.dDmg = calculateDamage(defender, attacker, map);
            preview.dHit = calculateHitRate(defender, attacker, map);
            preview.dCrit = calculateCritRate(defender);
            preview.dHits = (followUp == 2) ? 2 : 1;
        } else {
            preview.defenderCanCounter = false;
        }

        return preview;
    }

    public static int determineFollowUp(Unit a, Unit b) {
        if (a.getStats().speed >= b.getStats().speed + DOUBLING_THRESHOLD) return 1;
        if (b.getStats().speed >= a.getStats().speed + DOUBLING_THRESHOLD) return 2;
        return 0;
    }

    public static int calculateHitRate(Unit attacker, Unit defender, GameMap map) {
        Stats aStats = attacker.getStats();
        Weapon aWeapon = attacker.getWeapon();
        Stats dStats = defender.getStats();
        Weapon dWeapon = defender.getWeapon();

        double attackerHit = (aStats.dexterity * DEX_MULTIPLIER) + (aStats.luck * LUCK_MULTIPLIER) + aWeapon.hit;
        int defenderAvoid = dStats.speed + (dWeapon != null ? dWeapon.avoid : 0);

        if (map != null) {
            TerrainType t = map.getTile(defender.getX(), defender.getY()).getType();
            defenderAvoid += t.avoid;
        }

        return Math.max(0, Math.min(100, (int) Math.round(attackerHit) - defenderAvoid));
    }

    public static int calculateCritRate(Unit attacker) {
        Stats aStats = attacker.getStats();
        Weapon aWeapon = attacker.getWeapon();
        int critFromDex = (int) Math.floor((aStats.dexterity - 4) / 2.0);
        return Math.max(0, aWeapon.crit + critFromDex);
    }

    public static int calculateDamage(Unit attacker, Unit defender, GameMap map) {
        Stats aStats = attacker.getStats();
        Weapon aWeapon = attacker.getWeapon();
        Stats dStats = defender.getStats();

        int attackPower = aStats.strength + aWeapon.might;
        int defense = dStats.defense;

        if (map != null) {
            TerrainType t = map.getTile(defender.getX(), defender.getY()).getType();
            defense += t.defense;
        }

        return Math.max(0, attackPower - defense);
    }
}
