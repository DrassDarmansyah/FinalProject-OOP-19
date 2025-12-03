package com.kelompok19.finpro.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.commands.ActionMenu;
import com.kelompok19.finpro.commands.MoveCommand;
import com.kelompok19.finpro.units.Unit;

public class ActionMenuState extends BattleState {
    private final Unit selectedUnit;
    private final ActionMenu menu;
    private final MoveCommand lastMove;

    public ActionMenuState(GameStateManager gsm, BattleContext context, Unit unit, MoveCommand lastMove) {
        super(gsm, context);
        this.selectedUnit = unit;
        this.lastMove = lastMove;
        this.menu = new ActionMenu();

        boolean isPostMove = (lastMove != null);
        boolean canMove = !isPostMove;
        boolean canAttack = checkCanAttack(unit);

        menu.show(canMove, canAttack, isPostMove);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) menu.navigateUp();
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) menu.navigateDown();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            String choice = menu.getSelectedOption();
            if (choice == null) return;

            switch (choice) {
                case ActionMenu.CMD_MOVE:
                    gsm.set(new MoveSelectionState(gsm, context, selectedUnit));
                    break;
                case ActionMenu.CMD_ATTACK:
                    gsm.set(new AttackSelectionState(gsm, context, selectedUnit, lastMove));
                    break;
                case ActionMenu.CMD_WAIT:
                    selectedUnit.setHasMoved(true);
                    gsm.pop();
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (lastMove != null) {
                lastMove.undo();

                gsm.set(new MoveSelectionState(gsm, context, selectedUnit));
            } else {
                gsm.pop();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        renderMapAndUnits(batch);
        batch.begin();
        menu.render(batch, context.font, selectedUnit.getX(), selectedUnit.getY(), context.camera);
        batch.end();
        renderTerrainInfo(batch);
    }

    @Override
    public void dispose() {
        menu.dispose();
    }

    private boolean checkCanAttack(Unit unit) {
        Weapon w = unit.getWeapon();
        if (w == null) return false;

        for (Unit enemy : context.unitManager.getEnemyUnits()) {
            int dist = Math.abs(unit.getX() - enemy.getX()) + Math.abs(unit.getY() - enemy.getY());
            if (dist >= w.rangeMin && dist <= w.rangeMax) return true;
        }
        return false;
    }
}
