package team.ngup.douban.common.cache;

public class CacheFactory {
    private static ICache iCache = null;

    static {
        iCache = new ExireCache();
    }

    public ICache getInstance() {
        return iCache;
    }
}
