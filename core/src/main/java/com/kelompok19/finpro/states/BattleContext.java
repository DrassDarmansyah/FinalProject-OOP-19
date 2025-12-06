package com.kelompok19.finpro.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.MapConfig;
import com.kelompok19.finpro.pools.DamagePopupPool;
import com.kelompok19.finpro.ui.DamagePopup;
import com.kelompok19.finpro.ui.MapOverlay;
import com.kelompok19.finpro.ui.TerrainInfoPreview;
import com.kelompok19.finpro.ui.TurnInfoPreview;
import com.kelompok19.finpro.ui.UnitInfoPreview;
import com.kelompok19.finpro.units.UnitManager;
import com.kelompok19.finpro.utils.Pathfinder;

import java.util.List;
import java.util.Set;

public class BattleContext {
    public GameMap map;
    public UnitManager unitManager;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public BitmapFont font;
    public MapOverlay mapOverlay;

    public TerrainInfoPreview terrainPreview;
    public UnitInfoPreview unitPreview;
    public TurnInfoPreview turnPreview;

    public MapConfig config;
    public Texture whitePixel;
    public int cursorX = 0;
    public int cursorY = 0;

    private boolean showDangerZone = false;
    private Set<GridPoint2> dangerZoneCache = null;

    public final DamagePopupPool popupPool = new DamagePopupPool();

    public BattleContext(GameMap map, UnitManager unitManager, OrthographicCamera camera, SpriteBatch batch, BitmapFont font, MapConfig config) {
        this.map = map;
        this.unitManager = unitManager;
        this.camera = camera;
        this.batch = batch;
        this.font = font;
        this.config = config;

        this.mapOverlay = new MapOverlay();
        this.terrainPreview = new TerrainInfoPreview();
        this.unitPreview = new UnitInfoPreview();
        this.turnPreview = new TurnInfoPreview();

        createWhitePixel();
    }

    public void addDamagePopup(String text, int tileX, int tileY, Color color, float scale) {
        float worldX = tileX * 48;
        float worldY = tileY * 48;
        popupPool.obtain(text, worldX, worldY, color, scale);
    }

    public void addMissPopup(int tileX, int tileY) {
        float worldX = tileX * 48;
        float worldY = tileY * 48;
        popupPool.createMiss(worldX, worldY);
    }

    public void updatePopups(float delta) {
        List<DamagePopup> active = popupPool.getInUse();

        for (DamagePopup p : active) {
            if (p.update(delta)) {
                popupPool.release(p);
            }
        }
    }

    public void renderPopups() {
        List<DamagePopup> active = popupPool.getInUse();

        for (DamagePopup p : active) {
            p.render(batch, font);
        }
    }

    private boolean fightAnimationsOn = true;

    public void toggleFightAnimations() {
        fightAnimationsOn = !fightAnimationsOn;
        System.out.println("Animations: " + (fightAnimationsOn ? "ON" : "OFF"));
    }

    public boolean isFightAnimationsOn() {
        return fightAnimationsOn;
    }

    public void toggleDangerZone() {
        showDangerZone = !showDangerZone;

        if (showDangerZone) {
            recalculateDangerZone();
        }

        else dangerZoneCache = null;
    }

    public void recalculateDangerZone() {
        if (showDangerZone) {
            dangerZoneCache = Pathfinder.calculateTotalDangerZone(unitManager.getEnemyUnits(), map, unitManager);
        }
    }

    public boolean isShowDangerZone() {
        return showDangerZone;
    }

    public Set<GridPoint2> getDangerZone() {
        if (showDangerZone && dangerZoneCache == null) {
            recalculateDangerZone();
        }

        return dangerZoneCache;
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
        terrainPreview.dispose();
        unitPreview.dispose();
        turnPreview.dispose();

        if (whitePixel != null) {
            whitePixel.dispose();
        }
    }
}
