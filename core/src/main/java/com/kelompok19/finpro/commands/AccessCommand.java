package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.maps.GameMap;

public class AccessCommand implements Command {
    private final GameMap map;

    public AccessCommand(GameMap map) {
        this.map = map;
    }

    @Override
    public void execute() {
        map.triggerLeylineEffect();
    }
}
