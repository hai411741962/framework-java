package net.getbang.core.util;


import net.getbang.core.constant.CacheConstants;
import cn.hutool.core.convert.Convert;


/**
 * 权限获取工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {
    /**
     * 获取用户
     */
    public static String getUsername() {
        String username = ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_USERNAME);
        return ServletUtils.urlDecode(username);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return Convert.toLong(ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_USER_ID));
    }

    /**
     * 获取租户ID
     */
    public static Long getTenantId() {
        Long cid = Convert.toLong(ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_COMPANY_ID));
        return cid;
    }

    /**
     * 获取用户ID String
     * @return
     */
    public static String getUserIdS() {
        return ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_USER_ID);
    }

    /**
     * 获取用户组织ID
     */
    public static Long getUserDeptId() {
        return Convert.toLong(ServletUtils.getRequest().getHeader(CacheConstants.DETAILS_USER_DEPT_ID));
    }

    public static String getToken() {
        return ServletUtils.getRequest().getHeader(CacheConstants.AUTHORIZATION_HEADER);
    }

}
