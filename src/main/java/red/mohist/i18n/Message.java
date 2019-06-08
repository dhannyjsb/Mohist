package red.mohist.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum Message {
    Mohist_Test("mohist.test"),
    Mohist_Start("mohist.start"),
    Mohist_Server_Start("mohist.server.start"),
    Mohist_Start_Error("mohist.start.error"),
    Not_Have_Library("mohist.start.error.nothavelibrary"),

    Dw_File("file.download"),
    Dw_Start("file.download.start"),
    Dw_Now("file.download.now"),
    Dw_Ok("file.download.ok"),

    UnZip_Start("file.unzip.start"),
    UnZip_Now("file.unzip.now"),
    UnZip_Ok("file.unzip.ok"),

    Mohist_Load_Map("mohist.load.map"),
    Mohist_Load_Map_Spawn("mohist.load.map.spawn"),
    Mohist_Stop("mohist.stop"),
    Mohist_Start_Fail("mohist.start.fail"),
    Mohist_Save_Players("mohist.save.players"),
    Mohist_Save_Worlds("mohist.save.worlds"),
    Mohist_Save_Other("mohist.save.other"),
    Load_libraries("load.libraries"),
    Server_Ip("server.ip"),
    EULA("eula"),
    EULA_LOAD_FAIL("eula.load.fail"),
    EULA_SAVE_FAIL("eula.save.fail"),
    EULA_TEXT("eula.text"),
    ERROR_START_DIRECTORY("error.start.directory"),
    error_load_icon("error.load.icon"),
    error_load_icon_wide("error.load.icon.wide"),
    error_load_icon_high("error.load.icon.high"),
    Load_dimension("load.dimension"),
    UnLoad_dimension("unload.dimension"),
    World_settings("world.settings"),
    whitelistMessage("message.whitelist"),
    serverFullMessage("message.serverfull"),
    bungeecord("message.bungeecord"),
    disconnect_flying("disconnect.flying"),
    disconnect_duplicate_login("disconnect.duplicate_login"),
    client_join_mods("client.join.mods"),
    save_chunks_level("save.chunks.level"),
    crash_report("crash.report"),
    crash_report1("crash.report1"),
    crash_report2("crash.report2"),
    crash_report3("crash.report3"),
    crash_report4("crash.report4"),
    crash_mc_version("crash.mc.version"),
    crash_system("crash.system"),
    crash_version("crash.version"),
    crash_java_version("crash.java.version"),
    crash_jvm_version("crash.jvm.version"),
    crash_Memory("crash.memory"),
    mohist_bytes("mohist.bytes"),
    crash_jvm_flags("crash.jvm.flags"),
    crash_craftbukkit_info("crash.craftbukkit.info"),
    crash_thread("crash.thread"),
    crash_Stacktrace("crash.stacktrace"),
    crash_mc_report("crash.mc.report"),
    crash_msg("crash.msg"),
    crash_save_error("crash.save.error"),
    crash_Negative_index("crash.negative.index"),

    other_upto("other.upto"),
    other_total("other.total"),
    other_IntCache("other.intcache"),
    other_head("other.head"),
    other_time("other.time"),
    other_Description("other.description"),

    command_nopermission("command.nopermission"),
    pluginscommand_load("pluginscommand.load"),
    pluginscommand_unload("pluginscommand.unload"),
    pluginscommand_reload("pluginscommand.reload"),
    pluginscommand_loaded("pluginscommand.loaded"),
    pluginscommand_unloaded("pluginscommand.unloaded"),
    pluginscommand_reloaded("pluginscommand.reloaded"),
    pluginscommand_noplugin("pluginscommand.noplugin"),
    pluginscommand_nofile("pluginscommand.nofile"),
    pluginscommand_noyml("pluginscommand.noyml"),
    pluginscommand_alreadyloaded("pluginscommand.alreadyloaded"),
    pluginscommand_notload("pluginscommand.notload"),
    pluginscommand_notunload("pluginscommand.notunload"),
    pluginscommand_nojar("pluginscommand.nojar"),
    pluginscommand_unloaderror("pluginscommand.unloaderror"),
    pluginscommand_reloaderror("pluginscommand.reloaderror"),

    bukkit_plugin_noyml("bukkit.plugin.noyml"),
    bukkit_plugin_enabling("bukkit.plugin.enabling"),
    bukkit_plugin_enablingunreg("bukkit.plugin.enablingunreg"),
    bukkit_plugin_enablingerror("bukkit.plugin.enablingerror"),
    bukkit_plugin_disabling("bukkit.plugin.disabling"),
    bukkit_plugin_disablingerror("bukkit.plugin.disablingerror"),

    craftserver_addworld("craftserver.addworld"),

    growth_modifier_defaulting("growth.modifier.defaulting"),
    growth_modifier("growth.modifier"),
    merge_radius_item("merge.radius.item"),
    merge_radius_exp("merge.radius.exp"),
    view_distance("view.distance"),
    mob_spawn_range("mob.spawn.range"),
    item_despawn_rate("item.despawn.rate"),
    entity_activation_range("entity.activation.range"),
    entity_tracking_range("entity.tracking.range"),
    ticks_per_hopper_transfer("ticks.per.hopper.transfer"),
    random_light_updates("random.light.updates"),
    save_structure_info("save.structure.info"),
    save_structure_info_error("save.structure.info.error"),
    save_structure_info_error1("save.structure.info.error1"),
    arrow_despawn_rate("arrow.despawn.rate"),
    zombie_aggressive_towards_villager("zombie.aggressive.towards.villager"),
    nerf_spawner_mobs("nerf.spawner.mobs"),
    enable_zombie_pigmen_portal_spawns("enable.zombie.pigmen.portal.spawns"),
    custom_map_seeds("custom.map.seeds"),
    max_tnt_per_tick("max-tnt.per.tick"),
    max_tick_time_tile("max.tick.time.tile"),

    Exception_Could_not_load_plugin("exception.could.not.load.plugin"),
    Exception_plugin_not_hav_depend("exception.plugin.not.hav.depend"),
    Exception_Invalid_Plugin("exception.invalid.plugin"),
    Exception_Invalid_Description("exception.invalid.description"),

    Use_Unkonw_Comamnd("use.unknow.command"),
    outdated_Client("outdate.client"),
    outdated_Server("outdate.server"),

    Server_Start_Done("server.start.done"),
	
	Mohist_update_program("mohist.update.program"),
	Mohist_update_program_check_hasupdate("mohist.update.program.check.hasupdate"),
	Mohist_update_program_check_noupdate("mohist.update.program.check.noupdate"),
	Mohist_update_program_tips_stopautoget("mohist.update.program.tips.stopautoget"),
	Mohist_update_message("mohist.update.message"),
	Mohist_update_date("mohist.update.date"),
	Mohist_update_program_tips_done("mohist.update.program.tips.done"),
	Mohist_update_program_tips_false("mohist.update.program.tips.false"),
	
	Watchdog_1("watchdog.1"),
	Watchdog_2("watchdog.2"),
	Watchdog_3("watchdog.3"),
	Watchdog_4("watchdog.4"),
	Watchdog_5("watchdog.5"),
	Watchdog_6("watchdog.6"),
	Watchdog_7("watchdog.7"),
	Watchdog_8("watchdog.8"),
	Watchdog_9("watchdog.9"),
	Watchdog_10("watchdog.10"),
	Watchdog_11("watchdog.11"),
	Watchdog_12("watchdog.12"),
	Watchdog_13("watchdog.13"),
	Watchdog_14("watchdog.14"),
	Watchdog_15("watchdog.15"),
	Watchdog_16("watchdog.16"),
	
	forge_loader_1("forge_loader_1"),
	forge_loader_2("forge_loader_2"),
	forge_loader_3("forge_loader_3"),
	forge_loader_4("forge_loader_4"),
	forge_loader_5("forge_loader_5"),
	forge_loader_6("forge_loader_6"),
	forge_loader_7("forge_loader_7"),
	forge_loader_8("forge_loader_8"),
	forge_loader_9("forge_loader_9"),
	forge_loader_10("forge_loader_10"),
	forge_loader_11("forge_loader_11"),
	forge_loader_12("forge_loader_12"),
	forge_loader_13("forge_loader_13"),
	forge_loader_14("forge_loader_14"),
	forge_loader_15("forge_loader_15"),
	forge_loader_16("forge_loader_16"),
	forge_loader_17("forge_loader_17"),
	forge_loader_18("forge_loader_18"),
	forge_loader_19("forge_loader_19"),
	forge_loader_20("forge_loader_20"),
	forge_loader_21("forge_loader_21"),
	forge_loader_22("forge_loader_22"),
	forge_loader_23("forge_loader_23"),
	forge_loader_24("forge_loader_24"),
	forge_loader_25("forge_loader_25"),
	forge_loader_26("forge_loader_26"),
	forge_loader_27("forge_loader_27"),
	forge_loader_28("forge_loader_28"),
	forge_loader_29("forge_loader_29"),
	forge_loader_30("forge_loader_30"),
	forge_loader_31("forge_loader_31"),
	forge_loader_32("forge_loader_32"),
	forge_loader_33("forge_loader_33"),
	forge_loader_34("forge_loader_34"),
	forge_loader_35("forge_loader_35"),
	forge_loader_36("forge_loader_36"),
	forge_loader_37("forge_loader_37"),
	forge_loader_38("forge_loader_38"),
	forge_loader_39("forge_loader_39"),
	forge_loader_40("forge_loader_40"),
	forge_loader_41("forge_loader_41"),
	forge_loader_42("forge_loader_42"),
	forge_loader_43("forge_loader_43"),
	forge_loader_44("forge_loader_44"),
	forge_loader_45("forge_loader_45"),
	forge_loader_46("forge_loader_46"),
	forge_loader_47("forge_loader_47"),
	forge_loader_48("forge_loader_48"),
	forge_loader_49("forge_loader_49"),
	forge_loader_50("forge_loader_50"),
	forge_loader_51("forge_loader_51"),
	forge_loader_52("forge_loader_52"),
	forge_loader_53("forge_loader_53"),
	forge_loader_54("forge_loader_54"),
	forge_loader_55("forge_loader_55"),
	forge_loader_56("forge_loader_56"),
	forge_loader_57("forge_loader_57"),

    WatchMohist_1("watchmohist.1"),
    WatchMohist_2("watchmohist.2"),
    WatchMohist_3("watchmohist.3"),
    WatchMohist_4("watchmohist.4");


    private final String value;
    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new UTF8Control());
    //public static ResourceBundle rb = Mohist.rb;


    public static String getString(Message key){
        return rb.getString(key.toString());
    }

    public static String getFormatString(Message key, Object[] f){
        return new MessageFormat(getString(key)).format(f);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    Message(String value) {
        this.value = value;
    }

}
