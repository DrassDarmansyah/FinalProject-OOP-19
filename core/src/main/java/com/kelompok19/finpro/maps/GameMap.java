package com.kelompok19.finpro.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class GameMap implements Disposable {
    private final int width;
    private final int height;
    private final int tileSize;
    private final Tile[][] tiles;

    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final MapConfig config;

    public GameMap(MapConfig config) {
        this.config = config;

        this.tiledMap = new TmxMapLoader().load(config.getFilePath());
        this.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        this.width = tiledMap.getProperties().get("width", Integer.class);
        this.height = tiledMap.getProperties().get("height", Integer.class);
        this.tileSize = tiledMap.getProperties().get("tilewidth", Integer.class);

        this.tiles = new Tile[width][height];
        parseGrid();
    }

    public void triggerLeylineEffect() {
        config.onLeyline(this);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTileLogic(int x, int y, TerrainType newType) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = new Tile(x, y, newType);
        }
    }

    public void render(OrthographicCamera camera) {
        if (mapRenderer != null) {
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
    }

    private void parseGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TerrainType type = TerrainType.PLAIN;

                type = checkLayerForTerrain("ground", x, y, type);
                type = checkLayerForTerrain("background", x, y, type);
                type = checkLayerForTerrain("foreground", x, y, type);
                type = checkLayerForTerrain("foregroundTop", x, y, type);
                type = checkLayerForTerrain("objects", x, y, type);

                tiles[x][y] = new Tile(x, y, type);
            }
        }
    }

    private TerrainType checkLayerForTerrain(String layerName, int x, int y, TerrainType currentType) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(layerName);

        if (layer == null) {
            return currentType;
        }

        TiledMapTileLayer.Cell cell = layer.getCell(x, y);

        if (cell == null) {
            return currentType;
        }

        TiledMapTile tile = cell.getTile();

        if (tile == null) {
            return currentType;
        }

        Object property = tile.getProperties().get("terrain");

        if (property != null) {
            String terrainName = property.toString().toUpperCase();

            try {
                return TerrainType.valueOf(terrainName);
            }

            catch (IllegalArgumentException e) {
                System.err.println("Map Warning: Unknown terrain '" + terrainName + "' at " + x + "," + y);
            }
        }

        return currentType;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return null;
        }

        return tiles[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public float getPixelWidth() {
        return width * tileSize;
    }

    public float getPixelHeight() {
        return height * tileSize;
    }

    @Override
    public void dispose() {
        if (tiledMap != null) {
            tiledMap.dispose();
        }

        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
    }
}
