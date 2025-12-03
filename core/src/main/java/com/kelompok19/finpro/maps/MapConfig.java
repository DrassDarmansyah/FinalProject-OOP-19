package com.kelompok19.finpro.maps;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public enum MapConfig {
    CHAPTER_1("maps/map1.tmx") {
        @Override
        public void onLeyline(GameMap map) {
            System.out.println("LEYLINE ACTIVATED: The waters recede!");

            TiledMapTileLayer backgroundLayer = (TiledMapTileLayer) map.getTiledMap().getLayers().get("background");
            if (backgroundLayer == null) return;

            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = backgroundLayer.getCell(x, y);

                    if (cell != null && cell.getTile() != null) {
                        Object property = cell.getTile().getProperties().get("terrain");

                        if (property != null && "WATER".equals(property.toString().toUpperCase())) {

                            backgroundLayer.setCell(x, y, null);

                            Tile logicTile = map.getTile(x, y);
                            if (logicTile.getType() == TerrainType.WATER) {
                                map.setTileLogic(x, y, TerrainType.PLAIN);
                            }
                        }
                    }
                }
            }
        }
    },

    CHAPTER_2("maps/map2.tmx") {
        @Override
        public void onLeyline(GameMap map) {
            System.out.println("The Leyline hums, but nothing happens...");
        }
    };

    private final String filePath;

    MapConfig(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public abstract void onLeyline(GameMap map);
}
