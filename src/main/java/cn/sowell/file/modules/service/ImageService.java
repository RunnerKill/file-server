package cn.sowell.file.modules.service;

import java.io.IOException;

import cn.sowell.file.modules.model.Image;

public interface ImageService {
    boolean scale(Image image, String dir, int width, int height) throws IOException;
}
