package com.kelompok19.finpro.factories;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.units.*;

public class UnitFactory {
    public static Unit createPlayer(String name, UnitJob job, Stats specificStats, Weapon weapon) {
        Unit u = new Unit(name, 0, 0, UnitType.PLAYER, job, specificStats, weapon);

        switch (name) {
            case "Diana":
                u.addSkill(Skill.DIANA_SKILL);
                break;

            case "Jade":
                u.addSkill(Skill.JADE_RALLY);
                break;

            case "Mason":
                u.addSkill(Skill.MASON_SKILL);
                break;

            case "Drake":
                u.addSkill(Skill.DRAKE_SKILL);
                break;

            case "Leon":
                u.addSkill(Skill.LEON_SKILL);
                break;

            case "Ben":
                u.addSkill(Skill.BEN_AURA);
                break;

            case "Monty":
                u.addSkill(Skill.MONTY_REFLECT);
                break;

            case "Mark":
                u.addSkill(Skill.MARK_SKILL);
                break;

            case "Harold":
                u.addSkill(Skill.HAROLD_AURA);
                break;

            case "Elise":
                u.addSkill(Skill.ELISE_AURA);
                break;

            case "Camilla":
                u.addSkill(Skill.CAMILLA_AURA);
                break;

            case "Chloe":
                u.addSkill(Skill.CHLOE_AURA);
                break;

            case "Dawn":
                u.addSkill(Skill.DAWN_AURA);
                break;
        }

        return u;
    }

    public static Unit createEnemy(String name, UnitJob job, Weapon weapon) {
        return new Unit(name, 0, 0, UnitType.ENEMY, job, job.getBaseStats(), weapon);
    }

    public static Unit createBoss(String name, UnitJob job, Stats bonusStats, Weapon weapon) {
        Stats finalStats = job.getBaseStats().add(bonusStats);
        return new Unit(name, 0, 0, UnitType.ENEMY, job, finalStats, weapon);
    }
}
