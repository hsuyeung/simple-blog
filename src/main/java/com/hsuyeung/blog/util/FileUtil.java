package com.hsuyeung.blog.util;

/**
 * 文件相关工具类
 *
 * @author hsuyeung
 * @date 2022/06/27
 */
public final class FileUtil {
    /**
     * 获取文件名的后缀
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getSuffix(String fileName) {
        AssertUtil.hasLength(fileName, "fileName 不能为空");
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot < 0) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    private FileUtil() {
    }
}
