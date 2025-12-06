package com.kelompok19.finpro.combat;

import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.units.Skill;
import com.kelompok19.finpro.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class CombatSimulator {
    public static List<CombatStep> simulate(Unit attacker, Unit defender, BattleContext context) {
        List<CombatStep> steps = new ArrayList<>();

        CombatPreview stats = CombatEngine.calculateCombat(attacker, defender, context);
        int dist = Math.abs(attacker.getX() - defender.getX()) + Math.abs(attacker.getY() - defender.getY());

        int currentAttackerHp = attacker.getCurrentHp();
        int currentDefenderHp = defender.getCurrentHp();

        simulateStrike(attacker, defender, stats.aHit, stats.aCrit, stats.aDmg, steps);

        CombatStep lastStep = steps.get(steps.size() - 1);
        if (lastStep.type == CombatStep.Type.ATTACK) {
            currentDefenderHp -= lastStep.damage;

            if (currentDefenderHp > 0 && dist == 1 && defender.hasSkill(Skill.MONTY_REFLECT)) {
                int reflectDmg = lastStep.damage / 2;

                if (reflectDmg > 0) {
                    steps.add(new CombatStep(CombatStep.Type.REFLECT, defender, attacker, reflectDmg, false));
                    currentAttackerHp -= reflectDmg;
                }
            }
        }

        if (currentDefenderHp <= 0) {
            steps.add(new CombatStep(CombatStep.Type.DIE, defender, defender, 0, false));
            return steps;
        }
        if (currentAttackerHp <= 0) {
            steps.add(new CombatStep(CombatStep.Type.DIE, attacker, attacker, 0, false));
            return steps;
        }

        if (stats.defenderCanCounter) {
            simulateStrike(defender, attacker, stats.dHit, stats.dCrit, stats.dDmg, steps);

            lastStep = steps.get(steps.size() - 1);

            if (lastStep.type == CombatStep.Type.ATTACK) {
                currentAttackerHp -= lastStep.damage;

                if (currentAttackerHp > 0 && dist == 1 && attacker.hasSkill(Skill.MONTY_REFLECT)) {
                    int reflectDmg = lastStep.damage / 2;

                    if (reflectDmg > 0) {
                        steps.add(new CombatStep(CombatStep.Type.REFLECT, attacker, defender, reflectDmg, false));
                        currentDefenderHp -= reflectDmg;
                    }
                }
            }
        }

        if (currentAttackerHp <= 0) {
            steps.add(new CombatStep(CombatStep.Type.DIE, attacker, attacker, 0, false));
            return steps;
        }

        if (currentDefenderHp <= 0) {
            steps.add(new CombatStep(CombatStep.Type.DIE, defender, defender, 0, false));
            return steps;
        }

        if (stats.aHits == 2) {
            simulateStrike(attacker, defender, stats.aHit, stats.aCrit, stats.aDmg, steps);

            if (steps.get(steps.size()-1).type == CombatStep.Type.ATTACK) {
                currentDefenderHp -= steps.get(steps.size()-1).damage;
            }
        }

        else if (stats.dHits == 2) {
            simulateStrike(defender, attacker, stats.dHit, stats.dCrit, stats.dDmg, steps);

            if (steps.get(steps.size()-1).type == CombatStep.Type.ATTACK) {
                currentAttackerHp -= steps.get(steps.size()-1).damage;
            }
        }

        if (currentDefenderHp <= 0) {
            steps.add(new CombatStep(CombatStep.Type.DIE, defender, defender, 0, false));
        }

        if (currentAttackerHp <= 0) {
            steps.add(new CombatStep(CombatStep.Type.DIE, attacker, attacker, 0, false));
        }

        return steps;
    }

    private static void simulateStrike(Unit source, Unit target, int hit, int crit, int dmg, List<CombatStep> steps) {
        int roll = (int)(Math.random() * 100);
        int critRoll = (int)(Math.random() * 100);

        if (roll < hit) {
            boolean isCrit = (critRoll < crit);
            int finalDmg = isCrit ? dmg * 3 : dmg;
            steps.add(new CombatStep(CombatStep.Type.ATTACK, source, target, finalDmg, isCrit));
        }

        else {
            steps.add(new CombatStep(CombatStep.Type.MISS, source, target, 0, false));
        }
    }
}
