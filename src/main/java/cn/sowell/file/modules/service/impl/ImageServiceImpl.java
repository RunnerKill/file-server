package cn.sowell.file.modules.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import cn.sowell.file.common.util.ImageUtil;
import cn.sowell.file.modules.model.Image;
import cn.sowell.file.modules.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    public boolean scale(Image image, String dir, int width, int height) throws IOException {
        int tw, th; // 目标(target)宽高
        int ow = image.getWidth(), oh = image.getHeight(); // 原(origin)宽高
        if(width > 0 && height > 0) { // 不等比
            tw = width; th = height;
        } else if(width > 0) { // 按宽
            tw = width; th = oh * width / ow;
        } else if(height > 0) { // 按高
            tw = ow * height / oh; th = height;
        } else { // 不缩放
            return false;
        }
        String path = image.getPath();
        int pos = path.lastIndexOf(".");
        String oldPath = dir + path;
        String newPath = dir + path.substring(0, pos) + "-scale(w" + width + "_h" + height + ")" + path.substring(pos);
        ImageUtil.scale(oldPath, newPath, tw, th);
        return true;
    }

}
