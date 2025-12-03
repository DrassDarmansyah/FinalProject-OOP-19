package com.kelompok19.finpro.states;

import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;

public class PlayingState {

    public int cursorX = 0;
    public int cursorY = 0;

    public Unit selectedUnit = null;

    private GameMap gameMap;
    private UnitManager unitManager;

    public PlayingState(GameMap map, UnitManager units) {
        this.gameMap = map;
        this.unitManager = units;
    }

    public Unit getUnitAtCursor() {
        return unitManager.getUnitAt(cursorX, cursorY);
    }

    public void moveCursor(int dx, int dy) {
        int newX = cursorX + dx;
        int newY = cursorY + dy;

        if (newX >= 0 && newX < gameMap.getWidth() &&
            newY >= 0 && newY < gameMap.getHeight()) {
            cursorX = newX;
            cursorY = newY;
        }
    }

    public UnitManager getUnitManager() { return unitManager; }
    public GameMap getGameMap() { return gameMap; }
}
