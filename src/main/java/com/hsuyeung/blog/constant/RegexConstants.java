package com.hsuyeung.blog.constant;

import java.util.regex.Pattern;

/**
 * @author hsuyeung
 * @date 2022/06/17
 */
public final class RegexConstants {
    public static final String WEBSITE_PREFIX_REGEX = "^(http:\\/\\/|https:\\/\\/)([\\w.]+\\/?)\\S*$";
    public static final String EMAIL_REGEX = "^([A-Za-z\\d_\\-\\.])+\\@([A-Za-z\\d_\\-\\.])+\\.([A-Za-z]{2,8})$";
    public static final String JAVASCRIPT_TAG_REGEX = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    public static final Pattern JAVASCRIPT_TAG_PATTERN = Pattern.compile(JAVASCRIPT_TAG_REGEX, Pattern.CASE_INSENSITIVE);
    public static final String STYLE_TAG_REGEX = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    public static final Pattern STYLE_TAG_PATTERN = Pattern.compile(STYLE_TAG_REGEX, Pattern.CASE_INSENSITIVE);
    public static final String HTML_TAG_REGEX = "<[^>]+>";
    public static final Pattern HTML_TAG_PATTERN = Pattern.compile(HTML_TAG_REGEX, Pattern.CASE_INSENSITIVE);
    public static final Pattern SPACE_TAG_PATTERN = Pattern.compile("\\s+|\t|\r|\n", Pattern.CASE_INSENSITIVE);

    public static final String ALL_NUMBER_REGEX = "^\\d+$";
    public static final String USERNAME_REGEX = "^[a-zA-Z]\\w*$";
    public static final String ROLE_CODE_REGEX = "^[a-zA-Z]\\w*$";
    public static final String NICKNAME_REGEX = "^[a-zA-Z\\d\\u4e00-\\u9fa5]+$";
    public static final String PASSWORD_REGEX = "^[a-zA-Z][a-zA-Z_!@%\\d]{7,15}$";
    public static final String HTTP_METHOD_REGEX = "^(?i)(get|put|post|delete)$";

    private RegexConstants() {
    }
}
