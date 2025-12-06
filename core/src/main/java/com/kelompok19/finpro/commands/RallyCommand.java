package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.units.Stats;
import com.kelompok19.finpro.units.Unit;
import com.kelompok19.finpro.units.UnitManager;

public class RallyCommand implements Command {
    private final Unit user;
    private final UnitManager manager;

    public RallyCommand(Unit user, UnitManager manager) {
        this.user = user;
        this.manager = manager;
    }

    @Override
    public void execute() {
        System.out.println(user.getName() + " uses Rally!");
        Stats bonus = new Stats(0, 4, 0, 0, 4, 0, 0, 0);

        for (Unit ally : manager.getPlayerUnits()) {
            if (ally == user) {
                continue;
            }

            int dist = Math.abs(user.getX() - ally.getX()) + Math.abs(user.getY() - ally.getY());

            if (dist <= 2) {
                ally.addTempStats(bonus);
                System.out.println("-> " + ally.getName() + " rallied!");
            }
        }
    }
}
