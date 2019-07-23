package red.mohist.down;

import red.mohist.i18n.Message;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Download {

    public Download(String url, String fileName) {
        Object[] o1 = {fileName};
        System.out.println("");
        System.out.println(Message.getFormatString("file.download", o1));
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            System.out.println(Message.getFormatString("file.download.ok", new Object[]{fileName}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
