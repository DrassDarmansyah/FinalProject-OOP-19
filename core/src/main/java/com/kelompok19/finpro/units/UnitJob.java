package com.kelompok19.finpro.units;

public enum UnitJob {
    SWORDMASTER("Swordmaster", MovementType.LORD,
        "Characters/Swordsman/Swordsman with shadows/Swordsman-Idle.png", true, 6,
        new Stats(38, 28, 0, 35, 38, 15, 18, 18),
        "Characters/Swordsman/Swordsman with shadows/Swordsman-Attack01.png",
        "Characters/Swordsman/Swordsman with shadows/Swordsman-Attack02.png",
        "Characters/Swordsman/Swordsman with shadows/Swordsman-Attack03.png",
        null,
        "Characters/Swordsman/Swordsman with shadows/Swordsman-Hurt.png",
        "Characters/Swordsman/Swordsman with shadows/Swordsman-Death.png"),

    SWORD_SOLDIER("Soldier", MovementType.INFANTRY, "Characters/Soldier/Soldier with shadows/Soldier-Idle.png", false, 6,
        new Stats(35, 26, 0, 26, 26, 10, 20, 15),
        "Characters/Soldier/Soldier with shadows/Soldier-Attack01.png",
        "Characters/Soldier/Soldier with shadows/Soldier-Attack02.png",
        "Characters/Soldier/Soldier with shadows/Soldier-Attack02.png",
        null,
        "Characters/Soldier/Soldier with shadows/Soldier-Hurt.png",
        "Characters/Soldier/Soldier with shadows/Soldier-Death.png"),

    BOW_SOLDIER("Soldier", MovementType.INFANTRY, "Characters/Soldier/Soldier with shadows/Soldier-Idle.png", false, 6,
        new Stats(35, 26, 0, 26, 26, 10, 20, 15),
        "Characters/Soldier/Soldier with shadows/Soldier-Attack03.png",
        "Characters/Soldier/Soldier with shadows/Soldier-Attack03.png",
        "Characters/Soldier/Soldier with shadows/Soldier-Attack03.png",
        null,
        "Characters/Soldier/Soldier with shadows/Soldier-Hurt.png",
        "Characters/Soldier/Soldier with shadows/Soldier-Death.png"),

    KNIGHT("Knight", MovementType.INFANTRY, "Characters/Knight/Knight with shadows/Knight-Idle.png", false, 6,
        new Stats(40, 30, 0, 24, 22, 10, 28, 15),
        "Characters/Knight/Knight with shadows/Knight-Attack01.png",
        "Characters/Knight/Knight with shadows/Knight-Attack02.png",
        "Characters/Knight/Knight with shadows/Knight-Attack02.png",
        null,
        "Characters/Knight/Knight with shadows/Knight-Hurt.png",
        "Characters/Knight/Knight with shadows/Knight-Death.png"),

    FIRE_KNIGHT("Knight", MovementType.INFANTRY, "Characters/Knight/Knight with shadows/Knight-Idle.png", false, 6,
        new Stats(40, 3, 30, 24, 22, 10, 28, 15),
        "Characters/Knight/Knight with shadows/Knight-Attack03.png",
        "Characters/Knight/Knight with shadows/Knight-Attack03.png",
        "Characters/Knight/Knight with shadows/Knight-Attack03.png",
        "Magic/Wizard-Attack02_Effect.png",
        "Characters/Knight/Knight with shadows/Knight-Hurt.png",
        "Characters/Knight/Knight with shadows/Knight-Death.png"),

    TEMPLAR("Templar", MovementType.INFANTRY, "Characters/Knight Templar/Knight Templar with shadows/Knight Templar-Idle.png", false, 6,
        new Stats(38, 28, 15, 22, 24, 12, 25, 20),
        "Characters/Knight Templar/Knight Templar with shadows/Knight Templar-Attack01.png",
        "Characters/Knight Templar/Knight Templar with shadows/Knight Templar-Attack02.png",
        "Characters/Knight Templar/Knight Templar with shadows/Knight Templar-Attack03.png",
        null,
        "Characters/Knight Templar/Knight Templar with shadows/Knight Templar-Hurt.png",
        "Characters/Knight Templar/Knight Templar with shadows/Knight Templar-Death.png"),

