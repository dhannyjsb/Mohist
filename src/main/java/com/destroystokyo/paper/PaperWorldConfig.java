package com.destroystokyo.paper;

import cn.pfcraft.Mohist;
import cn.pfcraft.i18n.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spigotmc.SpigotWorldConfig;

import java.util.List;

public class PaperWorldConfig
{
    private final String worldName;
    private final SpigotWorldConfig spigotConfig;
    private final YamlConfiguration config;
    private boolean verbose;
    public int cactusMaxHeight;
    public int reedMaxHeight;
    public double babyZombieMovementSpeed;
    public int fishingMinTicks;
    public int fishingMaxTicks;
    public boolean nerfedMobsShouldJump;
    public int softDespawnDistance;
    public int hardDespawnDistance;
    public boolean keepSpawnInMemory;
    public int fallingBlockHeightNerf;
    public int entityTNTHeightNerf;
    public int waterOverLavaFlowSpeed;
    public boolean netherVoidTopDamage;
    public boolean queueLightUpdates;
    public boolean disableEndCredits;
    public boolean generateCanyon;
    public boolean generateCaves;
    public boolean generateDungeon;
    public boolean generateFortress;
    public boolean generateMineshaft;
    public boolean generateMonument;
    public boolean generateStronghold;
    public boolean generateTemple;
    public boolean generateVillage;
    public boolean generateFlatBedrock;
    public boolean disableExtremeHillsEmeralds;
    public boolean disableExtremeHillsMonsterEggs;
    public boolean disableMesaAdditionalGold;
    public boolean optimizeExplosions;
    public boolean fastDrainLava;
    public boolean fastDrainWater;
    public int lavaFlowSpeedNormal;
    public int lavaFlowSpeedNether;
    public boolean disableExplosionKnockback;
    public boolean disableThunder;
    public boolean disableIceAndSnow;
    public int mobSpawnerTickRate;
    public int containerUpdateTickRate;
    public boolean disableChestCatDetection;
    public boolean disablePlayerCrits;
    public boolean allChunksAreSlimeChunks;
    public int portalSearchRadius;
    public boolean disableTeleportationSuffocationCheck;
    public boolean nonPlayerEntitiesOnScoreboards;
    public boolean allowLeashingUndeadHorse;
    public int nonPlayerArrowDespawnRate;
    public double skeleHorseSpawnChance;
    public boolean firePhysicsEventForRedstone;
    public boolean useInhabitedTime;
    public int grassUpdateRate;
    public short keepLoadedRange;
    public boolean useVanillaScoreboardColoring;
    public boolean frostedIceEnabled;
    public int frostedIceDelayMin;
    public int frostedIceDelayMax;
    public boolean autoReplenishLootables;
    public boolean restrictPlayerReloot;
    public boolean changeLootTableSeedOnFill;
    public int maxLootableRefills;
    public int lootableRegenMin;
    public int lootableRegenMax;
    public boolean preventTntFromMovingInWater;
    public boolean altFallingBlockOnGround;
    public boolean isHopperPushBased;
    public long delayChunkUnloadsBy;
    public boolean skipEntityTickingInChunksScheduledForUnload;
    public boolean elytraHitWallDamage;
    public int queueSizeAutoSaveThreshold;
    public boolean removeCorruptTEs;
    public boolean filterNBTFromSpawnEgg;
    public boolean enableTreasureMaps;
    public boolean treasureMapsAlreadyDiscovered;
    public boolean armorStandEntityLookups;
    public int maxCollisionsPerEntity;
    public boolean parrotsHangOnBetter;
    public boolean disableCreeperLingeringEffect;
    public boolean antiXray;
    public boolean asynchronous;
    public int maxChunkSectionIndex;
    public int updateRadius;
    public List<Object> hiddenBlocks;
    public List<Object> replacementBlocks;
    public int expMergeMaxValue;
    public int maxChunkSendsPerTick;
    public int maxChunkGensPerTick;
    public double squidMaxSpawnHeight;
    public boolean cooldownHopperWhenFull;
    public boolean disableHopperMoveEvents;
    public boolean disableSprintInterruptionOnAttack;
    public boolean allowPermaChunkLoaders;
    public boolean disableEnderpearlExploit;
    public int shieldBlockingDelay;
    public boolean scanForLegacyEnderDragon;
    public int bedSearchRadius;
    public DuplicateUUIDMode duplicateUUIDMode;
    public int duplicateUUIDDeleteRange;
    public boolean armorStandTick;

