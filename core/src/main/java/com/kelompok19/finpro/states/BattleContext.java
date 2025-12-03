package com.kelompok19.finpro.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.ui.MapOverlay;
import com.kelompok19.finpro.ui.TerrainInfo;
import com.kelompok19.finpro.units.UnitManager;

public class BattleContext {
    public GameMap map;
    public UnitManager unitManager;
    public OrthographicCamera camera;

    public SpriteBatch batch;
    public BitmapFont font;
    public MapOverlay mapOverlay;
    public TerrainInfo terrainInfo;

    public Texture whitePixel;

    public int cursorX = 0;
    public int cursorY = 0;

    public BattleContext(GameMap map, UnitManager unitManager, OrthographicCamera camera, SpriteBatch batch, BitmapFont font) {
        this.map = map;
        this.unitManager = unitManager;
        this.camera = camera;
        this.batch = batch;
        this.font = font;
        this.mapOverlay = new MapOverlay();
        this.terrainInfo = new TerrainInfo();

        createWhitePixel();
    }

    private void createWhitePixel() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        whitePixel = new Texture(p);
        p.dispose();
    }

    public void dispose() {
        mapOverlay.dispose();
        terrainInfo.dispose();
        if (whitePixel != null) whitePixel.dispose();
    }
}
