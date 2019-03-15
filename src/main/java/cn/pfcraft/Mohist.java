package cn.pfcraft;


import cn.pfcraft.i18n.Message;
import net.minecraftforge.fml.relauncher.ServerLaunchWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class Mohist implements Runnable{

	private static final String name = "Mohist";
	private static final String version = "0.0.1";
	private static final String native_verson = "v1_12_R1";
	public static final Logger LOGGER = LogManager.getLogger("Mohist");
	private static final String bukkit_version = "1.12.2-R0.1-SNAPSHOT";
	private static String[] args;

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

	public static void main(String[] args){
		Mohist.args = args;
		Thread t = new Thread(new Mohist(),"Mohist");
		t.start();
	}
	public void run(String[] args)
	{
		if (System.getProperty("log4j.configurationFile") == null)
		{
			// Set this early so we don't need to reconfigure later
			System.setProperty("log4j.configurationFile", "log4j2_server.xml");
		}
		System.out.println(Message.getObject(Message.Mohist_Start));
		Class<?> launchwrapper = null;
		try
		{
			launchwrapper = Class.forName("net.minecraft.launchwrapper.Launch",true,getClass().getClassLoader());
			Class.forName("org.objectweb.asm.Type",true,getClass().getClassLoader());
		}
		catch (Exception e)
		{
			System.err.printf(Message.getObject(Message.Not_Have_Library_1) + Message.getObject(Message.Not_Have_Library_2).toString());
			e.printStackTrace(System.err);
			System.exit(1);
		}

		try
		{
			Method main = launchwrapper.getMethod("main", String[].class);
			String[] allArgs = new String[args.length + 2];
			allArgs[0] = "--tweakClass";
			allArgs[1] = "net.minecraftforge.fml.common.launcher.FMLServerTweaker";
			System.arraycopy(args, 0, allArgs, 2, args.length);
			main.invoke(null,(Object)allArgs);
		}
		catch (Exception e)
		{
			System.err.printf(Message.rb.getString(Message.Mohist_Start_Error.toString()));
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	@Override
	public void run() {
		this.run(this.args);
	}
}
