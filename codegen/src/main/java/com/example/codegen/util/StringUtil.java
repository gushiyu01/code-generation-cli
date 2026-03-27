package com.example.codegen.util;

/**
 * 字符串工具类
 *
 * @author CodeGenerator
 */
public class StringUtil {

    /**
     * 下划线转驼峰
     */
    public static String toCamelCase(String underscore) {
        if (underscore == null || underscore.isEmpty()) {
            return underscore;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < underscore.length(); i++) {
            char c = underscore.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    /**
     * 下划线转PascalCase
     */
    public static String toPascalCase(String underscore) {
        if (underscore == null || underscore.isEmpty()) {
            return underscore;
        }
        String camel = toCamelCase(underscore);
        return Character.toUpperCase(camel.charAt(0)) + camel.substring(1);
    }

    /**
     * 首字母小写
     */
    public static String firstCharToLower(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
