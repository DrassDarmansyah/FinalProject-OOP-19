package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.units.Unit;

import java.util.List;

public class BrowsingState extends BattleState {

    public BrowsingState(GameStateManager gsm, BattleContext context) {
        super(gsm, context);

        if (context.cursorX == 0 && context.cursorY == 0) {
            List<Unit> players = context.unitManager.getPlayerUnits();
            if (!players.isEmpty()) {
                Unit p = players.get(0);
                context.cursorX = p.getX();
                context.cursorY = p.getY();
                context.camera.position.set(p.getX() * 48, p.getY() * 48, 0);
            }
        }
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) context.cursorY++;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) context.cursorY--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) context.cursorX--;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) context.cursorX++;
        clampCursor();

        updateCamera(context.cursorX, context.cursorY);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Unit u = context.unitManager.getPlayerUnitAt(context.cursorX, context.cursorY);
            if (u != null && !u.hasMoved()) {
                gsm.push(new ActionMenuState(gsm, context, u, null));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);
        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();
        context.mapOverlay.drawCursor(batch, context.cursorX, context.cursorY);
        batch.end();
        renderTerrainInfo(batch);
    }

    private void clampCursor() {
        context.cursorX = Math.max(0, Math.min(context.cursorX, context.map.getWidth() - 1));
        context.cursorY = Math.max(0, Math.min(context.cursorY, context.map.getHeight() - 1));
    }

    @Override public void dispose() {}
}
