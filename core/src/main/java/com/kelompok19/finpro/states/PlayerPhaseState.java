package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.combat.CombatEngine;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.commands.*;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.units.Skill;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitJob;
import com.kelompok19.finpro.utils.HoverSystem;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerPhaseState extends BattleState {
    private enum SubState {
        BROWSING,
        MOVING,
        ACTION_MENU,
        ATTACKING,
        HEALING
    }

    private SubState state = SubState.BROWSING;
    private Unit selectedUnit;
    private MoveCommand lastMove;
    private final ActionMenu actionMenu;

    private final Array<int[]> validMoves = new Array<>();
    private final Array<int[]> attackableTiles = new Array<>();
    private final Array<int[]> validTargets = new Array<>();
    private final Array<int[]> validHealTargets = new Array<>();

    private CombatPreview combatPreview;
    private final HoverSystem hoverSystem;

    public PlayerPhaseState(GameStateManager gsm, BattleContext context) {
        super(gsm, context);
        this.actionMenu = new ActionMenu();
        this.hoverSystem = new HoverSystem(context);

        if (context.isShowDangerZone()) {
            context.recalculateDangerZone();
        }
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            context.toggleDangerZone();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            context.toggleFightAnimations();
        }

        switch (state) {
            case BROWSING: updateBrowsing(); break;
            case MOVING: updateMoving(); break;
            case ACTION_MENU: updateActionMenu(); break;
            case ATTACKING: updateAttacking(); break;
            case HEALING: updateHealing(); break;
        }
    }

    private void updateAttacking() {
        handleCursorInput();
        Unit target = context.unitManager.getEnemyUnitAt(context.cursorX, context.cursorY);

        if (target != null && isValidTarget(context.cursorX, context.cursorY)) {
            combatPreview = CombatEngine.calculateCombat(selectedUnit, target, context);
        }

        else {
            combatPreview = null;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            state = SubState.ACTION_MENU;
            context.cursorX = selectedUnit.getX();
            context.cursorY = selectedUnit.getY();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (target != null && isValidTarget(context.cursorX, context.cursorY)) {
                new AttackCommand(selectedUnit, target, context, gsm).execute();
                selectedUnit = null;
                lastMove = null;
                state = SubState.BROWSING;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);

        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();

        if (context.isShowDangerZone()) {
            context.mapOverlay.drawDangerZoneOutline(batch, context.getDangerZone(), context.whitePixel);
        }

        if (state == SubState.BROWSING) {
            hoverSystem.renderRanges(batch);
        }

        if (state == SubState.MOVING) {
            context.mapOverlay.drawRangeTiles(batch, attackableTiles, 1, 0.3f);
            context.mapOverlay.drawRangeTiles(batch, validMoves, 0, 0.3f);
        }

        else if (state == SubState.ATTACKING) {
            context.mapOverlay.drawRangeTiles(batch, validTargets, 1, 0.3f);
        }

        else if (state == SubState.HEALING) {
            context.mapOverlay.drawRangeTiles(batch, validHealTargets, 2, 0.3f);
        }

        if (state != SubState.ACTION_MENU) {
            context.mapOverlay.drawCursor(batch, context.cursorX, context.cursorY);
        }

        if (state == SubState.ACTION_MENU) {
            actionMenu.render(batch, context.font, selectedUnit.getX(), selectedUnit.getY(), context.camera);
        }

        else if (state == SubState.ATTACKING && combatPreview != null) {
            combatPreview.render(batch, context.font, context.camera);
        }

        batch.end();

        renderPopups(batch);
        batch.begin();

        if (state == SubState.BROWSING) {
            hoverSystem.renderUnitInfo(batch);
        }

        context.turnPreview.render(batch, context.font, context.camera, GameManager.getInstance().getTurnCount(), true, context.config.getObjectiveText());
        batch.end();
        renderTerrainInfo(batch);
    }

    private void updateBrowsing() {
        handleCursorInput();
        hoverSystem.update(context.cursorX, context.cursorY);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Unit u = hoverSystem.getHoveredUnit();

            if (u != null && u.getType() == com.kelompok19.finpro.units.UnitType.PLAYER && !u.hasMoved()) {
                selectedUnit = u;
                lastMove = null;
                calculateMovementRange();
                openActionMenu();
            }
        }
    }

    private void updateMoving() {
        handleCursorInput();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            state = SubState.ACTION_MENU;
            context.cursorX = selectedUnit.getX();
            context.cursorY = selectedUnit.getY();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            boolean valid = false;

            for(int[] p : validMoves) {
                if(p[0] == context.cursorX && p[1] == context.cursorY) {
                    valid = true;
                }
            }

            if (valid) {
                lastMove = new MoveCommand(selectedUnit, context.cursorX, context.cursorY);
                lastMove.execute();
                openActionMenu();
            }
        }
    }

    private void updateActionMenu() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            actionMenu.navigateUp();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            actionMenu.navigateDown();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            String choice = actionMenu.getSelectedOption();

            if (choice == null) {
                return;
            }

            switch (choice) {
                case ActionMenu.CMD_MOVE:
                    calculateMovementRange();
                    state = SubState.MOVING;
                    context.cursorX = selectedUnit.getX();
                    context.cursorY = selectedUnit.getY();
                    break;

                case ActionMenu.CMD_ATTACK:
                    calculateAttackRange();
                    state = SubState.ATTACKING;
                    context.cursorX = selectedUnit.getX();
                    context.cursorY = selectedUnit.getY();
                    break;

                case ActionMenu.CMD_HEAL:
                    calculateHealRange();
                    state = SubState.HEALING;
                    context.cursorX = selectedUnit.getX();
                    context.cursorY = selectedUnit.getY();
                    break;

                case ActionMenu.CMD_RALLY:
                    new RallyCommand(selectedUnit, context.unitManager).execute();
                    new WaitCommand(selectedUnit).execute();
                    finishTurn();
                    break;

                case ActionMenu.CMD_ACCESS:
                    new AccessCommand(context.map).execute();
                    if (context.isShowDangerZone()) context.recalculateDangerZone();
                    new WaitCommand(selectedUnit).execute();
                    finishTurn();
                    break;

                case ActionMenu.CMD_WAIT:
                    new WaitCommand(selectedUnit).execute();
                    finishTurn();
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (lastMove != null) {
                lastMove.undo();
                lastMove = null;
                calculateMovementRange();
                state = SubState.MOVING;
                context.cursorX = selectedUnit.getX();
                context.cursorY = selectedUnit.getY();
            }

            else {
                selectedUnit = null;
                state = SubState.BROWSING;
            }
        }
    }

    private void updateHealing() {
        handleCursorInput();

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            state = SubState.ACTION_MENU;
            context.cursorX = selectedUnit.getX();
            context.cursorY = selectedUnit.getY();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            boolean isValid = false;
            for(int[] p : validHealTargets) if(p[0] == context.cursorX && p[1] == context.cursorY) isValid = true;

            if (isValid) {
                Unit target = context.unitManager.getPlayerUnitAt(context.cursorX, context.cursorY);

                if (target != null) {
                    new HealCommand(selectedUnit, target).execute();
                    selectedUnit.setHasMoved(true);
                    finishTurn();
                }
            }
        }
    }

    private void handleCursorInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            context.cursorY++;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            context.cursorY--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            context.cursorX--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            context.cursorX++;
        }

        clampCursor();
        updateCamera(context.cursorX, context.cursorY);
    }

    private void clampCursor() {
        context.cursorX = Math.max(0, Math.min(context.cursorX, context.map.getWidth() - 1));
        context.cursorY = Math.max(0, Math.min(context.cursorY, context.map.getHeight() - 1));
    }

    private void finishTurn() {
        selectedUnit = null;
        lastMove = null;
        hoverSystem.update(context.cursorX, context.cursorY);
        state = SubState.BROWSING;
        boolean allMoved = context.unitManager.getPlayerUnits().stream().allMatch(Unit::hasMoved);

        if (allMoved) {
            GameManager.getInstance().startEnemyPhase(context);
            gsm.set(new EnemyPhaseState(gsm, context));
        }
    }

    private void openActionMenu() {
        boolean isPostMove = (lastMove != null);
        boolean canMove = !isPostMove;
        boolean canAttack = checkCanAttack(selectedUnit);
        boolean canHeal = checkCanHeal(selectedUnit);
        boolean canRally = (selectedUnit.hasSkill(Skill.JADE_RALLY));
        boolean canAccess = false;

        if (selectedUnit.getJob().hasOriginPulse()) {
            TerrainType currentTerrain = context.map.getTile(selectedUnit.getX(), selectedUnit.getY()).getType();

            if (currentTerrain == TerrainType.LEYLINE && !context.config.isEventTriggered()) {
                canAccess = true;
            }
        }

        actionMenu.show(canMove, canAttack, canHeal, canRally, canAccess, isPostMove);
        state = SubState.ACTION_MENU;
    }

    private boolean checkCanAttack(Unit unit) {
        com.kelompok19.finpro.Weapon w = unit.getWeapon();

        if (w == null) {
            return false;
        }

        for (Unit enemy : context.unitManager.getEnemyUnits()) {
            int dist = Math.abs(unit.getX() - enemy.getX()) + Math.abs(unit.getY() - enemy.getY());

            if (dist >= w.rangeMin && dist <= w.rangeMax) {
                return true;
            }
        }

        return false;
    }

    private boolean checkCanHeal(Unit unit) {
        if (unit.getJob() != UnitJob.PRIEST) {
            return false;
        }

        for (Unit ally : context.unitManager.getPlayerUnits()) {
            if (ally == unit) {
                continue;
            }

            int dist = Math.abs(unit.getX() - ally.getX()) + Math.abs(unit.getY() - ally.getY());

            if (dist >= 1 && dist <= 2 && ally.getCurrentHp() < ally.getStats().hp) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidTarget(int x, int y) {
        for (int[] pos : validTargets) if (pos[0] == x && pos[1] == y) {
            return true;
        }

        return false;
    }

    private void calculateAttackRange() {
        validTargets.clear();
        com.kelompok19.finpro.Weapon w = selectedUnit.getWeapon();

        if (w == null) {
            return;
        }

        for (int x = 0; x < context.map.getWidth(); x++) {
            for (int y = 0; y < context.map.getHeight(); y++) {
                int dist = Math.abs(x - selectedUnit.getX()) + Math.abs(y - selectedUnit.getY());

                if (dist >= w.rangeMin && dist <= w.rangeMax) {
                    if (context.unitManager.getEnemyUnitAt(x, y) != null) {
                        validTargets.add(new int[]{x, y});
                    }
                }
            }
        }
    }

    private void calculateHealRange() {
        validHealTargets.clear();

        for (Unit ally : context.unitManager.getPlayerUnits()) {
            if (ally == selectedUnit) {
                continue;
            }

            int dist = Math.abs(selectedUnit.getX() - ally.getX()) + Math.abs(selectedUnit.getY() - ally.getY());

            if (dist >= 1 && dist <= 2) {
                if (ally.getCurrentHp() < ally.getStats().hp) {
                    validHealTargets.add(new int[]{ally.getX(), ally.getY()});
                }
            }
        }
    }

    private void calculateMovementRange() {
        validMoves.clear();
        attackableTiles.clear();

        int maxMove = selectedUnit.getMoveRange();
        int mapW = context.map.getWidth();
        int mapH = context.map.getHeight();

        int[][] costMap = new int[mapW][mapH];

        for(int i=0; i<mapW; i++) for(int j=0; j<mapH; j++) {
            costMap[i][j] = 9999;
        }

        Queue<int[]> queue = new LinkedList<>();
        costMap[selectedUnit.getX()][selectedUnit.getY()] = 0;
        queue.add(new int[]{selectedUnit.getX(), selectedUnit.getY()});
        int[][] dirs = {{0,1}, {0,-1}, {-1,0}, {1,0}};

        while(!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0], cy = current[1];

            if (costMap[cx][cy] >= maxMove) {
                continue;
            }

            for(int[] dir : dirs) {
                int nx = cx + dir[0], ny = cy + dir[1];

                if (nx < 0 || ny < 0 || nx >= mapW || ny >= mapH) {
                    continue;
                }

                int moveCost = context.map.getTile(nx, ny).getType().getMoveCost(selectedUnit.getMovementType());

                if (costMap[cx][cy] + moveCost > maxMove) {
                    continue;
                }

                Unit occupant = context.unitManager.getUnitAt(nx, ny);

                if (occupant != null && occupant != selectedUnit && occupant.getType() != selectedUnit.getType()) {
                    continue;
                }

                if (costMap[nx][ny] > costMap[cx][cy] + moveCost) {
                    costMap[nx][ny] = costMap[cx][cy] + moveCost;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        boolean[][] isBlue = new boolean[mapW][mapH];

        for(int x=0; x<mapW; x++) {
            for(int y=0; y<mapH; y++) {
                if (costMap[x][y] <= maxMove) {
                    if (context.map.getTile(x, y).getType() == TerrainType.RUBBLE) {
                        continue;
                    }

                    if (!context.unitManager.isOccupied(x, y, selectedUnit)) {
                        validMoves.add(new int[]{x, y});
                        isBlue[x][y] = true;
                    }
                }
            }
        }

        if (!isBlue[selectedUnit.getX()][selectedUnit.getY()]) {
            validMoves.add(new int[]{selectedUnit.getX(), selectedUnit.getY()});
            isBlue[selectedUnit.getX()][selectedUnit.getY()] = true;
        }

        com.kelompok19.finpro.Weapon w = selectedUnit.getWeapon();

        if (w != null) {
            for (int[] moveTile : validMoves) {
                int mx = moveTile[0], my = moveTile[1];
                int minRx = Math.max(0, mx - w.rangeMax);
                int maxRx = Math.min(mapW - 1, mx + w.rangeMax);
                int minRy = Math.max(0, my - w.rangeMax);
                int maxRy = Math.min(mapH - 1, my + w.rangeMax);

                for (int x = minRx; x <= maxRx; x++) {
                    for (int y = minRy; y <= maxRy; y++) {
                        if (isBlue[x][y]) {
                            continue;
                        }

                        boolean alreadyRed = false;

                        for(int[] p : attackableTiles) if(p[0] == x && p[1] == y) {
                            alreadyRed = true;
                        }

                        if(alreadyRed) {
                            continue;
                        }

                        int dist = Math.abs(x - mx) + Math.abs(y - my);

                        if (dist >= w.rangeMin && dist <= w.rangeMax) {
                            attackableTiles.add(new int[]{x, y});
                        }
                    }
                }
            }
        }
    }

    @Override public void dispose() {
        actionMenu.dispose();
    }
}
