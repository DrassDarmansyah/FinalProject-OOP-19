package com.kelompok19.finpro.commands;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class ActionMenu implements Disposable {
    private final List<String> options = new ArrayList<>();
    private int selectedIndex = 0;
    private boolean isVisible = false;

    private Texture backgroundTex;

    public static final String CMD_MOVE = "Move";
    public static final String CMD_ATTACK = "Attack";
    public static final String CMD_WAIT = "Wait";

    public ActionMenu() {
        createBackground();
    }

    private void createBackground() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0.8f);
        p.fill();
        backgroundTex = new Texture(p);
        p.dispose();
    }

    public void render(SpriteBatch batch, BitmapFont font, float unitX, float unitY, OrthographicCamera camera) {
        if (!isVisible) return;

        int TILE_SIZE = 48;
        float menuX = unitX * TILE_SIZE + TILE_SIZE + 5;
        float menuY = unitY * TILE_SIZE + TILE_SIZE;
        float width = 80;
        float height = options.size() * 25 + 10;

        if (menuX + width > camera.position.x + camera.viewportWidth * camera.zoom / 2) {
            menuX = unitX * TILE_SIZE - width - 5;
        }

        batch.draw(backgroundTex, menuX, menuY - height, width, height);

        for (int i = 0; i < options.size(); i++) {
            font.setColor(i == selectedIndex ? Color.YELLOW : Color.WHITE);
            font.draw(batch, options.get(i), menuX + 5, menuY - 10 - (i * 25));
        }

        font.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        if (backgroundTex != null) backgroundTex.dispose();
    }

    public void show(boolean canMove, boolean canAttack, boolean isPostMove) {
        options.clear();
        selectedIndex = 0;
        isVisible = true;
        if (isPostMove) {
            if (canAttack) options.add(CMD_ATTACK);
            options.add(CMD_WAIT);
        } else {
            if (canMove) options.add(CMD_MOVE);
            if (canAttack) options.add(CMD_ATTACK);
        }
    }

    public void hide() { isVisible = false; options.clear(); }
    public void navigateUp() {
        if (!isVisible || options.isEmpty()) return;
        selectedIndex--;
        if (selectedIndex < 0) selectedIndex = options.size() - 1;
    }
    public void navigateDown() {
        if (!isVisible || options.isEmpty()) return;
        selectedIndex++;
        if (selectedIndex >= options.size()) selectedIndex = 0;
    }
    public String getSelectedOption() {
        if (!isVisible || options.isEmpty()) return null;
        return options.get(selectedIndex);
    }
    public boolean isVisible() { return isVisible; }
    public List<String> getOptions() { return options; }
    public int getSelectedIndex() { return selectedIndex; }
}
