package com.kelompok19.finpro.combat;

import com.kelompok19.finpro.units.Unit;

public class CombatStep {
    public enum Type {
        ATTACK,
        MISS,
        REFLECT,
        DIE
    }

    public final Type type;
    public final Unit source;
    public final Unit target;
    public final int damage;
    public final boolean isCrit;

    public CombatStep(Type type, Unit source, Unit target, int damage, boolean isCrit) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.damage = damage;
        this.isCrit = isCrit;
    }
}
