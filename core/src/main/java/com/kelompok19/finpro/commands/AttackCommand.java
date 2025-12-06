package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.combat.CombatEngine;
import com.kelompok19.finpro.combat.CombatPreview;
import com.kelompok19.finpro.combat.CombatSimulator;
import com.kelompok19.finpro.combat.CombatStep;
import com.kelompok19.finpro.states.BattleAnimationState;
import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.states.CombatCutsceneState;
import com.kelompok19.finpro.states.GameStateManager;
import com.kelompok19.finpro.units.Unit;

import java.util.List;

public class AttackCommand implements Command {
    private final Unit attacker;
    private final Unit defender;
    private final BattleContext context;
    private final GameStateManager gsm;

    public AttackCommand(Unit attacker, Unit defender, BattleContext context, GameStateManager gsm) {
        this.attacker = attacker;
        this.defender = defender;
        this.context = context;
        this.gsm = gsm;
    }

    @Override
    public void execute() {
        CombatPreview stats = CombatEngine.calculateCombat(attacker, defender, context);
        List<CombatStep> steps = CombatSimulator.simulate(attacker, defender, context);

        if (context.isFightAnimationsOn()) {
            gsm.push(new BattleAnimationState(gsm, context, steps, stats, attacker, defender));
        }

        else {
            gsm.push(new CombatCutsceneState(gsm, context, steps, attacker));
        }
    }
}
