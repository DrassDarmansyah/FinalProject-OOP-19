package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.maps.Tile;

public abstract class BattleState implements GameState {
    protected GameStateManager gsm;
    protected BattleContext context;

    public BattleState(GameStateManager gsm, BattleContext context) {
        this.gsm = gsm;
        this.context = context;
    }

    protected boolean checkObjectives() {
        if (context.config.checkObjective(context.unitManager)) {
            System.out.println("VICTORY CONDITION MET!");
            gsm.set(new MapTransitionState(gsm, context, context.config.getNextMap()));
            return true;
        }

        return false;
    }

    protected void renderMapAndUnits(SpriteBatch batch) {
        context.map.render(context.camera);

        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();

        for(com.kelompok19.finpro.units.Unit u : context.unitManager.getPlayerUnits()) {
            u.render(batch, context.whitePixel);
        }

        for(com.kelompok19.finpro.units.Unit u : context.unitManager.getEnemyUnits()) {
            u.render(batch, context.whitePixel);
        }

        batch.end();
    }

    protected void renderPopups(SpriteBatch batch) {
        context.updatePopups(Gdx.graphics.getDeltaTime());
        batch.begin();
        context.renderPopups();
        batch.end();
    }

    protected void renderTerrainInfo(SpriteBatch batch) {
        Tile tile = context.map.getTile(context.cursorX, context.cursorY);

        if (tile != null) {
            batch.begin();
            context.terrainPreview.render(batch, context.font, context.camera, tile.getType());
            batch.end();
        }
    }

    protected void updateCamera(float targetTileX, float targetTileY) {
        float tileSize = context.map.getTileSize();

        float targetWorldX = targetTileX * tileSize + tileSize / 2f;
        float targetWorldY = targetTileY * tileSize + tileSize / 2f;

        float lerp = 5f * Gdx.graphics.getDeltaTime();
        context.camera.position.x += (targetWorldX - context.camera.position.x) * lerp;
        context.camera.position.y += (targetWorldY - context.camera.position.y) * lerp;

        float viewportHalfW = context.camera.viewportWidth * context.camera.zoom * 0.5f;
        float viewportHalfH = context.camera.viewportHeight * context.camera.zoom * 0.5f;

        float mapW = context.map.getWidth() * tileSize;
        float mapH = context.map.getHeight() * tileSize;

        if (mapW < context.camera.viewportWidth * context.camera.zoom) {
            context.camera.position.x = mapW / 2f;
        }

        else {
            context.camera.position.x = Math.max(viewportHalfW, Math.min(context.camera.position.x, mapW - viewportHalfW));
        }

        if (mapH < context.camera.viewportHeight * context.camera.zoom) {
            context.camera.position.y = mapH / 2f;
        }

        else {
            context.camera.position.y = Math.max(viewportHalfH, Math.min(context.camera.position.y, mapH - viewportHalfH));
        }

        context.camera.update();
    }
}
