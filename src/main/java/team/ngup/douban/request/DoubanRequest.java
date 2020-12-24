package team.ngup.douban.request;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import team.ngup.douban.common.COLLECT_EVENT_TYPE;
import team.ngup.douban.common.http.HttpClientResult;
import team.ngup.douban.common.http.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DoubanRequest {

    private static String cookie = null;
    private static final String AUTH_URL = "https://accounts.douban.com/j/mobile/login/qrlogin_code";
    private static final String USER_INFO_URL = "https://fm.douban.com/j/v2/user_info";
    private static final String SIREN_ZHAOHE_URL = "https://fm.douban.com/j/v2/playlist";
    private static final String CHECK_LOGIN_STATUS="https://accounts.douban.com/j/mobile/login/qrlogin_status";

    private static Map<String,String> headers = new HashMap<>();

    static{
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");

    }

    public static JSONObject getQr() throws IOException {
        //headers.put("Origin", "https://accounts.douban.com");
        Map<String, String> params = new HashMap<>();
        params.put("ck", "");
        HttpClientResult result = HttpClientUtils.doPost(AUTH_URL, headers, params);
        cookie = HttpClientUtils.getCookie();
        System.out.println("当前Cookie"+cookie);
        if (result.getCode() == 200) {
            //System.out.println(result.getContent());
            JSONObject object = JSONObject.parseObject(result.getContent());
            if ("success".equals(object.get("status"))) {
                return object.getJSONObject("payload");
            } else {
                return null;
            }
        } else {

            System.err.println(JSONObject.parse(result.getContent()));
            return null;
        }
    }

    public static JSONObject getUserInfo() throws IOException {
        //headers.put("Origin", "https://fm.douban.com");
        headers.put("Cookie", cookie);
        Map<String, String> params = new HashMap<>();
        params.put("avatar_size", "large");
        HttpClientResult result = HttpClientUtils.doPost(USER_INFO_URL, headers, params);
        cookie = HttpClientUtils.getCookie();
        //cookie = HttpClientUtils.getCookie();
        //System.out.println("获取用户信息时的cookie:"+cookie);
        return JSONObject.parseObject(result.getContent());
    }

    public static  JSONObject getSiRen(String id,String type) throws IOException, URISyntaxException {
       // headers.put("Origin", "https://fm.douban.com");
        headers.put("Cookie", cookie);
        Map<String, String> params = new HashMap<>();
        params.put("channel", "0");
        params.put("kbps", "192");
        params.put("client", "s:mainsite|y:3.0");
        params.put("app_name", "radio_website");
        params.put("version", "100");
        if(StringUtils.isNotEmpty(id)&&StringUtils.isNotEmpty(type)){
            params.put("type",type);
            params.put("sid",id);
            params.put("pt", "");
            params.put("pb", "192");
            params.put("apikey", "");
        }else {
            params.put("type", "n");
        }
        HttpClientResult result = HttpClientUtils.doGet(SIREN_ZHAOHE_URL, headers, params);
        cookie = HttpClientUtils.getCookie();
        return JSONObject.parseObject(result.getContent());
    }

    public static void promo(String id) throws IOException, URISyntaxException {
        headers.put("Cookie", cookie);
        System.out.println("提：" + HttpClientUtils.doGet("https://fm.douban.com/j/v2/song/" + id + "/promo", headers, new HashMap<>()).getContent());
    }

    public static void collect_event(COLLECT_EVENT_TYPE collect_event_type) throws IOException {
        headers.put("Cookie", cookie);
        Map<String, String> params = new HashMap<>();
        params.put("category", "fullplayer");
        params.put("action", collect_event_type.getStr());
        params.put("label", "");
        params.put("value", "");
        String[] items = cookie.split(";");
        String ck = "";
        for (String item : items) {
            if (item.contains("ck=")) {
                String temp[] = item.split("=");
                if (temp.length > 1) {
                    ck = temp[1];
                }
                break;
            }
        }
        params.put("ck", ck);
        System.out.println("收：" + HttpClientUtils.doPost("https://fm.douban.com/j/v2/collect_event", headers, params).getContent());
    }

    public static JSONObject getReadHeartSong() throws IOException, URISyntaxException {
        headers.put("Cookie",cookie);
        Map<String,String> params = new HashMap<>();
        LocalDateTime date3 = LocalDateTime.now();//代替calendar
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        params.put("updated_time",formatter.format(date3));
        HttpClientResult result = HttpClientUtils.doGet("https://fm.douban.com/j/v2/redheart/basic", headers, params);
        JSONObject resultObject = JSONObject.parseObject(result.getContent());
        System.out.println(resultObject);
        return resultObject;
    }
    public static boolean isLogined(String code) throws IOException, URISyntaxException {
        //headers.put("Origin", "https://accounts.douban.com");
        headers.put("Cookie", cookie);
        Map<String, String> params = new HashMap<>();
        params.put("ck", "");
        params.put("code", code);

        HttpClientResult result = HttpClientUtils.doGet(CHECK_LOGIN_STATUS, headers, params);
        JSONObject resultObject = JSONObject.parseObject(result.getContent());
        if ("success".equals(resultObject.getString("status"))) {
            if ("login".equals(resultObject.getJSONObject("payload").getString("login_status"))) {
                cookie = HttpClientUtils.getCookie();
                return true;
            }
        }
        return false;
    }

}
