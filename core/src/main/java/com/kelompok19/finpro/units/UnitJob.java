package com.kelompok19.finpro.units;

public enum UnitJob {
    ARCHER("Archer", MovementType.INFANTRY, "Characters/Archer/Archer with shadows/Archer-Idle.png", false, 5,
        new Stats(18, 6, 0, 8, 7, 0, 4, 2)),

    AXEMAN("Axeman", MovementType.ARMOR, "Characters/Armored Axeman/Armored Axeman with shadows/Armored_Axeman-Idle.png", false, 4,
        new Stats(22, 8, 0, 5, 4, 0, 9, 1)),

    KNIGHT("Knight", MovementType.INFANTRY, "Characters/Knight/Knight with shadows/Knight-Idle.png", false, 5,
        new Stats(20, 7, 0, 6, 6, 0, 7, 3)),

    TEMPLAR("Templar", MovementType.INFANTRY, "Characters/Knight Templar/Knight Templar with shadows/Knight_Templar-Idle.png", false, 5,
        new Stats(20, 7, 4, 6, 6, 0, 6, 6)),

    LANCER("Lancer", MovementType.CAVALRY, "Characters/Lancer/Lancer with shadows/Lancer-Idle.png", false, 7,
        new Stats(20, 7, 0, 6, 7, 0, 6, 2)),

    PRIEST("Priest", MovementType.MAGE, "Characters/Priest/Priest with shadows/Priest-Idle.png", false, 5,
        new Stats(16, 0, 8, 5, 6, 0, 2, 8)),

    SOLDIER("Soldier", MovementType.INFANTRY, "Characters/Soldier/Soldier with shadows/Soldier-Idle.png", false, 5,
        new Stats(20, 6, 0, 6, 6, 0, 5, 2)),

    SWORDMASTER("Swordmaster", MovementType.LORD, "Characters/Swordsman/Swordsman with shadows/Swordsman-Idle.png", true, 5,
        new Stats(19, 6, 0, 9, 10, 0, 5, 3)),

    WIZARD("Wizard", MovementType.MAGE, "Characters/Wizard/Wizard with shadows/Wizard-Idle.png", false, 5,
        new Stats(17, 1, 9, 6, 7, 0, 3, 7)),

    // --- ENEMY CLASSES ---
    ARMORED_ORC("Armored Orc", MovementType.ARMOR, "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Idle.png", false, 4,
        new Stats(24, 9, 0, 5, 3, 0, 10, 1)),

    ARMORED_SKELETON("Armored Skel", MovementType.ARMOR, "Characters/Armored Skeleton/Armord Skeleton with shadows/Armored Skeleton-Idle.png", false, 4,
        new Stats(22, 8, 0, 6, 4, 0, 8, 2)),

    ELITE_ORC("Elite Orc", MovementType.MONSTER_ELITE, "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Idle.png", false, 5,
        new Stats(30, 12, 0, 9, 8, 0, 10, 4)),

    GREATSWORD_SKELETON("Greatsword Skel", MovementType.MONSTER_ELITE, "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Idle.png", false, 5,
        new Stats(28, 13, 0, 8, 7, 0, 8, 3)),

    ORC("Orc", MovementType.MONSTER, "Characters/Orc/Orc with shadows/Orc-Idle.png", false, 5,
        new Stats(22, 8, 0, 6, 5, 0, 5, 1)),

    ORC_RIDER("Orc Rider", MovementType.CAVALRY, "Characters/Orc rider/Orc rider with shadows/Orc rider-Idle.png", false, 7,
        new Stats(22, 8, 0, 6, 6, 0, 6, 1)),

    SKELETON("Skeleton", MovementType.MONSTER, "Characters/Skeleton/Skeleton with shadows/Skeleton-Idle.png", false, 5,
        new Stats(18, 7, 0, 8, 7, 0, 4, 1)),

    SKELETON_ARCHER("Skel Archer", MovementType.MONSTER, "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Idle.png", false, 5,
        new Stats(18, 6, 0, 9, 7, 0, 3, 1)),

    SLIME("Slime", MovementType.MONSTER, "Characters/Slime/Slime with shadows/Slime-Idle.png", false, 4,
        new Stats(15, 5, 0, 4, 4, 0, 10, 0)),

    WEREBEAR("Werebear", MovementType.BEAST, "Characters/Werebear/Werebear with shadows/Werebear-Idle.png", false, 6,
        new Stats(26, 10, 0, 7, 8, 0, 7, 2)),

    WEREWOLF("Werewolf", MovementType.BEAST, "Characters/Werewolf/Werewolf with shadows/Werewolf-Idle.png", false, 6,
        new Stats(24, 9, 0, 8, 9, 0, 5, 2));

    private final String displayName;
    private final MovementType movementType;
    private final String texturePath;
    private final boolean hasOriginPulse;
    private final int moveRange;
    private final Stats baseStats;

    UnitJob(String displayName, MovementType movementType, String texturePath, boolean hasOriginPulse, int moveRange, Stats baseStats) {
        this.displayName = displayName;
        this.movementType = movementType;
        this.texturePath = texturePath;
        this.hasOriginPulse = hasOriginPulse;
        this.moveRange = moveRange;
        this.baseStats = baseStats;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MovementType getMovementType() {
        return movementType;
    }
    public String getTexturePath() {
        return texturePath;
    }

    public boolean hasOriginPulse() {
        return hasOriginPulse;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public Stats getBaseStats() {
        return baseStats;
    }
}
