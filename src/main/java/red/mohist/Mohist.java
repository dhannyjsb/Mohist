package red.mohist;

import net.minecraftforge.fml.relauncher.ServerLaunchWrapper;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import red.mohist.i18n.UTF8Control;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Mohist implements Runnable{

    public static Logger LOGGER;
    public static ResourceBundle rb;
    private static String[] args;
    private static final String name = "Mohist";
    private static final String version = "0.0.9";
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
        /*
        File f = new File("mohist.yml");
        YamlConfiguration y;
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        y = YamlConfiguration.loadConfiguration(f);
        if(y.getString("locale") == null){
            y.set("locale","Default");
            try {
                y.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            y = YamlConfiguration.loadConfiguration(f);
        }
        switch (y.getString("locale")){
            default:
                rb = ResourceBundle.getBundle("assets.mohist.lang.message", Locale.getDefault(), new UTF8Control());
            case "Default":
                rb = ResourceBundle.getBundle("assets.mohist.lang.message", new Locale(y.getString("locale")), new UTF8Control());
            case "default":
                rb = ResourceBundle.getBundle("assets.mohist.lang.message", new Locale(y.getString("locale")), new UTF8Control());
        }
        */
        Mohist.args = args;
        Thread t = new Thread(new Mohist(),"Mohist");
        t.start();
    }

    @Override
    public void run() {
        new ServerLaunchWrapper().run(args);
    }

}
