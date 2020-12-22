package team.ngup.douban.common.cache;

import java.util.Map.Entry;
import java.util.Set;

public interface ICache {
    void add(String k, Object v, long expire);

    void add(String k, Object v);

    void delByKey(String k);

    void clearAll();

    Object getbyKey(String k);

    Set<Entry<String, Object>> entrySet();

    boolean containKey(String k);
}
