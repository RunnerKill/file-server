package cn.sowell.file.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 路径管理类
 * @author Xiaojie.Xu
 */
public class UrlManegerUtil {

    /**
     * 当前项目的物理路径
     * @param request
     * @return String as "F:/tomcat/webapps/${projectName}/"
     */
    public static String getServerPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/");
    }

    /**
     * 当前项目的web路径
     * @param request
     * @return String as "http://localhost:8080/${projectName}/"
     */
    public static String getWebUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
            + "/";
    }

    /**
     * 物理路径
     * @param request
     * @param dirName web服务器里的目录名
     * @return String as "F:/tomcat/webapps/${dirName}/"
     */
    public static String getServerPath(HttpServletRequest request, String dirName) {
        return getServerPath(request) + "../" + dirName + "/";
    }

    /**
     * web路径
     * @param request
     * @param dirName web服务器里的目录名
     * @return String as "http://localhost:8080/${dirName}/"
     */
    public static String getWebUrl(HttpServletRequest request, String dirName) {
        return getWebUrl(request) + "../" + dirName + "/";
    }

}
