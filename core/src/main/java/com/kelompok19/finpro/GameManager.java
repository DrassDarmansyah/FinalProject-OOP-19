package com.kelompok19.finpro;

import com.badlogic.gdx.graphics.Color;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;

import java.util.List;

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
        System.out.println("--- New Session: Turn 1 ---");
    }

    public void startEnemyPhase(BattleContext context) {
        isPlayerTurn = false;
        System.out.println("--- Enemy Phase (Turn " + turnCount + ") ---");

        UnitManager unitManager = context.unitManager;

        for(Unit u : unitManager.getPlayerUnits()) {
            u.setHasMoved(false);
        }

        applyTerrainHealing(unitManager.getEnemyUnits(), context);
    }

    public void startPlayerPhase(BattleContext context) {
        turnCount++;
        isPlayerTurn = true;
        System.out.println("--- Player Phase (Turn " + turnCount + ") ---");

        UnitManager unitManager = context.unitManager;

        for(Unit u : unitManager.getEnemyUnits()) {
            u.setHasMoved(false);
        }

        for(Unit u : unitManager.getPlayerUnits()) {
            u.resetTurn();
        }

        applyTerrainHealing(unitManager.getPlayerUnits(), context);
    }

    private void applyTerrainHealing(List<Unit> units, BattleContext context) {
        for (Unit unit : units) {
            if (unit.getCurrentHp() <= 0) {
                continue;
            }

            TerrainType terrain = context.map.getTile(unit.getX(), unit.getY()).getType();

            if (terrain.heal > 0 && unit.getCurrentHp() < unit.getStats().hp) {
                int healAmount = (int)(unit.getStats().hp * (terrain.heal / 100.0f));

                if (healAmount < 1) {
                    healAmount = 1;
                }

                int oldHp = unit.getCurrentHp();
                int newHp = Math.min(unit.getStats().hp, oldHp + healAmount);

                unit.setCurrentHp(newHp);
                int actualHealed = newHp - oldHp;

                if (actualHealed > 0) {
                    System.out.println(unit.getName() + " healed " + actualHealed + " from " + terrain);
                    context.addDamagePopup("+" + actualHealed, unit.getX(), unit.getY(), Color.GREEN, 1.0f);
                }
            }
        }
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public int getTurnCount() {
        return turnCount;
    }
}
