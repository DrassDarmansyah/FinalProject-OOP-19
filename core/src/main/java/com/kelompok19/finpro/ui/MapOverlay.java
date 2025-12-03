package com.kelompok19.finpro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class MapOverlay implements Disposable {
    private final int TILE_SIZE = 48;

    private Texture indicatorTex;
    private Texture moveRangeTex;
    private Texture attackRangeTex;

    public MapOverlay() {
        indicatorTex = createColoredTexture(TILE_SIZE, new Color(1, 1, 0, 0.5f));
        moveRangeTex = createColoredTexture(TILE_SIZE, new Color(0, 1, 1, 0.3f));
        attackRangeTex = createColoredTexture(TILE_SIZE, new Color(1, 0, 0, 0.3f));
    }

    private Texture createColoredTexture(int size, Color color) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture t = new Texture(pixmap);
        pixmap.dispose();
        return t;
    }

    public void drawCursor(SpriteBatch batch, int x, int y) {
        batch.draw(indicatorTex, x * TILE_SIZE, y * TILE_SIZE);
    }

    public void drawRangeTiles(SpriteBatch batch, Array<int[]> tiles, boolean isAttack) {
        Texture textureToUse = isAttack ? attackRangeTex : moveRangeTex;

        for (int[] pos : tiles) {
            batch.draw(textureToUse, pos[0] * TILE_SIZE, pos[1] * TILE_SIZE);
        }
    }

    @Override
    public void dispose() {
        indicatorTex.dispose();
        moveRangeTex.dispose();
        attackRangeTex.dispose();
    }
}
