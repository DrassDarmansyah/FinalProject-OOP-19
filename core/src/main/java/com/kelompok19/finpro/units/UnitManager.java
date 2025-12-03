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

        Stats heroStats = new Stats(25, 12, 6, 14, 15, 10, 8, 5);
        Weapon heroWeapon = new Weapon(8, 90, 10, 5, 1);
        int[] pos = convert.apply(7, 12);
        playerUnits.add(new Unit("Hero", pos[0], pos[1], UnitType.PLAYER, UnitJob.SWORDMASTER, heroStats, heroWeapon));

        Stats jagenStats = new Stats(32, 14, 0, 11, 10, 4, 11, 3);
        Weapon jagenWeapon = new Weapon(10, 80, 5, 0, 1);
        pos = convert.apply(6, 11);
        playerUnits.add(new Unit("Jagen", pos[0], pos[1], UnitType.PLAYER, UnitJob.LANCER, jagenStats, jagenWeapon));

        Stats mageStats = new Stats(20, 3, 15, 12, 11, 8, 4, 9);
        Weapon mageWeapon = new Weapon(6, 85, 5, 0, 1, 2);
        pos = convert.apply(5, 13);
        playerUnits.add(new Unit("Mage", pos[0], pos[1], UnitType.PLAYER, UnitJob.WIZARD, mageStats, mageWeapon));

        Stats orcStats = new Stats(24, 11, 0, 8, 7, 3, 6, 1);
        Weapon axeWeapon = new Weapon(9, 70, 0, 0, 1);

        int[][] enemyPos = {
            convert.apply(8, 12), convert.apply(13, 16), convert.apply(13, 18),
            convert.apply(11, 7), convert.apply(13, 6), convert.apply(13, 8)
        };

        enemyUnits.add(new Unit("Orc 1", enemyPos[0][0], enemyPos[0][1], UnitType.ENEMY, UnitJob.ORC, orcStats, axeWeapon));
        enemyUnits.add(new Unit("Skel 1", enemyPos[1][0], enemyPos[1][1], UnitType.ENEMY, UnitJob.SKELETON, new Stats(18, 9, 0, 12, 10, 5, 4, 2), axeWeapon));
        enemyUnits.add(new Unit("Skel 2", enemyPos[2][0], enemyPos[2][1], UnitType.ENEMY, UnitJob.SKELETON_ARCHER, new Stats(18, 8, 0, 13, 9, 5, 3, 2), new Weapon(7, 85, 0, 0, 2, 2)));
        enemyUnits.add(new Unit("Orc 2", enemyPos[3][0], enemyPos[3][1], UnitType.ENEMY, UnitJob.ORC, orcStats, axeWeapon));
        enemyUnits.add(new Unit("Boss", enemyPos[4][0], enemyPos[4][1], UnitType.ENEMY, UnitJob.ELITE_ORC, new Stats(35, 15, 0, 10, 8, 5, 12, 4), new Weapon(12, 75, 10, 0, 1)));
    }
}
