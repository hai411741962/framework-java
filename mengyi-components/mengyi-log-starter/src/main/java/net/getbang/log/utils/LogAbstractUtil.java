package net.getbang.log.utils;



import net.getbang.core.constant.CacheConstants;
import net.getbang.core.util.ServletUtils;
import net.getbang.log.model.LogAbstract;
import net.getbang.log.model.LogError;
import net.getbang.log.model.ServerInfo;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;

/**
 * Log 相关工具
 */
public class LogAbstractUtil {
	/**
	 * 向log中添加补齐request的信息
	 *
	 * @param request     请求
	 * @param logAbstract 日志基础类
	 */
	public static void addRequestInfoToLog(HttpServletRequest request, LogAbstract logAbstract) {
		if (ObjectUtil.isNotEmpty(request)) {
			logAbstract.setRemoteIp(ServletUtils.getClientIP(request));
			logAbstract.setRequestUri(URLUtil.getPath(request.getRequestURI()));
			logAbstract.setActionMethod(request.getMethod());
			//是否为Multipart类型表单，此类型表单用于文件上传
			if(ServletUtils.isMultipart(request)){
					//待定义
			}else{
				logAbstract.setParams(JSON.toJSONString(ServletUtils.getParamMap(request)));
			}

		}
	}

	/**
	 * 向log中添加补齐其他的信息
	 * @param logAbstract     日志基础类
	 * @param serverInfo      服务信息
	 */
	public static void addOtherInfoToLog(LogAbstract logAbstract,ServerInfo serverInfo) {

		logAbstract.setServerHost(serverInfo.getHostName());
		logAbstract.setServerIp(serverInfo.getIpWithPort());
		logAbstract.setCreateTime(DateUtil.date());
		if (logAbstract.getParams() == null) {
			logAbstract.setParams("");
		}
	}

	/**
	 * 向log中添加租户id
	 * @param request
	 * @param logError
	 */
	public static void addCompanyIdInfoToLog(HttpServletRequest request, LogError logError) {
		String companyIdStr = request.getHeader(CacheConstants.DETAILS_COMPANY_ID);
		Long companyId = StrUtil.isNumeric(companyIdStr) ? Long.valueOf(companyIdStr) : 0L;
		logError.setCompanyId(companyId);
	}
}