    public PaperWorldConfig(final String worldName, final SpigotWorldConfig spigotConfig) {
        this.nonPlayerEntitiesOnScoreboards = false;
        this.allowLeashingUndeadHorse = false;
        this.nonPlayerArrowDespawnRate = -1;
        this.firePhysicsEventForRedstone = false;
        this.useInhabitedTime = true;
        this.grassUpdateRate = 1;
        this.frostedIceEnabled = true;
        this.frostedIceDelayMin = 20;
        this.frostedIceDelayMax = 40;
        this.skipEntityTickingInChunksScheduledForUnload = true;
        this.elytraHitWallDamage = true;
        this.queueSizeAutoSaveThreshold = 50;
        this.removeCorruptTEs = false;
        this.filterNBTFromSpawnEgg = true;
        this.enableTreasureMaps = true;
        this.treasureMapsAlreadyDiscovered = false;
        this.armorStandEntityLookups = true;
        this.maxChunkSendsPerTick = 81;
        this.maxChunkGensPerTick = 10;
        this.cooldownHopperWhenFull = true;
        this.disableHopperMoveEvents = false;
        this.allowPermaChunkLoaders = false;
        this.disableEnderpearlExploit = true;
        this.shieldBlockingDelay = 5;
        this.scanForLegacyEnderDragon = true;
        this.bedSearchRadius = 1;
        this.duplicateUUIDMode = DuplicateUUIDMode.SAFE_REGEN;
        this.duplicateUUIDDeleteRange = 32;
        this.armorStandTick = true;
        this.worldName = worldName;
        this.spigotConfig = spigotConfig;
        this.config = PaperConfig.config;
        this.init();
    }

    public void init() {
        Object[] p = {worldName};
        PaperConfig.LOGGER.info(Message.getFormatString(Message.World_settings, p));
        PaperConfig.readConfig(PaperWorldConfig.class, this);
    }

    private void set(final String path, final Object val) {
        this.config.set("world-settings.default." + path, val);
        if (this.config.get("world-settings." + this.worldName + "." + path) != null) {
            this.config.set("world-settings." + this.worldName + "." + path, val);
        }
    }

    private boolean getBoolean(final String path, final boolean def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getBoolean("world-settings." + this.worldName + "." + path, this.config.getBoolean("world-settings.default." + path));
    }

    private double getDouble(final String path, final double def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getDouble("world-settings." + this.worldName + "." + path, this.config.getDouble("world-settings.default." + path));
    }

    private int getInt(final String path, final int def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getInt("world-settings." + this.worldName + "." + path, this.config.getInt("world-settings.default." + path));
    }

    private float getFloat(final String path, final float def) {
        return (float)this.getDouble(path, def);
    }

    private <T> List getList(final String path, final T def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getList("world-settings." + this.worldName + "." + path, this.config.getList("world-settings.default." + path));
    }

    private String getString(final String path, final String def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getString("world-settings." + this.worldName + "." + path, this.config.getString("world-settings.default." + path));
    }

    private void blockGrowthHeight() {
        this.cactusMaxHeight = this.getInt("max-growth-height.cactus", 3);
        this.reedMaxHeight = this.getInt("max-growth-height.reeds", 3);
        PaperConfig.log("Max height for cactus growth " + this.cactusMaxHeight + ". Max height for reed growth " + this.reedMaxHeight);
    }

