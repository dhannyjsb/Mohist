package red.mohist.down;

import red.mohist.i18n.Message;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {

    public Download(String url, String fileName) {
        try {
            URL website = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestMethod("GET");

            long alreadySize = 0;
            File file = new File(fileName);
            int code = connection.getResponseCode();
            if (code == 200) {
                System.out.println(Message.getFormatString("file.download.size", new Object[]{getSize(connection.getContentLengthLong())}));
                long unfinishedSize = connection.getContentLength();

                long size = alreadySize + unfinishedSize;

                InputStream in = connection.getInputStream();
                OutputStream out = new BufferedOutputStream(new FileOutputStream(file, true));

                byte[] buff = new byte[2048];
                int len;
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                    alreadySize += len;
                    Progress cpb = new Progress(50, '#');
                    cpb.show(fileName, (int) (alreadySize * 1.0 / size * 100));
                    Thread.sleep(2);
                }
                out.close();
                System.out.println(Message.getFormatString("file.download.ok", new Object[]{ fileName}));
            } else {
                System.out.println(Message.getFormatString("file.download.nook", new Object[]{url}));
            }
            connection.disconnect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getSize(long size) {
        if (size >= 1099511627776L) {
            return (float) size / 1099511627776.0F + " TiB";
        }
        if (size >= 1073741824L) {
            return (float) size / 1073741824.0F + " GiB";
        }
        if (size >= 1048576L) {
            return (float) size / 1048576.0F + " MiB";
        }
        if (size >= 1024) {
            return (float) size / 1024.0F + " KiB";
        }
        return size + " B";
    }
}
