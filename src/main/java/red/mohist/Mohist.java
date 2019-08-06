package red.mohist;

import net.minecraftforge.fml.relauncher.ServerLaunchWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import red.mohist.down.DownloadLibraries;
import red.mohist.down.DownloadServer;
import red.mohist.down.Update;
import red.mohist.i18n.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mohist {

    private static final String NAME = "Mohist";
    private static final String VERSION = "1.2";
    private static final String NATIVE_VERSON = "v1_12_R1";
    private static final String NMS_PREFIX = "net/minecraft/server/";
    public static Logger LOGGER;

    public static String getName() {
        return NAME;
    }

    public static String getVersion() {
        return VERSION + "-" + ((Mohist.class.getPackage().getImplementationVersion() != null) ? Metrics.class.getPackage().getImplementationVersion() : "unknown");
    }

    public static String getNativeVersion() {
        return NATIVE_VERSON;
    }

    public static String getNmsPrefix() {
        return NMS_PREFIX;
    }

    public static void main(String[] args) {
        Update.hasLatestVersion();
        if (System.getProperty("log4j.configurationFile") == null)
        {
            // Set this early so we don't need to reconfigure later
            System.setProperty("log4j.configurationFile", "log4j2_mohist.xml");
        }
        Class<?> launchwrapper = null;
        try
        {
            launchwrapper = Class.forName("net.minecraft.launchwrapper.Launch",true, Mohist.class.getClassLoader());
            Class.forName("org.objectweb.asm.Type",true, Mohist.class.getClassLoader());
            System.out.println("");
            System.out.println("");
            System.out.println(" /'\\_/`\\          /\\ \\       __          /\\ \\__   ");
            System.out.println("/\\      \\     ___ \\ \\ \\___  /\\_\\     ____\\ \\ ,_\\  ");
            System.out.println("\\ \\ \\__\\ \\   / __`\\\\ \\  _ `\\\\/\\ \\   /',__\\\\ \\ \\/  ");
            System.out.println(" \\ \\ \\_/\\ \\ /\\ \\L\\ \\\\ \\ \\ \\ \\\\ \\ \\ /\\__, `\\\\ \\ \\_ ");
            System.out.println("  \\ \\_\\\\ \\_\\\\ \\____/ \\ \\_\\ \\_\\\\ \\_\\\\/\\____/ \\ \\__\\");
            System.out.println("   \\/_/ \\/_/ \\/___/   \\/_/\\/_/ \\/_/ \\/___/   \\/__/");
            System.out.println("");
            System.out.println("");
            System.out.println("                        " + Message.getString(Message.forge_ServerLanunchWrapper_1));
            System.out.println("");
            Mohist.LOGGER = LogManager.getLogger("Mohist");
            Mohist.LOGGER.info(Message.getString(Message.Mohist_Start));
            Mohist.LOGGER.info(Message.getString(Message.Load_libraries));
            new ServerLaunchWrapper().run(args);
        }
        catch (Exception e)
        {
            System.out.println(Message.getString(Message.Not_Have_Library));
            System.out.println("");
            ExecutorService ds = Executors.newCachedThreadPool();
            ExecutorService dl = Executors.newCachedThreadPool();
            ds.execute(new DownloadServer());
            dl.execute(new DownloadLibraries());
            System.out.println(Message.getString("file.ok"));
        }
    }
}
