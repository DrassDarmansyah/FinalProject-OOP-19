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

        if (p != null) {
            return p;
        }

        return getEnemyUnitAt(x, y);
    }

    public Unit getPlayerUnitAt(int x, int y) {
        for (Unit unit : playerUnits) {
            if (unit.isAt(x, y) && unit.getCurrentHp() > 0) {
                return unit;
            }
        }

        return null;
    }

    public Unit getEnemyUnitAt(int x, int y) {
        for (Unit unit : enemyUnits) {
            if (unit.isAt(x, y) && unit.getCurrentHp() > 0) {
                return unit;
            }
        }

        return null;
    }

    public boolean isOccupied(int x, int y, Unit excludeUnit) {
        for (Unit unit : playerUnits) {
            if (unit != excludeUnit && unit.isAt(x, y) && unit.getCurrentHp() > 0) {
                return true;
            }
        }

        for (Unit unit : enemyUnits) {
            if (unit.isAt(x, y) && unit.getCurrentHp() > 0) {
                return true;
            }
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

        Stats heroPersonal = new Stats(6, 6, 6, 5, 5, 10, 3, 2);
        Weapon heroWeapon = new Weapon(8, 90, 10, 5, 1);
        int[] pos = convert.apply(7, 12);
        playerUnits.add(new Unit("Hero", pos[0], pos[1], UnitType.PLAYER, UnitJob.SWORDMASTER, heroPersonal, heroWeapon));

        Stats jagenPersonal = new Stats(12, 7, 0, 5, 3, 4, 5, 1);
        Weapon jagenWeapon = new Weapon(10, 80, 5, 0, 1);
        pos = convert.apply(6, 11);
        playerUnits.add(new Unit("Jagen", pos[0], pos[1], UnitType.PLAYER, UnitJob.LANCER, jagenPersonal, jagenWeapon));

        Stats magePersonal = new Stats(3, 2, 6, 6, 4, 8, 1, 2);
        Weapon mageWeapon = new Weapon(6, 85, 5, 0, 1, 2);
        pos = convert.apply(5, 13);
        playerUnits.add(new Unit("Mage", pos[0], pos[1], UnitType.PLAYER, UnitJob.WIZARD, magePersonal, mageWeapon));

        Weapon axeWeapon = new Weapon(9, 70, 0, 0, 1);
        Stats genericBonus = new Stats(2, 2, 0, 1, 1, 0, 1, 0);

        int[][] enemyPos = {
            convert.apply(8, 12), convert.apply(13, 16), convert.apply(13, 18),
            convert.apply(11, 7), convert.apply(13, 6), convert.apply(13, 8)
        };

        enemyUnits.add(new Unit("Orc 1", enemyPos[0][0], enemyPos[0][1], UnitType.ENEMY, UnitJob.ORC, genericBonus, axeWeapon));
        enemyUnits.add(new Unit("Skel 1", enemyPos[1][0], enemyPos[1][1], UnitType.ENEMY, UnitJob.SKELETON, genericBonus, axeWeapon));
        enemyUnits.add(new Unit("Skel 2", enemyPos[2][0], enemyPos[2][1], UnitType.ENEMY, UnitJob.SKELETON_ARCHER, genericBonus, new Weapon(7, 85, 0, 0, 2, 2)));
        enemyUnits.add(new Unit("Orc 2", enemyPos[3][0], enemyPos[3][1], UnitType.ENEMY, UnitJob.ORC, genericBonus, axeWeapon));

        Stats bossBonus = new Stats(5, 3, 0, 2, 2, 5, 2, 2);
        enemyUnits.add(new Unit("Boss", enemyPos[4][0], enemyPos[4][1], UnitType.ENEMY, UnitJob.ELITE_ORC, bossBonus, new Weapon(12, 75, 10, 0, 1)));
    }
}
