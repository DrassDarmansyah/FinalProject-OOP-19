package com.kelompok19.finpro.units;

public class Stats {
    public final int hp;
    public final int strength;
    public final int magic;
    public final int dexterity;
    public final int speed;
    public final int luck;
    public final int defense;
    public final int resistance;

    public Stats(int hp, int strength, int magic, int dexterity, int speed, int luck, int defense, int resistance) {
        this.hp = hp;
        this.strength = strength;
        this.magic = magic;
        this.dexterity = dexterity;
        this.speed = speed;
        this.luck = luck;
        this.defense = defense;
        this.resistance = resistance;
    }

    public Stats add(Stats other) {
        return new Stats(
            this.hp + other.hp,
            this.strength + other.strength,
            this.magic + other.magic,
            this.dexterity + other.dexterity,
            this.speed + other.speed,
            this.luck + other.luck,
            this.defense + other.defense,
            this.resistance + other.resistance
        );
    }
}
