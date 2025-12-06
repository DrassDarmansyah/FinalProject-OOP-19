package com.kelompok19.finpro.units;

import com.kelompok19.finpro.states.BattleContext;

public enum Skill {
    CHLOE_AURA("Quiet Strength", "Allies within 2 tiles: Dmg Taken -2") {
        @Override
        public int getAuraDamageTaken(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist <= 2) ? -2 : 0;
        }
    },
    ELISE_AURA("Lily's Poise", "Allies within 1 tile: Dmg Taken -3, Dmg Dealt +1") {
        @Override
        public int getAuraDamageTaken(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? -3 : 0;
        }
        @Override
        public int getAuraDamageDealt(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? 1 : 0;
        }
    },
    BEN_AURA("Fierce Mien", "Enemies within 2 tiles: Avoid -10") {
        @Override
        public int getAuraAvoid(Unit me, Unit target, int dist) {
            return (isEnemy(me, target) && dist <= 2) ? -10 : 0;
        }
    },
    HAROLD_AURA("Misfortunate", "Enemies within 2 tiles: Dodge -15") {
        @Override
        public int getAuraDodge(Unit me, Unit target, int dist) {
            return (isEnemy(me, target) && dist <= 2) ? -15 : 0;
        }
    },
    CAMILLA_AURA("Rose's Thorns", "Adjacent Allies: Dmg Dealt +3, Dmg Taken -1") {
        @Override
        public int getAuraDamageDealt(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? 3 : 0;
        }
        @Override
        public int getAuraDamageTaken(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? -1 : 0;
        }
    },
    DAWN_AURA("Rallying Cry", "Allies within 2 tiles: Dmg Dealt +2") {
        @Override
        public int getAuraDamageDealt(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist <= 2) ? 2 : 0;
        }
    },
    MASON_SKILL("Competitive", "Adj Ally: Crit +10, Dmg +3, Taken -1") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (hasAdjacentAlly(me, context)) {
                bonuses.crit += 10;
                bonuses.damageDealt += 3;
                bonuses.damageTaken -= 1;
            }
        }
    },
    DRAKE_SKILL("Warrior Code", "Adj Ally: Crit +10, Dmg +2, Taken -2") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (hasAdjacentAlly(me, context)) {
                bonuses.crit += 10;
                bonuses.damageDealt += 2;
                bonuses.damageTaken -= 2;
            }
        }
    },
    DIANA_SKILL("Supportive", "Adj Ally: Hit +10, Dmg +2, Taken -2") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (hasAdjacentAlly(me, context)) {
                bonuses.hit += 10;
                bonuses.damageDealt += 2;
                bonuses.damageTaken -= 2;
            }
        }
    },
    LEON_SKILL("Pragmatic", "Enemy not Full HP: Dmg +3, Taken -1") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (enemy.getCurrentHp() < enemy.getStats().hp) {
                bonuses.damageDealt += 3;
                bonuses.damageTaken -= 1;
            }
        }
    },
    MARK_SKILL("Chivalry", "Enemy Full HP: Dmg +2, Taken -2") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (enemy.getCurrentHp() >= enemy.getStats().hp) {
                bonuses.damageDealt += 2;
                bonuses.damageTaken -= 2;
            }
        }
    },
    JADE_RALLY("Fancy Footwork", "Command: Allies in 2 tiles get +4 Str/Spd"),
    MONTY_REFLECT("Divine Retribution", "Reflect half damage if attacked at 1 range");

    public final String name;
    public final String desc;

    Skill(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public int getAuraDamageDealt(Unit me, Unit target, int dist) {
        return 0;
    }

    public int getAuraDamageTaken(Unit me, Unit target, int dist) {
        return 0;
    }

    public int getAuraAvoid(Unit me, Unit target, int dist) {
        return 0;
    }

    public int getAuraDodge(Unit me, Unit target, int dist) {
        return 0;
    }

    public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {

    }

    protected boolean isAlly(Unit me, Unit target) {
        return me.getType() == target.getType();
    }

    protected boolean isEnemy(Unit me, Unit target) {
        return me.getType() != target.getType();
    }

    protected boolean hasAdjacentAlly(Unit me, BattleContext ctx) {
        for (Unit u : ctx.unitManager.getAllUnits()) {
            if (u != me && u.getType() == me.getType()) {
                int dist = Math.abs(me.getX() - u.getX()) + Math.abs(me.getY() - u.getY());

                if (dist == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static class CombatBonusContainer {
        public int damageDealt = 0;
        public int damageTaken = 0;
        public int hit = 0;
        public int crit = 0;
        public int avoid = 0;
        public int dodge = 0;
    }
}
