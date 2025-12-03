package com.kelompok19.finpro.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kelompok19.finpro.Weapon;

public class Unit {
    private String name;
    private int x;
    private int y;
    private final UnitType type;
    private final UnitJob job;
    private boolean hasMoved;

    private final Stats stats; // The Final Calculated Stats
    private final Stats personalStats; // Kept for reference if needed
    private final Weapon weapon;
    private int currentHp;

    private Texture characterTexture;
    private Animation<TextureRegion> idleAnimation;
    private float stateTime;

    public Unit(String name, int x, int y, UnitType type, UnitJob job, Stats personalStats, Weapon weapon) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
        this.job = job;
        this.personalStats = personalStats;
        this.hasMoved = false;

        this.stats = job.getBaseStats().add(personalStats);

        this.weapon = weapon;
        this.currentHp = this.stats.hp;

        initializeAnimations(job.getTexturePath());
    }

    public void update(float delta) {
        if (!hasMoved) {
            stateTime += delta;
        }
    }

    public void render(SpriteBatch batch, Texture whitePixel) {
        update(Gdx.graphics.getDeltaTime());

        TextureRegion frame = getCurrentFrame();

        if (frame == null) {
            return;
        }

        float w = frame.getRegionWidth();
        float h = frame.getRegionHeight();
        float tileSize = 48f;
        float drawX = x * tileSize + (tileSize - w) / 2;
        float drawY = y * tileSize + (tileSize - h) / 2;

        if (hasMoved) {
            batch.setColor(0.5f, 0.5f, 0.5f, 1f);
        }

        else {
            batch.setColor(1f, 1f, 1f, 1f);
        }

        batch.draw(frame, drawX, drawY);

        float barWidth = 40f;
        float barHeight = 5f;
        float barX = x * tileSize + (tileSize - barWidth) / 2;
        float barY = y * tileSize + 2;
        float hpRatio = (float) currentHp / (float) stats.hp;

        batch.setColor(0f, 0f, 0f, 0.8f);
        batch.draw(whitePixel, barX, barY, barWidth, barHeight);

        if (hpRatio > 0.5f) {
            batch.setColor(Color.GREEN);
        }

        else if (hpRatio > 0.25f) {
            batch.setColor(Color.YELLOW);
        }

        else {
            batch.setColor(Color.RED);
        }

        if (hpRatio > 0) {
            batch.draw(whitePixel, barX + 1, barY + 1, (barWidth - 2) * hpRatio, barHeight - 2);
        }

        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void takeDamage(int damage) {
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    public void setCurrentHp(int hp) {
        this.currentHp = hp;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return stats.strength;
    }

    public int getDefense() {
        return stats.defense;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public UnitType getType() {
        return type;
    }

    public UnitJob getJob() {
        return job;
    }
    public MovementType getMovementType() {
        return job.getMovementType();
    }

    public int getMoveRange() {
        return job.getMoveRange();
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public Stats getStats() {
        return stats;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;

        if (!hasMoved) {
            stateTime = 0f;
        }
    }

    public boolean isAt(int tileX, int tileY) {
        return x == tileX && y == tileY;
    }

    public TextureRegion getCurrentFrame() {
        if (idleAnimation == null) {
            return null;
        }

        if (hasMoved) {
            return idleAnimation.getKeyFrames()[0];
        }

        return idleAnimation.getKeyFrame(stateTime, true);
    }

    public void dispose() {
        if (characterTexture != null) {
            characterTexture.dispose();
        }
    }

    private void initializeAnimations(String textureName) {
        try {
            characterTexture = new Texture(Gdx.files.internal(textureName));
        }

        catch (Exception e) {
            Gdx.app.error("ASSET ERROR", "Could not load " + textureName, e);

            try {
                if (type == UnitType.PLAYER) {
                    characterTexture = new Texture(Gdx.files.internal("Knight-Idle.png"));
                }

                else {
                    characterTexture = new Texture(Gdx.files.internal("Orc-Idle.png"));
                }
            }

            catch (Exception ex) {
                return;
            }
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
