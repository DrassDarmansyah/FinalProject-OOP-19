package com.kelompok19.finpro.ai;

import com.kelompok19.finpro.states.BattleContext;
import com.kelompok19.finpro.states.GameStateManager;
import com.kelompok19.finpro.units.Unit;

public interface AIBehavior {
    boolean act(Unit me, BattleContext context, GameStateManager gsm);
}
