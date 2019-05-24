package red.mohist;

import net.minecraftforge.fml.relauncher.ServerLaunchWrapper;
import org.apache.logging.log4j.Logger;

public class Mohist implements Runnable{

	public static Logger LOGGER;
	private static String[] args;
	private static final String name = "Mohist";
	private static final String version = "0.0.8b";
	private static final String native_verson = "v1_12_R1";
	private static final String nms_prefix = "net/minecraft/server/";

	public static String getName(){
		return name;
	}

	public static String getVersion(){
		return version;
	}

    public static String getNativeVersion() {
        return native_verson;
    }

	public static String getNmsPrefix()
	{
		return nms_prefix;
	}

	public static void main(String[] args){
		Mohist.args = args;
		Thread t = new Thread(new Mohist(),"Mohist");
		t.start();
	}

	@Override
	public void run() {
		new ServerLaunchWrapper().run(args);
	}
}
