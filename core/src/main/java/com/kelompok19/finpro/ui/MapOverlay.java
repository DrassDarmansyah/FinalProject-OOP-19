package com.kelompok19.finpro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Set;

public class MapOverlay implements Disposable {
    private final int TILE_SIZE = 48;

    private Texture indicatorTex;
    private Texture moveRangeTex;
    private Texture attackRangeTex;
    private Texture healRangeTex;

    public MapOverlay() {
        indicatorTex = createColoredTexture(TILE_SIZE, new Color(1, 1, 0, 0.5f));
        moveRangeTex = createColoredTexture(TILE_SIZE, new Color(0, 1, 1, 1.0f));
        attackRangeTex = createColoredTexture(TILE_SIZE, new Color(1, 0, 0, 1.0f));
        healRangeTex = createColoredTexture(TILE_SIZE, new Color(0, 1, 0, 1.0f));
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

    public void drawRangeTiles(SpriteBatch batch, Array<int[]> tiles, int type, float alpha) {
        Texture tex;

        if (type == 1) {
            tex = attackRangeTex;
        }

        else if (type == 2) {
            tex = healRangeTex;
        }

        else {
            tex = moveRangeTex;
        }

        batch.setColor(1f, 1f, 1f, alpha);

        for (int[] pos : tiles) {
            batch.draw(tex, pos[0] * TILE_SIZE, pos[1] * TILE_SIZE);
        }

        batch.setColor(Color.WHITE);
    }

    public void drawRangeTiles(SpriteBatch batch, Array<int[]> tiles, boolean isAttack) {
        drawRangeTiles(batch, tiles, isAttack ? 1 : 0, 0.2f);
    }

    public void drawDangerZoneOutline(SpriteBatch batch, Set<GridPoint2> dangerTiles, Texture whitePixel) {
        if (dangerTiles == null || dangerTiles.isEmpty()) {
            return;
        }

        float thickness = 3f;
        batch.setColor(Color.RED);

        for (GridPoint2 p : dangerTiles) {
            float x = p.x * TILE_SIZE;
            float y = p.y * TILE_SIZE;

            if (!dangerTiles.contains(new GridPoint2(p.x, p.y + 1))) {
                batch.draw(whitePixel, x, y + TILE_SIZE - thickness, TILE_SIZE, thickness);
            }

            if (!dangerTiles.contains(new GridPoint2(p.x, p.y - 1))) {
                batch.draw(whitePixel, x, y, TILE_SIZE, thickness);
            }

            if (!dangerTiles.contains(new GridPoint2(p.x + 1, p.y))) {
                batch.draw(whitePixel, x + TILE_SIZE - thickness, y, thickness, TILE_SIZE);
            }

            if (!dangerTiles.contains(new GridPoint2(p.x - 1, p.y))) {
                batch.draw(whitePixel, x, y, thickness, TILE_SIZE);
            }
        }

        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        indicatorTex.dispose();
        moveRangeTex.dispose();
        attackRangeTex.dispose();
        healRangeTex.dispose();
    }
}