    private void babyZombieMovementSpeed() {
        this.babyZombieMovementSpeed = this.getDouble("baby-zombie-movement-speed", 0.5);
        PaperConfig.log("Baby zombies will move at the speed of " + this.babyZombieMovementSpeed);
    }

    private void fishingTickRange() {
        this.fishingMinTicks = this.getInt("fishing-time-range.MinimumTicks", 100);
        this.fishingMaxTicks = this.getInt("fishing-time-range.MaximumTicks", 600);
        PaperConfig.log("Fishing time ranges are between " + this.fishingMinTicks + " and " + this.fishingMaxTicks + " ticks");
    }

    private void nerfedMobsShouldJump() {
        this.nerfedMobsShouldJump = this.getBoolean("spawner-nerfed-mobs-should-jump", false);
    }

    private void despawnDistances() {
        this.softDespawnDistance = this.getInt("despawn-ranges.soft", 32);
        this.hardDespawnDistance = this.getInt("despawn-ranges.hard", 128);
        if (this.softDespawnDistance > this.hardDespawnDistance) {
            this.softDespawnDistance = this.hardDespawnDistance;
        }
        PaperConfig.log("Living Entity Despawn Ranges:  Soft: " + this.softDespawnDistance + " Hard: " + this.hardDespawnDistance);
        this.softDespawnDistance *= this.softDespawnDistance;
        this.hardDespawnDistance *= this.hardDespawnDistance;
    }

    private void keepSpawnInMemory() {
        this.keepSpawnInMemory = this.getBoolean("keep-spawn-loaded", true);
        PaperConfig.log("Keep spawn chunk loaded: " + this.keepSpawnInMemory);
    }

    private void heightNerfs() {
        this.fallingBlockHeightNerf = this.getInt("falling-block-height-nerf", 0);
        this.entityTNTHeightNerf = this.getInt("tnt-entity-height-nerf", 0);
        if (this.fallingBlockHeightNerf != 0) {
            PaperConfig.log("Falling Block Height Limit set to Y: " + this.fallingBlockHeightNerf);
        }
        if (this.entityTNTHeightNerf != 0) {
            PaperConfig.log("TNT Entity Height Limit set to Y: " + this.entityTNTHeightNerf);
        }
    }

    private void waterOverLawFlowSpeed() {
        this.waterOverLavaFlowSpeed = this.getInt("water-over-lava-flow-speed", 5);
        PaperConfig.log("Water over lava flow speed: " + this.waterOverLavaFlowSpeed);
    }

    private void netherVoidTopDamage() {
        this.netherVoidTopDamage = this.getBoolean("nether-ceiling-void-damage", false);
        PaperConfig.log("Top of the nether void damage: " + this.netherVoidTopDamage);
    }

    private void queueLightUpdates() {
        this.queueLightUpdates = this.getBoolean("queue-light-updates", false);
        PaperConfig.log("Lighting Queue enabled: " + this.queueLightUpdates);
    }

    private void disableEndCredits() {
        this.disableEndCredits = this.getBoolean("game-mechanics.disable-end-credits", false);
        PaperConfig.log("End credits disabled: " + this.disableEndCredits);
    }

    private void generatorSettings() {
        this.generateCanyon = this.getBoolean("generator-settings.canyon", true);
        this.generateCaves = this.getBoolean("generator-settings.caves", true);
        this.generateDungeon = this.getBoolean("generator-settings.dungeon", true);
        this.generateFortress = this.getBoolean("generator-settings.fortress", true);
        this.generateMineshaft = this.getBoolean("generator-settings.mineshaft", true);
        this.generateMonument = this.getBoolean("generator-settings.monument", true);
        this.generateStronghold = this.getBoolean("generator-settings.stronghold", true);
        this.generateTemple = this.getBoolean("generator-settings.temple", true);
        this.generateVillage = this.getBoolean("generator-settings.village", true);
        this.generateFlatBedrock = this.getBoolean("generator-settings.flat-bedrock", false);
        this.disableExtremeHillsEmeralds = this.getBoolean("generator-settings.disable-extreme-hills-emeralds", false);
        this.disableExtremeHillsMonsterEggs = this.getBoolean("generator-settings.disable-extreme-hills-monster-eggs", false);
        this.disableMesaAdditionalGold = this.getBoolean("generator-settings.disable-mesa-additional-gold", false);
    }

