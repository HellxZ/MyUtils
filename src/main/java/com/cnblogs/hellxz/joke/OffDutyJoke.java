package com.cnblogs.hellxz.joke;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <b>类名</b>: OffDutyJoke
 * <p><b>描    述</b> 下班倒计时，用来玩的
 * 十一放假前的一个下午，提升一下Api的熟悉度:p
 * </p>
 *
 * <p><b>创建日期</b>: 2018/9/30 18:04 </p>
 * @author  HELLXZ
 * @version  1.0
 * @since  jdk 1.8
 */
public class OffDutyJoke {

    /**
     * 下班计时
     * @param offHours 下班的小时
     * @param offMinutes 下班的分钟
     */
    public static void offDutyCountDown(final String offHours, final String offMinutes){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                LocalDateTime today = LocalDateTime.now();
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                long from,to,mid=0L;
                try {
                    int year = today.getYear();
                    int month = today.getMonthValue();
                    int day = today.getDayOfMonth();
                    String datePrefix = year + "-" + month + "-" + day + " ";
                    from = simpleFormat.parse(datePrefix+ today.getHour() + ":" + today.getMinute()).getTime();
                    to = simpleFormat.parse(datePrefix + " "+offHours+":"+offMinutes).getTime();
                    mid = to - from;
                    if(from>=to) timer.cancel(); //取消定时器，会执行完本方法后结束
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int hours = (int) (mid / (1000 * 60 * 60));
                int minutes = (int) (mid / (1000 * 60));
                if(mid <= 0)
                    System.out.println("已经下班，别试了！");
                else
                    System.out.println("还剩" + (hours <= 0 ? "" : hours + "小时") + minutes + "分钟！");
            }
        }, 0, 60000);
    }

    public static void main(String[] args) {
        offDutyCountDown("18","16");
    }
}
