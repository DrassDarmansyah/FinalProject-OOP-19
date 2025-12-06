package com.kelompok19.finpro.units;

import com.kelompok19.finpro.states.BattleContext;

public enum Skill {
    CHLOE_AURA("Quiet Strength", "Allies within two spaces take -2 damage") {
        @Override
        public int getAuraDamageTaken(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist <= 2) ? -2 : 0;
        }
    },
    ELISE_AURA("Lily's Poise", "Grants adjacent allies +1 damage dealt and -3 to damage taken") {
        @Override
        public int getAuraDamageTaken(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? -3 : 0;
        }
        @Override
        public int getAuraDamageDealt(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? 1 : 0;
        }
    },
    BEN_AURA("Fierce Mien", "Enemies within two spaces get Avoid -10") {
        @Override
        public int getAuraAvoid(Unit me, Unit target, int dist) {
            return (isEnemy(me, target) && dist <= 2) ? -10 : 0;
        }
    },
    HAROLD_AURA("Misfortunate", "Enemies within two spaces get Dodge -15") {
        @Override
        public int getAuraDodge(Unit me, Unit target, int dist) {
            return (isEnemy(me, target) && dist <= 2) ? -15 : 0;
        }
    },
    CAMILLA_AURA("Rose's Thorns", "Grants adjacent allies +3 damage dealt and -1 to damage taken") {
        @Override
        public int getAuraDamageDealt(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? 3 : 0;
        }
        @Override
        public int getAuraDamageTaken(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist == 1) ? -1 : 0;
        }
    },
    DAWN_AURA("Rallying Cry", "Allies within two spaces deal +2 damage") {
        @Override
        public int getAuraDamageDealt(Unit me, Unit target, int dist) {
            return (isAlly(me, target) && dist <= 2) ? 2 : 0;
        }
    },
    MASON_SKILL("Competitive", "If there is an adjacent ally, gets Crit+10, +3 to damage dealt, and -1 to damage taken") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (hasAdjacentAlly(me, context)) {
                bonuses.crit += 10;
                bonuses.damageDealt += 3;
                bonuses.damageTaken -= 1;
            }
        }
    },
    DRAKE_SKILL("Warrior Code", "If unit's Speed is higher than foe's, gets Crit+10, +2 to damage dealt, and -2 to damage taken") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (me.getEffectiveStats().speed > enemy.getEffectiveStats().speed) {
                bonuses.crit += 10;
                bonuses.damageDealt += 2;
                bonuses.damageTaken -= 2;
            }
        }
    },
    DIANA_SKILL("Supportive", "If there is an adjacent ally, gets Hit+10, +2 to damage dealt, and -2 to damage taken") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (hasAdjacentAlly(me, context)) {
                bonuses.hit += 10;
                bonuses.damageDealt += 2;
                bonuses.damageTaken -= 2;
            }
        }
    },
    LEON_SKILL("Pragmatic", "Grants +3 to damage dealt and -1 to damage taken against injured foes") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (enemy.getCurrentHp() < enemy.getStats().hp) {
                bonuses.damageDealt += 3;
                bonuses.damageTaken -= 1;
            }
        }
    },
    MARK_SKILL("Chivalry", "Grants +2 to damage dealt and -2 to damage taken against foes with full HP") {
        @Override
        public void applySelfBonuses(Unit me, Unit enemy, BattleContext context, CombatBonusContainer bonuses) {
            if (enemy.getCurrentHp() >= enemy.getStats().hp) {
                bonuses.damageDealt += 2;
                bonuses.damageTaken -= 2;
            }
        }
    },
    JADE_RALLY("Fancy Footwork", "Use 'Rally' to grant +1 Strength and Speed to allies within two spaces for one turn"),
    MONTY_REFLECT("Divine Retribution", "Reflect half damage to foe if attacked from an adjacent space");

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