    private void optimizeExplosions() {
        this.optimizeExplosions = this.getBoolean("optimize-explosions", false);
        PaperConfig.log("Optimize explosions: " + this.optimizeExplosions);
    }

    private void fastDrain() {
        this.fastDrainLava = this.getBoolean("fast-drain.lava", false);
        this.fastDrainWater = this.getBoolean("fast-drain.water", false);
    }

    private void lavaFlowSpeeds() {
        this.lavaFlowSpeedNormal = this.getInt("lava-flow-speed.normal", 30);
        this.lavaFlowSpeedNether = this.getInt("lava-flow-speed.nether", 10);
    }

    private void disableExplosionKnockback() {
        this.disableExplosionKnockback = this.getBoolean("disable-explosion-knockback", false);
    }

    private void disableThunder() {
        this.disableThunder = this.getBoolean("disable-thunder", false);
    }

    private void disableIceAndSnow() {
        this.disableIceAndSnow = this.getBoolean("disable-ice-and-snow", false);
    }

    private void mobSpawnerTickRate() {
        this.mobSpawnerTickRate = this.getInt("mob-spawner-tick-rate", 1);
    }

    private void containerUpdateTickRate() {
        this.containerUpdateTickRate = this.getInt("container-update-tick-rate", 1);
    }

    private void disableChestCatDetection() {
        this.disableChestCatDetection = this.getBoolean("game-mechanics.disable-chest-cat-detection", false);
    }

    private void disablePlayerCrits() {
        this.disablePlayerCrits = this.getBoolean("game-mechanics.disable-player-crits", false);
    }

    private void allChunksAreSlimeChunks() {
        this.allChunksAreSlimeChunks = this.getBoolean("all-chunks-are-slime-chunks", false);
    }

    private void portalSearchRadius() {
        this.portalSearchRadius = this.getInt("portal-search-radius", 128);
    }

    private void disableTeleportationSuffocationCheck() {
        this.disableTeleportationSuffocationCheck = this.getBoolean("disable-teleportation-suffocation-check", false);
    }

    private void nonPlayerEntitiesOnScoreboards() {
        this.nonPlayerEntitiesOnScoreboards = this.getBoolean("allow-non-player-entities-on-scoreboards", false);
    }

    private void allowLeashingUndeadHorse() {
        this.allowLeashingUndeadHorse = this.getBoolean("allow-leashing-undead-horse", false);
    }

    private void nonPlayerArrowDespawnRate() {
        this.nonPlayerArrowDespawnRate = this.getInt("non-player-arrow-despawn-rate", -1);
        if (this.nonPlayerArrowDespawnRate == -1) {
            this.nonPlayerArrowDespawnRate = this.spigotConfig.arrowDespawnRate;
        }
        PaperConfig.log("Non Player Arrow Despawn Rate: " + this.nonPlayerArrowDespawnRate);
    }

    private void skeleHorseSpawnChance() {
        this.skeleHorseSpawnChance = this.getDouble("skeleton-horse-thunder-spawn-chance", 0.01);
    }

    private void firePhysicsEventForRedstone() {
        this.firePhysicsEventForRedstone = this.getBoolean("fire-physics-event-for-redstone", this.firePhysicsEventForRedstone);
    }

    private void useInhabitedTime() {
        this.useInhabitedTime = this.getBoolean("use-chunk-inhabited-timer", true);
    }

    private void grassUpdateRate() {
        this.grassUpdateRate = Math.max(0, this.getInt("grass-spread-tick-rate", this.grassUpdateRate));
        PaperConfig.log("Grass Spread Tick Rate: " + this.grassUpdateRate);
    }

