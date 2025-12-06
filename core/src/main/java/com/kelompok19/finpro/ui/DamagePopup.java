package com.kelompok19.finpro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DamagePopup {
    private float x, y;
    private String text;
    private Color color;
    private float timer;
    private float scale;

    private final float MAX_TIME = 1.5f;
    private final float FLOAT_SPEED = 30f;

    public DamagePopup() {
    }

    public void init(String text, float startX, float startY, Color color, float scale) {
        this.text = text;
        this.x = startX;
        this.y = startY;
        this.color = color;
        this.scale = scale;
        this.timer = MAX_TIME;
    }

    public boolean update(float delta) {
        y += FLOAT_SPEED * delta;
        timer -= delta;
        return timer <= 0;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        Color oldColor = font.getColor();
        float oldScaleX = font.getData().scaleX;
        float oldScaleY = font.getData().scaleY;

        font.setColor(color);
        font.getData().setScale(scale);

        font.draw(batch, text, x + 10, y + 40);

        font.setColor(oldColor);
        font.getData().setScale(oldScaleX, oldScaleY);
    }
}
