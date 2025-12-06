package com.kelompok19.finpro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class TurnInfoPreview implements Disposable {
    private final Texture bgTexture;

    private final float WIDTH = 200;
    private final float HEIGHT = 90;
    private final float PADDING = 10;

    public TurnInfoPreview() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0.1f, 0.1f, 0.1f, 0.8f);
        p.fill();
        bgTexture = new Texture(p);
        p.dispose();
    }

    public void render(SpriteBatch batch, BitmapFont font, OrthographicCamera camera, int turnCount, boolean isPlayerTurn, String objective) {
        float camLeft = camera.position.x - (camera.viewportWidth * camera.zoom / 2);
        float camTop = camera.position.y + (camera.viewportHeight * camera.zoom / 2);

        float x = camLeft + PADDING;
        float y = camTop - HEIGHT - PADDING;

        batch.draw(bgTexture, x, y, WIDTH, HEIGHT);

        float textX = x + 10;
        float textY = y + HEIGHT - 20;
        float lineHeight = 22;

        if (isPlayerTurn) {
            font.setColor(Color.CYAN);
            font.draw(batch, "PLAYER PHASE", textX, textY);
        }

        else {
            font.setColor(Color.RED);
            font.draw(batch, "ENEMY PHASE", textX, textY);
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "Turn: " + turnCount, textX, textY - lineHeight);

        font.setColor(Color.YELLOW);
        float originalScaleX = font.getData().scaleX;
        float originalScaleY = font.getData().scaleY;
        font.getData().setScale(0.9f);
        font.draw(batch, "Obj: " + objective, textX, textY - lineHeight * 2);

        font.getData().setScale(originalScaleX, originalScaleY);
        font.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
    }
}
