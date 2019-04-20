package cn.pfcraft.down;

import cn.pfcraft.i18n.Message;

import javax.crypto.MacSpi;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.*;

public class DownloadLibraries implements Runnable {
    @Override
    public void run() {
        String url = "https://github.com/PFCraft/Mohist/releases/download/libraries/libraries.zip";
        String fileName = "libraries.zip";
		Locale locale = Locale.getDefault();
        Object[] o1 = {fileName};
        System.out.println(Message.getFormatString(Message.Dw_File,o1));
        BufferedOutputStream bos = null;
        InputStream is1 = null;
        try {
            byte[] buff = new byte[8192];
		    if (locale.getCountry().equals("CN")) {
                url = "https://gitee.com/PFCraft/MohistDown/raw/master/mohist/libraries.zip";
            }
            is1 = new URL(url).openStream();
            File file = new File(".", fileName);
            file.getParentFile().mkdirs();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            HttpURLConnection urlcon=(HttpURLConnection)new URL(url).openConnection();
            long l = urlcon.getContentLengthLong();
            urlcon.disconnect();
            int count = 0;
            System.out.println(Message.getString(Message.Dw_Start));
            while ( (count = is1.read(buff)) != -1) {
                Object[] o = {fileName,file.length(),l};
                System.out.println(Message.getFormatString(Message.Dw_Now,o));
                bos.write(buff, 0, count);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is1 != null) {
                try {
                    is1.close();
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
        System.out.println(Message.getFormatString(Message.Dw_Ok,new Object[] {fileName}));
        int size = 0;
        try {
            @SuppressWarnings("resource")
            ZipFile zip = new ZipFile(new File(fileName), Charset.forName("GBK"));
            File filepath = new File(".");
            if(!(filepath.exists())) {
                filepath.mkdirs();
            }
            System.out.println(Message.getFormatString(Message.UnZip_Start,new Object[]{fileName}));
            for(Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream is = zip.getInputStream(entry);
                String outPath = ("."+"/"+ zipEntryName).replaceAll("\\*", "/");
                File file1 = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if(!(file1.exists())) {
                    file1.mkdirs();
                }
                if(new File(outPath).isDirectory()) {
                    continue;
                }
                FileOutputStream fos = new FileOutputStream(outPath);
                byte[] b = new byte[1024];
                int i;

                size++;
                System.out.println(Message.getFormatString(Message.UnZip_Now,new Object[]{fileName,size,zip.size()}));

                while((i = is.read(b)) > 0) {
                    fos.write(b,0,i);
                }
                is.close();
                fos.close();
            }
            System.out.println(Message.getFormatString(Message.UnZip_Ok,new Object[]{fileName}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
