package com.kelompok19.finpro.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.combat.CombatEngine;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;

public class InputHandler {
    public enum GameState { BROWSING, ACTION_MENU, SHOWING_MOVE_RANGE, SHOWING_ATTACK_RANGE }
    private GameState state = GameState.BROWSING;

    private final GameMap map;
    private final UnitManager unitManager;
    private final ActionMenu actionMenu;

    private int cx = 0, cy = 0;

    private Unit activeUnit = null;

    private MoveCommand activeMoveCommand = null;

    private final Array<int[]> moveRangeTiles = new Array<>();
    private final Array<int[]> attackRangeTiles = new Array<>();
    private CombatPreview combatPreview = null;

    public InputHandler(GameMap map, UnitManager unitManager) {
        this.map = map;
        this.unitManager = unitManager;
        this.actionMenu = new ActionMenu();
        if (!unitManager.getPlayerUnits().isEmpty()) {
            Unit p = unitManager.getPlayerUnits().get(0);
            cx = p.getX(); cy = p.getY();
        }
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            cancel();
            return;
        }

        switch (state) {
            case BROWSING: handleBrowsing(); break;
            case ACTION_MENU: handleMenu(); break;
            case SHOWING_MOVE_RANGE: handleMoveSelection(); break;
            case SHOWING_ATTACK_RANGE: handleAttackSelection(); break;
        }
    }

    private void handleBrowsing() {
        moveCursor();
        if (isConfirmPressed()) {
            Unit u = unitManager.getPlayerUnitAt(cx, cy);
            if (u != null && !u.hasMoved()) {
                activeUnit = u;

                boolean canAttack = canAttackFrom(activeUnit, cx, cy);

                actionMenu.show(true, canAttack, false);
                state = GameState.ACTION_MENU;
            }
        }
    }

    private void handleMoveSelection() {
        moveCursor();
        if (isConfirmPressed()) {
            if (isTileInList(cx, cy, moveRangeTiles)) {
                activeMoveCommand = new MoveCommand(activeUnit, cx, cy);

                activeMoveCommand.execute();

                boolean canAttack = canAttackFrom(activeUnit, cx, cy);
                actionMenu.show(false, canAttack, true);
                state = GameState.ACTION_MENU;
            }
        }
    }

    private void handleMenu() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) actionMenu.navigateUp();
        if (Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) actionMenu.navigateDown();

        if (isConfirmPressed()) {
            String choice = actionMenu.getSelectedOption();
            if (choice == null) return;

            switch (choice) {
                case ActionMenu.CMD_MOVE:
                    calculateMovementRange(activeUnit);
                    state = GameState.SHOWING_MOVE_RANGE;
                    break;
                case ActionMenu.CMD_ATTACK:
                    calculateAttackRange(activeUnit);
                    state = GameState.SHOWING_ATTACK_RANGE;
                    break;
                case ActionMenu.CMD_WAIT:
                    finishTurn();
                    break;
            }
        }
    }

    private void handleAttackSelection() {
        moveCursor();

        Unit target = unitManager.getEnemyUnitAt(cx, cy);
        if (target != null && isTileInList(cx, cy, attackRangeTiles)) {
            combatPreview = CombatEngine.calculateCombat(activeUnit, target);
        } else {
            combatPreview = null;
        }

        if (isConfirmPressed()) {
            if (combatPreview != null) {
                Command attackCommand = new AttackCommand(activeUnit, target);
                attackCommand.execute();

                finishTurn();
            }
        }
    }

    private void cancel() {
        combatPreview = null;

        if (state == GameState.ACTION_MENU) {
            if (activeMoveCommand != null) {
                activeMoveCommand.undo();

                cx = activeUnit.getX();
                cy = activeUnit.getY();

                activeMoveCommand = null;

                calculateMovementRange(activeUnit);
                state = GameState.SHOWING_MOVE_RANGE;
            } else {
                reset();
            }
        } else if (state == GameState.SHOWING_MOVE_RANGE) {
            reset();
        } else if (state == GameState.SHOWING_ATTACK_RANGE) {
            state = GameState.ACTION_MENU;
            cx = activeUnit.getX();
            cy = activeUnit.getY();
        }
    }

    private void finishTurn() {
        activeUnit.setHasMoved(true);
        activeMoveCommand = null;
        reset();
    }

    private void reset() {
        activeUnit = null;
        activeMoveCommand = null;
        combatPreview = null;
        state = GameState.BROWSING;
        unitManager.cleanup();
    }

    private void moveCursor() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) cy++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) cy--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) cx--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) cx++;
        clampCursor();
    }

    private void clampCursor() {
        cx = Math.max(0, Math.min(cx, map.getWidth() - 1));
        cy = Math.max(0, Math.min(cy, map.getHeight() - 1));
    }

    private boolean isConfirmPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
    }

    private void calculateMovementRange(Unit unit) {
        moveRangeTiles.clear();
        int range = unit.getMoveRange();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int dist = Math.abs(x - unit.getX()) + Math.abs(y - unit.getY());
                if (dist <= range && !unitManager.isOccupied(x, y, unit)) {
                    moveRangeTiles.add(new int[]{x, y});
                }
            }
        }
        moveRangeTiles.add(new int[]{unit.getX(), unit.getY()});
    }

    private void calculateAttackRange(Unit unit) {
        attackRangeTiles.clear();
        Weapon w = unit.getWeapon();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                int dist = Math.abs(x - unit.getX()) + Math.abs(y - unit.getY());
                if (dist >= w.rangeMin && dist <= w.rangeMax) {
                    if (unitManager.getEnemyUnitAt(x, y) != null) {
                        attackRangeTiles.add(new int[]{x, y});
                    }
                }
            }
        }
    }

    private boolean canAttackFrom(Unit unit, int x, int y) {
        Weapon w = unit.getWeapon();
        for (Unit enemy : unitManager.getEnemyUnits()) {
            int dist = Math.abs(x - enemy.getX()) + Math.abs(y - enemy.getY());
            if (dist >= w.rangeMin && dist <= w.rangeMax) return true;
        }
        return false;
    }

    private boolean isTileInList(int x, int y, Array<int[]> list) {
        for(int[] p : list) if(p[0] == x && p[1] == y) return true;
        return false;
    }

    public int getCursorX() { return cx; }
    public int getCursorY() { return cy; }
    public GameState getState() { return state; }
    public ActionMenu getActionMenu() { return actionMenu; }
    public Array<int[]> getMoveRangeTiles() { return moveRangeTiles; }
    public Array<int[]> getAttackRangeTiles() { return attackRangeTiles; }
    public CombatPreview getCombatPreview() { return combatPreview; }
}