    LANCER("Lancer", MovementType.CAVALRY, "Characters/Lancer/Lancer with shadows/Lancer-Idle.png", false, 8,
        new Stats(38, 30, 0, 28, 30, 8, 22, 18),
        "Characters/Lancer/Lancer with shadows/Lancer-Attack01.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Attack02.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Attack02.png",
        null,
        "Characters/Lancer/Lancer with shadows/Lancer-Hurt.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Death.png"),

    FIRE_LANCER("Lancer", MovementType.CAVALRY, "Characters/Lancer/Lancer with shadows/Lancer-Idle.png", false, 8,
        new Stats(38, 30, 0, 28, 30, 8, 22, 18),
        "Characters/Lancer/Lancer with shadows/Lancer-Attack03.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Attack03.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Attack03.png",
        "Magic/Wizard-Attack02_Effect.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Hurt.png",
        "Characters/Lancer/Lancer with shadows/Lancer-Death.png"),

    ARMORED_AXEMAN("Axeman", MovementType.ARMOR, "Characters/Armored Axeman/Armored Axeman with shadows/Armored Axeman-Idle.png", false, 5,
        new Stats(45, 35, 0, 20, 18, 5, 30, 10),
        "Characters/Armored Axeman/Armored Axeman with shadows/Armored Axeman-Attack01.png",
        "Characters/Armored Axeman/Armored Axeman with shadows/Armored Axeman-Attack02.png",
        "Characters/Armored Axeman/Armored Axeman with shadows/Armored Axeman-Attack03.png",
        null,
        "Characters/Armored Axeman/Armored Axeman with shadows/Armored Axeman-Hurt.png",
        "Characters/Armored Axeman/Armored Axeman with shadows/Armored Axeman-Death.png"),

    FIRE_WIZARD("Wizard", MovementType.MAGE, "Characters/Wizard/Wizard with shadows/Wizard-Idle.png", false, 6,
        new Stats(32, 0, 32, 24, 28, 10, 14, 28),
        "Characters/Wizard/Wizard with shadows/Wizard-Attack02.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Attack02.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Attack02.png",
        "Magic/Wizard-Attack02_Effect.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Hurt.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Death.png"),

    ICE_WIZARD("Wizard", MovementType.MAGE, "Characters/Wizard/Wizard with shadows/Wizard-Idle.png", false, 6,
        new Stats(32, 0, 32, 24, 28, 10, 14, 28),
        "Characters/Wizard/Wizard with shadows/Wizard-Attack01.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Attack01.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Attack01.png",
        "Magic/Wizard-Attack01_Effect.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Hurt.png",
        "Characters/Wizard/Wizard with shadows/Wizard-Death.png"),

    PRIEST("Priest", MovementType.MAGE, "Characters/Priest/Priest with shadows/Priest-Idle.png", false, 6,
        new Stats(30, 0, 28, 20, 26, 15, 12, 30),
        "Characters/Priest/Priest with shadows/Priest-Attack.png",
        "Characters/Priest/Priest with shadows/Priest-Attack.png",
        "Characters/Priest/Priest with shadows/Priest-Attack.png",
        "Magic/Priest-Attack_Effect.png",
        "Characters/Priest/Priest with shadows/Priest-Hurt.png",
        "Characters/Priest/Priest with shadows/Priest-Death.png"),

    ARCHER("Archer", MovementType.INFANTRY, "Characters/Archer/Archer with shadows/Archer-Idle.png", false, 6,
        new Stats(32, 28, 0, 30, 32, 10, 18, 15),
        "Characters/Archer/Archer with shadows/Archer-Attack01.png",
        "Characters/Archer/Archer with shadows/Archer-Attack02.png",
        "Characters/Archer/Archer with shadows/Archer-Attack02.png",
        null,
        "Characters/Archer/Archer with shadows/Archer-Hurt.png",
        "Characters/Archer/Archer with shadows/Archer-Death.png"),

