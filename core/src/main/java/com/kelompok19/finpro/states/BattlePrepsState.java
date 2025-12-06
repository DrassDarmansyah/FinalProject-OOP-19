package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.kelompok19.finpro.GameManager;
import com.kelompok19.finpro.units.Skill;
import com.kelompok19.finpro.utils.HoverSystem;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.units.Stats;
import com.kelompok19.finpro.units.Unit;

import java.util.List;

public class BattlePrepsState extends BattleState {
    private final List<Unit> roster;
    private final Array<Unit> selectedUnits = new Array<>();
    private final List<int[]> deploymentSlots;
    private final int maxUnits;

    private int cursorIndex = 0;
    private final ShapeRenderer shapeRenderer;
    private final HoverSystem hoverSystem;

    private boolean viewingMap = false;

    public BattlePrepsState(GameStateManager gsm, BattleContext context) {
        super(gsm, context);
        this.roster = context.unitManager.getRoster();
        this.deploymentSlots = context.config.getDeploymentSlots(context.map.getHeight());
        this.maxUnits = context.config.getMaxUnits();
        this.shapeRenderer = new ShapeRenderer();
        this.hoverSystem = new HoverSystem(context);

        if (!roster.isEmpty()) {
            selectedUnits.add(roster.get(0));
        }

        context.unitManager.clearDeployedUnits();
        context.config.setupEnemies(context.unitManager, context.map.getHeight());

        centerCameraOnSlots();
    }

    private void centerCameraOnSlots() {
        if (!deploymentSlots.isEmpty()) {
            int[] center = deploymentSlots.get(0);
            context.cursorX = center[0];
            context.cursorY = center[1];
            context.camera.position.set(center[0] * 48 + 24, center[1] * 48 + 24, 0);
            context.camera.update();
        }
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            viewingMap = !viewingMap;
        }

        if (viewingMap) {
            handleMapInput(delta);
            hoverSystem.update(context.cursorX, context.cursorY);
        }

