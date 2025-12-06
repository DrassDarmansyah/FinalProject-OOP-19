package com.kelompok19.finpro.chapters;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.kelompok19.finpro.Weapon;
import com.kelompok19.finpro.ai.AggressiveBehavior;
import com.kelompok19.finpro.ai.DefensiveBehavior;
import com.kelompok19.finpro.factories.UnitFactory;
import com.kelompok19.finpro.maps.GameMap;
import com.kelompok19.finpro.maps.MapConfig;
import com.kelompok19.finpro.maps.TerrainType;
import com.kelompok19.finpro.units.*;

import java.util.ArrayList;
import java.util.List;

public class Chapter1 implements MapConfig {
    private boolean eventTriggered = false;

    @Override
    public String getFilePath() {
        return "maps/map1.tmx";
    }

    @Override
    public void onLeyline(GameMap map) {
        if (eventTriggered) {
            return;
        }

        System.out.println("LEYLINE ACTIVATED: The waters recede!");
        eventTriggered = true;

        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer) map.getTiledMap().getLayers().get("background");

        if (backgroundLayer == null) {
            return;
        }

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = backgroundLayer.getCell(x, y);

                if (cell != null && cell.getTile() != null) {
                    Object property = cell.getTile().getProperties().get("terrain");

                    if (property != null && "WATER".equalsIgnoreCase(property.toString())) {
                        backgroundLayer.setCell(x, y, null);

                        if (map.getTile(x, y).getType() == TerrainType.WATER) {
                            map.setTileLogic(x, y, TerrainType.PLAIN);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isEventTriggered() {
        return eventTriggered;
    }

    @Override
    public void setupEnemies(UnitManager manager, int mapHeight) {
        Stats bossStats = new Stats(0, 0, 0, 0, 0, 0, 0, 0);
        Weapon swordWeapon = new Weapon("Sword", 12, 90, 0, 0, -5, 1, 1, null);
        Weapon lanceWeapon = new Weapon("Lance", 14, 80, 0, 0, -5, 1, 1, null);
        Weapon axeWeapon = new Weapon("Axe", 16, 70, 0, 0, -5, 1, 1, null);
        Weapon bowWeapon = new Weapon("Bow", 15, 80, 0, 0, -5, 2, 2, null);

        Unit boss1 = UnitFactory.createBoss("Lancer Boss", UnitJob.LANCER, bossStats, lanceWeapon);
        boss1.setPosition(5, 9);
        boss1.setAI(new DefensiveBehavior());
        manager.addUnit(boss1);

        Unit left1 = UnitFactory.createEnemy("Axeman", UnitJob.ARMORED_AXEMAN, axeWeapon);
        left1.setPosition(6, 11);
        left1.setAI(new DefensiveBehavior());
        manager.addUnit(left1);
        Unit left2 = UnitFactory.createEnemy("Templar", UnitJob.TEMPLAR, lanceWeapon);
        left2.setPosition(7, 9);
        left2.setAI(new DefensiveBehavior());
        manager.addUnit(left2);
        Unit left3 = UnitFactory.createEnemy("Soldier", UnitJob.BOW_SOLDIER, bowWeapon);
        left3.setPosition(3, 10);
        left3.setAI(new DefensiveBehavior());
        manager.addUnit(left3);
        Unit left4 = UnitFactory.createEnemy("Lancer", UnitJob.LANCER, lanceWeapon);
        left4.setPosition(5, 7);
        left4.setAI(new DefensiveBehavior());
        manager.addUnit(left4);

        Unit boss2 = UnitFactory.createBoss("Knight Boss", UnitJob.KNIGHT, bossStats, swordWeapon);
        boss2.setPosition(17, 9);
        boss2.setAI(new AggressiveBehavior());
        manager.addUnit(boss2);

        Unit right1 = UnitFactory.createEnemy("Axeman", UnitJob.ARMORED_AXEMAN, axeWeapon);
        right1.setPosition(16, 11);
        right1.setAI(new AggressiveBehavior());
        manager.addUnit(right1);
        Unit right2 = UnitFactory.createEnemy("Templar", UnitJob.TEMPLAR, lanceWeapon);
        right2.setPosition(15, 9);
        right2.setAI(new AggressiveBehavior());
        manager.addUnit(right2);
        Unit right3 = UnitFactory.createEnemy("Archer", UnitJob.ARCHER, bowWeapon);
        right3.setPosition(16, 7);
        right3.setAI(new AggressiveBehavior());
        manager.addUnit(right3);
        Unit right4 = UnitFactory.createEnemy("Soldier", UnitJob.SWORD_SOLDIER, swordWeapon);
        right4.setPosition(14, 7);
        right4.setAI(new AggressiveBehavior());
        manager.addUnit(right4);
    }

    @Override
    public List<int[]> getDeploymentSlots(int mapHeight) {
        List<int[]> slots = new ArrayList<>();
        slots.add(new int[]{11, 15});
        slots.add(new int[]{10, 16});
        slots.add(new int[]{12, 17});
        return slots;
    }

    @Override
    public int getMaxUnits() {
        return 3;
    }

    @Override
    public boolean checkObjective(UnitManager manager) {
        boolean alphaAlive = false;
        boolean betaAlive = false;

        for (Unit u : manager.getEnemyUnits()) {
            if (u.getName().equals("Lancer Boss")) {
                alphaAlive = true;
            }

            if (u.getName().equals("Knight Boss")) {
                betaAlive = true;
            }
        }
        return !alphaAlive && !betaAlive;
    }

    @Override
    public String getObjectiveText() {
        return "Defeat both Bosses";
    }

    @Override
    public MapConfig getNextMap() {
        return new Chapter2();
    }
}
