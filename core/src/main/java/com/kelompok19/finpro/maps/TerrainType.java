package com.kelompok19.finpro.maps;

import com.kelompok19.finpro.units.MovementType;

public enum TerrainType {
    BRIDGE(0, 0, 0),
    CAVERN(0, 0, 0),
    FORT(2, 20, 20),
    LEYLINE(0, 0, 0),
    PLAIN(0, 0, 0),
    RUBBLE(0, 0, 0),
    WATER(0, -10, 0),
    WOODS(1, 10, 0);

    public final int defense;
    public final int avoid;
    public final int heal;

    TerrainType(int defense, int avoid, int heal) {
        this.defense = defense;
        this.avoid = avoid;
        this.heal = heal;
    }

    public int getMoveCost(MovementType moveType) {
        switch (this) {
            case BRIDGE:
            case LEYLINE:
            case PLAIN:
                return 1;

            case FORT:
                if (moveType == MovementType.MAGE) {
                    return 1;
                }

                return 2;

            case RUBBLE:
                if (moveType == MovementType.MAGE) {
                    return 1;
                }

                return 999;

            case CAVERN:
                return 999;

            case WATER:
                if (moveType == MovementType.MAGE) {
                    return 1;
                }

                if (moveType == MovementType.LORD || moveType == MovementType.MONSTER) {
                    return 5;
                }

                if (moveType == MovementType.MONSTER_ELITE || moveType == MovementType.BEAST) {
                    return 4;
                }

                return 999;

            case WOODS:
                if (moveType == MovementType.MAGE) {
                    return 1;
                }

                if (moveType == MovementType.CAVALRY) {
                    return 3;
                }

                return 2;

            default:
                return 1;
        }
    }
}
