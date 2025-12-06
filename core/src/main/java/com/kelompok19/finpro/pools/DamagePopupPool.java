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

    public DamagePopup obtain(String text, float x, float y, Color color, float scale) {
        DamagePopup p = this.obtain();
        p.init(text, x, y, color, scale);
        return p;
    }

    public DamagePopup createNormalDamage(int damage, float x, float y) {
        return obtain(String.valueOf(damage), x, y, Color.WHITE, 1.0f);
    }

    public DamagePopup createCritDamage(int damage, float x, float y) {
        return obtain(String.valueOf(damage), x, y, Color.YELLOW, 1.5f);
    }

    public DamagePopup createMiss(float x, float y) {
        return obtain("MISS", x, y, Color.GRAY, 1.0f);
    }

    public DamagePopup createHeal(int amount, float x, float y) {
        return obtain("+" + amount, x, y, Color.GREEN, 1.3f);
    }

    public DamagePopup createReflect(int damage, float x, float y) {
        return obtain(String.valueOf(damage), x, y, Color.CYAN, 1.0f);
    }
}
