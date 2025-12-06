package com.kelompok19.finpro.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.ai.AIBehavior;
import com.kelompok19.finpro.factories.AnimationFactory;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private String name;
    private int x;
    private int y;
    private final UnitType type;
    private final UnitJob job;
    private boolean hasMoved;

    private final Stats stats;
    private final Weapon weapon;
    private int currentHp;
    private final List<Skill> skills = new ArrayList<>();
    private Stats tempStats;

    private Texture characterTexture;
    private Animation<TextureRegion> idleAnimation;
    private float stateTime;

    private AIBehavior aiBehavior;

    public Unit(String name, int x, int y, UnitType type, UnitJob job, Stats providedStats, Weapon weapon) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
        this.job = job;
        this.hasMoved = false;
        this.weapon = weapon;
        this.stats = providedStats;
        this.tempStats = new Stats(0,0,0,0,0,0,0,0);
        this.currentHp = this.stats.hp;
        initializeAnimations(job.getTexturePath());
    }

    public Stats getEffectiveStats() {
        Stats base = stats.add(tempStats);

        if (weapon != null && weapon.bonuses != null) {
            return base.add(weapon.bonuses);
        }

        return base;
    }

    public void addTempStats(Stats buff) {
        this.tempStats = this.tempStats.add(buff);
    }

    public void resetTurn() {
        this.setHasMoved(false);
        this.tempStats = new Stats(0,0,0,0,0,0,0,0);
    }

    public void addSkill(Skill skill) {
        if (!skills.contains(skill)) {
            skills.add(skill);
        }
    }

    public boolean hasSkill(Skill skill) {
        return skills.contains(skill);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void update(float delta) {
        if (!hasMoved) {
            stateTime += delta;
        }
    }

    public void render(SpriteBatch batch, Texture whitePixel) {
        if (!hasMoved) {
            stateTime += Gdx.graphics.getDeltaTime();
        }

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

    public Animation<TextureRegion> loadAnimation(int type) {
        String path = null;

        switch (type) {
            case 1:
                path = job.getAttack1Path();
                break;

            case 2:
                path = job.getAttack2Path();
                break;

            case 3:
                path = job.getCritPath();
                break;

            case 4:
                path = job.getHurtPath();
                break;

            case 5:
                path = job.getDeathPath();
                break;
        }

        return AnimationFactory.getInstance().getAnimation(path);
    }

    public Animation<TextureRegion> loadMagicAnimation() {
        return com.kelompok19.finpro.factories.AnimationFactory.getInstance().getAnimation(job.getMagicPath());
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

        if (hasMoved && type == UnitType.PLAYER) {
            return idleAnimation.getKeyFrames()[0];
        }

        return idleAnimation.getKeyFrame(stateTime, true);
    }

    private void initializeAnimations(String textureName) {
        try {
            characterTexture = new Texture(Gdx.files.internal(textureName));
        }

        catch (Exception e) {
            Gdx.app.error("ASSET", "Error loading " + textureName);
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

    public void setAI(AIBehavior behavior) {
        this.aiBehavior = behavior;
    }

    public AIBehavior getAI() {
        return aiBehavior;
    }

    public void dispose() {
        if (characterTexture != null) {
            characterTexture.dispose();
        }
    }
}
