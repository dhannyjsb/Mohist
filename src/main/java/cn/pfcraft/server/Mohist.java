package cn.pfcraft.server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mohist {

	public static String name = "Mohist";
	private static final String version = "0.0.1";
	private static final String native_verson = "v1_12_R1";
	public static final Logger LOGGER = LogManager.getLogger("Mohist");
	public static final String bukkit_version = "1.12.2-R0.1-SNAPSHOT";

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

	public static void bigWarning(String format, Object... data){
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		LOGGER.warn("****************************************");
		LOGGER.warn("* "+format, data);
		for (int i = 2; i < 8 && i < trace.length; i++)
		{
			LOGGER.warn("*  at {}{}", trace[i].toString(), i == 7 ? "..." : "");
		}
		LOGGER.warn("****************************************");
	}
}
