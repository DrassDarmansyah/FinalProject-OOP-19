package com.kelompok19.finpro.utils;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.units.MovementType;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;

import java.util.*;

public class Pathfinder {
    public static Array<int[]> getValidMoves(Unit unit, BattleContext context) {
        return getValidMoves(
            unit.getX(),
            unit.getY(),
            unit.getMoveRange(),
            unit.getMovementType(),
            context.map,
            context.unitManager,
            unit
        );
    }

    public static Array<int[]> getValidMoves(int startX, int startY, int maxMove, MovementType moveType, GameMap map, UnitManager unitManager, Unit ignoreUnit) {
        Array<int[]> validMoves = new Array<>();
        int mapW = map.getWidth();
        int mapH = map.getHeight();
        int[][] costMap = new int[mapW][mapH];

        for (int i = 0; i < mapW; i++) for (int j = 0; j < mapH; j++) costMap[i][j] = 9999;

        Queue<int[]> queue = new LinkedList<>();
        costMap[startX][startY] = 0;
        queue.add(new int[]{startX, startY});
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

                int moveCost = map.getTile(nx, ny).getType().getMoveCost(moveType);

                if (costMap[cx][cy] + moveCost > maxMove) {
                    continue;
                }

                Unit occupant = unitManager.getUnitAt(nx, ny);
                if (occupant != null && occupant != ignoreUnit && occupant.getType() != ignoreUnit.getType()) {
                    continue;
                }

                if (costMap[nx][ny] > costMap[cx][cy] + moveCost) {
                    costMap[nx][ny] = costMap[cx][cy] + moveCost;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        for (int x = 0; x < mapW; x++) {
            for (int y = 0; y < mapH; y++) {
                if (costMap[x][y] <= maxMove) {
                    if (map.getTile(x, y).getType() == TerrainType.RUBBLE) {
                        continue;
                    }

                    if (!unitManager.isOccupied(x, y, ignoreUnit)) {
                        validMoves.add(new int[]{x, y});
                    }
                }
            }
        }

        validMoves.add(new int[]{startX, startY});
        return validMoves;
    }

    public static Set<GridPoint2> calculateTotalDangerZone(List<Unit> units, GameMap map, UnitManager unitManager) {
        Set<GridPoint2> dangerZone = new HashSet<>();

        for (Unit unit : units) {
            if (unit.getCurrentHp() <= 0) {
                continue;
            }

            Array<int[]> moveTiles = getValidMoves(
                unit.getX(), unit.getY(),
                unit.getMoveRange(), unit.getMovementType(),
                map, unitManager, unit
            );

            Weapon weapon = unit.getWeapon();
            int minRange = (weapon != null) ? weapon.rangeMin : 1;
            int maxRange = (weapon != null) ? weapon.rangeMax : 1;

            for (int[] moveTile : moveTiles) {
                dangerZone.add(new GridPoint2(moveTile[0], moveTile[1]));
                int mx = moveTile[0];
                int my = moveTile[1];

                for (int x = mx - maxRange; x <= mx + maxRange; x++) {
                    for (int y = my - maxRange; y <= my + maxRange; y++) {
                        if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
                            continue;
                        }

                        int dist = Math.abs(mx - x) + Math.abs(my - y);

                        if (dist >= minRange && dist <= maxRange) {
                            dangerZone.add(new GridPoint2(x, y));
                        }
                    }
                }
            }
        }

        return dangerZone;
    }
}
