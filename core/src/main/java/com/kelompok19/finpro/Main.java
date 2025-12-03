package com.kelompok19.finpro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kelompok19.finpro.commands.InputHandler;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.MapFactory;
import com.kelompok19.finpro.units.UnitManager;

public class Main extends ApplicationAdapter {
    private GameMap gameMap;
    private UnitManager unitManager;
    private InputHandler inputHandler;
    private Renderer renderer;

    private OrthographicCamera camera;

    @Override
    public void create() {
        gameMap = MapFactory.createMap("maps/map1.tmx");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);
        camera.setToOrtho(false, w, h);

        camera.zoom = 1.0f;

        float mapWidthWorld = gameMap.getWidth() * gameMap.getTileSize();
        float mapHeightWorld = gameMap.getHeight() * gameMap.getTileSize();
        camera.position.set(mapWidthWorld / 2f, mapHeightWorld / 2f, 0);
        camera.update();

        unitManager = new UnitManager(gameMap.getHeight());
        inputHandler = new InputHandler(gameMap, unitManager);

        renderer = new Renderer(camera);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        inputHandler.update();

        updateCameraPosition();

        renderer.render(gameMap, unitManager, inputHandler);
    }

    private void updateCameraPosition() {
        final float TILE_SIZE = gameMap.getTileSize();
        float mapW = gameMap.getWidth() * TILE_SIZE;
        float mapH = gameMap.getHeight() * TILE_SIZE;

        float targetX = inputHandler.getCursorX() * TILE_SIZE + TILE_SIZE / 2f;
        float targetY = inputHandler.getCursorY() * TILE_SIZE + TILE_SIZE / 2f;

        float lerpSpeed = 0.1f;
        camera.position.x += (targetX - camera.position.x) * lerpSpeed;
        camera.position.y += (targetY - camera.position.y) * lerpSpeed;

        float viewportHalfW = camera.viewportWidth * camera.zoom * 0.5f;
        float viewportHalfH = camera.viewportHeight * camera.zoom * 0.5f;

        if (mapW < camera.viewportWidth * camera.zoom) {
            camera.position.x = mapW / 2f;
        } else {
            float minX = viewportHalfW;
            float maxX = mapW - viewportHalfW;
            camera.position.x = Math.max(minX, Math.min(camera.position.x, maxX));
        }

        if (mapH < camera.viewportHeight * camera.zoom) {
            camera.position.y = mapH / 2f;
        } else {
            float minY = viewportHalfH;
            float maxY = mapH - viewportHalfH;
            camera.position.y = Math.max(minY, Math.min(camera.position.y, maxY));
        }

        camera.update();
    }

    @Override
    public void dispose() {
        if (renderer != null) {
            renderer.dispose();
        }

        if (gameMap != null) {
            gameMap.dispose();
        }
    }
}
