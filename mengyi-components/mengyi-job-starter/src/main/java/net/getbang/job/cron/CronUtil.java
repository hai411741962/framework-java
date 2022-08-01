package net.getbang.job.cron;

/**
 * @description:
 * @author: CJ
 * @date: 2022/6/21
 */

/**
 * @author
 * @ClassName: CronUtil
 * @Description: Cron表达式工具类
 * 目前支持三种常用的cron表达式
 * 0.每天的某个时间点执行 例:12 12 12 * * ?表示每天12时12分12秒执行
 * 1.每周的哪几天执行         例:12 12 12 ? * 1,2,3表示每周的周1周2周3 ,12时12分12秒执行
 * 2.每月的哪几天执行         例:12 12 12 1,21,13 * ?表示每月的1号21号13号 12时12分12秒执行
 * 4.每年的那天执行           0 0 0  1 1 ? *
 * 5.自定义，一段时间范围内，每隔几天/几月/几年执行
 * @date
 */
public class CronUtil {
    /**
     * 方法摘要：构建Cron表达式
     *
     * @param taskScheduleModel
     * @return String
     */
    public static String createCronExpression(TaskScheduleModel taskScheduleModel) {
        StringBuffer cronExp = new StringBuffer("");

        if (null == taskScheduleModel.getJobType()) {
            System.out.println("执行周期未配置");//执行周期未配置
        }

        if (null != taskScheduleModel.getSecond()
                && null != taskScheduleModel.getMinute()
                && null != taskScheduleModel.getHour()) {
            //秒
            cronExp.append(taskScheduleModel.getSecond()).append(" ");
            //分
            cronExp.append(taskScheduleModel.getMinute()).append(" ");
            //小时
            cronExp.append(taskScheduleModel.getHour()).append(" ");

            //每天
            if (taskScheduleModel.getJobType().intValue() == 0 || taskScheduleModel.getJobType().intValue() == 5) {
                cronExp.append("* ");//日
                cronExp.append("* ");//月
                cronExp.append("?");//周
            }

            //按每周
            else if (taskScheduleModel.getJobType().intValue() == 1) {
                //一个月中第几天
                cronExp.append("? ");
                //月份
                cronExp.append("* ");
                //周
                cronExp.append(taskScheduleModel.getDayOfWeeks());
            }

            //按每月
            else if (taskScheduleModel.getJobType().intValue() == 2) {
                //一个月中的哪几天
                cronExp.append(taskScheduleModel.getDayOfMonths());
                //月份
                cronExp.append(" * ");
                //周
                cronExp.append("?");
            }

            //按每年
            else if (taskScheduleModel.getJobType().intValue() == 4) {
                //日
                cronExp.append(taskScheduleModel.getDayOfYear()).append(" ");
                //月份
                cronExp.append(taskScheduleModel.getMonthOfYear()).append(" ");
                //周
                cronExp.append("?");
            }

        } else {
            System.out.println("时或分或秒参数未配置");//时或分或秒参数未配置
        }
        return cronExp.toString();
    }
}

