package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.MapConfig;
import com.kelompok19.finpro.units.UnitManager;

public class MapTransitionState implements GameState {
    private final GameStateManager gsm;
    private final BattleContext oldContext;
    private final MapConfig nextMapConfig;
    private final BitmapFont font;
    private float timer = 2.0f;

    public MapTransitionState(GameStateManager gsm, BattleContext oldContext, MapConfig nextMapConfig) {
        this.gsm = gsm;
        this.oldContext = oldContext;
        this.nextMapConfig = nextMapConfig;
        this.font = new BitmapFont();
        this.font.getData().setScale(2.0f);
    }

    @Override
    public void update(float delta) {
        timer -= delta;

        if (timer <= 0) {
            loadNextMap();
        }
    }

    private void loadNextMap() {
        if (nextMapConfig == null) {
            System.out.println("GAME FINISHED!");
            Gdx.app.exit();
            return;
        }

        oldContext.dispose();

        GameMap newMap = new GameMap(nextMapConfig);
        UnitManager unitManager = oldContext.unitManager;
        unitManager.healAllUnits();
        BattleContext newContext = new BattleContext(newMap, unitManager, oldContext.camera, oldContext.batch, oldContext.font, nextMapConfig);
        gsm.set(new UnitSelectionState(gsm, newContext));
    }

    @Override
    public void render(SpriteBatch batch) {
        oldContext.map.render(oldContext.camera);
        batch.begin();
        font.setColor(Color.YELLOW);
        String text = (nextMapConfig == null) ? "GAME CLEAR!" : "MAP COMPLETE!";
        GlyphLayout layout = new GlyphLayout(font, text);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2;
        font.draw(batch, text, x, y);
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
