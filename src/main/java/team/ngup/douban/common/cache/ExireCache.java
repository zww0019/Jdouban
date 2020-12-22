package team.ngup.douban.common.cache;

import java.util.Map.Entry;
import java.util.Set;

public class ExireCache implements ICache {
    private ExpiryMap<String, Object> map = new ExpiryMap<>();

    @Override
    public synchronized void add(String k, Object v, long expire) {
        map.put(k, v, expire);
    }

    @Override
    public synchronized void add(String k, Object v) {
        map.put(k, v);
    }

    @Override
    public synchronized void delByKey(String k) {
        map.remove(k);
    }

    @Override
    public synchronized void clearAll() {
        map.clear();
    }

    @Override
    public synchronized Object getbyKey(String k) {
        return map.get(k);
    }

    @Override
    public synchronized boolean containKey(String k) {
        return map.containsKey(k);
    }

    /* (non-Javadoc)
     * @see team.ngup.common.util.cache.ICache#entrySet()
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}