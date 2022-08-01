package net.getbang.log.feign;

import net.getbang.core.base.CommonResult;
import net.getbang.core.constant.CommonConstant;
import net.getbang.log.model.LogError;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = CommonConstant.APPLICATION_USER_NAME,
        fallback = LogClientFallback.class
)
public interface ILogClient {


    String API_PREFIX = "/log";

    /**
     * 保存错误日志
     *
     * @param log 日志实体
     * @return boolean
     */
    @PostMapping(API_PREFIX + "/saveErrorLog")
    CommonResult<Boolean> saveErrorLog(@RequestBody LogError log);

}
