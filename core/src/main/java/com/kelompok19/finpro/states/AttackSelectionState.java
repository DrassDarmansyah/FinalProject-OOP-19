package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.combat.CombatEngine;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.commands.AttackCommand;
import com.kelompok19.finpro.commands.MoveCommand;
import com.kelompok19.finpro.units.Unit;

public class AttackSelectionState extends BattleState {
    private final Unit attacker;
    private final MoveCommand lastMove;
    private final Array<int[]> validTargets = new Array<>();
    private CombatPreview preview;

    public AttackSelectionState(GameStateManager gsm, BattleContext context, Unit attacker, MoveCommand lastMove) {
        super(gsm, context);
        this.attacker = attacker;
        this.lastMove = lastMove;

        context.cursorX = attacker.getX();
        context.cursorY = attacker.getY();

        calculateAttackRange();
    }

    @Override
    public void update(float delta) {
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

        Unit target = context.unitManager.getEnemyUnitAt(context.cursorX, context.cursorY);

        if (target != null && isValidTarget(context.cursorX, context.cursorY)) {
            preview = CombatEngine.calculateCombat(attacker, target, context.map);
        }

        else {
            preview = null;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            gsm.set(new ActionMenuState(gsm, context, attacker, lastMove));
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (target != null && isValidTarget(context.cursorX, context.cursorY)) {
                AttackCommand attack = new AttackCommand(attacker, target, context.map);
                attack.execute();
                attacker.setHasMoved(true);
                context.unitManager.cleanup();
                gsm.pop();
                boolean allMoved = context.unitManager.getPlayerUnits().stream().allMatch(Unit::hasMoved);

                if (allMoved) {
                    GameManager.getInstance().endPlayerTurn(context.unitManager);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);
        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();

        context.mapOverlay.drawRangeTiles(batch, validTargets, true);
        context.mapOverlay.drawCursor(batch, context.cursorX, context.cursorY);

        if (preview != null) {
            preview.render(batch, context.font, context.camera);
        }

        batch.end();

        renderTerrainInfo(batch);
    }

    private void calculateAttackRange() {
        validTargets.clear();
        Weapon w = attacker.getWeapon();

        for (int x = 0; x < context.map.getWidth(); x++) {
            for (int y = 0; y < context.map.getHeight(); y++) {
                int dist = Math.abs(x - attacker.getX()) + Math.abs(y - attacker.getY());

                if (dist >= w.rangeMin && dist <= w.rangeMax) {
                    if (context.unitManager.getEnemyUnitAt(x, y) != null) {
                        validTargets.add(new int[]{x, y});
                    }
                }
            }
        }
    }

    private boolean isValidTarget(int x, int y) {
        for (int[] pos : validTargets) if (pos[0] == x && pos[1] == y) {
            return true;
        }

        return false;
    }

    private void clampCursor() {
        context.cursorX = Math.max(0, Math.min(context.cursorX, context.map.getWidth() - 1));
        context.cursorY = Math.max(0, Math.min(context.cursorY, context.map.getHeight() - 1));
    }

    @Override public void dispose() {

    }
}
