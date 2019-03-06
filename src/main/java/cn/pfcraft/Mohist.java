package cn.pfcraft;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mohist {

	private static final String name = "Mohist";
	private static final String version = "0.0.1";
	private static final String native_verson = "v1_12_R1";
	public static final Logger LOGGER = LogManager.getLogger("Mohist");
	private static final String bukkit_version = "1.12.2-R0.1-SNAPSHOT";

	public static String getName(){
		return name;
	}

	public static String getVersion(){
		return version;
	}

    public static String getNativeVersion() {
        return native_verson;
    }

	public static String getBukkitVersion() {
		return bukkit_version;
	}

}
