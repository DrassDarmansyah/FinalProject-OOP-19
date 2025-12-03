package com.kelompok19.finpro;

import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;

public class GameManager {
    private static GameManager instance;

    private int turnCount;
    private boolean isPlayerTurn;

    private GameManager() {
        this.turnCount = 1;
        this.isPlayerTurn = true;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public void startNewSession() {
        this.turnCount = 1;
        this.isPlayerTurn = true;
        System.out.println("New Game started");
        System.out.println("Turn 1");
    }

    public void endPlayerTurn(UnitManager unitManager) {
        System.out.println("Player Phase ended");
        isPlayerTurn = false;

        for(Unit u : unitManager.getPlayerUnits()) {
            u.setHasMoved(false);
        }

        startPlayerTurn(unitManager);
    }

    public void startPlayerTurn(UnitManager unitManager) {
        turnCount++;
        isPlayerTurn = true;
        System.out.println("Turn " + turnCount + "start: Player Phase");

        for(Unit u : unitManager.getEnemyUnits()) {
            u.setHasMoved(false);
        }
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }
    public int getTurnCount() {
        return turnCount;
    }
}
