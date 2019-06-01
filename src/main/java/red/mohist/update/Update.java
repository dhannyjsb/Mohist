package red.mohist.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import red.mohist.Mohist;
import red.mohist.down.Download;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Update {

    private static String pre = "§c§l[§bMohist更新程序§c§l] §b";

    public static boolean getUpdate(){
        try {
            URL u = new URL("https://api.github.com/repos/PFCraft/Mohist/releases/latest");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("contentType", "UTF-8");
            InputStream in = conn.getInputStream();
            int i = 0;
            int n = 0;
            byte[] b = new byte[1024];
            while((i = in.read()) != -1) {
                b[n] = (byte) i;
                n++;
            }
            String jsonText = new String(b,0,n);

            JSONObject json = JSON.parseObject(jsonText);
            String version = json.getString("name");
            if(!version.equals(Mohist.getVersion())){
                return true;
            }
            //String releasesPeople;
            //String downloadUrl;
            //String releasesDate;
            //String fileName;
            //String releasesMsg;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void downloadUpdate() {
        JSONObject json = getJson();

        String version = json.getString("name");
        String releasesPeople = json.getJSONObject("author").getString("login");

        JSONArray ja = json.getJSONArray("assets");
        int size = ja.size();
        String releasesDate = json.getString("created_at").replaceAll("T","T ");
        String releasesMsg = json.getString("body");
        Bukkit.getConsoleSender().sendMessage(pre + "共有 §e" + size + "§f 个文件");
        for (int i = 0;i < size;i++){
            Bukkit.getConsoleSender().sendMessage(pre + "开始下载...");
            new Download(ja.getJSONObject(i).getString("browser_download_url"),"Mohist-update.jar");
            Bukkit.getConsoleSender().sendMessage(pre + "更新消息: §e" + releasesMsg);
            Bukkit.getConsoleSender().sendMessage(pre + "发布日期: §e" + releasesDate);
        }


    }

    private static JSONObject getJson(){
        String jsonText = null;
        try {
            URL u = new URL("https://api.github.com/repos/PFCraft/Mohist/releases/latest");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("contentType", "UTF-8");
            InputStream in = conn.getInputStream();
            int i = 0;
            int n = 0;
            byte[] b = new byte[1024];
            while((i = in.read()) != -1) {
                b[n] = (byte) i;
                n++;
            }
            jsonText = new String(b,0,n);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(jsonText);
    }
}
