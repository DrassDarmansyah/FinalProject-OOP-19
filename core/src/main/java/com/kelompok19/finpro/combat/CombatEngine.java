package com.kelompok19.finpro.combat;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.units.Stats;
import com.kelompok19.finpro.units.Unit;

public class CombatEngine {

    private static final int DOUBLING_THRESHOLD = 5;
    private static final double DEX_MULTIPLIER = 1.5;
    private static final double LUCK_MULTIPLIER = 0.5;

    public static CombatPreview calculateCombat(Unit attacker, Unit defender) {
        CombatPreview preview = new CombatPreview();

        preview.attackerName = attacker.getType().toString(); // Or .getName() if you added that
        preview.aHp = attacker.getCurrentHp();
        preview.aMaxHp = attacker.getStats().hp;

        preview.defenderName = defender.getType().toString();
        preview.dHp = defender.getCurrentHp();
        preview.dMaxHp = defender.getStats().hp;

        preview.aDmg = calculateDamage(attacker, defender);
        preview.aHit = calculateHitRate(attacker, defender);
        preview.aCrit = calculateCritRate(attacker);

        int followUp = determineFollowUp(attacker, defender);

        preview.aHits = (followUp == 1) ? 2 : 1;

        Weapon dWeapon = defender.getWeapon();
        int dist = Math.abs(attacker.getX() - defender.getX()) + Math.abs(attacker.getY() - defender.getY());

        if (dWeapon != null && dist >= dWeapon.rangeMin && dist <= dWeapon.rangeMax) {
            preview.defenderCanCounter = true;

            preview.dDmg = calculateDamage(defender, attacker);
            preview.dHit = calculateHitRate(defender, attacker);
            preview.dCrit = calculateCritRate(defender);

            preview.dHits = (followUp == 2) ? 2 : 1;
        } else {
            preview.defenderCanCounter = false;
            preview.dDmg = 0;
            preview.dHits = 0;
            preview.dHit = 0;
            preview.dCrit = 0;
        }

        return preview;
    }

    public static int determineFollowUp(Unit a, Unit b) {
        int speedA = a.getStats().speed;
        int speedB = b.getStats().speed;

        if (speedA >= speedB + DOUBLING_THRESHOLD) return 1;
        if (speedB >= speedA + DOUBLING_THRESHOLD) return 2;
        return 0;
    }

    public static int calculateHitRate(Unit attacker, Unit defender) {
        Stats aStats = attacker.getStats();
        Weapon aWeapon = attacker.getWeapon();
        Stats dStats = defender.getStats();
        Weapon dWeapon = defender.getWeapon();

        double attackerHit = (aStats.dexterity * DEX_MULTIPLIER) + (aStats.luck * LUCK_MULTIPLIER) + aWeapon.hit;
        int defenderAvoid = dStats.speed + (dWeapon != null ? dWeapon.avoid : 0);

        return Math.max(0, Math.min(100, (int) Math.round(attackerHit) - defenderAvoid));
    }

    public static int calculateCritRate(Unit attacker) {
        Stats aStats = attacker.getStats();
        Weapon aWeapon = attacker.getWeapon();
        int critFromDex = (int) Math.floor((aStats.dexterity - 4) / 2.0);
        return Math.max(0, aWeapon.crit + critFromDex);
    }

    public static int calculateDamage(Unit attacker, Unit defender) {
        Stats aStats = attacker.getStats();
        Weapon aWeapon = attacker.getWeapon();
        Stats dStats = defender.getStats();

        int attackPower = aStats.strength + aWeapon.might;
        int defense = dStats.defense;

        return Math.max(0, attackPower - defense);
    }
}
