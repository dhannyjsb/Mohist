package red.mohist.down;

import red.mohist.i18n.Message;

import java.util.Locale;

public class DownloadServer implements Runnable {

    @Override
    public void run() {
        String url = "https://launcher.mojang.com/v1/objects/886945bfb2b978778c3a0288fd7fab09d315b25f/server.jar";
        Locale locale = Locale.getDefault();
        if (locale.getCountry().equals("zh_CN") || locale.getCountry().equals("CN") || locale.getCountry().equals("zh_TW") || locale.getCountry().equals("TW")) {
            url = "https://bmclapi2.bangbang93.com/version/1.12.2/server";
        }
        new Download(url,"minecraft_server.1.12.2.jar");
        System.out.println(Message.getFormatString(Message.Dw_Ok,new Object[] {"minecraft_server.1.12.2.jar"}));
    }
}
