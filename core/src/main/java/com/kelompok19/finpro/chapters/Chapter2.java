package com.kelompok19.finpro.chapters;

import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.ai.DefensiveBehavior;
import com.kelompok19.finpro.factories.UnitFactory;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.MapConfig;
import com.kelompok19.finpro.units.*;

import java.util.ArrayList;
import java.util.List;

public class Chapter2 implements MapConfig {
    @Override
    public String getFilePath() {
        return "maps/map2.tmx";
    }

    @Override
    public void onLeyline(GameMap map) {

    }

    @Override
    public boolean isEventTriggered() {
        return false;
    }

    @Override
    public void setupEnemies(UnitManager manager, int mapHeight) {
        Stats bossStats = new Stats(0, 0, 0, 0, 0, 0, 0, 0);
        Weapon axeWeapon = new Weapon("Axe", 16, 70, 0, 0, -5, 1, 1, null);

        Unit boss1 = UnitFactory.createBoss("Orc Boss", UnitJob.ELITE_ORC, bossStats, axeWeapon);
        boss1.setPosition(16, 20);
        boss1.setAI(new DefensiveBehavior());
        manager.addUnit(boss1);
    }

    @Override
    public List<int[]> getDeploymentSlots(int mapHeight) {
        List<int[]> slots = new ArrayList<>();
        slots.add(new int[]{15, 1});
        slots.add(new int[]{16, 0});
        slots.add(new int[]{16, 2});
        slots.add(new int[]{17, 1});
        return slots;
    }

    @Override
    public int getMaxUnits() {
        return 4;
    }

    @Override
    public boolean checkObjective(UnitManager manager) {
        return manager.getEnemyUnits().isEmpty();
    }

    @Override
    public String getObjectiveText() {
        return "";
    }

    @Override
    public MapConfig getNextMap() {
        return null;
    }
}
