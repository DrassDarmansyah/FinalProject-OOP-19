package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.commands.MoveCommand;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.units.Unit;

import java.util.LinkedList;
import java.util.Queue;

public class MoveSelectionState extends BattleState {
    private final Unit unit;
    private final Array<int[]> validMoves = new Array<>();
    private final Array<int[]> attackableTiles = new Array<>();

    public MoveSelectionState(GameStateManager gsm, BattleContext context, Unit unit) {
        super(gsm, context);
        this.unit = unit;
        context.cursorX = unit.getX();
        context.cursorY = unit.getY();
        calculateMovementAndAttackRange();
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) context.cursorY++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) context.cursorY--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) context.cursorX--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) context.cursorX++;
        clampCursor();
        updateCamera(context.cursorX, context.cursorY);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            gsm.pop();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (isValidMove(context.cursorX, context.cursorY)) {
                MoveCommand move = new MoveCommand(unit, context.cursorX, context.cursorY);
                move.execute();
                gsm.set(new ActionMenuState(gsm, context, unit, move));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);
        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();
        context.mapOverlay.drawRangeTiles(batch, attackableTiles, true);
        context.mapOverlay.drawRangeTiles(batch, validMoves, false);
        context.mapOverlay.drawCursor(batch, context.cursorX, context.cursorY);
        batch.end();
        renderTerrainInfo(batch);
    }

    private void calculateMovementAndAttackRange() {
        validMoves.clear();
        attackableTiles.clear();

        int mapW = context.map.getWidth();
        int mapH = context.map.getHeight();
        int maxMove = unit.getMoveRange();

        int[][] costMap = new int[mapW][mapH];
        for(int i=0; i<mapW; i++) for(int j=0; j<mapH; j++) costMap[i][j] = 9999;

        Queue<int[]> queue = new LinkedList<>();

        costMap[unit.getX()][unit.getY()] = 0;
        queue.add(new int[]{unit.getX(), unit.getY()});

        int[][] dirs = {{0,1}, {0,-1}, {-1,0}, {1,0}};

        while(!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0];
            int cy = current[1];
            int currentCost = costMap[cx][cy];

            if (currentCost >= maxMove) continue;

            for(int[] dir : dirs) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (nx < 0 || ny < 0 || nx >= mapW || ny >= mapH) continue;

                TerrainType terrain = context.map.getTile(nx, ny).getType();
                int moveCost = terrain.getMoveCost(unit.getMovementType());

                if (currentCost + moveCost > maxMove) continue;

                Unit occupant = context.unitManager.getUnitAt(nx, ny);
                if (occupant != null && occupant != unit && occupant.getType() != unit.getType()) {
                    continue;
                }

                if (costMap[nx][ny] > currentCost + moveCost) {
                    costMap[nx][ny] = currentCost + moveCost;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        boolean[][] isBlue = new boolean[mapW][mapH];

        for(int x=0; x<mapW; x++) {
            for(int y=0; y<mapH; y++) {
                if (costMap[x][y] <= maxMove) {
                    TerrainType terrain = context.map.getTile(x, y).getType();

                    if (terrain == TerrainType.RUBBLE) continue;

                    if (!context.unitManager.isOccupied(x, y, unit)) {
                        validMoves.add(new int[]{x, y});
                        isBlue[x][y] = true;
                    }
                }
            }
        }

        if (!isBlue[unit.getX()][unit.getY()]) {
            validMoves.add(new int[]{unit.getX(), unit.getY()});
            isBlue[unit.getX()][unit.getY()] = true;
        }

        Weapon w = unit.getWeapon();
        if (w == null) return;

        for (int[] moveTile : validMoves) {
            int mx = moveTile[0];
            int my = moveTile[1];

            int minRx = Math.max(0, mx - w.rangeMax);
            int maxRx = Math.min(mapW - 1, mx + w.rangeMax);
            int minRy = Math.max(0, my - w.rangeMax);
            int maxRy = Math.min(mapH - 1, my + w.rangeMax);

            for (int x = minRx; x <= maxRx; x++) {
                for (int y = minRy; y <= maxRy; y++) {
                    if (isBlue[x][y]) continue;
                    if (isAlreadyRed(x, y)) continue;

                    int dist = Math.abs(x - mx) + Math.abs(y - my);
                    if (dist >= w.rangeMin && dist <= w.rangeMax) {
                        attackableTiles.add(new int[]{x, y});
                    }
                }
            }
        }
    }

    private boolean isAlreadyRed(int x, int y) {
        for(int[] p : attackableTiles) {
            if (p[0] == x && p[1] == y) return true;
        }
        return false;
    }

    private boolean isValidMove(int x, int y) {
        for (int[] pos : validMoves) if (pos[0] == x && pos[1] == y) return true;
        return false;
    }

    private void clampCursor() {
        context.cursorX = Math.max(0, Math.min(context.cursorX, context.map.getWidth() - 1));
        context.cursorY = Math.max(0, Math.min(context.cursorY, context.map.getHeight() - 1));
    }

    @Override public void dispose() {}
}
