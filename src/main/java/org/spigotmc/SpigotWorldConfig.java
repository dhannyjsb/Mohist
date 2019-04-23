package org.spigotmc;

import red.mohist.Mohist;
import red.mohist.i18n.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class SpigotWorldConfig
{

    private final String worldName;
    private final YamlConfiguration config;
    private boolean verbose;

    public SpigotWorldConfig(String worldName)
    {
        this.worldName = worldName;
        this.config = SpigotConfig.config;
        init();
    }

    public void init()
    {
        this.verbose = getBoolean( "verbose", true );

        Object[] p = {worldName};
        Mohist.LOGGER.info(Message.getFormatString(Message.World_settings, p));
        SpigotConfig.readConfig( SpigotWorldConfig.class, this );
    }

    private void log(String s)
    {
        if ( verbose )
        {
			Bukkit.getLogger().info( s );
        }
    }

    private void set(String path, Object val)
    {
        config.set( "world-settings.default." + path, val );
    }

    public boolean getBoolean(String path, boolean def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getBoolean( "world-settings." + worldName + "." + path, config.getBoolean( "world-settings.default." + path ) );
    }

    public double getDouble(String path, double def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getDouble( "world-settings." + worldName + "." + path, config.getDouble( "world-settings.default." + path ) );
    }

    public int getInt(String path, int def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getInt( "world-settings." + worldName + "." + path, config.getInt( "world-settings.default." + path ) );
    }

    public <T> List getList(String path, T def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return (List<T>) config.getList( "world-settings." + worldName + "." + path, config.getList( "world-settings.default." + path ) );
    }

    public String getString(String path, String def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getString( "world-settings." + worldName + "." + path, config.getString( "world-settings.default." + path ) );
    }

    // Crop growth rates
    public int cactusModifier;
    public int caneModifier;
    public int melonModifier;
    public int mushroomModifier;
    public int pumpkinModifier;
    public int saplingModifier;
    public int wheatModifier;
    public int wartModifier;
    public int vineModifier;
    public int cocoaModifier;
    private int getAndValidateGrowth(String crop)
    {
        int modifier = getInt( "growth." + crop.toLowerCase(java.util.Locale.ENGLISH) + "-modifier", 100 );
        if ( modifier == 0 )
        {
            log(Message.getFormatString(Message.growth_modifier_defaulting, new Object[] {crop}));
            modifier = 100;
        }
        log(Message.getFormatString(Message.growth_modifier, new Object[] {crop, modifier}));

        return modifier;
    }
    private void growthModifiers()
    {
        cactusModifier = getAndValidateGrowth( "Cactus" );
        caneModifier = getAndValidateGrowth( "Cane" );
        melonModifier = getAndValidateGrowth( "Melon" );
        mushroomModifier = getAndValidateGrowth( "Mushroom" );
        pumpkinModifier = getAndValidateGrowth( "Pumpkin" );
        saplingModifier = getAndValidateGrowth( "Sapling" );
        wheatModifier = getAndValidateGrowth( "Wheat" );
        wartModifier = getAndValidateGrowth( "NetherWart" );
        vineModifier = getAndValidateGrowth( "Vine" );
        cocoaModifier = getAndValidateGrowth( "Cocoa" );
    }

    public double itemMerge;
    private void itemMerge()
    {
        itemMerge = getDouble("merge-radius.item", 2.5 );
        log(Message.getFormatString(Message.merge_radius_item, new Object[] {itemMerge}));
    }

    public double expMerge;
    private void expMerge()
    {
        expMerge = getDouble("merge-radius.exp", 3.0 );
        log(Message.getFormatString(Message.merge_radius_exp, new Object[] {expMerge}));
    }

    public int viewDistance;
    private void viewDistance()
    {
        viewDistance = getInt( "view-distance", Bukkit.getViewDistance() );
        log(Message.getFormatString(Message.view_distance, new Object[] {viewDistance}));
    }

    public byte mobSpawnRange;
    private void mobSpawnRange()
    {
        mobSpawnRange = (byte) getInt( "mob-spawn-range", 4 );
        log(Message.getFormatString(Message.mob_spawn_range, new Object[] {mobSpawnRange}));
    }

    public int itemDespawnRate;
    private void itemDespawnRate()
    {
        itemDespawnRate = getInt( "item-despawn-rate", 6000 );
        log(Message.getFormatString(Message.item_despawn_rate, new Object[] {itemDespawnRate}));
    }

    public int animalActivationRange = 32;
    public int monsterActivationRange = 32;
    public int miscActivationRange = 16;
    public int waterActivationRange = 16; // Paper
    public boolean tickInactiveVillagers = true;
    private void activationRange()
    {
        animalActivationRange = getInt( "entity-activation-range.animals", animalActivationRange );
        monsterActivationRange = getInt( "entity-activation-range.monsters", monsterActivationRange );
        miscActivationRange = getInt( "entity-activation-range.misc", miscActivationRange );
        waterActivationRange = getInt( "entity-activation-range.water", waterActivationRange ); // Paper
        tickInactiveVillagers = getBoolean( "entity-activation-range.tick-inactive-villagers", tickInactiveVillagers );
        log( Message.getFormatString(Message.entity_activation_range, new Object[] {animalActivationRange, monsterActivationRange, miscActivationRange, tickInactiveVillagers}));
    }

    public int playerTrackingRange = 48;
    public int animalTrackingRange = 48;
    public int monsterTrackingRange = 48;
    public int miscTrackingRange = 32;
    public int otherTrackingRange = 64;
    private void trackingRange()
    {
        playerTrackingRange = getInt( "entity-tracking-range.players", playerTrackingRange );
        animalTrackingRange = getInt( "entity-tracking-range.animals", animalTrackingRange );
        monsterTrackingRange = getInt( "entity-tracking-range.monsters", monsterTrackingRange );
        miscTrackingRange = getInt( "entity-tracking-range.misc", miscTrackingRange );
        otherTrackingRange = getInt( "entity-tracking-range.other", otherTrackingRange );
        log( Message.getFormatString(Message.entity_tracking_range, new Object[] {playerTrackingRange, animalTrackingRange, monsterTrackingRange, miscTrackingRange,  otherTrackingRange}));
    }

    public int hopperTransfer;
    public int hopperCheck;
    public int hopperAmount;
    private void hoppers()
    {
        // Set the tick delay between hopper item movements
        hopperTransfer = getInt( "ticks-per.hopper-transfer", 8 );
        if ( SpigotConfig.version < 11 )
        {
            set( "ticks-per.hopper-check", 1 );
        }
        hopperCheck = getInt( "ticks-per.hopper-check", 1 );
        hopperAmount = getInt( "hopper-amount", 1 );
        log( Message.getFormatString(Message.ticks_per_hopper_transfer, new Object[] {hopperTransfer, hopperCheck, hopperAmount}));
    }

    public boolean randomLightUpdates;
    private void lightUpdates()
    {
        randomLightUpdates = getBoolean( "random-light-updates", false );
        log(Message.getFormatString(Message.random_light_updates, new Object[] {randomLightUpdates}));
    }

    public boolean saveStructureInfo;
    private void structureInfo()
    {
        saveStructureInfo = getBoolean( "save-structure-info", true );
        log(Message.getFormatString(Message.save_structure_info, new Object[] {saveStructureInfo}));
        if ( !saveStructureInfo )
        {
            log( Message.getString(Message.save_structure_info_error));
            log( Message.getString(Message.save_structure_info_error1));
        }
    }

    public int arrowDespawnRate;
    private void arrowDespawnRate()
    {
        arrowDespawnRate = getInt( "arrow-despawn-rate", 1200  );
        log(Message.getFormatString(Message.arrow_despawn_rate, new Object[] {arrowDespawnRate}));
    }

    public boolean zombieAggressiveTowardsVillager;
    private void zombieAggressiveTowardsVillager()
    {
        zombieAggressiveTowardsVillager = getBoolean( "zombie-aggressive-towards-villager", true );
        log(Message.getFormatString(Message.zombie_aggressive_towards_villager, new Object[] {zombieAggressiveTowardsVillager}));
    }

    public boolean nerfSpawnerMobs;
    private void nerfSpawnerMobs()
    {
        nerfSpawnerMobs = getBoolean( "nerf-spawner-mobs", false );
        log(Message.getFormatString(Message.nerf_spawner_mobs, new Object[] {nerfSpawnerMobs}));
    }

    public boolean enableZombiePigmenPortalSpawns;
    private void enableZombiePigmenPortalSpawns()
    {
        enableZombiePigmenPortalSpawns = getBoolean( "enable-zombie-pigmen-portal-spawns", true );
        log(Message.getFormatString(Message.enable_zombie_pigmen_portal_spawns, new Object[] {enableZombiePigmenPortalSpawns}));
    }

    public int dragonDeathSoundRadius;
    private void keepDragonDeathPerWorld()
    {
        dragonDeathSoundRadius = getInt( "dragon-death-sound-radius", 0 );
    }

    public int witherSpawnSoundRadius;
    private void witherSpawnSoundRadius()
    {
        witherSpawnSoundRadius = getInt( "wither-spawn-sound-radius", 0 );
    }

    public int villageSeed;
    public int largeFeatureSeed;
    public int monumentSeed;
    public int slimeSeed;
    private void initWorldGenSeeds()
    {
        villageSeed = getInt( "seed-village", 10387312 );
        largeFeatureSeed = getInt( "seed-feature", 14357617 );
        monumentSeed = getInt( "seed-monument", 10387313 );
        slimeSeed = getInt( "seed-slime", 987234911 );
        log( Message.getFormatString(Message.custom_map_seeds, new Object[] {villageSeed, largeFeatureSeed, monumentSeed, slimeSeed}));
    }

    public float jumpWalkExhaustion;
    public float jumpSprintExhaustion;
    public float combatExhaustion;
    public float regenExhaustion;
    public float swimMultiplier;
    public float sprintMultiplier;
    public float otherMultiplier;
    private void initHunger()
    {
        if ( SpigotConfig.version < 10 )
        {
            set( "hunger.walk-exhaustion", null );
            set( "hunger.sprint-exhaustion", null );
            set( "hunger.combat-exhaustion", 0.1 );
            set( "hunger.regen-exhaustion", 6.0 );
        }

        jumpWalkExhaustion = (float) getDouble( "hunger.jump-walk-exhaustion", 0.05 );
        jumpSprintExhaustion = (float) getDouble( "hunger.jump-sprint-exhaustion", 0.2 );
        combatExhaustion = (float) getDouble( "hunger.combat-exhaustion", 0.1 );
        regenExhaustion = (float) getDouble( "hunger.regen-exhaustion", 6.0 );
        swimMultiplier =  (float) getDouble( "hunger.swim-multiplier", 0.01 );
        sprintMultiplier = (float) getDouble( "hunger.sprint-multiplier", 0.1 );
        otherMultiplier = (float) getDouble( "hunger.other-multiplier", 0.0 );
    }

    public int currentPrimedTnt = 0;
    public int maxTntTicksPerTick;
    private void maxTntPerTick() {
        if ( SpigotConfig.version < 7 )
        {
            set( "max-tnt-per-tick", 100 );
        }
        maxTntTicksPerTick = getInt( "max-tnt-per-tick", 100 );
        log(Message.getFormatString(Message.max_tnt_per_tick, new Object[] {maxTntTicksPerTick}));
    }

    public int hangingTickFrequency;
    private void hangingTickFrequency()
    {
        hangingTickFrequency = getInt( "hanging-tick-frequency", 100 );
    }

    public int tileMaxTickTime;
    public int entityMaxTickTime;
    private void maxTickTimes()
    {
        tileMaxTickTime = getInt("max-tick-time.tile", 50);
        entityMaxTickTime = getInt("max-tick-time.entity", 50);
        log(Message.getFormatString(Message.max_tick_time_tile, new Object[] {tileMaxTickTime, entityMaxTickTime}));
    }

    public double squidSpawnRangeMin;
    private void squidSpawnRange()
    {
        squidSpawnRangeMin = getDouble("squid-spawn-range.min", 45.0D);
    }
}