    private void keepLoadedRange() {
        this.keepLoadedRange = (short)(this.getInt("keep-spawn-loaded-range", Math.min(this.spigotConfig.viewDistance, 8)) * 16);
        PaperConfig.log("Keep Spawn Loaded Range: " + this.keepLoadedRange / 16);
    }

    private void useVanillaScoreboardColoring() {
        this.useVanillaScoreboardColoring = this.getBoolean("use-vanilla-world-scoreboard-name-coloring", false);
    }

    private void frostedIce() {
        this.frostedIceEnabled = this.getBoolean("frosted-ice.enabled", this.frostedIceEnabled);
        this.frostedIceDelayMin = this.getInt("frosted-ice.delay.min", this.frostedIceDelayMin);
        this.frostedIceDelayMax = this.getInt("frosted-ice.delay.max", this.frostedIceDelayMax);
        PaperConfig.log("Frosted Ice: " + (this.frostedIceEnabled ? "enabled" : "disabled") + " / delay: min=" + this.frostedIceDelayMin + ", max=" + this.frostedIceDelayMax);
    }

    private void enhancedLootables() {
        this.autoReplenishLootables = this.getBoolean("lootables.auto-replenish", false);
        this.restrictPlayerReloot = this.getBoolean("lootables.restrict-player-reloot", true);
        this.changeLootTableSeedOnFill = this.getBoolean("lootables.reset-seed-on-fill", true);
        this.maxLootableRefills = this.getInt("lootables.max-refills", -1);
        this.lootableRegenMin = PaperConfig.getSeconds(this.getString("lootables.refresh-min", "12h"));
        this.lootableRegenMax = PaperConfig.getSeconds(this.getString("lootables.refresh-max", "2d"));
        if (this.autoReplenishLootables) {
            PaperConfig.log("Lootables: Replenishing every " + PaperConfig.timeSummary(this.lootableRegenMin) + " to " + PaperConfig.timeSummary(this.lootableRegenMax) + (this.restrictPlayerReloot ? " (restricting reloot)" : ""));
        }
    }

    private void preventTntFromMovingInWater() {
        if (PaperConfig.version < 13) {
            final boolean oldVal = this.getBoolean("enable-old-tnt-cannon-behaviors", false);
            this.set("prevent-tnt-from-moving-in-water", oldVal);
        }
        this.preventTntFromMovingInWater = this.getBoolean("prevent-tnt-from-moving-in-water", false);
        PaperConfig.log("Prevent TNT from moving in water: " + this.preventTntFromMovingInWater);
    }

    private void altFallingBlockOnGround() {
        this.altFallingBlockOnGround = this.getBoolean("use-alternate-fallingblock-onGround-detection", false);
    }

    private void isHopperPushBased() {
        this.isHopperPushBased = this.getBoolean("hopper.push-based", false);
    }

    private void delayChunkUnloadsBy() {
        this.delayChunkUnloadsBy = PaperConfig.getSeconds(this.getString("delay-chunk-unloads-by", "10s"));
        if (this.delayChunkUnloadsBy > 0L) {
            PaperConfig.log("Delaying chunk unloads by " + this.delayChunkUnloadsBy + " seconds");
            this.delayChunkUnloadsBy *= 1000L;
        }
    }

    private void skipEntityTickingInChunksScheduledForUnload() {
        this.skipEntityTickingInChunksScheduledForUnload = this.getBoolean("skip-entity-ticking-in-chunks-scheduled-for-unload", this.skipEntityTickingInChunksScheduledForUnload);
    }

    private void elytraHitWallDamage() {
        this.elytraHitWallDamage = this.getBoolean("elytra-hit-wall-damage", true);
    }

    private void queueSizeAutoSaveThreshold() {
        this.queueSizeAutoSaveThreshold = this.getInt("save-queue-limit-for-auto-save", 50);
    }

