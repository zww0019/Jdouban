package team.ngup.douban.auth;

import com.alibaba.fastjson.JSONObject;
import team.ngup.douban.common.HttpClientResult;
import team.ngup.douban.common.HttpClientUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Auth {

    private static final String AUTH_URL="https://accounts.douban.com/j/mobile/login/qrlogin_code";

    public void doAuth() throws IOException {
        Map<String,String> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("Accept","application/json");
        headers.put("Origin","https://accounts.douban.com");
        Map<String,String> params = new HashMap<>();
        params.put("ck","");
        HttpClientResult result = HttpClientUtils.doPost(AUTH_URL,headers,params);

        if(result.getCode()==200){
            System.out.println(JSONObject.parse(result.getContent()));
        }
    }

    public static void main(String[] args) throws IOException {
        new Auth().doAuth();
    }
}
