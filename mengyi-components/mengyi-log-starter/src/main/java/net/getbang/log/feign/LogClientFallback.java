package net.getbang.log.feign;

import net.getbang.core.base.CommonResult;
import net.getbang.log.model.LogError;
import org.springframework.stereotype.Component;

/**
 * 日志fallback
 *
 * @author yhg
 */
@Component
public class LogClientFallback implements ILogClient {

	@Override
	public CommonResult<Boolean> saveErrorLog(LogError log) {
		return CommonResult.error("LogClient error log save fail");
	}
}
