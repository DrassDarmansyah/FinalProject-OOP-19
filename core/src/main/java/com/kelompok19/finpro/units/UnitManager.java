package com.kelompok19.finpro.units;

import com.kelompok19.finpro.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class UnitManager {
    private final List<Unit> playerUnits = new ArrayList<>();
    private final List<Unit> enemyUnits = new ArrayList<>();

    public UnitManager(int mapHeight) {
        setupCharacters(mapHeight);
    }

    public List<Unit> getPlayerUnits() { return playerUnits; }
    public List<Unit> getEnemyUnits() { return enemyUnits; }

    public Unit getUnitAt(int x, int y) {
        Unit p = getPlayerUnitAt(x, y);
        if (p != null) return p;
        return getEnemyUnitAt(x, y);
    }

    public Unit getPlayerUnitAt(int x, int y) {
        for (Unit unit : playerUnits) {
            if (unit.isAt(x, y) && unit.getCurrentHp() > 0) return unit;
        }
        return null;
    }

    public Unit getEnemyUnitAt(int x, int y) {
        for (Unit unit : enemyUnits) {
            if (unit.isAt(x, y) && unit.getCurrentHp() > 0) return unit;
        }
        return null;
    }

    public boolean isOccupied(int x, int y, Unit excludeUnit) {
        for (Unit unit : playerUnits) {
            if (unit != excludeUnit && unit.isAt(x, y) && unit.getCurrentHp() > 0) return true;
        }
        for (Unit unit : enemyUnits) {
            if (unit.isAt(x, y) && unit.getCurrentHp() > 0) return true;
        }
        return false;
    }

    public void removeUnit(Unit unit) {
        playerUnits.remove(unit);
        enemyUnits.remove(unit);
        unit.dispose();
    }

    public void cleanup() {
        playerUnits.removeIf(u -> u.getCurrentHp() <= 0);
        enemyUnits.removeIf(u -> u.getCurrentHp() <= 0);
    }

    private void setupCharacters(int mapHeight) {
        BiFunction<Integer, Integer, int[]> convert = (v, h) -> new int[]{h - 1, mapHeight - v};

        Stats protagStats = new Stats(21, 9, 6, 9, 11, 7, 6, 3);
        Weapon protagWeapon = new Weapon(8, 85, 0, 0, 1);
        int[] pos = convert.apply(7, 12);
        playerUnits.add(new Unit("Hero", pos[0], pos[1], UnitType.PLAYER, protagStats, protagWeapon));

        Stats p1Stats = new Stats(25, 12, 0, 8, 9, 5, 8, 2);
        Weapon p1Weapon = new Weapon(10, 80, 5, 0, 1);
        pos = convert.apply(6, 11);
        playerUnits.add(new Unit("Jagen", pos[0], pos[1], UnitType.PLAYER, p1Stats, p1Weapon));

        Stats p2Stats = new Stats(18, 5, 12, 10, 10, 6, 4, 7);
        Weapon p2Weapon = new Weapon(4, 95, 0, 0, 1, 2);
        pos = convert.apply(5, 13);
        playerUnits.add(new Unit("Mage", pos[0], pos[1], UnitType.PLAYER, p2Stats, p2Weapon));

        Stats enemyStats = new Stats(20, 7, 0, 7, 7, 5, 5, 2);
        Weapon enemyWeapon = new Weapon(5, 90, 0, 0, 1);

        int[][] enemyPos = {
            convert.apply(8, 12), convert.apply(13, 16), convert.apply(13, 18),
            convert.apply(11, 7), convert.apply(13, 6), convert.apply(13, 8)
        };

        int count = 1;
        for (int[] p : enemyPos) {
            enemyUnits.add(new Unit("Orc " + count++, p[0], p[1], UnitType.ENEMY, enemyStats, enemyWeapon));
        }
    }
}
