package com.kelompok19.finpro.ai;

import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.commands.AttackCommand;
import com.kelompok19.finpro.commands.MoveCommand;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.states.GameStateManager;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.utils.Pathfinder;

public class AggressiveBehavior implements AIBehavior {
    @Override
    public boolean act(Unit me, BattleContext context, GameStateManager gsm) {
        Unit closestTarget = findClosestTarget(me, context);

        if (closestTarget == null) {
            return false;
        }

        Array<int[]> moves = Pathfinder.getValidMoves(me, context);
        int[] bestTile = null;
        int minDistance = 9999;
        boolean canAttackFromBest = false;
        Weapon w = me.getWeapon();

        for (int[] tile : moves) {
            int distToTarget = Math.abs(tile[0] - closestTarget.getX()) + Math.abs(tile[1] - closestTarget.getY());
            boolean canAttack = false;

            if (w != null && distToTarget >= w.rangeMin && distToTarget <= w.rangeMax) {
                canAttack = true;
            }

            if (bestTile == null) {
                bestTile = tile;
                minDistance = distToTarget;
                canAttackFromBest = canAttack;
            }

            else {
                if (canAttack && !canAttackFromBest) {
                    bestTile = tile;
                    minDistance = distToTarget;
                    canAttackFromBest = true;
                }

                else if (canAttack == canAttackFromBest) {
                    if (distToTarget < minDistance) {
                        bestTile = tile;
                        minDistance = distToTarget;
                    }
                }
            }
        }

        if (bestTile != null) {
            new MoveCommand(me, bestTile[0], bestTile[1]).execute();

            if (canAttackFromBest) {
                new AttackCommand(me, closestTarget, context, gsm).execute();
            }

            return true;
        }

        return false;
    }

    private Unit findClosestTarget(Unit me, BattleContext context) {
        Unit closest = null;
        int minDist = 9999;

        for (Unit p : context.unitManager.getPlayerUnits()) {
            int d = Math.abs(me.getX() - p.getX()) + Math.abs(me.getY() - p.getY());

            if (d < minDist) {
                minDist = d;
                closest = p;
            }
        }

        return closest;
    }
}
