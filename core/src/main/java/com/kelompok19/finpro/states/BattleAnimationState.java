package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.combat.CombatStep;
import com.kelompok19.finpro.pools.DamagePopupPool;
import com.kelompok19.finpro.ui.DamagePopup;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BattleAnimationState extends BattleState {
    private final List<CombatStep> steps;
    private final CombatPreview combatStats;
    private int stepIndex = 0;

    private final Unit playerUnit;
    private final Unit enemyUnit;
    private final Unit originalAttacker;

    private float stateTime = 0f;
    private boolean isAnimating = false;
    private Animation<TextureRegion> currentAnimation;

    private Animation<TextureRegion> currentMagicAnimation;

    private Unit currentActor;
    private Unit currentTarget;

    private boolean playingReaction = false;
    private final Map<Unit, Integer> attackCounts = new HashMap<>();
    private final Set<Unit> deadUnits = new HashSet<>();

    private final Texture bgTexture;
    private final DamagePopupPool localPool = new DamagePopupPool();
    private final List<DamagePopup> localPopups = new ArrayList<>();

    private enum Phase { START_DELAY, PLAYING, END_DELAY }
    private Phase phase = Phase.START_DELAY;
    private float delayTimer = 0.8f;

    public BattleAnimationState(GameStateManager gsm, BattleContext context, List<CombatStep> steps, CombatPreview combatStats, Unit initiator, Unit target) {
        super(gsm, context);
        this.steps = steps;
        this.combatStats = combatStats;
        this.originalAttacker = initiator;

        if (initiator.getType() == UnitType.PLAYER) {
            this.playerUnit = initiator;
            this.enemyUnit = target;
        }

        else {
            this.playerUnit = target;
            this.enemyUnit = initiator;
        }

        this.bgTexture = context.whitePixel;
        attackCounts.put(playerUnit, 0);
        attackCounts.put(enemyUnit, 0);
    }

    @Override
    public void update(float delta) {
        Iterator<DamagePopup> it = localPopups.iterator();

        while (it.hasNext()) {
            DamagePopup p = it.next();

            if (p.update(delta)) {
                localPool.release(p);
                it.remove();
            }
        }

        switch (phase) {
            case START_DELAY:
                delayTimer -= delta;

                if (delayTimer <= 0) {
                    phase = Phase.PLAYING;
                }

                break;

            case PLAYING:
                updatePlaying(delta);
                break;

            case END_DELAY:
                delayTimer -= delta;

                if (delayTimer <= 0) {
                    endCombat();
                }

                break;
        }
    }

    private void updatePlaying(float delta) {
        if (isAnimating) {
            stateTime += delta;

            if (currentAnimation != null && currentAnimation.isAnimationFinished(stateTime)) {
                onAnimationFinished();
            }

            else if (currentAnimation == null) {
                if (stateTime > 0.5f) {
                    onAnimationFinished();
                }
            }
        }

        else {
            if (stepIndex < steps.size()) {
                setupStepAnimation(steps.get(stepIndex));
            }

            else {
                phase = Phase.END_DELAY;
                delayTimer = 1.2f;
            }
        }
    }

    private void setupStepAnimation(CombatStep step) {
        stateTime = 0f;
        playingReaction = false;
        isAnimating = true;
        currentMagicAnimation = null;

        if (step.type == CombatStep.Type.ATTACK || step.type == CombatStep.Type.MISS || step.type == CombatStep.Type.REFLECT) {
            currentActor = step.source;
            currentTarget = step.target;

            int animType = 1;
            int count = attackCounts.getOrDefault(step.source, 0) + 1;
            attackCounts.put(step.source, count);

            if (step.isCrit) {
                animType = 3;
            }

            else if (count >= 2) {
                animType = 2;
            }

            currentAnimation = step.source.loadAnimation(animType);

            if (step.source.getJob().getMagicPath() != null) {
                currentMagicAnimation = step.source.loadMagicAnimation();
            }
        }

        else if (step.type == CombatStep.Type.DIE) {
            currentActor = step.target;
            currentTarget = null;
            currentAnimation = step.target.loadAnimation(5);
        }

        else {
            currentAnimation = null;
            currentTarget = null;
        }
    }

    private void onAnimationFinished() {
        CombatStep step = steps.get(stepIndex);

        if (step.type == CombatStep.Type.DIE) {
            deadUnits.add(step.target);
        }

        if (!playingReaction) {
            applyStepEffect(step);

            if (step.type == CombatStep.Type.ATTACK || step.type == CombatStep.Type.REFLECT) {
                currentActor = step.target;
                currentMagicAnimation = null;
                currentAnimation = step.target.loadAnimation(4);

                if (currentAnimation != null) {
                    playingReaction = true;
                    stateTime = 0f;
                    return;
                }
            }
        }

        isAnimating = false;
        stepIndex++;
    }

    private void applyStepEffect(CombatStep step) {
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float popupX = (step.target == playerUnit) ? sw * 0.25f : sw * 0.75f;
        float popupY = sh * 0.3f + 150;

        DamagePopup popup = null;

        switch (step.type) {
            case ATTACK:
                step.target.takeDamage(step.damage);

                if (step.isCrit) {
                    popup = localPool.createCritDamage(step.damage, popupX, popupY);
                }

                else {
                    popup = localPool.createNormalDamage(step.damage, popupX, popupY);
                }

                break;

            case MISS:
                popup = localPool.createMiss(popupX, popupY);
                break;

            case REFLECT:
                step.target.takeDamage(step.damage);
                popup = localPool.createReflect(step.damage, popupX, popupY);
                break;

            case DIE:
                break;
        }

        if (popup != null) {
            localPopups.add(popup);
        }
    }

    private void endCombat() {
        context.unitManager.cleanup();

        if (originalAttacker.getCurrentHp() > 0) {
            originalAttacker.setHasMoved(true);
        }

        if (context.isShowDangerZone()) {
            context.recalculateDangerZone();
        }

        gsm.pop();

        if (checkObjectives()) {
            return;
        }

        if (GameManager.getInstance().isPlayerTurn()) {
            boolean allMoved = context.unitManager.getPlayerUnits().stream().allMatch(Unit::hasMoved);

            if (allMoved) {
                GameManager.getInstance().startEnemyPhase(context);
                gsm.set(new EnemyPhaseState(gsm, context));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        batch.setColor(0.2f, 0.2f, 0.3f, 1f);
        batch.draw(bgTexture, 0, 0, sw, sh);
        batch.setColor(Color.WHITE);

        float floorY = sh * 0.3f;

        drawUnit(batch, playerUnit, sw * 0.25f, floorY, false);
        drawUnit(batch, enemyUnit, sw * 0.75f, floorY, true);

        if (currentMagicAnimation != null && !playingReaction && isAnimating && phase == Phase.PLAYING) {
            float targetX = (currentTarget == playerUnit) ? sw * 0.25f : sw * 0.75f;
            boolean flipMagic = (currentTarget == playerUnit);
            drawMagicEffect(batch, targetX, floorY, flipMagic);
        }

        for(DamagePopup p : localPopups) {
            p.render(batch, context.font);
        }

        if (!deadUnits.contains(playerUnit)) {
            drawHUD(batch, playerUnit, 20, sh - 150);
        }

        if (!deadUnits.contains(enemyUnit)) {
            drawHUD(batch, enemyUnit, sw - 220, sh - 150);
        }

        batch.end();
    }

    private void drawUnit(SpriteBatch batch, Unit unit, float x, float y, boolean flip) {
        if (deadUnits.contains(unit)) {
            return;
        }

        TextureRegion frame;

        if (phase == Phase.PLAYING && isAnimating && currentActor == unit && currentAnimation != null) {
            frame = currentAnimation.getKeyFrame(stateTime, false);
        }

        else {
            frame = unit.getCurrentFrame();
        }

        if (frame != null) {
            float scale = 3.0f;
            float w = frame.getRegionWidth() * scale;
            float h = frame.getRegionHeight() * scale;
            float drawWidth = flip ? -w : w;
            float drawX = flip ? x + w/2 : x - w/2;
            batch.draw(frame, drawX, y, drawWidth, h);
        }
    }

    private void drawMagicEffect(SpriteBatch batch, float x, float y, boolean flip) {
        TextureRegion frame = currentMagicAnimation.getKeyFrame(stateTime, false);

        if (frame != null) {
            float scale = 3.0f;
            float w = frame.getRegionWidth() * scale;
            float h = frame.getRegionHeight() * scale;
            float drawWidth = flip ? -w : w;
            float drawX = flip ? x + w/2 : x - w/2;

            batch.draw(frame, drawX, y, drawWidth, h);
        }
    }

    private void drawHUD(SpriteBatch batch, Unit unit, float x, float y) {
        batch.setColor(0, 0, 0, 0.6f);
        batch.draw(bgTexture, x, y, 200, 130);
        batch.setColor(Color.WHITE);

        context.font.setColor(Color.ORANGE);
        context.font.draw(batch, unit.getName(), x + 10, y + 110);
        context.font.setColor(Color.WHITE);
        context.font.draw(batch, "HP: " + unit.getCurrentHp() + "/" + unit.getStats().hp, x + 10, y + 85);

        float pct = (float)unit.getCurrentHp() / unit.getStats().hp;
        batch.setColor(Color.RED);
        batch.draw(context.whitePixel, x + 10, y + 65, 180, 10);
        batch.setColor(Color.GREEN);
        batch.draw(context.whitePixel, x + 10, y + 65, 180 * pct, 10);

        batch.setColor(Color.WHITE);
        boolean isAttacker = (unit == originalAttacker);

        int dmg = 0, hit = 0, crit = 0;
        boolean showStats = true;

        if (isAttacker) {
            dmg = combatStats.aDmg;
            hit = combatStats.aHit;
            crit = combatStats.aCrit;
        }

        else {
            if (combatStats.defenderCanCounter) {
                dmg = combatStats.dDmg;
                hit = combatStats.dHit;
                crit = combatStats.dCrit;
            }

            else {
                showStats = false;
            }
        }

        float statsY = y + 50;

        if (showStats) {
            int hits = isAttacker ? combatStats.aHits : combatStats.dHits;
            String dmgStr = dmg + (hits > 1 ? " x" + hits : "");
            context.font.draw(batch, "Dmg: " + dmgStr, x + 10, statsY);
            context.font.draw(batch, "Hit: " + hit + "%", x + 10, statsY - 20);
            context.font.draw(batch, "Crit: " + crit + "%", x + 10, statsY - 40);
        }

        else {
            context.font.setColor(Color.GRAY);
            context.font.draw(batch, "--", x + 10, statsY);
            context.font.setColor(Color.WHITE);
        }
    }

    @Override
    public void dispose() {

    }
}
