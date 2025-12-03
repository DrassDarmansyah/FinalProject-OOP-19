package com.kelompok19.finpro;

import com.kelompok19.finpro.units.UnitManager;

public class GameManager {
    private static GameManager instance;

    private UnitManager unitManager;
    private int turnCount;
    private boolean isPlayerTurn;
    private boolean gameActive;

    private GameManager() {
        this.gameActive = false;
        this.turnCount = 0;
        this.isPlayerTurn = true;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void startNewSession(UnitManager unitManager) {
        this.unitManager = unitManager;
        this.turnCount = 1;
        this.isPlayerTurn = true;
        this.gameActive = true;
        System.out.println("New Game Session Started");
    }

    public void endTurn() {
        isPlayerTurn = !isPlayerTurn;
        if (isPlayerTurn) {
            turnCount++;
            System.out.println("--- Turn " + turnCount + " Start ---");
        } else {
            System.out.println("--- Enemy Phase ---");
        }
    }

    public UnitManager getUnitManager() {
        return unitManager;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isGameActive() {
        return gameActive;
    }
}
