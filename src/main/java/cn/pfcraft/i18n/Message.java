package cn.pfcraft.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum Message {
    Mohist_Test("mohist.test"),
    Mohist_Start("mohist.start"),
    Mohsit_Server_Start("mohist.server.start"),
    Mohist_Start_Error("mohist.start.error"),
    Not_Have_Library_1("mohist.start.error.nothavelibrary.1"),
    Not_Have_Library_2("mohist.start.error.nothavelibrary.2"),
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
    disconnect_duplicate_login("disconnect.duplicate_login");

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