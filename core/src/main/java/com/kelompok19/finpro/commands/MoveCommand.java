package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.units.Unit;

public class MoveCommand implements Command {
    private final Unit unit;
    private final int startX, startY;
    private final int endX, endY;

    public MoveCommand(Unit unit, int endX, int endY) {
        this.unit = unit;
        this.endX = endX;
        this.endY = endY;
        this.startX = unit.getX();
        this.startY = unit.getY();
    }

    @Override
    public void execute() {
        unit.setPosition(endX, endY);
    }

    public void undo() {
        unit.setPosition(startX, startY);
    }

    public int getDestX() { return endX; }
    public int getDestY() { return endY; }
}
