package com.kelompok19.finpro.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.combat.CombatStep;
import com.kelompok19.finpro.units.Unit;

import java.util.List;

public class CombatCutsceneState extends BattleState {
    private final List<CombatStep> steps;
    private int stepIndex = 0;

    private float timer = 0f;
    private final float STEP_DELAY = 0.8f;

    private final Unit attacker;

    public CombatCutsceneState(GameStateManager gsm, BattleContext context, List<CombatStep> steps, Unit originalAttacker) {
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
        switch (step.type) {
            case ATTACK:
                step.target.takeDamage(step.damage);
                Color c = step.isCrit ? Color.YELLOW : Color.WHITE;
                float scale = step.isCrit ? 1.5f : 1.0f;
                context.addDamagePopup(String.valueOf(step.damage), step.target.getX(), step.target.getY(), c, scale);
                break;

            case MISS:
                context.addDamagePopup("MISS", step.target.getX(), step.target.getY(), Color.GRAY, 0.8f);
                break;

            case REFLECT:
                step.target.takeDamage(step.damage);
                context.addDamagePopup(String.valueOf(step.damage), step.target.getX(), step.target.getY(), Color.CYAN, 1.0f);
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