        else {
            handleMenuInput();
            roster.get(cursorIndex).update(delta);
        }
    }

    private void handleMapInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            context.cursorY++;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            context.cursorY--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            context.cursorX--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            context.cursorX++;
        }

        clampCursor();
        updateCamera(context.cursorX, context.cursorY);
    }

    private void clampCursor() {
        context.cursorX = Math.max(0, Math.min(context.cursorX, context.map.getWidth() - 1));
        context.cursorY = Math.max(0, Math.min(context.cursorY, context.map.getHeight() - 1));
    }

    private void handleMenuInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            cursorIndex--;

            if (cursorIndex < 0) {
                cursorIndex = roster.size() - 1;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            cursorIndex++;

            if (cursorIndex >= roster.size()) {
                cursorIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Unit targeted = roster.get(cursorIndex);

            if (cursorIndex == 0) {
                return;
            }

            if (selectedUnits.contains(targeted, true)) {
                selectedUnits.removeValue(targeted, true);
            }

            else {
                if (selectedUnits.size < maxUnits) {
                    selectedUnits.add(targeted);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            startGame();
        }
    }

    private void startGame() {
        for (int i = 0; i < selectedUnits.size; i++) {
            if (i < deploymentSlots.size()) {
                int[] slot = deploymentSlots.get(i);
                context.unitManager.deployUnit(selectedUnits.get(i), slot[0], slot[1]);
            }
        }

        GameManager.getInstance().startNewSession();
        gsm.set(new PlayerPhaseState(gsm, context));
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);
        batch.setProjectionMatrix(context.camera.combined);
        batch.begin();

        for (int[] slot : deploymentSlots) {
            context.mapOverlay.drawRangeTiles(batch, new Array<>(new int[][]{slot}), false);
        }

        if (viewingMap) {
            hoverSystem.renderRanges(batch);
            context.mapOverlay.drawCursor(batch, context.cursorX, context.cursorY);
        }

        batch.end();

        if (!viewingMap) {
            drawUI(batch);
        }

        else {
            batch.begin();
            hoverSystem.renderUnitInfo(batch);
            batch.end();
            renderTerrainInfo(batch);
        }
    }

    private void drawUI(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.setProjectionMatrix(batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.begin();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float listW = sw * 0.3f;

        drawBackground(batch, 0, 0, listW, sh, new Color(0.1f, 0.1f, 0.1f, 0.95f));
        context.font.setColor(Color.CYAN);
        context.font.draw(batch, "ROSTER (" + selectedUnits.size + "/" + maxUnits + ")", 20, sh - 20);

        float listStartY = sh - 60;
        float lineHeight = 30;

        for (int i = 0; i < roster.size(); i++) {
            Unit u = roster.get(i);
            float y = listStartY - (i * lineHeight);

            if (i == cursorIndex) {
                drawBackground(batch, 10, y - 20, listW - 20, 25, new Color(0.3f, 0.3f, 0.3f, 1f));
                context.font.setColor(Color.YELLOW);
            }

            else {
                context.font.setColor(selectedUnits.contains(u, true) ? Color.GREEN : Color.GRAY);
            }

            String name = u.getName();
            if (i == 0) {
                name += " (Lord)";
            }

            context.font.draw(batch, name, 30, y);
        }

        float detailsX = listW;
        float detailsW = sw - listW;
        drawBackground(batch, detailsX, 0, detailsW, sh, new Color(0.05f, 0.05f, 0.08f, 0.95f));
        Unit current = roster.get(cursorIndex);
        renderUnitDetails(batch, current, detailsX, sh);
        batch.end();
    }

    private void renderUnitDetails(SpriteBatch batch, Unit unit, float x, float topY) {
        float rowH = 25;
        float currY = topY - 50;

        context.font.getData().setScale(1.5f);
        context.font.setColor(Color.ORANGE);
        context.font.draw(batch, unit.getName(), x + 50, currY);
        context.font.getData().setScale(1.0f);
        context.font.setColor(Color.LIGHT_GRAY);
        context.font.draw(batch, unit.getJob().getDisplayName(), x + 50, currY - 30);

        TextureRegion frame = unit.getCurrentFrame();

        if (frame != null) {
            float scale = 2.0f;
            float w = frame.getRegionWidth() * scale;
            float h = frame.getRegionHeight() * scale;
            batch.draw(frame, x + 50, currY - 150, w, h);
        }

        Stats stats = unit.getEffectiveStats();
        float col1 = x + 250;
        float col2 = x + 400;
        float statY = currY - 50;

        drawLabelValue(batch, "HP", "" + stats.hp, col1, statY);
        drawLabelValue(batch, "Str", "" + stats.strength, col1, statY - rowH);
        drawLabelValue(batch, "Mag", "" + stats.magic, col1, statY - rowH * 2);
        drawLabelValue(batch, "Dex", "" + stats.dexterity, col1, statY - rowH * 3);
        drawLabelValue(batch, "Spd", "" + stats.speed, col2, statY);
        drawLabelValue(batch, "Lck", "" + stats.luck, col2, statY - rowH);
        drawLabelValue(batch, "Def", "" + stats.defense, col2, statY - rowH * 2);
        drawLabelValue(batch, "Res", "" + stats.resistance, col2, statY - rowH * 3);

        float wpY = statY - (rowH * 5);
        context.font.setColor(Color.YELLOW);
        context.font.draw(batch, "WEAPON", col1, wpY);
        Weapon w = unit.getWeapon();

        if (w != null) {
            context.font.setColor(Color.WHITE);
            context.font.draw(batch, w.name, col1, wpY - rowH);
            drawLabelValue(batch, "Mt", "" + w.might, col1, wpY - rowH * 2);
            drawLabelValue(batch, "Hit", "" + w.hit, col1, wpY - rowH * 3);
            drawLabelValue(batch, "Crit", "" + w.crit, col1, wpY - rowH * 4);
            String rangeStr = (w.rangeMin == w.rangeMax) ? String.valueOf(w.rangeMax) : w.rangeMin + "-" + w.rangeMax;
            drawLabelValue(batch, "Rng", rangeStr, col2, wpY - rowH * 2);
            drawLabelValue(batch, "Avo", "" + w.avoid, col2, wpY - rowH * 3);
            drawLabelValue(batch, "Ddg", "" + w.dodge, col2, wpY - rowH * 4);
            String bonusStr = getBonusString(w.bonuses);

            if (!bonusStr.isEmpty()) {
                context.font.setColor(Color.GREEN);
                context.font.draw(batch, bonusStr, col1, wpY - rowH * 5.5f);
            }
        }

        float skillY = wpY - (rowH * 7);
        context.font.setColor(Color.YELLOW);
        context.font.draw(batch, "SKILL", col1, skillY);

        List<Skill> skills = unit.getSkills();
        if (!skills.isEmpty()) {
            for (int i = 0; i < skills.size(); i++) {
                Skill s = skills.get(i);
                float sy = skillY - rowH - (i * rowH * 2);

                context.font.setColor(Color.CYAN);
                context.font.draw(batch, s.name, col1, sy);

                context.font.setColor(Color.LIGHT_GRAY);
                context.font.getData().setScale(1.0f);
                context.font.draw(batch, s.desc, col1, sy - 20);
                context.font.getData().setScale(1.0f);
            }
        }

        else {
            context.font.setColor(Color.GRAY);
            context.font.draw(batch, "None", col1, skillY - rowH);
        }

        context.font.setColor(Color.LIGHT_GRAY);
        context.font.draw(batch, "SPACE: Toggle | TAB: View Map | ENTER: Start", x + 50, 50);
    }

    private String getBonusString(Stats b) {
        if (b == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (b.hp > 0) {
            sb.append("+").append(b.hp).append("HP ");
        }

        if (b.strength > 0) {
            sb.append("+").append(b.strength).append("Str ");
        }

        if (b.magic > 0) {
            sb.append("+").append(b.magic).append("Mag ");
        }

        if (b.dexterity > 0) {
            sb.append("+").append(b.dexterity).append("Dex ");
        }

        if (b.speed > 0) {
            sb.append("+").append(b.speed).append("Spd ");
        }

        if (b.luck > 0) {
            sb.append("+").append(b.luck).append("Lck ");
        }

        if (b.defense > 0) {
            sb.append("+").append(b.defense).append("Def ");
        }

        if (b.resistance > 0) {
            sb.append("+").append(b.resistance).append("Res ");
        }

        return sb.toString();
    }

    private void drawLabelValue(SpriteBatch batch, String label, String value, float x, float y) {
        context.font.setColor(Color.GRAY);
        context.font.draw(batch, label, x, y);
        context.font.setColor(Color.WHITE);
        context.font.draw(batch, value, x + 60, y);
    }

    private void drawBackground(SpriteBatch batch, float x, float y, float w, float h, Color c) {
        batch.setColor(c);
        batch.draw(context.whitePixel, x, y, w, h);
        batch.setColor(Color.WHITE);
    }

    @Override public void dispose() {
        shapeRenderer.dispose();
    }
}
