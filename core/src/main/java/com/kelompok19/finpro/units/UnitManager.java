package com.kelompok19.finpro.units;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.factories.UnitFactory;

import java.util.ArrayList;
import java.util.List;

public class UnitManager {
    private final List<Unit> playerUnits = new ArrayList<>();
    private final List<Unit> enemyUnits = new ArrayList<>();
    private final List<Unit> roster = new ArrayList<>();

    public UnitManager(boolean initRoster) {
        if (initRoster) {
            initializeRoster();
        }
    }

    public void healAllUnits() {
        for (Unit unit : roster) {
            unit.setCurrentHp(unit.getStats().hp);
            unit.resetTurn();
        }
    }

    private void initializeRoster() {
        Stats noBonus = new Stats(0,0,0,0,0,0,0,0);

        Stats holyBonuses = new Stats(0, 2, 0, 0, 3, 0, 1, 1);
        Weapon holySword = new Weapon("Holy Sword", 11, 85, 5, 10, 0, 1, 1, holyBonuses);
        roster.add(UnitFactory.createPlayer("Diana", UnitJob.SWORDMASTER,
            new Stats(44, 37, 18, 29, 28, 18, 28, 13), holySword));

        Weapon jadeSword = new Weapon("Sword", 12, 90, 0, 0, -5, 1, 1, noBonus);
        roster.add(UnitFactory.createPlayer("Jade", UnitJob.SWORD_SOLDIER,
            new Stats(49, 33, 0, 37, 27, 31, 24, 14), jadeSword));

        Weapon masonBow = new Weapon("Bow", 15, 80, 0, 0, -5, 2, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Mason", UnitJob.BOW_SOLDIER,
            new Stats(45, 29, 0, 38, 30, 28, 25, 11), masonBow));

        Weapon drakeSword = new Weapon("Sword", 12, 90, 0, 0, -5, 1, 1, noBonus);
        roster.add(UnitFactory.createPlayer("Drake", UnitJob.KNIGHT,
            new Stats(46, 29, 3, 28, 34, 29, 22, 19), drakeSword));

        Weapon fireSword = new Weapon("Fire Sword", 11, 80, 0, -20, 0, 1, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Leon", UnitJob.FIRE_KNIGHT,
            new Stats(45, 22, 32, 21, 24, 24, 24, 29), fireSword));

        Weapon benLance = new Weapon("Lance", 14, 80, 0, 0, -5, 1, 1, noBonus);
        roster.add(UnitFactory.createPlayer("Ben", UnitJob.TEMPLAR,
            new Stats(52, 32, 0, 32, 9, 24, 41, 24), benLance));

        Weapon fireLance = new Weapon("Fire Lance", 11, 70, 0, -20, 0, 1, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Monty", UnitJob.FIRE_LANCER,
            new Stats(47, 19, 34, 25, 32, 31, 29, 21), fireLance));

        Weapon markLance = new Weapon("Lance", 14, 80, 0, 0, -5, 1, 1, noBonus);
        roster.add(UnitFactory.createPlayer("Mark", UnitJob.LANCER,
            new Stats(47, 33, 5, 26, 22, 32, 31, 15), markLance));

        Weapon haroldAxe = new Weapon("Axe", 16, 70, 0, 0, -5, 1, 1, noBonus);
        roster.add(UnitFactory.createPlayer("Harold", UnitJob.ARMORED_AXEMAN,
            new Stats(47, 33, 0, 36, 26, 5, 29, 11), haroldAxe));

        Weapon ice = new Weapon("Ice", 11, 80, 0, 0, -5, 1, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Elise", UnitJob.ICE_WIZARD,
            new Stats(30, 4, 36, 16, 32, 34, 10, 33), ice));

        Weapon fire = new Weapon("Fire", 11, 80, 0, 0, -5, 1, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Camilla", UnitJob.FIRE_WIZARD,
            new Stats(38, 19, 31, 27, 28, 17, 27, 26), fire));

        Weapon light = new Weapon("Light", 11, 80, 0, 0, -5, 1, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Chloe", UnitJob.PRIEST,
            new Stats(32, 14, 33, 24, 28, 27, 17, 21), light));

        Weapon dawnBow = new Weapon("Bow", 15, 80, 0, 0, -5, 2, 2, noBonus);
        roster.add(UnitFactory.createPlayer("Dawn", UnitJob.ARCHER,
            new Stats(40, 29, 9, 33, 33, 29, 24, 28), dawnBow));
    }

    public List<Unit> getRoster() { return roster; }

    public void deployUnit(Unit unit, int x, int y) {
        unit.setPosition(x, y);
        unit.setHasMoved(false);

        if (!playerUnits.contains(unit)) {
            playerUnits.add(unit);
        }
    }

    public void clearDeployedUnits() {
        playerUnits.clear();
        enemyUnits.clear();
    }

    public void addUnit(Unit unit) {
        if (unit.getType() == UnitType.PLAYER) {
            playerUnits.add(unit);
        }

        else enemyUnits.add(unit);
    }

    public List<Unit> getPlayerUnits() {
        return playerUnits;
    }

    public List<Unit> getEnemyUnits() {
        return enemyUnits;
    }

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

    public List<Unit> getAllUnits() {
        List<Unit> all = new ArrayList<>(playerUnits);
        all.addAll(enemyUnits);
        return all;
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
}
