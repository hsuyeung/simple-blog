package com.hsuyeung.blog.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Objects;

/**
 * ip 相关工具类
 *
 * @author hsuyeung
 * @date 2022/06/16
 */
public final class IpUtil {
    private IpUtil() {
    }

    /**
     * 获取 IP 地址
     *
     * @param request {@link HttpServletRequest}
     * @return IP 地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("0:0:0:0:0:0:0:1".equals(ipAddress) || "127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = InetAddress.getLocalHost();
                    ipAddress = Objects.requireNonNull(inet).getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                int indexOf = ipAddress.indexOf(",");
                if (indexOf > 0) {
                    ipAddress = ipAddress.substring(0, indexOf);
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
