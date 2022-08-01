
package net.getbang.log.model;



import net.getbang.core.util.DateUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * logApi、logError的父类，拥有相同的属性值
 *
 * @author yhg
 */
@Data
public class LogAbstract implements Serializable {

	protected static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	protected Long id;

	/**
	 * 服务ID
	 */
	protected String serviceId;

	/**
	 * 租户id
	 */
	protected Long companyId;
	/**
	 * 服务器 ip
	 */
	protected String serverIp;
	/**
	 * 服务器名
	 */
	protected String serverHost;
	/**
	 * 环境
	 */
	protected String serverEnv;
	/**
	 * 操作IP地址
	 */
	protected String remoteIp;
	/**
	 * 用户代理
	 */
	protected String userAgent;
	/**
	 * 请求URI
	 */
	protected String requestUri;
	/**
	 * 操作方式
	 */
	protected String actionMethod;
	/**
	 * 方法类
	 */
	protected String methodClass;
	/**
	 * 方法名
	 */
	protected String methodName;
	/**
	 * 操作提交的数据
	 */
	protected String params;
	/**
	 * 执行时间
	 */
	protected String runTime;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = DateUtils.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtils.PATTERN_DATETIME)
	protected Date createTime;

}
