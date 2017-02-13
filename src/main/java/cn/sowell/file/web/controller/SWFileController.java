package cn.sowell.file.web.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sowell.file.common.util.FileUtil;
import cn.sowell.file.common.util.TransHtmlUtil;
import cn.sowell.file.common.util.UrlManegerUtil;
import cn.sowell.file.modules.Constants;
import cn.sowell.file.modules.enums.Command;
import cn.sowell.file.modules.enums.FileType;
import cn.sowell.file.modules.model.Excel;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.model.Word;
import cn.sowell.file.modules.result.UploadResult;
import cn.sowell.file.modules.service.FileService;
import cn.sowell.file.web.CustomProgress;

/**
 * PC Web形式jQuery插件（swfile.js）接口服务类 注：利用session做缓存交互（传sessionId）
 * 
 * @author Xiaojie.Xu
 */
@Controller
@RequestMapping("/swfile")
public class SWFileController extends BaseController implements Constants {
    
    @Autowired
    private FileService fileService;
    
    /**
     * 首次交互，初始化参数数据
     */
    @RequestMapping("/init")
    public String init(
        @RequestParam String proxy,
        @RequestParam(defaultValue = "") String fids,
        Model model) {
        List<File> files = fileService.listByIds(fids.trim().split(","));
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        cacheUtil.clear(key); // 清空cache
        cacheUtil.put(key, files); // 从db加载到cache
        model.addAttribute(PAGE_PROXY_KEY_URL, proxy + "?act=init");
        model.addAttribute(PAGE_PROXY_KEY_DATA, "{" +
            "\"key\":\"" + key + "\"," +
            "\"ftype\":" + FileType.toJson() + "," +
            "\"files\":" + gsonUtil.toJson(files) +
        "}");
        return PAGE_PROXY;
    }

    /**
     * 上传（支持多文件）
     */
    @RequestMapping("/upload")
    public String upload(
        HttpServletRequest request,
        Model model) throws Exception {
        UploadResult ur = fileService.upload(request);
        List<File> files = ur.getFiles();
        Map<String, Object> values = ur.getValues();
        String key = (String) values.get("key");
        String proxy = (String) values.get("proxy");
        cacheUtil.put(key, files); // 存储到cache中
        model.addAttribute(PAGE_PROXY_KEY_URL, proxy + "?act=upload");
        model.addAttribute(PAGE_PROXY_KEY_DATA, gsonUtil.toJson(files));
        return PAGE_PROXY;
    }
    
    /**
     * 上传进度
     */
    @RequestMapping("/progress")
    public String progress(
        @RequestParam String key,
        @RequestParam String index,
        @RequestParam String proxy,
        HttpSession session,
        Model model) {
        String skey = key + "-" + index;
        while(progressUtil.get(skey) == null || progressUtil.get(skey).getContentLength() <= 0) continue;
        CustomProgress progress = progressUtil.get(skey);
        model.addAttribute(PAGE_PROXY_KEY_URL, proxy + "?act=progress");
        model.addAttribute(PAGE_PROXY_KEY_DATA, gsonUtil.toJson(progress));
        return PAGE_PROXY;
    }
    
    /**
     * 预览
     */
    @RequestMapping("/preview")
    public String preview(
        @RequestParam String key, 
        @RequestParam String id,
        @RequestParam String proxy,
        HttpServletRequest request,
        Model model) {
        File file = cacheUtil.get(key, id);
        String fileDir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
        String fileUrl = UrlManegerUtil.getWebUrl(request, ROOT_DIR);
        String oldFilePath = fileDir + file.getPath();
        String oldFileUrl = fileUrl + file.getPath();
        model.addAttribute("proxy", proxy + "?act=close");
        model.addAttribute("json", "{key:\"" + key + "\", file:" + gsonUtil.toJson(file) + "}");
        model.addAttribute("file", file);
        if (file == null || !FileUtil.exist(oldFilePath)) { // 文件不存在（缓存过期或硬盘上文件被删除）
            return PAGE_PREVIEW_NONE;
        }
        FileType type = file.getType();
        if (type == FileType.OTHER) { // 其他
            return PAGE_PREVIEW_NONE;
        }
        if (type == FileType.IMAGE) { // 图片
            model.addAttribute("path", oldFileUrl);
            return PAGE_PREVIEW_IMG;
        }
        if (type == FileType.AUDIO) { // 音频
            model.addAttribute("path", oldFileUrl);
            return PAGE_PREVIEW_AUDIO;
        }
        if (type == FileType.VIDEO) { // 视频
            model.addAttribute("path", oldFileUrl);
            return PAGE_PREVIEW_VIDEO;
        }
        TransHtmlUtil transHtmlUtil = TransHtmlUtil.getInstance();
        if (type == FileType.WORD) { // word
            Word word = (Word) file;
            String htmlFileUrl = fileUrl + "/" + word.getPreviewPath();
            String htmlFilePath = fileDir + "/" + word.getPreviewPath();
            if (!FileUtil.exist(htmlFilePath)) {
                transHtmlUtil.transToHtml(oldFilePath, htmlFilePath);
            }
            model.addAttribute("path", htmlFileUrl);
            return PAGE_PREVIEW_DOC;
        }
        if (type == FileType.EXCEL) { // excel
            Excel excel = (Excel) file;
            String htmlFileUrl = fileUrl + "/" + excel.getPreviewPath();
            String htmlFilePath = fileDir + "/" + excel.getPreviewPath();
            if (!FileUtil.exist(htmlFilePath)) {
                transHtmlUtil.transToHtml(oldFilePath, htmlFilePath);
            }
            model.addAttribute("path", htmlFileUrl);
            return PAGE_PREVIEW_DOC;
        }
        return PAGE_PREVIEW_NONE;
    }

    /**
     * 下载
     */
    @RequestMapping("/download")
    public String download(
        @RequestParam String key,
        @RequestParam String id,
        HttpServletRequest request,
        Model model) {
        String fileDir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
        File file = cacheUtil.get(key, id);
        if (file == null) { // 文件不存在（可能是cache已过期，且未来得及存入db）
            return PAGE_PREVIEW_NONE;
        }
        String fileName = file.getName() + "." + file.getExt();
        String filePath = fileDir + file.getPath();
        model.addAttribute("fileName", fileName);
        model.addAttribute("filePath", filePath);
        return PAGE_DOWNLOAD;
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public String delete(
        @RequestParam String key,
        @RequestParam String id,
        @RequestParam String proxy,
        Model model) {
        File file = cacheUtil.get(key, id);
        file.setCommand(Command.DELETE);
        model.addAttribute(PAGE_PROXY_KEY_URL, proxy + "?act=delete");
        model.addAttribute(PAGE_PROXY_KEY_DATA, gsonUtil.toJson(file));
        return PAGE_PROXY;
    }
    
    /**
     * 根据id获取文件对象
     */
    @RequestMapping("/get")
    public String get(
        @RequestParam String key,
        @RequestParam String proxy,
        @RequestParam String id,
        Model model) {
        File file = cacheUtil.get(key, id);
        model.addAttribute(PAGE_PROXY_KEY_URL, proxy + "?act=get");
        model.addAttribute(PAGE_PROXY_KEY_DATA, gsonUtil.toJson(file));
        return PAGE_PROXY;
    }
}
