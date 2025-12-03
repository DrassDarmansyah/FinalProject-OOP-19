package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.units.Unit;

public class WaitCommand implements Command {
    private final Unit unit;

    public WaitCommand(Unit unit) {
        this.unit = unit;
    }

    @Override
    public void execute() {
        unit.setHasMoved(true);
        System.out.println(unit.getName() + " waits.");
    }
}
