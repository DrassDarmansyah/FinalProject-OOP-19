package com.kelompok19.finpro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.chapters.Chapter1;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.MapConfig;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.states.GameStateManager;
import com.kelompok19.finpro.states.BattlePrepsState;
import com.kelompok19.finpro.units.UnitManager;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;

    private GameStateManager gsm;
    private GameMap map;
    private UnitManager unitManager;
    private OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        gsm = new GameStateManager();

        MapConfig startConfig = new Chapter1();
        map = new GameMap(startConfig);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);
        camera.setToOrtho(false, w, h);
        camera.zoom = 1.0f;
        camera.position.set(map.getWidth() * map.getTileSize() / 2f, map.getHeight() * map.getTileSize() / 2f, 0);
        camera.update();

        unitManager = new UnitManager(true);

        BattleContext context = new BattleContext(map, unitManager, camera, batch, font, startConfig);

        gsm.push(new BattlePrepsState(gsm, context));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        map.dispose();
        CombatPreview.dispose();
    }
}