    ELITE_ORC("Elite Orc", MovementType.MONSTER_ELITE, "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Idle.png", false, 6,
        new Stats(63, 40, 0, 26, 27, 5, 22, 9),
        "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Attack01.png",
        "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Attack02.png",
        "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Attack03.png",
        null,
        "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Hurt.png",
        "Characters/Elite Orc/Elite Orc with shadows/Elite Orc-Death.png"),

    ORC("Orc", MovementType.MONSTER, "Characters/Orc/Orc with shadows/Orc-Idle.png", false, 6,
        new Stats(63, 40, 0, 26, 27, 5, 22, 9),
        "Characters/Orc/Orc with shadows/Orc-Attack01.png",
        "Characters/Orc/Orc with shadows/Orc-Attack02.png",
        "Characters/Orc/Orc with shadows/Orc-Attack02.png",
        null,
        "Characters/Orc/Orc with shadows/Orc-Hurt.png",
        "Characters/Orc/Orc with shadows/Orc-Death.png"),

    ARMORED_ORC("Armored Orc", MovementType.ARMOR, "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Idle.png", false, 5,
        new Stats(59, 38, 0, 25, 11, 14, 40, 19),
        "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Attack01.png",
        "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Attack02.png",
        "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Attack03.png",
        null,
        "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Hurt.png",
        "Characters/Armored Orc/Armored Orc with shadows/Armored Orc-Death.png"),

    ARMORED_SKELETON("Armored Skel", MovementType.ARMOR, "Characters/Armored Skeleton/Armord Skeleton with shadows/Armored Skeleton-Idle.png", false, 6,
        new Stats(57, 35, 0, 22, 17, 10, 37, 17),
        "Characters/Armored Skeleton/Armored Skeleton with shadows/Armored Skeleton-Attack01.png",
        "Characters/Armored Skeleton/Armored Skeleton with shadows/Armored Skeleton-Attack02.png",
        "Characters/Armored Skeleton/Armored Skeleton with shadows/Armored Skeleton-Attack02.png",
        null,
        "Characters/Armored Skeleton/Armored Skeleton with shadows/Armored Skeleton-Hurt.png",
        "Characters/Armored Skeleton/Armored Skeleton with shadows/Armored Skeleton-Death.png"),

    GREATSWORD_SKELETON("Greatsword Skel", MovementType.MONSTER_ELITE, "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Idle.png", false, 6,
        new Stats(58, 32, 0, 33, 27, 14, 28, 17),
        "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Attack01.png",
        "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Attack02.png",
        "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Attack03.png",
        null,
        "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Hurt.png",
        "Characters/Greatsword Skeleton/Greatsword Skeleton with shadows/Greatsword Skeleton-Death.png"),

    ORC_RIDER("Orc Rider", MovementType.CAVALRY, "Characters/Orc rider/Orc rider with shadows/Orc rider-Idle.png", false, 8,
        new Stats(51, 31, 1, 22, 22, 14, 30, 24),
        "Characters/Orc rider/Orc rider with shadows/Orc rider-Attack01.png",
        "Characters/Orc rider/Orc rider with shadows/Orc rider-Attack02.png",
        "Characters/Orc rider/Orc rider with shadows/Orc rider-Attack03.png",
        null,
        "Characters/Orc rider/Orc rider with shadows/Orc rider-Hurt.png",
        "Characters/Orc rider/Orc rider with shadows/Orc rider-Death.png"),

    SKELETON("Skeleton", MovementType.MONSTER, "Characters/Skeleton/Skeleton with shadows/Skeleton-Idle.png", false, 6,
        new Stats(58, 32, 0, 33, 27, 14, 28, 17),
        "Characters/Skeleton/Skeleton with shadows/Skeleton-Attack01.png",
        "Characters/Skeleton/Skeleton with shadows/Skeleton-Attack02.png",
        "Characters/Skeleton/Skeleton with shadows/Skeleton-Attack02.png",
        null,
        "Characters/Skeleton/Skeleton with shadows/Skeleton-Hurt.png",
        "Characters/Skeleton/Skeleton with shadows/Skeleton-Death.png"),

