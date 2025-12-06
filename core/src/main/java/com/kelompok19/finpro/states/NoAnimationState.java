package com.kelompok19.finpro.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.combat.CombatStep;
import com.kelompok19.finpro.units.Unit;

import java.util.List;

public class NoAnimationState extends BattleState {
    private final List<CombatStep> steps;
    private int stepIndex = 0;

    private float timer = 0f;
    private final float STEP_DELAY = 0.8f;

    private final Unit attacker;

    public NoAnimationState(GameStateManager gsm, BattleContext context, List<CombatStep> steps, Unit originalAttacker) {
        super(gsm, context);
        this.steps = steps;
        this.attacker = originalAttacker;
    }

    @Override
    public void update(float delta) {
        timer -= delta;

        if (timer <= 0) {
            if (stepIndex < steps.size()) {
                executeStep(steps.get(stepIndex));
                stepIndex++;
                timer = STEP_DELAY;
            }

            else {
                endCombat();
            }
        }
    }

    private void executeStep(CombatStep step) {
        float popupX = step.target.getX() * 48;
        float popupY = step.target.getY() * 48;

        switch (step.type) {
            case ATTACK:
                step.target.takeDamage(step.damage);

                if (step.isCrit) {
                    context.popupPool.createCritDamage(step.damage, popupX, popupY);
                }

                else {
                    context.popupPool.createNormalDamage(step.damage, popupX, popupY);
                }

                break;

            case MISS:
                context.popupPool.createMiss(popupX, popupY);
                break;

            case REFLECT:
                step.target.takeDamage(step.damage);
                context.popupPool.createReflect(step.damage, popupX, popupY);
                break;

            case DIE:
                System.out.println(step.target.getName() + " defeated.");
                break;
        }
    }

    private void endCombat() {
        context.unitManager.cleanup();

        if (attacker.getCurrentHp() > 0) {
            attacker.setHasMoved(true);
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
        renderMapAndUnits(batch);
        renderPopups(batch);
    }

    @Override
    public void dispose() {

    }
}
