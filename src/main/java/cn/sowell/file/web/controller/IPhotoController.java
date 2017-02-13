package cn.sowell.file.web.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.file.modules.Constants;
import cn.sowell.file.modules.enums.Command;
import cn.sowell.file.modules.enums.FileType;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.result.UploadResult;
import cn.sowell.file.modules.service.FileService;
import cn.sowell.file.web.CustomProgress;

/**
 * 移动端jQuery插件（iphoto.js）接口服务类 注：利用session做缓存交互（传sessionId）
 * 
 * @author Xiaojie.Xu
 */
@Controller
@RequestMapping("/iphoto")
public class IPhotoController extends BaseController implements Constants {
    
    @Autowired
    private FileService fileService;
    
    private static final String MIME_JSON = "application/json;charset=utf-8";
    
    private static final String MIME_SCRIPT = "application/javascript;charset=utf-8";
    
    /**
     * 首次交互，初始化参数数据
     */
    @ResponseBody
    @RequestMapping(value="/init", produces=MIME_SCRIPT)
    public String init(
        @RequestParam String callback) {
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        cacheUtil.clear(key); // 清空cache
        return callback(callback, "{" +
            "\"key\":\"" + key + "\"," +
            "\"ftype\":" + FileType.toJson() +
        "}");
    }

    /**
     * 上传（支持多文件）
     */
    @ResponseBody
    @RequestMapping(value="/upload", produces=MIME_JSON)
    public String upload(
        HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        UploadResult ur = fileService.upload(request);
        List<File> files = ur.getFiles();
        Map<String, Object> values = ur.getValues();
        String key = (String) values.get("key");
        cacheUtil.put(key, files); // 存储到cache中
        response.setHeader("Access-Control-Allow-Origin", "*");
        return gsonUtil.toJson(files);
    }
    
    /**
     * 上传进度
     */
    @ResponseBody
    @RequestMapping(value="/progress", produces=MIME_SCRIPT)
    public String progress(
        @RequestParam String key,
        @RequestParam String index,
        @RequestParam String callback) {
        String skey = key + "-" + index;
        while(progressUtil.get(skey) == null || progressUtil.get(skey).getContentLength() <= 0) continue;
        CustomProgress progress = progressUtil.get(skey);
        return callback(callback, gsonUtil.toJson(progress));
    }
    
    /**
     * 删除
     */
    @ResponseBody
    @RequestMapping(value="/delete", produces=MIME_SCRIPT)
    public String delete(
        @RequestParam String key,
        @RequestParam String id,
        @RequestParam String callback) {
        File file = cacheUtil.get(key, id);
        file.setCommand(Command.DELETE);
        return callback(callback, gsonUtil.toJson(file));
    }
    
    /**
     * 根据id获取文件对象
     */
    @ResponseBody
    @RequestMapping(value="/get", produces=MIME_SCRIPT)
    public String get(
        @RequestParam String key,
        @RequestParam String id,
        @RequestParam String callback) {
        File file = cacheUtil.get(key, id);
        return callback(callback, gsonUtil.toJson(file));
    }
    
    private String callback(String cbname, String json) {
        return cbname + "(" + json + ")";
    }
}
