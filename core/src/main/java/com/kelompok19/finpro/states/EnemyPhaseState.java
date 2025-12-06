package com.kelompok19.finpro.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.units.Unit;

import java.util.List;

public class EnemyPhaseState extends BattleState {
    private final List<Unit> enemies;
    private int currentEnemyIndex = 0;

    private float actionTimer = 0f;
    private final float DELAY_PER_UNIT = 1.0f;

    public EnemyPhaseState(GameStateManager gsm, BattleContext context) {
        super(gsm, context);
        this.enemies = context.unitManager.getEnemyUnits();

        for (Unit e : enemies) {
            e.setHasMoved(false);
        }

        System.out.println(">>> ENEMY PHASE STARTED <<<");
    }

    @Override
    public void update(float delta) {
        actionTimer += delta;

        if (actionTimer >= DELAY_PER_UNIT) {
            actionTimer = 0f;

            if (currentEnemyIndex < enemies.size()) {
                Unit enemy = enemies.get(currentEnemyIndex);

                if (enemy.getCurrentHp() <= 0) {
                    currentEnemyIndex++;
                    return;
                }

                updateCamera(enemy.getX(), enemy.getY());

                if (enemy.getAI() != null) {
                    boolean acted = enemy.getAI().act(enemy, context, gsm);

                    if (acted) {
                        enemy.setHasMoved(true);

                        if (context.isShowDangerZone()) {
                            context.recalculateDangerZone();
                        }
                    }
                }

                context.unitManager.cleanup();
                if (checkObjectives()) {
                    return;
                }

                currentEnemyIndex++;
            }

            else {
                endEnemyPhase();
            }
        }

        else {
            if (currentEnemyIndex < enemies.size()) {
                Unit e = enemies.get(currentEnemyIndex);
                updateCamera(e.getX(), e.getY());
            }
        }
    }

    private void endEnemyPhase() {
        System.out.println(">>> ENEMY PHASE ENDED <<<");
        GameManager.getInstance().startPlayerPhase(context);
        gsm.set(new PlayerPhaseState(gsm, context));
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);
        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();

        if (context.isShowDangerZone()) {
            context.mapOverlay.drawDangerZoneOutline(batch, context.getDangerZone(), context.whitePixel);
        }

        batch.end();
        renderPopups(batch);
        batch.begin();
        context.turnPreview.render(batch, context.font, context.camera, GameManager.getInstance().getTurnCount(), false, context.config.getObjectiveText());
        batch.end();
        renderTerrainInfo(batch);
    }

    @Override
    public void dispose() {

    }
}
