package com.kelompok19.finpro;

import com.kelompok19.finpro.units.Stats;

public class Weapon {
    public final String name;
    public final int might;
    public final int hit;
    public final int crit;
    public final int avoid;
    public final int dodge;
    public final int rangeMin;
    public final int rangeMax;
    public final Stats bonuses;

    public Weapon(String name, int might, int hit, int crit, int avoid, int dodge, int rangeMin, int rangeMax, Stats bonuses) {
        this.name = name;
        this.might = might;
        this.hit = hit;
        this.crit = crit;
        this.avoid = avoid;
        this.dodge = dodge;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.bonuses = (bonuses != null) ? bonuses : new Stats(0,0,0,0,0,0,0,0);
    }
}
