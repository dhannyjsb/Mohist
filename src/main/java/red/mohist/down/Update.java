package red.mohist.down;

import red.mohist.i18n.Message;
import red.mohist.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Update {


    public static void hasLatestVersion() {
        String str = "https://api.github.com/repos/PFCraft/Mohist/commits";
        try {
            System.out.println(Message.getString("update.check"));
            URL url = new URL(str);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();

            String sha = IOUtil.readContent(is, "UTF-8");
            String sha1 = sha.substring(sha.indexOf("\"sha\":\""));
            String s1 = sha1.substring(sha1.indexOf("\"sha\":\"") + 7);
            String s2 = s1.substring(0, 7);
            String oldver = Update.class.getPackage().getImplementationVersion();
            String time = sha.substring(sha.indexOf("\"date\":\""));
            String time1 = time.substring(time.indexOf("\"date\":\"") + 8);
            String time2 = time1.substring(0, 20);
            if (oldver.contains(s2)) {
                System.out.println(Message.getFormatString("update.latest", new Object[]{s2,oldver}));
            } else {
                System.out.println(Message.getFormatString("update.old", new Object[]{s2,time2,oldver}));
            }
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
