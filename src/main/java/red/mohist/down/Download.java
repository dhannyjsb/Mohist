package red.mohist.down;

import red.mohist.i18n.Message;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Download {

    public Download(String url,String fileName){
        //String url = "https://s3.amazonaws.com/Minecraft.Download/versions/1.12.2/minecraft_server.1.12.2.jar";
        //String fileName = "minecraft_server.1.12.2.jar";
        Object[] o1 = {fileName};
        System.out.println(Message.getFormatString(Message.Dw_File,o1));
        BufferedOutputStream bos = null;
        InputStream is = null;
        try {
            byte[] buff = new byte[8192];
            is = new URL(url).openStream();
            File file = new File(".", fileName);
            file.getParentFile().mkdirs();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            HttpURLConnection urlcon=(HttpURLConnection)new URL(url).openConnection();
            long l = urlcon.getContentLengthLong();
            urlcon.disconnect();
            int count = 0;
            System.out.println(Message.getString(Message.Dw_Start));
            while ( (count = is.read(buff)) != -1) {
                Object[] o = {fileName,file.length(),l};
                System.out.println(Message.getFormatString(Message.Dw_Now,o));
                bos.write(buff, 0, count);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
