package com.kelompok19.finpro.maps;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapFactory {
    public static GameMap createMap(String tmxFilePath) {
        TiledMap tiledMap = new TmxMapLoader().load(tmxFilePath);

        int width = tiledMap.getProperties().get("width", Integer.class);
        int height = tiledMap.getProperties().get("height", Integer.class);

        Tile[][] tiles = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                TerrainType type = TerrainType.PLAIN;

                type = checkLayerForTerrain(tiledMap, "ground", x, y, type);
                type = checkLayerForTerrain(tiledMap, "background", x, y, type);
                type = checkLayerForTerrain(tiledMap, "foreground", x, y, type);
                type = checkLayerForTerrain(tiledMap, "foregroundTop", x, y, type);
                type = checkLayerForTerrain(tiledMap, "objects", x, y, type);

                tiles[x][y] = new Tile(x, y, type);
            }
        }

        return new GameMap(tiledMap, tiles, width, height);
    }

    private static TerrainType checkLayerForTerrain(TiledMap map, String layerName, int x, int y, TerrainType currentType) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);

        if (layer == null) return currentType;

        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell == null) return currentType;

        TiledMapTile tile = cell.getTile();
        if (tile == null) return currentType;

        Object property = tile.getProperties().get("terrain");

        if (property != null) {
            String terrainName = property.toString().toUpperCase();
            try {
                return TerrainType.valueOf(terrainName);
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown terrain type in TMX: " + terrainName);
            }
        }

        return currentType;
    }
}