    private void removeCorruptTEs() {
        this.removeCorruptTEs = this.getBoolean("remove-corrupt-tile-entities", false);
    }

    private void fitlerNBTFromSpawnEgg() {
        if (!(this.filterNBTFromSpawnEgg = this.getBoolean("filter-nbt-data-from-spawn-eggs-and-related", true))) {
            Mohist.LOGGER.warn("Spawn Egg and Armor Stand NBT filtering disabled, this is a potential security risk");
        }
    }

    private void treasureMapsAlreadyDiscovered() {
        this.enableTreasureMaps = this.getBoolean("enable-treasure-maps", true);
        this.treasureMapsAlreadyDiscovered = this.getBoolean("treasure-maps-return-already-discovered", false);
        if (this.treasureMapsAlreadyDiscovered) {
            PaperConfig.log("Treasure Maps will return already discovered locations");
        }
    }

    private void armorStandEntityLookups() {
        this.armorStandEntityLookups = this.getBoolean("armor-stands-do-collision-entity-lookups", true);
    }

    private void maxEntityCollision() {
        this.maxCollisionsPerEntity = this.getInt("max-entity-collisions", this.spigotConfig.getInt("max-entity-collisions", 8));
        PaperConfig.log("Max Entity Collisions: " + this.maxCollisionsPerEntity);
    }

    private void parrotsHangOnBetter() {
        this.parrotsHangOnBetter = this.getBoolean("parrots-are-unaffected-by-player-movement", false);
        PaperConfig.log("Parrots are unaffected by player movement: " + this.parrotsHangOnBetter);
    }

    private void setDisableCreeperLingeringEffect() {
        this.disableCreeperLingeringEffect = this.getBoolean("disable-creeper-lingering-effect", false);
        PaperConfig.log("Creeper lingering effect: " + this.disableCreeperLingeringEffect);
    }

    private void expMergeMaxValue() {
        this.expMergeMaxValue = this.getInt("experience-merge-max-value", -1);
        PaperConfig.log("Experience Merge Max Value: " + this.expMergeMaxValue);
    }

    private void maxChunkSendsPerTick() {
        this.maxChunkSendsPerTick = this.getInt("max-chunk-sends-per-tick", this.maxChunkSendsPerTick);
        if (this.maxChunkSendsPerTick <= 0) {
            this.maxChunkSendsPerTick = 81;
        }
        PaperConfig.log("Max Chunk Sends Per Tick: " + this.maxChunkSendsPerTick);
    }

    private void maxChunkGensPerTick() {
        this.maxChunkGensPerTick = this.getInt("max-chunk-gens-per-tick", this.maxChunkGensPerTick);
        if (this.maxChunkGensPerTick <= 0) {
            this.maxChunkGensPerTick = Integer.MAX_VALUE;
            PaperConfig.log("Max Chunk Gens Per Tick: Unlimited (NOT RECOMMENDED)");
        }
        else {
            PaperConfig.log("Max Chunk Gens Per Tick: " + this.maxChunkGensPerTick);
        }
    }

    private void squidMaxSpawnHeight() {
        this.squidMaxSpawnHeight = this.getDouble("squid-spawn-height.maximum", 0.0);
    }

    private void hopperOptimizations() {
        this.cooldownHopperWhenFull = this.getBoolean("hopper.cooldown-when-full", this.cooldownHopperWhenFull);
        PaperConfig.log("Cooldown Hoppers when Full: " + (this.cooldownHopperWhenFull ? "enabled" : "disabled"));
        this.disableHopperMoveEvents = this.getBoolean("hopper.disable-move-event", this.disableHopperMoveEvents);
        PaperConfig.log("Hopper Move Item Events: " + (this.disableHopperMoveEvents ? "disabled" : "enabled"));
    }

    private void disableSprintInterruptionOnAttack() {
        this.disableSprintInterruptionOnAttack = this.getBoolean("game-mechanics.disable-sprint-interruption-on-attack", false);
    }

