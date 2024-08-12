package org.owasp.webgoat.lessons.jwt.mytest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DateTimeConverter {

    // 日期格式化
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 将标准时间转换为时间戳
    public static long dateToTimestamp(String dateStr) throws ParseException {
        Date date = dateFormat.parse(dateStr);
        return date.getTime() / 1000;
    }

    // 将时间戳转换为标准时间
    public static String timestampToDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        return dateFormat.format(date);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请选择操作: ");
            System.out.println("1. 标准时间转换为时间戳");
            System.out.println("2. 时间戳转换为标准时间");
            System.out.println("3. 退出");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 读取换行符

            try {
                switch (choice) {
                    case 1:
                        System.out.print("请输入标准时间 (格式: yyyy-MM-dd HH:mm:ss): ");
                        String dateStr = scanner.nextLine();
                        long timestamp = dateToTimestamp(dateStr);
                        System.out.println("时间戳: " + timestamp);
                        break;
                    case 2:
                        System.out.print("请输入时间戳: ");
                        long ts = scanner.nextLong();
                        scanner.nextLine(); // 读取换行符
                        String date = timestampToDate(ts);
                        System.out.println("标准时间: " + date);
                        break;
                    case 3:
                        System.out.println("退出程序");
                        scanner.close();
                        return;
                    default:
                        System.out.println("无效的选择，请重新选择");
                }
            } catch (ParseException e) {
                System.out.println("日期格式错误，请输入正确的日期格式");
            } catch (Exception e) {
                System.out.println("发生错误: " + e.getMessage());
            }

            System.out.println(); // 输出空行分隔
        }
    }
}

