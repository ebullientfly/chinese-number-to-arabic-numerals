package com.yit.riskcontrolbi.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author guopengfei  2018-10-25 下午4:15
 **/

public class StringUtil {

    public static Map<Character, Integer> CnNum2Num = new HashMap<Character, Integer>() {{
        put('一', 1);
        put('二', 2);
        put('三', 3);
        put('四', 4);
        put('五', 5);
        put('六', 6);
        put('七', 7);
        put('八', 8);
        put('九', 9);
    }};

    public static Map<Character, Integer> ExtendCnNum2Num = new HashMap<Character, Integer>() {{
        put('一', 1);
        put('二', 2);
        put('三', 3);
        put('四', 4);
        put('五', 5);
        put('六', 6);
        put('七', 7);
        put('八', 8);
        put('九', 9);
        put('零', 0);
    }};
    
    /**
     * 从指定位置开始识别中文数字
     * @param chineseNumber 字符串
     * @param startIndex    识别的起始位置
     * @return Pair<识别的数值, 非数字的起始下标>
     */
    public static Pair<Long, Integer> chineseNumberToLong(String chineseNumber, int startIndex) {
        int len = chineseNumber.length();
        if (startIndex >= len) return Pair.of(null, null);

        boolean hasUnit = false;
        boolean meetNoneNumber = false;
        long result = 0;    // 总结果

        long unParsedTemp = 0;  // 当前分段结果

        int number = 0; // 当前单位前的数值

        int i = startIndex;
        for (; i < len; i++) {
            char c = chineseNumber.charAt(i);
            if (CnNum2Num.containsKey(c)) {
                number = CnNum2Num.get(c);
            } else {
                switch (c) {
                    case '零':
                        break;
                    case '十':
                        hasUnit = true;
                        if (number == 0) unParsedTemp += 10;
                        else unParsedTemp += number * 10;
                        number = 0;     // 清理
                        break;
                    case '百':
                        hasUnit = true;
                        if (number == 0) unParsedTemp += 100;
                        else unParsedTemp += number * 100;
                        number = 0;     // 清理
                        break;
                    case '千':
                        hasUnit = true;
                        if (number == 0) unParsedTemp += 1000;
                        else unParsedTemp += number * 1000;
                        number = 0;     // 清理
                        break;
                    case '万':
                        hasUnit = true;
                        unParsedTemp *= 10000;
                        result += unParsedTemp;
                        unParsedTemp = 0;
                        number = 0;     // 清理
                        break;
                    case '亿':
                        hasUnit = true;
                        unParsedTemp *= 100000000;
                        result += unParsedTemp;
                        unParsedTemp = 0;
                        number = 0;     // 清理
                        break;
                    default:
                        meetNoneNumber = true;
                        break;
                }

            }
            if (meetNoneNumber) break;
        }
        if (!hasUnit) {
            result = 0;
            int j = startIndex;
            for (; j < len; j++) {
                char c = chineseNumber.charAt(j);
                if (ExtendCnNum2Num.containsKey(c)) {
                    number = ExtendCnNum2Num.get(c);
                    result = result * 10 + number;
                } else {
                    break;
                }
            }
            return Pair.of(result, j);
        } else {
            return Pair.of(result + unParsedTemp + number, i);
        }
    }
}
