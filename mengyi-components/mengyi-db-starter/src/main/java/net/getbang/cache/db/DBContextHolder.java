package net.getbang.cache.db;

/**
 * @author chenjun
 * @description: 数据源上下文
 */
public class DBContextHolder {

    /**
     * 租户模式下，异步线程使用中，定义companyId
     */
    private static final ThreadLocal<Long> companyId = new ThreadLocal<Long>();

    private static final ThreadLocal<String> dbKey = new ThreadLocal<String>();
    private static final ThreadLocal<String> tbKey = new ThreadLocal<String>();

    public static void setDBKey(String dbKeyIdx){
        dbKey.set(dbKeyIdx);
    }

    public static String getDBKey(){
        return dbKey.get();
    }

    public static void setTBKey(String tbKeyIdx){
        tbKey.set(tbKeyIdx);
    }

    public static String getTBKey(){
        return tbKey.get();
    }

    public static void clearDBKey(){
        dbKey.remove();
    }

    public static void clearTBKey(){
        tbKey.remove();
    }

    public static void setCompanyId(Long value){
        companyId.set(value);
    }

    public static Long getCompanyId(){
        return companyId.get();
    }

    public static void clearCompanyId(){
        companyId.remove();
    }

}
