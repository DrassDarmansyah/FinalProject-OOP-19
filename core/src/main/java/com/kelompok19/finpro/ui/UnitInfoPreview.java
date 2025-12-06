package com.kelompok19.finpro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.units.Stats;
import com.kelompok19.finpro.units.Unit;

public class UnitInfoPreview implements Disposable {
    private final Texture bgTexture;

    private final float WIDTH = 120;
    private final float HEIGHT = 350;
    private final float PADDING = 10;

    public UnitInfoPreview() {
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        p.fill();
        bgTexture = new Texture(p);
        p.dispose();
    }

    public void render(SpriteBatch batch, BitmapFont font, OrthographicCamera camera, Unit unit) {
        if (unit == null) {
            return;
        }

        float camLeft = camera.position.x - (camera.viewportWidth * camera.zoom / 2);
        float camBottom = camera.position.y - (camera.viewportHeight * camera.zoom / 2);

        float x = camLeft + PADDING;
        float y = camBottom + PADDING;

        batch.draw(bgTexture, x, y, WIDTH, HEIGHT);

        float textX = x + 10;
        float textY = y + HEIGHT - 15;
        float lineHeight = 20;

        font.setColor(Color.ORANGE);
        font.draw(batch, unit.getName(), textX, textY);
        font.setColor(Color.GRAY);
        font.draw(batch, unit.getJob().getDisplayName(), textX, textY - lineHeight);

        Stats stats = unit.getEffectiveStats();
        Weapon w = unit.getWeapon();

        int dmgStat = Math.max(stats.strength, stats.magic);
        int atk = dmgStat + (w != null ? w.might : 0);
        int weaponHit = (w != null) ? w.hit : 0;
        int weaponCrit = (w != null) ? w.crit : 0;
        int weaponAvoid = (w != null) ? w.avoid : 0;
        int weaponDodge = (w != null) ? w.dodge : 0;

        int hit = (int)(stats.dexterity * 1.5 + stats.luck * 0.5) + weaponHit;
        int crit = (stats.dexterity / 2) + weaponCrit;
        int avo = ((stats.speed * 3) + stats.luck) / 2 + weaponAvoid;
        int ddg = (stats.luck / 2) + weaponDodge;

        font.setColor(Color.WHITE);
        float startY = textY - lineHeight * 2.5f;

        drawStat(batch, font, "HP", stats.hp + "/" + unit.getStats().hp, textX, startY);
        drawStat(batch, font, "Str", "" + stats.strength, textX, startY - lineHeight);
        drawStat(batch, font, "Mag", "" + stats.magic, textX, startY - lineHeight * 2);
        drawStat(batch, font, "Dex", "" + stats.dexterity, textX, startY - lineHeight * 3);
        drawStat(batch, font, "Spd", "" + stats.speed, textX, startY - lineHeight * 4);
        drawStat(batch, font, "Lck", "" + stats.luck, textX, startY - lineHeight * 5);
        drawStat(batch, font, "Def", "" + stats.defense, textX, startY - lineHeight * 6);
        drawStat(batch, font, "Res", "" + stats.resistance, textX, startY - lineHeight * 7);
        drawStat(batch, font, "Atk", "" + atk, textX, startY - lineHeight * 9);
        drawStat(batch, font, "Hit", "" + hit, textX, startY - lineHeight * 10);
        drawStat(batch, font, "Crit", "" + crit, textX, startY - lineHeight * 11);
        drawStat(batch, font, "Avo", "" + avo, textX, startY - lineHeight * 12);
        drawStat(batch, font, "Ddg", "" + ddg, textX, startY - lineHeight * 13);
    }

    private void drawStat(SpriteBatch batch, BitmapFont font, String label, String val, float x, float y) {
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, label, x, y);
        font.setColor(Color.WHITE);
        font.draw(batch, val, x + 40, y);
    }

    @Override
    public void dispose() {
        bgTexture.dispose();
    }
}
