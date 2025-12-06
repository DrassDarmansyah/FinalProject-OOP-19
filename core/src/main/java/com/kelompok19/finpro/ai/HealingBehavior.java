package com.kelompok19.finpro.ai;

import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.commands.HealCommand;
import com.kelompok19.finpro.commands.MoveCommand;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.states.GameStateManager;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitJob;
import com.kelompok19.finpro.utils.Pathfinder;

public class HealingBehavior implements AIBehavior {
    private final AIBehavior fallback;

    public HealingBehavior() {
        this.fallback = new DefensiveBehavior();
    }

    @Override
    public boolean act(Unit me, BattleContext context, GameStateManager gsm) {
        if (me.getJob() == UnitJob.PRIEST) {
            return actAsHealer(me, context, gsm);
        }

        else {
            return actAsPatient(me, context, gsm);
        }
    }

    private boolean actAsHealer(Unit me, BattleContext context, GameStateManager gsm) {
        Unit patient = null;
        float lowestHpRatio = 1.0f;

        for (Unit ally : context.unitManager.getEnemyUnits()) {
            if (ally == me) {
                continue;
            }

            float ratio = (float) ally.getCurrentHp() / ally.getStats().hp;

            if (ratio < 1.0f) {
                if (ratio < lowestHpRatio) {
                    lowestHpRatio = ratio;
                    patient = ally;
                }
            }
        }

        if (patient == null) {
            return fallback.act(me, context, gsm);
        }

        Array<int[]> moves = Pathfinder.getValidMoves(me, context);
        int[] bestTile = null;
        int minMoveDist = 9999;

        for (int[] tile : moves) {
            int distToPatient = Math.abs(tile[0] - patient.getX()) + Math.abs(tile[1] - patient.getY());

            if (distToPatient >= 1 && distToPatient <= 2) {
                int distFromStart = Math.abs(tile[0] - me.getX()) + Math.abs(tile[1] - me.getY());

                if (distFromStart < minMoveDist) {
                    minMoveDist = distFromStart;
                    bestTile = tile;
                }
            }
        }

        if (bestTile != null) {
            new MoveCommand(me, bestTile[0], bestTile[1]).execute();
            new HealCommand(me, patient).execute();
            return true;
        }

        return fallback.act(me, context, gsm);
    }

    private boolean actAsPatient(Unit me, BattleContext context, GameStateManager gsm) {
        float hpRatio = (float) me.getCurrentHp() / (float) me.getStats().hp;

        if (hpRatio > 0.5f) {
            return fallback.act(me, context, gsm);
        }

        Unit closestPriest = null;
        int minDist = 9999;

        for (Unit ally : context.unitManager.getEnemyUnits()) {
            if (ally.getJob() == UnitJob.PRIEST) {
                int d = Math.abs(me.getX() - ally.getX()) + Math.abs(me.getY() - ally.getY());

                if (d < minDist) {
                    minDist = d;
                    closestPriest = ally;
                }
            }
        }

        if (closestPriest == null) {
            return fallback.act(me, context, gsm);
        }

        Array<int[]> moves = Pathfinder.getValidMoves(me, context);
        int[] bestTile = null;
        int bestDistToPriest = 9999;

        for (int[] tile : moves) {
            int d = Math.abs(tile[0] - closestPriest.getX()) + Math.abs(tile[1] - closestPriest.getY());

            if (d < bestDistToPriest) {
                bestDistToPriest = d;
                bestTile = tile;
            }
        }

        if (bestTile != null) {
            int currentDist = Math.abs(me.getX() - closestPriest.getX()) + Math.abs(me.getY() - closestPriest.getY());

            if (bestDistToPriest < currentDist) {
                new MoveCommand(me, bestTile[0], bestTile[1]).execute();
                return true;
            }
        }

        return fallback.act(me, context, gsm);
    }
}
