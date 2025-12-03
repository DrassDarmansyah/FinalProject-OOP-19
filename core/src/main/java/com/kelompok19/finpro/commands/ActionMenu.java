package com.kelompok19.finpro.commands;

import java.util.ArrayList;
import java.util.List;

public class ActionMenu {
    private final List<String> options = new ArrayList<>();
    private int selectedIndex = 0;
    private boolean isVisible = false;

    // Standard Commands
    public static final String CMD_MOVE = "Move";
    public static final String CMD_ATTACK = "Attack";
    public static final String CMD_WAIT = "Wait";

    public void show(boolean canMove, boolean canAttack, boolean isPostMove) {
        options.clear();
        selectedIndex = 0;
        isVisible = true;

        if (isPostMove) {
            if (canAttack) options.add(CMD_ATTACK);
            options.add(CMD_WAIT);
        } else {
            if (canMove) options.add(CMD_MOVE);
            if (canAttack) options.add(CMD_ATTACK);
        }
    }

    public void hide() {
        isVisible = false;
        options.clear();
    }

    public void navigateUp() {
        if (!isVisible || options.isEmpty()) return;
        selectedIndex--;
        if (selectedIndex < 0) selectedIndex = options.size() - 1;
    }

    public void navigateDown() {
        if (!isVisible || options.isEmpty()) return;
        selectedIndex++;
        if (selectedIndex >= options.size()) selectedIndex = 0;
    }

    public String getSelectedOption() {
        if (!isVisible || options.isEmpty()) return null;
        return options.get(selectedIndex);
    }

    public boolean isVisible() { return isVisible; }
    public List<String> getOptions() { return options; }
    public int getSelectedIndex() { return selectedIndex; }
}
