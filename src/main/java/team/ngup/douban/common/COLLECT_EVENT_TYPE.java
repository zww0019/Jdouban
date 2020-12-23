package team.ngup.douban.common;

/**
 * @author zhangwenwu
 */

public enum COLLECT_EVENT_TYPE {
    NEXT("skip"),
    LIKE("toggleLike"),
    DELETE("ban");

    private String str;

    private COLLECT_EVENT_TYPE(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
