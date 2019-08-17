package red.mohist.util;

import org.apache.commons.io.IOUtils;
import red.mohist.i18n.Message;
import red.mohist.i18n.UTF8Properties;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerEula
{
    private final File eulaFile;
    private final boolean acceptedEULA;

    public ServerEula(File eulaFile)
    {
        this.eulaFile = eulaFile;
        this.acceptedEULA = this.loadEULAFile(eulaFile);
    }

    private boolean loadEULAFile(File inFile)
    {
        FileInputStream fileinputstream = null;
        boolean flag = false;

        try
        {
            UTF8Properties properties = new UTF8Properties();
            fileinputstream = new FileInputStream(inFile);
            properties.load(new InputStreamReader(fileinputstream, StandardCharsets.UTF_8));
            flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
        }
        catch (Exception var8)
        {
            this.createEULAFile();
        }
        finally
        {
            IOUtils.closeQuietly((InputStream)fileinputstream);
        }

        return flag;
    }

    public boolean hasAcceptedEULA()
    {
        return this.acceptedEULA;
    }

    public void createEULAFile()
    {
        FileOutputStream fileoutputstream = null;

        try
        {
            UTF8Properties properties = new UTF8Properties();
            fileoutputstream = new FileOutputStream(this.eulaFile);
            properties.put("eula", "false", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            Object[] p = {"https://account.mojang.com/documents/minecraft_eula"};
            properties.orderStore(new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8), Message.getFormatString(Message.EULA_TEXT,p));
        }
        catch (Exception exception)
        {
        }
        finally
        {
            IOUtils.closeQuietly((OutputStream)fileoutputstream);
        }
    }
}