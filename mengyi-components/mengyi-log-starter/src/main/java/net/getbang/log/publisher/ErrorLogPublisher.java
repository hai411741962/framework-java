package net.getbang.log.publisher;


import net.getbang.core.constant.EventConstant;
import net.getbang.core.exception.Exceptions;
import net.getbang.core.util.ServletUtils;
import net.getbang.log.event.ErrorLogEvent;
import net.getbang.log.model.LogError;
import net.getbang.log.utils.LogAbstractUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常信息事件发送
 *
 * @author yhg
 */
@Slf4j
@Component
public class ErrorLogPublisher {

	public static void publishEvent(Throwable error, String requestUri) {

		System.out.println("ErrorLogPublisher.publishEvent异常信息事件发送================");
		HttpServletRequest request = ServletUtils.getRequest();
		LogError logError = new LogError();
		logError.setRequestUri(requestUri);
		if (ObjectUtil.isNotEmpty(error)) {
			logError.setStackTrace(Exceptions.getStackTraceAsString(error));
			logError.setExceptionName(error.getClass().getName());
			logError.setMessage(error.getMessage());
			StackTraceElement[] elements = error.getStackTrace();
			if (ObjectUtil.isNotEmpty(elements)) {
				StackTraceElement element = elements[0];
				logError.setMethodName(element.getMethodName());
				logError.setMethodClass(element.getClassName());
				logError.setFileName(element.getFileName());
				logError.setLineNumber(element.getLineNumber());
			}
		}
		LogAbstractUtil.addRequestInfoToLog(request, logError);
		LogAbstractUtil.addCompanyIdInfoToLog(request, logError);

		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logError);
		event.put(EventConstant.EVENT_REQUEST, request);
		SpringUtil.getApplicationContext().publishEvent(new ErrorLogEvent(event));
	}

}
