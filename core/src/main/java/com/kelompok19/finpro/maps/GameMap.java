package com.kelompok19.finpro.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class GameMap implements Disposable {
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    private final int TILE_SIZE = 48;

    public GameMap(TiledMap tiledMap, Tile[][] tiles, int width, int height) {
        this.tiledMap = tiledMap;
        this.tiles = tiles;
        this.width = width;
        this.height = height;

        this.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render(OrthographicCamera camera) {
        if (mapRenderer != null) {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public float getPixelWidth() {
        return width * TILE_SIZE;
    }

    public float getPixelHeight() {
        return height * TILE_SIZE;
    }

    @Override
    public void dispose() {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}
