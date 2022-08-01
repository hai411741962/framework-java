package net.getbang.job.config;

import net.getbang.job.properties.JobProperties;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: CJ
 * @date: 2022/6/21
 */
@Slf4j
@Component
public class DynamicXxlJobService {

    private volatile String jobGroupId;

    @Resource
    private JobProperties jobProperties;


    public Long createAndStartTaskJob(Long id, String desc, String corn, String param, String jobHandler, String cookie) {
        Long job = createOrUpdateJob(id, cookie, desc, corn, param, jobHandler);
        if (job != -1) {
            start(cookie, job);
        }
        return job;
    }

    public Long createTaskJob(Long id, String desc, String corn, String param, String jobHandler, String cookie) {
        return createOrUpdateJob(id, cookie, desc, corn, param, jobHandler);
    }

    public Boolean stop(Long jobId, String cookie) {
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("id", String.valueOf(jobId));
        HttpResponse response = HttpRequest.post(jobProperties.getAdmin().getAddresses() + "/jobinfo/stop")
                .header("cookie", cookie)
                .form(paramMap).execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            return true;
        } else {
            log.error("job任务停止失败：{}", jsonObject.toJSONString());
        }
        return false;
    }


    /**
     * 创建固定任务
     *
     * @param desc  任务描述
     * @param corn  cron 表达式
     * @param param param
     * @return jobId
     */
    private long createOrUpdateJob(Long id, String cookie, String desc, String corn, String param, String jobHandler) {

        long jobId = -1L;
        Map<String, Object> paramMap = new HashMap<>(8);
        if (id != null && id > 0) {
            paramMap.put("id", id);
        }
        paramMap.put("jobGroup", StrUtil.isBlank(jobGroupId) ? jobGroupId = getJobGroupId(cookie) : jobGroupId);
        paramMap.put("jobDesc", desc);
        paramMap.put("author", "system");
        paramMap.put("scheduleType", "CRON");
        paramMap.put("scheduleConf", corn);
        paramMap.put("cronGen_display", corn);
        paramMap.put("glueType", "BEAN");
        paramMap.put("executorHandler", jobHandler);
        // 此处hander需提前在项目中定义
        paramMap.put("executorParam", param);
        paramMap.put("executorRouteStrategy", "FIRST");
        paramMap.put("misfireStrategy", "DO_NOTHING");
        paramMap.put("executorBlockStrategy", "SERIAL_EXECUTION");
        paramMap.put("executorTimeout", "0");
        paramMap.put("executorFailRetryCount", "0");
        paramMap.put("glueRemark", "edgeworth项目" + jobProperties.getExecutor().getAppName());
        if (StrUtil.isNotEmpty(cookie)) {
            cookie = getCookie();
        }
        HttpResponse response = HttpRequest.post(jobProperties.getAdmin().getAddresses() + (id != null && id > 0 ? "/jobinfo/update" : "/jobinfo/add"))
                .header("cookie", cookie)
                .form(paramMap)
                .execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            jobId = id != null && id > 0 ? id : jsonObject.getLongValue("content") + 1;
        } else {
            log.error("job任务创建更新失败：{}", jsonObject.toJSONString());
        }

        return jobId;
    }


    /**
     * 启动固定任务
     *
     * @param cookie cookie
     * @param jobId  jobId
     * @return 启动固定任务
     */
    public boolean start(String cookie, long jobId) {
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("id", String.valueOf(jobId));

        if (StringUtils.isEmpty(cookie)) {
            cookie = getCookie();
        }
        HttpResponse response = HttpRequest.post(jobProperties.getAdmin().getAddresses() + "/jobinfo/start")
                .header("cookie", cookie)
                .form(paramMap).execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            return true;
        } else {
            log.error("job任务启动失败：{}", jsonObject.toJSONString());
        }
        return false;
    }


    /**
     * 删除固定任务
     *
     * @param cookie cookie
     * @param jobId  jobId
     * @return 启动固定任务
     */
    public boolean remove(Long jobId, String cookie) {
        if (jobId == null || jobId == -1) {
            return true;
        }
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("id", String.valueOf(jobId));
        if (StringUtils.isEmpty(cookie)) {
            cookie = getCookie();
        }
        HttpResponse response = HttpRequest.post(jobProperties.getAdmin().getAddresses() + "/jobinfo/remove")
                .header("cookie", cookie)
                .form(paramMap).execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        int code = jsonObject.getInteger("code");
        if (code == 200) {
            return true;
        } else {
            log.error("job删除创建失败：{}", jsonObject.toJSONString());
        }
        return false;
    }

    /**
     * 获取cookie
     *
     * @return 返回cookie值
     */
    public String getJobGroupId(String cookie) {
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("start", 0);
        paramMap.put("length", 100000);
        if (StringUtils.isEmpty(cookie)) {
            cookie = getCookie();
        }
        HttpResponse response = HttpRequest.post(jobProperties.getAdmin().getAddresses() + "/jobgroup/pageList")
                .header("cookie", cookie)
                .form(paramMap).execute();
        JSONObject jsonObject = JSON.parseObject(response.body());
        JSONArray array = jsonObject.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject item = array.getJSONObject(i);
            if (StrUtil.equals(item.getString("appname"), jobProperties.getExecutor().getAppName())) {
                return item.getString("id");
            }
        }
        return null;
    }

    /**
     * 获取cookie
     *
     * @return 返回cookie值
     */
    public String getCookie() {
        String path = jobProperties.getAdmin().getAddresses() + "/login";
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("userName", jobProperties.getAdmin().getUserName());
        hashMap.put("password", jobProperties.getAdmin().getPassword());
        HttpResponse response = HttpRequest.post(path).form(hashMap).execute();
        List<HttpCookie> cookies = response.getCookies();
        StringBuilder sb = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            sb.append(cookie.toString());
        }
        return sb.toString();
    }

}

