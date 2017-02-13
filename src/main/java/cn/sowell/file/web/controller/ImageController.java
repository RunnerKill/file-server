package cn.sowell.file.web.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sowell.file.common.util.ImageUtil;
import cn.sowell.file.common.util.UrlManegerUtil;
import cn.sowell.file.modules.enums.FileType;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.model.Image;
import cn.sowell.file.modules.service.FileService;
import cn.sowell.file.modules.service.ImageService;

/**
 * 图片接口服务类
 * @author Xiaojie.Xu
 */
@Controller
@RequestMapping("/image")
public class ImageController extends BaseController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ImageService imageService;

	/**
	 * 缩放
	 */
	@ResponseBody
	@RequestMapping("/scale")
	public String scale(
		@RequestParam String id,
		@RequestParam(required=false, defaultValue="0") int width,
		@RequestParam(required=false, defaultValue="0") int height,
		HttpServletRequest request) throws IOException {
	    String dir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
		File file = fileService.findById(id);
		String msg = check(id, file, "scale");
		if (msg != null) {
			return failed(msg);
		}
		boolean result = imageService.scale((Image)file, dir, width, height);
		if(!result) return failed("no width or height to scale.");
		return success(null);
	}
	
	/**
     * 缩放
     */
    @ResponseBody
    @RequestMapping("/scaleMultiply")
    public String scaleMultiply(
        @RequestParam ArrayList<String> ids,
        @RequestParam(required=false, defaultValue="") ArrayList<Integer> widths,
        @RequestParam(required=false, defaultValue="") ArrayList<Integer> heights,
        HttpServletRequest request) throws IOException {
        String dir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
        for(int i = 0; i < ids.size(); i ++) {
            String id = ids.get(i);
            File file = fileService.findById(id);
            String msg = check(id, file, "scale");
            if (msg != null) continue;
            if(widths.size() > heights.size()) {
                for(int j = 0; j < widths.size(); j ++) {
                    int width = widths.get(j), height = 0;
                    if(j < heights.size()) height = heights.get(j);
                    imageService.scale((Image)file, dir, width, height);
                }
            } else {
                for(int j = 0; j < heights.size(); j ++) {
                    int width = 0, height = heights.get(j);
                    if(j < widths.size()) width = widths.get(j);
                    imageService.scale((Image)file, dir, width, height);
                }
            }
        }
        return success(null);
    }

	/**
	 * 裁剪
	 */
	@ResponseBody
	@RequestMapping("/cut")
	public String cut(
		@RequestParam String id,
		@RequestParam int x,
		@RequestParam int y, 
		@RequestParam int width,
		@RequestParam int height,
		HttpServletRequest request) throws IOException {
		File file = fileService.findById(id);
		String msg = check(id, file, "cut");
		if (msg != null) {
			return failed(msg);
		}
		String dir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
		String path = file.getPath();
		int pos = path.lastIndexOf(".");
		String newPath = dir + path.substring(0, pos) + "-cut(x" + x + "_y" + y + "_w" + width + "_h" + height + ")"
				+ path.substring(pos);
		String oldPath = dir + path;
		ImageUtil.cut(oldPath, newPath, x, y, width, height);
		return success(null);
	}

	/**
	 * 旋转
	 */
	@ResponseBody
	@RequestMapping("/rotate")
	public String rotate(
		@RequestParam String id,
		@RequestParam int degree,
		HttpServletRequest request) throws IOException {
		File file = fileService.findById(id);
		String msg = check(id, file, "rotate");
		if (msg != null) {
			return failed(msg);
		}
		String dir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
		String path = file.getPath();
		int pos = path.lastIndexOf(".");
		String newPath = dir + path.substring(0, pos) + "-rotate(d" + degree + ")"
		        + path.substring(pos);
		String oldPath = dir + path;
		ImageUtil.rotate(oldPath, newPath, degree);
		return success(null);
	}

	private String check(String id, File file, String method) {
		String msg = null;
		if (file == null) { // 文件未找到
			msg = method + " failed: file(" + id + ") not be found.";
		} else if (file.getType() != FileType.IMAGE) { // 非图片
			msg = method + " failed: file(" + file.getId() + ") isn't a image.";
		}
		return msg;
	}

}
