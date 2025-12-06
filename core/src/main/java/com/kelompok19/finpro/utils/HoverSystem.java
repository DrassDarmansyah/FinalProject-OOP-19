package com.kelompok19.finpro.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.units.Unit;

import java.util.LinkedList;
import java.util.Queue;

public class HoverSystem {
    private final BattleContext context;

    private Unit hoveredUnit;
    private final Array<int[]> hoverValidMoves = new Array<>();
    private final Array<int[]> hoverAttackableTiles = new Array<>();

    public HoverSystem(BattleContext context) {
        this.context = context;
    }

    public void update(int cursorX, int cursorY) {
        Unit u = context.unitManager.getUnitAt(cursorX, cursorY);

        if (u != hoveredUnit) {
            hoveredUnit = u;

            if (hoveredUnit != null) {
                calculateHoverRanges(hoveredUnit);
            }

            else {
                hoverValidMoves.clear();
                hoverAttackableTiles.clear();
            }
        }
    }

    public void renderRanges(SpriteBatch batch) {
        if (hoveredUnit != null) {
            context.mapOverlay.drawRangeTiles(batch, hoverAttackableTiles, 1, 0.2f);
            context.mapOverlay.drawRangeTiles(batch, hoverValidMoves, 0, 0.2f);
        }
    }

    public void renderUnitInfo(SpriteBatch batch) {
        if (hoveredUnit != null) {
            context.unitPreview.render(batch, context.font, context.camera, hoveredUnit);
        }
    }

    public Unit getHoveredUnit() {
        return hoveredUnit;
    }

    private void calculateHoverRanges(Unit unit) {
        hoverValidMoves.clear();
        hoverAttackableTiles.clear();

        int mapW = context.map.getWidth();
        int mapH = context.map.getHeight();
        int maxMove = unit.getMoveRange();
        int[][] costMap = new int[mapW][mapH];

        for (int i = 0; i < mapW; i++) for (int j = 0; j < mapH; j++) {
            costMap[i][j] = 9999;
        }

        Queue<int[]> queue = new LinkedList<>();
        costMap[unit.getX()][unit.getY()] = 0;
        queue.add(new int[]{unit.getX(), unit.getY()});
        int[][] dirs = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0], cy = current[1];

            if (costMap[cx][cy] >= maxMove) {
                continue;
            }

            for (int[] dir : dirs) {
                int nx = cx + dir[0], ny = cy + dir[1];

                if (nx < 0 || ny < 0 || nx >= mapW || ny >= mapH) {
                    continue;
                }

                int moveCost = context.map.getTile(nx, ny).getType().getMoveCost(unit.getMovementType());

                if (costMap[cx][cy] + moveCost > maxMove) {
                    continue;
                }

                Unit occupant = context.unitManager.getUnitAt(nx, ny);

                if (occupant != null && occupant != unit && occupant.getType() != unit.getType()) {
                    continue;
                }

                if (costMap[nx][ny] > costMap[cx][cy] + moveCost) {
                    costMap[nx][ny] = costMap[cx][cy] + moveCost;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        boolean[][] isBlue = new boolean[mapW][mapH];

        for (int x = 0; x < mapW; x++) {
            for (int y = 0; y < mapH; y++) {
                if (costMap[x][y] <= maxMove) {
                    if (context.map.getTile(x, y).getType() == TerrainType.RUBBLE) {
                        continue;
                    }

                    if (!context.unitManager.isOccupied(x, y, unit)) {
                        hoverValidMoves.add(new int[]{x, y});
                        isBlue[x][y] = true;
                    }
                }
            }
        }

        if (!isBlue[unit.getX()][unit.getY()]) {
            hoverValidMoves.add(new int[]{unit.getX(), unit.getY()});
            isBlue[unit.getX()][unit.getY()] = true;
        }

        Weapon w = unit.getWeapon();

        if (w != null) {
            for (int[] moveTile : hoverValidMoves) {
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

                        for (int[] p : hoverAttackableTiles) {
                            if (p[0] == x && p[1] == y) {
                                alreadyRed = true;
                            }
                        }

                        if (alreadyRed) {
                            continue;
                        }

                        int dist = Math.abs(x - mx) + Math.abs(y - my);

                        if (dist >= w.rangeMin && dist <= w.rangeMax) {
                            hoverAttackableTiles.add(new int[]{x, y});
                        }
                    }
                }
            }
        }
    }
}
