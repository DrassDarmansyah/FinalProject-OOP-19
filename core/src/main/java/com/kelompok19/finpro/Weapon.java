package com.kelompok19.finpro;

/**
 * Represents a weapon's combat properties.
 */
public class Weapon {
    public final int might;
    public final int hit;
    public final int crit;
    public final int avoid;
    public final int rangeMin;
    public final int rangeMax;

    public Weapon(int might, int hit, int crit, int avoid, int rangeMin, int rangeMax) {
        this.might = might;
        this.hit = hit;
        this.crit = crit;
        this.avoid = avoid;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public Weapon(int might, int hit, int crit, int avoid, int range) {
        this(might, hit, crit, avoid, 1, range);
    }
}
