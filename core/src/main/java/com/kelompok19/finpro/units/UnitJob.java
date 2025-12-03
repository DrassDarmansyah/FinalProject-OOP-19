package com.kelompok19.finpro.units;

public enum UnitJob {
    ARCHER("Archer", MovementType.INFANTRY, "Characters/Archer/Archer with shadows/Archer-Idle.png"),
    AXEMAN("Axeman", MovementType.ARMOR, "Characters/Armored Axeman/Armored Axeman with shadows/Armored_Axeman-Idle.png"),
    KNIGHT("Knight", MovementType.INFANTRY, "Characters/Knight/Knight with shadows/Knight-Idle.png"),
    TEMPLAR("Templar", MovementType.INFANTRY, "Characters/Knight Templar/Knight Templar with shadows/Knight_Templar-Idle.png"),
    LANCER("Lancer", MovementType.CAVALRY, "Characters/Lancer/Lancer with shadows/Lancer-Idle.png"),
    PRIEST("Priest", MovementType.MAGE, "Characters/Priest/Priest with shadows/Priest-Idle.png"),
    SOLDIER("Soldier", MovementType.INFANTRY, "Characters/Soldier/Soldier with shadows/Soldier-Idle.png"),
    SWORDMASTER("Swordmaster", MovementType.LORD, "Characters/Swordsman/Swordsman with shadows/Swordsman-Idle.png"),
    WIZARD("Wizard", MovementType.MAGE, "Characters/Wizard/Wizard with shadows/Wizard-Idle.png"),

    ARMORED_ORC("Armored Orc", MovementType.ARMOR, "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Idle.png"),
    ARMORED_SKELETON("Armored Skel", MovementType.ARMOR, "Characters/Armored Skeleton/Armord Skeleton with shadows/Armored Skeleton-Idle.png"),
    ELITE_ORC("Elite Orc", MovementType.MONSTER_ELITE, "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Idle.png"),
    GREATSWORD_SKELETON("Greatsword Skel", MovementType.MONSTER_ELITE, "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Idle.png"),
    ORC("Orc", MovementType.MONSTER, "Characters/Orc/Orc with shadows/Orc-Idle.png"),
    ORC_RIDER("Orc Rider", MovementType.CAVALRY, "Characters/Orc rider/Orc rider with shadows/Orc rider-Idle.png"),
    SKELETON("Skeleton", MovementType.MONSTER, "Characters/Skeleton/Skeleton with shadows/Skeleton-Idle.png"),
    SKELETON_ARCHER("Skel Archer", MovementType.MONSTER, "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Idle.png"),
    SLIME("Slime", MovementType.MONSTER, "Characters/Slime/Slime with shadows/Slime-Idle.png"),
    WEREBEAR("Werebear", MovementType.BEAST, "Characters/Werebear/Werebear with shadows/Werebear-Idle.png"),
    WEREWOLF("Werewolf", MovementType.BEAST, "Characters/Werewolf/Werewolf with shadows/Werewolf-Idle.png");

    private final String displayName;
    private final MovementType movementType;
    private final String texturePath;

    UnitJob(String displayName, MovementType movementType, String texturePath) {
        this.displayName = displayName;
        this.movementType = movementType;
        this.texturePath = texturePath;
    }

    public String getDisplayName() { return displayName; }
    public MovementType getMovementType() { return movementType; }
    public String getTexturePath() { return texturePath; }
}
