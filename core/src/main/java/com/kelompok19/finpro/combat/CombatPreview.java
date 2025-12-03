package com.kelompok19.finpro.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CombatPreview {
    public String attackerName;
    public String defenderName;

    public int aHp, aMaxHp;
    public int dHp, dMaxHp;

    public int aDmg, aHit, aCrit;
    public int dDmg, dHit, dCrit;

    public int aHits;
    public int dHits;

    public boolean defenderCanCounter;

    private static Texture uiBoxTex;

    public CombatPreview() {
        if (uiBoxTex == null) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(0.1f, 0.1f, 0.3f, 0.9f);
            p.fill();
            uiBoxTex = new Texture(p);
            p.dispose();
        }
    }

    public void render(SpriteBatch batch, BitmapFont font, OrthographicCamera camera) {
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
        font.draw(batch, attackerName, col1, row);
        font.setColor(Color.WHITE);
        font.draw(batch, "HP: " + aHp + "/" + aMaxHp, col1, row - lh);
        font.draw(batch, "Dmg: " + aDmg + (aHits > 1 ? " x" + aHits : ""), col1, row - lh * 2);
        font.draw(batch, "Hit: " + aHit + "%", col1, row - lh * 3);
        font.draw(batch, "Crit: " + aCrit + "%", col1, row - lh * 4);

        font.setColor(Color.RED);
        font.draw(batch, defenderName, col2, row);
        font.setColor(Color.WHITE);
        font.draw(batch, "HP: " + dHp + "/" + dMaxHp, col2, row - lh);

        if (defenderCanCounter) {
            font.draw(batch, "Dmg: " + dDmg + (dHits > 1 ? " x" + dHits : ""), col2, row - lh * 2);
            font.draw(batch, "Hit: " + dHit + "%", col2, row - lh * 3);
            font.draw(batch, "Crit: " + dCrit + "%", col2, row - lh * 4);
        } else {
            font.setColor(Color.GRAY);
            font.draw(batch, "- No Counter -", col2, row - lh * 2);
        }

        font.setColor(Color.WHITE);
    }

    public static void dispose() {
        if (uiBoxTex != null) {
            uiBoxTex.dispose();
            uiBoxTex = null;
        }
    }
}
