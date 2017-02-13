package cn.sowell.file.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.file.modules.Constants;
import cn.sowell.file.modules.enums.Command;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.service.FileService;
/**
 * 缓存工具类
 * Http方式直接交互
 * @author Xiaojie.Xu
 */
@Controller
@RequestMapping("/cache")
public class CacheController extends BaseController implements Constants {

    @Autowired
    private FileService fileService;

    /**
     * 同步缓存与数据库
     */
    @ResponseBody
    @RequestMapping("/synchronize")
    public String synchronize(
        @RequestParam String key,
        HttpServletRequest request,
        Model model) {
        List<File> insertFiles = new ArrayList<File>();
        List<String> deleteFileIds = new ArrayList<String>();
        List<File> cacheList = cacheUtil.get(key);
        Iterator<File> it = cacheList.iterator();
        while(it.hasNext()) {
            File file = it.next();
            Command c = file.getCommand();
            if(c == Command.INSERT) {
                insertFiles.add(file);
                file.setCommand(Command.NONE);
            } else if(c == Command.DELETE) {
                deleteFileIds.add(file.getId());
                it.remove();
            }
        }
        fileService.insert(insertFiles);
        fileService.delete(deleteFileIds.toArray(new String[0]), request);
        return success(cacheList);
    }
    
	/**
	 * 显示缓存内容
	 */
    @ResponseBody
	@RequestMapping("/show")
	public String show(
	    @RequestParam(required = false) String key) {
		return key == null ? cacheUtil.toHtml() : cacheUtil.toHtml(key);
	}

	/**
	 * 清空缓存
	 */
    @ResponseBody
	@RequestMapping("/clear")
	public String clear(
	    @RequestParam(required = false) String key) {
		if (key == null) {
			cacheUtil.clear();
			return "Clear all cache.";
		} else {
			cacheUtil.clear(key);
			return "Clear cache by key(" + key + ").";
		}
	}

}
