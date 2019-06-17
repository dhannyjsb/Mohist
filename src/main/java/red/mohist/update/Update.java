package red.mohist.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import red.mohist.Mohist;
import red.mohist.down.Download;
import red.mohist.i18n.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Update implements Runnable{

    private static String pre = Message.getString(Message.Mohist_update_program);

    public static boolean getUpdate(){
        JSONObject json = getJson();
        String version = json.getString("name");
        if(!version.equals(Mohist.getVersion())) {
			Mohist.LOGGER.info(pre + Message.getFormatString(Message.Mohist_update_program_check_hasupdate,new Object[] {version,Mohist.getVersion()}));
            return true;
        }
		Mohist.LOGGER.info(pre + Message.getFormatString(Message.Mohist_update_program_check_noupdate,new Object[] {version,Mohist.getVersion()}));
        Mohist.LOGGER.info(pre + Message.getString(Message.Mohist_update_program_tips_stopautoget));
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
        Mohist.LOGGER.info(pre + "Total §e" + size + "§b Files");
        for (int i = 0;i < size;i++){
            Mohist.LOGGER.info(pre + Message.getString(Message.Dw_Start));
            new Download(ja.getJSONObject(i).getString("browser_download_url"),"Mohist-update.jar",true);
            Mohist.LOGGER.info(pre + Message.getFormatString(Message.Mohist_update_message,new Object[] {releasesMsg}));
            Mohist.LOGGER.info(pre + Message.getFormatString(Message.Mohist_update_date,new Object[] {releasesDate}));
        }


    }

    private static JSONObject getJson(){
        URL url = null;
        HttpURLConnection httpConn = null;
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try{
            url = new URL("https://api.github.com/repos/PFCraft/Mohist/releases/latest");
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str = null;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            try{
                if(in!=null) {
                    in.close(); 
                }
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        String jsonText =sb.toString();
        return JSON.parseObject(jsonText);
    }

    @Override
    public void run() {
		/*
        if(MohistConfig.config.getBoolean("update.autoget")){
            if(Update.getUpdate()){
                //Mohist.LOGGER.info(pre + "将为您在后台下载更新...");
                if(!new File("Mohist-update.jar").exists()){
                    Update.downloadUpdate();
                }
                Mohist.LOGGER.info(pre + Message.getString(Message.Mohist_update_program_tips_done));
            }
        }else
            Mohist.LOGGER.info(pre + Message.getString(Message.Mohist_update_program_tips_false));
		*/
    }
}
