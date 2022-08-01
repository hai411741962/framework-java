package net.getbang.kafka.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CJ
 * @version 1.0
 * @date 2021/12/28 14:11
 */
@RestController
@RequestMapping("kafka/listener")
@Slf4j
@Api(tags = "kafka工具")
public class KafkaManageController {
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public KafkaManageController(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @PostMapping("/stop")
    @ApiOperation("暂停消费")
    public String stop(@RequestParam("id") String id) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            return "该消费者不存在";
        }
        listenerContainer.stop();
        return "success";
    }

    @PostMapping("/start")
    @ApiOperation("启动消费")
    public String start(@RequestParam("id") String id) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            return "该消费者不存在";
        }
        listenerContainer.start();
        return "success";
    }
}
