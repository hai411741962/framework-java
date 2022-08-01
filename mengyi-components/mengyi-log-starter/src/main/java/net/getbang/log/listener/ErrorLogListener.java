package net.getbang.log.listener;

import net.getbang.core.constant.EventConstant;
import net.getbang.log.event.ErrorLogEvent;
import net.getbang.log.feign.ILogClient;
import net.getbang.log.model.LogError;
import net.getbang.log.model.ServerInfo;
import net.getbang.log.utils.LogAbstractUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 * 异步监听错误日志事件
 */
@Slf4j
@Component
public class ErrorLogListener {


    @Autowired
    private ILogClient iLogClient;
    @Autowired
    private ServerInfo serverInfo;

    @Async
    @Order
    @EventListener(ErrorLogEvent.class)
    public void saveErrorLog(ErrorLogEvent event) {

        Map<String, Object> source = (Map<String, Object>) event.getSource();

        LogError logError = (LogError) source.get(EventConstant.EVENT_LOG);

        LogAbstractUtil.addOtherInfoToLog(logError, serverInfo);

        iLogClient.saveErrorLog(logError);

    }


}
