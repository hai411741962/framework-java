package net.getbang.job.cron;

import lombok.Data;

/**
 * @description:
 * @author: CJ
 * @date: 2022/6/21
 */
@Data
public class TaskScheduleModel {


    /**
     * 所选作业类型:
     * 0  -> 每天
     * 1 -> 每周
     * 2  -> 每月
     * 4.    每年
     * 5   -> 每天
     */
    Integer jobType;

    /**一周的哪几天  , 逗号隔开*/
    String dayOfWeeks;

    /**一个月的哪几天  , 逗号隔开*/
    String dayOfMonths;

    String dayOfYear;

    String monthOfYear;

    /**秒  */
    String second;

    /**分  */
    String minute;

    /**时  */
    String hour;


}

