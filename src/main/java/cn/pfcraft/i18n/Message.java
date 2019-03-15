package cn.pfcraft.i18n;

import java.util.ResourceBundle;

public enum Message {
    Mohist_Test("mohist.test"),
    Mohist_Start("mohist.start"),
    Mohist_Start_Error("mohist.start.error"),
    Not_Have_Library_1("mohist.start.error.nothavelibrary.1"),
    Not_Have_Library_2("mohist.start.error.nothavelibrary.2"),
    Mohist_Load_Map("mohist.load.map"),
    Mohist_Load_Map_Spawn("mohist.load.map.spawn"),
    Mohisy_Stop("mohist.stop"),
    Mohisy_Save_Players("mohist.save.players"),
    Mohisy_Save_Worlds("mohist.save.worlds"),
    Mohisy_Save_Other("mohist.save.other");

    private final String value;
    public static ResourceBundle rb = ResourceBundle.getBundle("assets.mohist.lang.message", new UTF8Control());

    public static Object getObject(Message key){
        return rb.getObject(key.toString().replace("&","§"));
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    Message(String value) {
        this.value = value;
    }

}