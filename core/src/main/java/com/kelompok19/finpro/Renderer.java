package com.kelompok19.finpro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.commands.ActionMenu;
import com.kelompok19.finpro.commands.InputHandler;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;
import com.kelompok19.finpro.units.UnitType;

import java.util.List;

public class Renderer {
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;

    private final int TILE_SIZE = 48;

    private Texture indicatorTex;
    private Texture playerTex, enemyTex, movedTex, moveRangeTex, attackRangeTex;
    private Texture menuBgTex;
    private Texture uiBoxTex;

    public Renderer(OrthographicCamera camera) {
        this.camera = camera;
        batch = new SpriteBatch();
        font = new BitmapFont();
        loadTextures();
    }

    private void loadTextures() {
        indicatorTex = createColoredTexture(TILE_SIZE, new Color(1, 1, 0, 0.5f));

        playerTex = createColoredTexture(48, Color.BLUE);
        enemyTex = createColoredTexture(48, Color.RED);
        movedTex = createColoredTexture(48, Color.GRAY);

        moveRangeTex = createColoredTexture(TILE_SIZE, new Color(0, 1, 1, 0.3f));
        attackRangeTex = createColoredTexture(TILE_SIZE, new Color(1, 0, 0, 0.3f));

        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0.8f);
        p.fill();
        menuBgTex = new Texture(p);

        Pixmap p2 = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p2.setColor(0.1f, 0.1f, 0.3f, 0.9f);
        p2.fill();
        uiBoxTex = new Texture(p2);

        p.dispose();
        p2.dispose();
    }

    private Texture createColoredTexture(int size, Color color) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture t = new Texture(pixmap);
        pixmap.dispose();
        return t;
    }

    public void render(GameMap map, UnitManager unitManager, InputHandler input) {
        camera.update();
        map.render(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (input.getState() == InputHandler.GameState.SHOWING_MOVE_RANGE) {
            for (int[] pos : input.getMoveRangeTiles())
                batch.draw(moveRangeTex, pos[0] * TILE_SIZE, pos[1] * TILE_SIZE);
        }
        if (input.getState() == InputHandler.GameState.SHOWING_ATTACK_RANGE) {
            for (int[] pos : input.getAttackRangeTiles())
                batch.draw(attackRangeTex, pos[0] * TILE_SIZE, pos[1] * TILE_SIZE);
        }

        drawUnits(unitManager.getPlayerUnits());
        drawUnits(unitManager.getEnemyUnits());

        batch.draw(indicatorTex, input.getCursorX() * TILE_SIZE, input.getCursorY() * TILE_SIZE);

        if (input.getState() == InputHandler.GameState.ACTION_MENU) {
            drawMenu(input.getActionMenu(), input.getCursorX(), input.getCursorY());
        }

        if (input.getCombatPreview() != null) {
            drawCombatPreview(input.getCombatPreview());
        }

        batch.end();
    }

    private void drawUnits(List<Unit> units) {
        float delta = Gdx.graphics.getDeltaTime();

        for (Unit unit : units) {
            TextureRegion frame = unit.getCurrentFrame();

            if (unit.getType() == UnitType.PLAYER)
                unit.update(delta);

            Texture t;
            float w, h;

            if (frame != null) {
                w = frame.getRegionWidth();
                h = frame.getRegionHeight();
                t = null;
            } else {
                t = (unit.getType() == UnitType.ENEMY)
                    ? enemyTex
                    : (unit.hasMoved() ? movedTex : playerTex);

                w = t.getWidth();
                h = t.getHeight();
            }

            float x = unit.getX() * TILE_SIZE + (TILE_SIZE - w) / 2;
            float y = unit.getY() * TILE_SIZE + (TILE_SIZE - h) / 2;

            if (frame != null) batch.draw(frame, x, y);
            else batch.draw(t, x, y, w, h);

            font.setColor(Color.WHITE);
            font.getData().setScale(0.8f);
            font.draw(batch, "" + unit.getCurrentHp(), x, y + h + 5);
            font.getData().setScale(1f);
        }
    }

    private void drawMenu(ActionMenu menu, int cx, int cy) {
        List<String> options = menu.getOptions();
        float menuX = cx * TILE_SIZE + TILE_SIZE + 5;
        float menuY = cy * TILE_SIZE + TILE_SIZE;
        float width = 80;
        float height = options.size() * 25 + 10;

        if (menuX + width > camera.position.x + camera.viewportWidth * camera.zoom / 2)
            menuX = cx * TILE_SIZE - width - 5;

        batch.draw(menuBgTex, menuX, menuY - height, width, height);

        for (int i = 0; i < options.size(); i++) {
            font.setColor(i == menu.getSelectedIndex() ? Color.YELLOW : Color.WHITE);
            font.draw(batch, options.get(i),
                menuX + 5, menuY - 10 - (i * 25));
        }
    }

    private void drawCombatPreview(CombatPreview cp) {
        float width = 300;
        float height = 150;
        float x = camera.position.x - width / 2;
        float y = camera.position.y - (camera.viewportHeight * camera.zoom / 2) + 20;

        batch.draw(uiBoxTex, x, y, width, height);

        font.setColor(Color.WHITE);
        font.draw(batch, "COMBAT PREVIEW", x + width/2 - 50, y + height - 10);

        float col1 = x + 40;
        float col2 = x + 200;
        float row = y + height - 35;
        float lh = 20;

        font.setColor(Color.CYAN);
        font.draw(batch, cp.attackerName, col1, row);

        font.setColor(Color.WHITE);
        font.draw(batch, "HP: " + cp.aHp + "/" + cp.aMaxHp, col1, row - lh);
        font.draw(batch, "Dmg: " + cp.aDmg + (cp.aHits > 1 ? " x" + cp.aHits : ""),
            col1, row - lh * 2);
        font.draw(batch, "Hit: " + cp.aHit + "%", col1, row - lh * 3);
        font.draw(batch, "Crit: " + cp.aCrit + "%", col1, row - lh * 4);

        font.setColor(Color.RED);
        font.draw(batch, cp.defenderName, col2, row);

        font.setColor(Color.WHITE);
        font.draw(batch, "HP: " + cp.dHp + "/" + cp.dMaxHp, col2, row - lh);

        if (cp.defenderCanCounter) {
            font.draw(batch, "Dmg: " + cp.dDmg + (cp.dHits > 1 ? " x" + cp.dHits : ""),
                col2, row - lh * 2);
            font.draw(batch, "Hit: " + cp.dHit + "%", col2, row - lh * 3);
            font.draw(batch, "Crit: " + cp.dCrit + "%", col2, row - lh * 4);
        } else {
            font.setColor(Color.GRAY);
            font.draw(batch, "- No Counter -", col2, row - lh * 2);
        }

        font.setColor(Color.WHITE);
    }

    public void dispose() {
        batch.dispose();
        font.dispose();

        indicatorTex.dispose();
        playerTex.dispose();
        enemyTex.dispose();
        movedTex.dispose();
        moveRangeTex.dispose();
        attackRangeTex.dispose();
        menuBgTex.dispose();
        uiBoxTex.dispose();
    }
}
