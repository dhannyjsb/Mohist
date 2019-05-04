package red.mohist.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum Message {
    Mohist_Test("mohist.test"),
    Mohist_Start("mohist.start"),
    Mohsit_Server_Start("mohist.server.start"),
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
    Mohisy_Stop("mohist.stop"),
    Mohist_Start_Fail("mohist.start.fail"),
    Mohisy_Save_Players("mohist.save.players"),
    Mohisy_Save_Worlds("mohist.save.worlds"),
    Mohisy_Save_Other("mohist.save.other"),
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
    ;

    private final String value;
    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new UTF8Control());


    public static String getString(Message key){
        return rb.getString(key.toString());
    }

    public static String getFormatString(Message key, Object[] f){
        return new MessageFormat(getString(key)).format(f);
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    Message(String value) {
        this.value = value;
    }

}