    SKELETON_ARCHER("Skel Archer", MovementType.MONSTER, "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Idle.png", false, 5,
        new Stats(54, 30, 0, 32, 28, 12, 28, 17),
        "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Attack01.png",
        "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Attack02.png",
        "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Attack02.png",
        null,
        "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Hurt.png",
        "Characters/Skeleton Archer/Skeleton Archer with shadows/Skeleton Archer-Death.png"),

    SLIME("Slime", MovementType.MONSTER, "Characters/Slime/Slime with shadows/Slime-Idle.png", false, 6,
        new Stats(49, 25, 0, 32, 33, 11, 21, 31),
        "Characters/Slime/Slime with shadows/Slime-Attack01.png",
        "Characters/Slime/Slime with shadows/Slime-Attack02.png",
        "Characters/Slime/Slime with shadows/Slime-Attack02.png",
        null,
        "Characters/Slime/Slime with shadows/Slime-Hurt.png",
        "Characters/Slime/Slime with shadows/Slime-Death.png"),

    WEREBEAR("Werebear", MovementType.BEAST, "Characters/Werebear/Werebear with shadows/Werebear-Idle.png", false, 6,
        new Stats(57, 33, 0, 23, 24, 13, 29, 16),
        "Characters/Werebear/Werebear with shadows/Werebear-Attack01.png",
        "Characters/Werebear/Werebear with shadows/Werebear-Attack02.png",
        "Characters/Werebear/Werebear with shadows/Werebear-Attack03.png",
        null,
        "Characters/Werebear/Werebear with shadows/Werebear-Hurt.png",
        "Characters/Werebear/Werebear with shadows/Werebear-Death.png"),

    WEREWOLF("Werewolf", MovementType.BEAST, "Characters/Werewolf/Werewolf with shadows/Werewolf-Idle.png", false, 6,
        new Stats(51, 29, 4, 26, 33, 16, 22, 23),
        "Characters/Werewolf/Werewolf with shadows/Werewolf-Attack01.png",
        "Characters/Werewolf/Werewolf with shadows/Werewolf-Attack02.png",
        "Characters/Werewolf/Werewolf with shadows/Werewolf-Attack02.png",
        null,
        "Characters/Werewolf/Werewolf with shadows/Werewolf-Hurt.png",
        "Characters/Werewolf/Werewolf with shadows/Werewolf-Death.png");

    private final String displayName;
    private final MovementType movementType;
    private final String texturePath;
    private final boolean hasOriginPulse;
    private final int moveRange;
    private final Stats baseStats;

    private final String attack1Path;
    private final String attack2Path;
    private final String critPath;
    private final String magicPath;
    private final String hurtPath;
    private final String deathPath;

    UnitJob(String displayName, MovementType movementType, String texturePath, boolean hasOriginPulse, int moveRange, Stats baseStats,
            String attack1Path, String attack2Path, String critPath, String magicPath, String hurtPath, String deathPath) {
        this.displayName = displayName;
        this.movementType = movementType;
        this.texturePath = texturePath;
        this.hasOriginPulse = hasOriginPulse;
        this.moveRange = moveRange;
        this.baseStats = baseStats;
        this.attack1Path = attack1Path;
        this.attack2Path = attack2Path;
        this.critPath = critPath;
        this.magicPath = magicPath;
        this.hurtPath = hurtPath;
        this.deathPath = deathPath;
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

    public String getAttack1Path() {
        return attack1Path;
    }

    public String getAttack2Path() {
        return attack2Path;
    }

    public String getCritPath() {
        return critPath;
    }

    public String getMagicPath() {
        return magicPath;
    }

    public String getHurtPath() {
        return hurtPath;
    }

    public String getDeathPath() {
        return deathPath;
    }
}
