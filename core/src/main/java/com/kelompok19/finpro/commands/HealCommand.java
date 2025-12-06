package com.kelompok19.finpro.commands;

import com.kelompok19.finpro.units.Unit;

public class HealCommand implements Command {
    private final Unit healer;
    private final Unit target;

    public HealCommand(Unit healer, Unit target) {
        this.healer = healer;
        this.target = target;
    }

    @Override
    public void execute() {
        int magic = healer.getEffectiveStats().magic;
        int healAmount = 20 + (magic / 3);

        int oldHp = target.getCurrentHp();
        int newHp = Math.min(target.getStats().hp, oldHp + healAmount);

        target.setCurrentHp(newHp);

        int actualHealed = newHp - oldHp;
        System.out.println(healer.getName() + " heals " + target.getName() + " for " + actualHealed + " HP!");
    }
}
