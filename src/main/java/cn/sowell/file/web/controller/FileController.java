package cn.sowell.file.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.file.common.util.UrlManegerUtil;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.service.FileService;

/**
 * Servlet形式http接口服务类 注：Client端可用HttpURLConnection/HttpClient直接交互
 * @author Xiaojie.Xu
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {

	@Autowired
	private FileService fileService;
	
	/**
	 * 上传
	 */
    @ResponseBody
    @RequestMapping("/upload")
	public String upload(
	    HttpServletRequest request) throws Exception {
		List<File> files = fileService.upload(request).getFiles();
		if(files.size() < 1) {
		    return failed("upload failed: no file returned.");
		}
		fileService.insert(files);
		return success(files);
	}

	/**
	 * 根据id拿文件
	 */
    @ResponseBody
	@RequestMapping("/getById")
	public String getById(
	    @RequestParam String id) {
		File file = fileService.findById(id);
		if (file == null) {
		    return failed("getById failed: file(" + id + ") can't be found.");
		}
		return success(file);
	}
    
    /**
     * 根据id列表拿文件
     */
    @ResponseBody
    @RequestMapping("/getByIds")
    public String getByIds(
        @RequestParam ArrayList<String> idArray) {
        List<File> files = fileService.listByIds(idArray.toArray(new String[0]));
        return success(files);
    }

	/**
	 * 根据id删文件
	 */
    @ResponseBody
	@RequestMapping("/deleteById")
	public String deleteById(
	    @RequestParam String id,
	    HttpServletRequest request) {
		int num = fileService.delete(id, request);
		return success(num);
	}
    
    /**
     * 根据id列表删文件
     */
    @ResponseBody
    @RequestMapping("/deleteByIds")
    public String deleteByIds(
        @RequestParam ArrayList<String> idArray,
        HttpServletRequest request) {
        int num = fileService.delete(idArray.toArray(new String[0]), request);
        return success(num);
    }

	/**
	 * 根据id下载文件
	 */
	@RequestMapping("/download")
	public String download(
	    @RequestParam String id,
	    HttpServletRequest request,
	    Model model) {
		String fileDir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
		File file = fileService.findById(id);
		if (file == null) { // 文件不存在
			return failed("download failed: file(" + id + ") not be found.");
		}
		String fileName = file.getName() + "." + file.getExt();
		String filePath = fileDir + file.getPath();
		model.addAttribute("fileName", fileName);
		model.addAttribute("filePath", filePath);
		return PAGE_DOWNLOAD;
	}

	/**
	 * 返回文件根目录名
	 */
	@ResponseBody
	@RequestMapping("/getFileDir")
	public String getFileDir() {
		return success(ROOT_DIR);
	}

}
