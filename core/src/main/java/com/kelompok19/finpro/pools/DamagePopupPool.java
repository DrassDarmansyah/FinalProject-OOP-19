package com.kelompok19.finpro.pools;

import com.badlogic.gdx.graphics.Color;
import com.kelompok19.finpro.ui.DamagePopup;

public class DamagePopupPool extends ObjectPool<DamagePopup> {
    @Override
    protected DamagePopup createObject() {
        return new DamagePopup();
    }

    @Override
    protected void resetObject(DamagePopup object) {
    }

    public void spawnPopup(String text, float x, float y, Color color, float scale) {
        DamagePopup p = this.obtain();
        p.init(text, x, y, color, scale);
    }
}
