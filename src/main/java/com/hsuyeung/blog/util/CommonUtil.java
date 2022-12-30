package com.hsuyeung.blog.util;

import java.util.regex.Matcher;

import static com.hsuyeung.blog.constant.RegexConstants.*;

/**
 * @author hsuyeung
 * @date 2022/06/21
 */
public final class CommonUtil {
    /**
     * 清除所有的 html 标签
     *
     * @param htmlStr 带有 html 标签的字符串
     * @return 清除 html 标签后的字符串
     */
    public static String removeTag(String htmlStr) {
        Matcher mScript = JAVASCRIPT_TAG_PATTERN.matcher(htmlStr);
        htmlStr = mScript.replaceAll("");
        Matcher mStyle = STYLE_TAG_PATTERN.matcher(htmlStr);
        htmlStr = mStyle.replaceAll("");
        Matcher mHtml = HTML_TAG_PATTERN.matcher(htmlStr);
        htmlStr = mHtml.replaceAll("");
        Matcher mSpace = SPACE_TAG_PATTERN.matcher(htmlStr);
        htmlStr = mSpace.replaceAll("");
        return htmlStr;
    }

    private CommonUtil() {
    }
}