    private void allowPermaChunkLoaders() {
        this.allowPermaChunkLoaders = this.getBoolean("game-mechanics.allow-permanent-chunk-loaders", this.allowPermaChunkLoaders);
        PaperConfig.log("Allow Perma Chunk Loaders: " + (this.allowPermaChunkLoaders ? "enabled" : "disabled"));
    }

    private void disableEnderpearlExploit() {
        this.disableEnderpearlExploit = this.getBoolean("game-mechanics.disable-unloaded-chunk-enderpearl-exploit", this.disableEnderpearlExploit);
        PaperConfig.log("Disable Unloaded Chunk Enderpearl Exploit: " + (this.disableEnderpearlExploit ? "enabled" : "disabled"));
    }

    private void shieldBlockingDelay() {
        this.shieldBlockingDelay = this.getInt("game-mechanics.shield-blocking-delay", 5);
    }

    private void scanForLegacyEnderDragon() {
        this.scanForLegacyEnderDragon = this.getBoolean("game-mechanics.scan-for-legacy-ender-dragon", true);
    }

    private void bedSearchRadius() {
        this.bedSearchRadius = this.getInt("bed-search-radius", 1);
        if (this.bedSearchRadius < 1) {
            this.bedSearchRadius = 1;
        }
        if (this.bedSearchRadius > 1) {
            PaperConfig.log("Bed Search Radius: " + this.bedSearchRadius);
        }
    }

    private void repairDuplicateUUID() {
        final String desiredMode = this.getString("duplicate-uuid-resolver", "saferegen").toLowerCase().trim();
        this.duplicateUUIDDeleteRange = this.getInt("duplicate-uuid-saferegen-delete-range", this.duplicateUUIDDeleteRange);
        final String lowerCase = desiredMode.toLowerCase();
        switch (lowerCase) {
            case "saferegen":
            case "saferegenerate":
            case "regen":
            case "regenerate": {
                this.duplicateUUIDMode = DuplicateUUIDMode.SAFE_REGEN;
                PaperConfig.log("Duplicate UUID Resolve: Safer Regenerate New UUID (Delete likely duplicates within " + this.duplicateUUIDDeleteRange + " blocks)");
                break;
            }
            case "remove":
            case "delete": {
                this.duplicateUUIDMode = DuplicateUUIDMode.DELETE;
                PaperConfig.log("Duplicate UUID Resolve: Delete Entity");
                break;
            }
            case "silent":
            case "nothing": {
                this.duplicateUUIDMode = DuplicateUUIDMode.NOTHING;
                PaperConfig.logError("Duplicate UUID Resolve: Do Nothing (no logs) - Warning, may lose indication of bad things happening");
                PaperConfig.logError("PaperMC Strongly discourages use of this setting! Triggering these messages means SOMETHING IS WRONG!");
                break;
            }
            case "log":
            case "warn": {
                this.duplicateUUIDMode = DuplicateUUIDMode.WARN;
                PaperConfig.log("Duplicate UUID Resolve: Warn (do nothing but log it happened, may be spammy)");
                break;
            }
            default: {
                this.duplicateUUIDMode = DuplicateUUIDMode.WARN;
                PaperConfig.logError("Warning: Invalidate duplicate-uuid-resolver config " + desiredMode + " - must be one of: regen, delete, nothing, warn");
                PaperConfig.log("Duplicate UUID Resolve: Warn (do nothing but log it happened, may be spammy)");
                break;
            }
        }
    }

    private void armorStandTick() {
        this.armorStandTick = this.getBoolean("armor-stands-tick", this.armorStandTick);
        PaperConfig.log("ArmorStand ticking is " + (this.armorStandTick ? "enabled" : "disabled") + " by default");
    }

    public enum DuplicateUUIDMode
    {
        SAFE_REGEN,
        DELETE,
        NOTHING,
        WARN;
    }
}
