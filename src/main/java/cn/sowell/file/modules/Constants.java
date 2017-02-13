package cn.sowell.file.modules;

/**
 * 常量接口
 * @author Xiaojie.Xu
 */
public interface Constants {
    
    /**
     * 跨域代理页面
     */
    String PAGE_PROXY = "/proxy";

    String PAGE_PROXY_KEY_URL = "url";

    String PAGE_PROXY_KEY_DATA = "data";
    
    /**
     * 下载页面
     */
    String PAGE_DOWNLOAD = "/download";

    /**
     * 预览页面
     */
    String PAGE_PREVIEW_DOC = "/preview/doc";

    String PAGE_PREVIEW_IMG = "/preview/img";
    
    String PAGE_PREVIEW_AUDIO = "/preview/audio";
    
    String PAGE_PREVIEW_VIDEO = "/preview/video";

    String PAGE_PREVIEW_NONE = "/preview/none";

    /**
     * 文件根路径
     */
    String ROOT_DIR = "file_system";

    String ROOT_PREVIEW_DIR = "preview";

    /**
     * 日期格式字符串
     */
    String DATE_PATTERN = "yyyy年MM月dd日 HH:mm:ss";

}
