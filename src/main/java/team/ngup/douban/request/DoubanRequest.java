package team.ngup.douban.request;

import com.alibaba.fastjson.JSONObject;
import team.ngup.douban.common.http.HttpClientResult;
import team.ngup.douban.common.http.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DoubanRequest {

    private static final String AUTH_URL = "https://accounts.douban.com/j/mobile/login/qrlogin_code";
    private static final String USER_INFO_URL = "https://fm.douban.com/j/v2/user_info";
    private static final String SIREN_ZHAOHE_URL = "https://fm.douban.com/j/v2/playlist";

    public JSONObject getQr() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");
        headers.put("Origin", "https://accounts.douban.com");
        Map<String, String> params = new HashMap<>();
        params.put("ck", "");
        HttpClientResult result = HttpClientUtils.doPost(AUTH_URL, headers, params);

        if (result.getCode() == 200) {
            System.out.println(result.getContent());
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

    public JSONObject getUserInfo() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");
        headers.put("Cookie", HttpClientUtils.getCookie());
        headers.put("Origin", "https://fm.douban.com");
        Map<String, String> params = new HashMap<>();
        params.put("avatar_size", "large");
        HttpClientResult result = HttpClientUtils.doPost(USER_INFO_URL, headers, params);
        return JSONObject.parseObject(result.getContent());
    }

    public JSONObject getSiRen() throws IOException, URISyntaxException {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "application/json");
        headers.put("Origin", "https://fm.douban.com");
        headers.put("Cookie", HttpClientUtils.getCookie());
        Map<String, String> params = new HashMap<>();
        params.put("channel", "0");
        params.put("kbps", "192");
        params.put("client", "s:mainsite|y:3.0");
        params.put("app_name", "radio_website");
        params.put("version", "100");
        params.put("type", "n");
        HttpClientResult result = HttpClientUtils.doGet(SIREN_ZHAOHE_URL, headers, params);
        return JSONObject.parseObject(result.getContent());
    }

    public static void main(String[] args) throws IOException {
        new DoubanRequest().getQr();
    }
}
