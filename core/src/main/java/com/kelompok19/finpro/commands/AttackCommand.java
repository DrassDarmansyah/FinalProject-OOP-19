package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.combat.CombatEngine;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.units.Unit;

public class AttackCommand implements Command {
    private final Unit attacker;
    private final Unit defender;
    private final GameMap map;

    public AttackCommand(Unit attacker, Unit defender, GameMap map) {
        this.attacker = attacker;
        this.defender = defender;
        this.map = map;
    }

    @Override
    public void execute() {
        System.out.println("COMBAT START");

        int hitRate = CombatEngine.calculateHitRate(attacker, defender, map);
        int critRate = CombatEngine.calculateCritRate(attacker);
        int damage = CombatEngine.calculateDamage(attacker, defender, map);
        int followUp = CombatEngine.determineFollowUp(attacker, defender);

        performStrike(attacker, defender, hitRate, critRate, damage);

        if (defender.getCurrentHp() <= 0) {
            handleDeath(defender);
            return;
        }

        Weapon dWeapon = defender.getWeapon();
        int dist = Math.abs(attacker.getX() - defender.getX()) + Math.abs(attacker.getY() - defender.getY());

        boolean canCounter = dWeapon != null && dist >= dWeapon.rangeMin && dist <= dWeapon.rangeMax;

        if (canCounter) {
            int dHit = CombatEngine.calculateHitRate(defender, attacker, map);
            int dCrit = CombatEngine.calculateCritRate(defender);
            int dDmg = CombatEngine.calculateDamage(defender, attacker, map);

            performStrike(defender, attacker, dHit, dCrit, dDmg);
        }

        if (attacker.getCurrentHp() <= 0) {
            handleDeath(attacker);
            return;
        }

        if (followUp == 1) {
            System.out.println(attacker.getName() + " attacks again!");
            performStrike(attacker, defender, hitRate, critRate, damage);
        }

        else if (followUp == 2 && canCounter) {
            System.out.println(defender.getName() + " attacks again!");

            int dHit = CombatEngine.calculateHitRate(defender, attacker, map);
            int dCrit = CombatEngine.calculateCritRate(defender);
            int dDmg = CombatEngine.calculateDamage(defender, attacker, map);

            performStrike(defender, attacker, dHit, dCrit, dDmg);
        }

        if (defender.getCurrentHp() <= 0) handleDeath(defender);
        if (attacker.getCurrentHp() <= 0) handleDeath(attacker);

        System.out.println("COMBAT END");
    }

    private void performStrike(Unit source, Unit target, int hit, int crit, int dmg) {
        int roll = (int)(Math.random() * 100);
        int critRoll = (int)(Math.random() * 100);

        if (roll < hit) {
            if (critRoll < crit) {
                dmg *= 3;
                System.out.println("CRITICAL HIT!");
            }

            target.takeDamage(dmg);
            System.out.println(source.getName() + " hits " + target.getName() + " for " + dmg + " damage.");
        } else {
            System.out.println(source.getName() + " missed " + target.getName() + "!");
        }
    }

    private void handleDeath(Unit unit) {
        System.out.println(unit.getName() + " has been defeated.");
    }
}
