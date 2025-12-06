package com.kelompok19.finpro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.kelompok19.finpro.maps.TerrainType;

public class TerrainInfoPreview implements Disposable {
    private final Texture bgTexture;
    private final float WIDTH = 160;
    private final float HEIGHT = 100;

    public TerrainInfoPreview() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0.1f, 0.1f, 0.1f, 0.8f);
        p.fill();
        bgTexture = new Texture(p);
        p.dispose();
    }

    public void render(SpriteBatch batch, BitmapFont font, OrthographicCamera camera, TerrainType terrain) {
        if (terrain == null) {
            return;
        }

        float camLeft = camera.position.x - (camera.viewportWidth * camera.zoom / 2);
        float camBottom = camera.position.y - (camera.viewportHeight * camera.zoom / 2);

        float camRight = camLeft + (camera.viewportWidth * camera.zoom);

        float x = camRight - WIDTH;
        float y = camBottom;

        batch.draw(bgTexture, x, y, WIDTH, HEIGHT);

        float textX = x + 10;
        float textY = y + HEIGHT - 15;
        float lineHeight = 20;

        font.setColor(Color.YELLOW);
        font.draw(batch, terrain.toString(), textX, textY);

        font.setColor(Color.WHITE);
        font.draw(batch, "DEF: " + terrain.defense, textX, textY - lineHeight);
        font.draw(batch, "AVO: " + terrain.avoid, textX, textY - lineHeight * 2);
        font.draw(batch, "HEAL: " + terrain.heal + "%", textX, textY - lineHeight * 3);
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
    }
}
