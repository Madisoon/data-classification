package com.syx.data.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Msater Zg
 *         时间处理工具
 */
public class DateUtil {
    public String getNowTimeFormat(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String nowTime = simpleDateFormat.format(new Date());
        return nowTime;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list.size());
        System.out.println(list);
        System.out.println(list.get(2));
        list.remove(2);
        System.out.println(list.size());
        System.out.println(list.get(2));
        System.out.println(list);

    }
}
