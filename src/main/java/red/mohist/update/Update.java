package red.mohist.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import red.mohist.Mohist;
import red.mohist.down.Download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Update {

    private static String pre = "§c§l[§bMohist更新程序§c§l] §b";

    public static boolean getUpdate(){
        JSONObject json = getJson();
        String version = json.getString("name");
        if(!version.equals(Mohist.getVersion())) {
            System.out.println(pre + "发现更新,最新版本号为: §e" + version + " §b目前版本为: §e" + Mohist.getVersion());
            return true;
        }
        System.out.println(pre + "没有发现更新,最新版本号为: §e" + version + " §b目前版本为: §e" + Mohist.getVersion());
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
        System.out.println(pre + "共有 §e" + size + "§f 个文件");
        for (int i = 0;i < size;i++){
            System.out.println(pre + "开始下载...");
            new Download(ja.getJSONObject(i).getString("browser_download_url"),"Mohist-update.jar");
            System.out.println(pre + "更新消息: §e" + releasesMsg);
            System.out.println(pre + "发布日期: §e" + releasesDate);
        }


    }

    private static JSONObject getJson(){
        //请求的url
        URL url = null;
        //建立的http链接
        HttpURLConnection httpConn = null;
        //请求的输入流
        BufferedReader in = null;
        //输入流的缓冲
        StringBuffer sb = new StringBuffer();
        try{
            url = new URL("https://api.github.com/repos/PFCraft/Mohist/releases/latest");
            in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8") );
            String str = null;
            //一行一行进行读入
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            try{
                if(in!=null) {
                    in.close(); //关闭流
                }
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        String jsonText =sb.toString();
        return JSON.parseObject(jsonText);
    }
}
