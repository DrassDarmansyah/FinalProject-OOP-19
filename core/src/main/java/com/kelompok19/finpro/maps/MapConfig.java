package com.kelompok19.finpro.maps;

import com.kelompok19.finpro.units.UnitManager;
import java.util.List;

public interface MapConfig {
    String getFilePath();
    void onLeyline(GameMap map);
    boolean isEventTriggered();
    void setupEnemies(UnitManager manager, int mapHeight);
    List<int[]> getDeploymentSlots(int mapHeight);
    int getMaxUnits();
    boolean checkObjective(UnitManager manager);
    String getObjectiveText();
    MapConfig getNextMap();
}
