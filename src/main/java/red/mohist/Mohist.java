package red.mohist;

import net.minecraftforge.fml.relauncher.ServerLaunchWrapper;
import org.apache.logging.log4j.Logger;
import red.mohist.down.DownloadServer;

import java.util.ResourceBundle;

public class Mohist{

    public static Logger LOGGER;
    public static ResourceBundle rb;
    private static final String NAME = "Mohist";
    private static final String VERSION = "1.1";
    private static final String NATIVE_VERSON = "v1_12_R1";
    private static final String NMS_PREFIX = "net/minecraft/server/";

    public static String getName(){
        return NAME;
    }

    public static String getVersion(){
        return VERSION;
    }

    public static String getNativeVersion() {
        return NATIVE_VERSON;
    }

    public static String getNmsPrefix()
    {
        return NMS_PREFIX;
    }

    public static void main(String[] args){
        Thread t = new Thread(new DownloadServer(),"Server Download Thread");
        t.start();
        new ServerLaunchWrapper().run(args);
    }
}
