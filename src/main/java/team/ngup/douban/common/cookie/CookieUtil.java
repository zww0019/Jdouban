package team.ngup.douban.common.cookie;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieUtil {

    private static Map<String,String> cookies = new ConcurrentHashMap<>();

    public void addCookie(String cookie){
        String[] temp = cookie.split(";");
        for(String item:temp){
            String[] a = item.split("=");
            if(a.length>1){
                cookies.put(a[0],a[1]);
            }else{
                cookies.put(a[0],"");
            }
        }
    }

    public String getCookies(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry entry : cookies.entrySet()){
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append(";");
        }
        String cookies = stringBuilder.toString();
        return cookies.substring(0,cookies.length()-1);
    }
}
