package com.kelompok19.finpro.maps;

public class Tile {
    private final int x;
    private final int y;
    private final TerrainType type;

    public Tile(int x, int y, TerrainType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TerrainType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Tile{" + "x=" + x + ", y=" + y + ", type=" + type + '}';
    }
}
