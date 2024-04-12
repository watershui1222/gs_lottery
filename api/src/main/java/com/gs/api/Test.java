package com.gs.api;

import cn.hutool.core.util.ArrayUtil;

public class Test {

    public static void main(String[] args) {
        String number = "11122"; // 假设用户输入的号码为11234

        boolean isDoushouHuLu = checkDoushouHuLu(number);
        if (isDoushouHuLu) {
            System.out.println(number + " 是斗牛梭哈葫芦号码");
        } else {
            System.out.println(number + " 不是斗牛梭哈葫芦号码");
        }
    }

    public static boolean checkDoushouHuLu(String number) {
        // 统计号码中各数字的出现次数
        int[] count = new int[10]; // 下标为数字，值为出现次数
        for (char c : number.toCharArray()) {
            int digit = Character.getNumericValue(c);
            count[digit]++;
        }

        // 判断是否有一个葫芦（三个相同数字加一个对子）
        return ArrayUtil.contains(count, 3) && ArrayUtil.contains(count, 2);
    }
}
