package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.units.Unit;

public class MoveCommand implements Command {
    private final Unit unit;
    private final int destX, destY;
    private final int startX, startY;

    public MoveCommand(Unit unit, int destX, int destY) {
        this.unit = unit;
        this.destX = destX;
        this.destY = destY;
        this.startX = unit.getX();
        this.startY = unit.getY();
    }

    @Override
    public void execute() {
        unit.setPosition(destX, destY);
    }

    public void undo() {
        unit.setPosition(startX, startY);
    }
}
