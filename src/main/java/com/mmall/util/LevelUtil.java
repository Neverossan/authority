package com.mmall.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: HP
 * @Date: 2019/3/22 15:25
 * @Description:
 */
public class LevelUtil {

    public static final String SEPARATOR = ".";

    public static final String ROOT = "0";

    public static String calculateLevel(String parentLevel, int parentId){
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
