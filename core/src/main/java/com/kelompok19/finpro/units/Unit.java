package com.kelompok19.finpro.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kelompok19.finpro.Weapon;

public class Unit {
    private String name; // Added for logs
    private int x;
    private int y;
    private final UnitType type;
    private boolean hasMoved;

    private final Stats stats;
    private final Weapon weapon;
    private int currentHp;

    private static final int MOVE_RANGE = 5;

    private Texture characterTexture;
    private Animation<TextureRegion> idleAnimation;
    private float stateTime;

    public Unit(String name, int x, int y, UnitType type, Stats stats, Weapon weapon) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
        this.hasMoved = false;

        this.stats = stats;
        this.weapon = weapon;
        this.currentHp = stats.hp;

        if (this.type == UnitType.PLAYER) {
            initializeAnimations("Knight-Idle.png");
        } else {
            initializeAnimations("Orc-Idle.png");
        }
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    public void setCurrentHp(int hp) { this.currentHp = hp; }
    public String getName() { return name; }

    public int getStrength() { return stats.strength; }
    public int getDefense() { return stats.defense; }

    public int getX() { return x; }
    public int getY() { return y; }
    public UnitType getType() { return type; }
    public boolean hasMoved() { return hasMoved; }
    public int getMoveRange() { return MOVE_RANGE; }
    public Stats getStats() { return stats; }
    public Weapon getWeapon() { return weapon; }
    public int getCurrentHp() { return currentHp; }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isAt(int tileX, int tileY) {
        return x == tileX && y == tileY;
    }

    public TextureRegion getCurrentFrame() {
        if (idleAnimation != null && !hasMoved) {
            return idleAnimation.getKeyFrame(stateTime, true);
        }
        return null;
    }

    public void dispose() {
        if (characterTexture != null) {
            characterTexture.dispose();
        }
    }

    private void initializeAnimations(String textureName) {
        try {
            characterTexture = new Texture(Gdx.files.internal(textureName));
        } catch (Exception e) {
            Gdx.app.error("ASSET LOAD ERROR", "Could not load " + textureName, e);
            return;
        }

        TextureRegion[][] idleFrames2D = TextureRegion.split(characterTexture, 100, 100);
        TextureRegion[] idleFrames = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            idleFrames[i] = idleFrames2D[0][i];
        }

        idleAnimation = new Animation<>(1f/12f, idleFrames);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0f;
    }
}
