package cn.sowell.file.common.util;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

public class ImageUtil {

    /**
     * 缩放图片
     * @param oldPath 原图
     * @param newPath 新图
     * @param width 缩放后宽
     * @param height 缩放后高
     * @throws IOException
     */
    public static void scale(String oldPath, String newPath, int width, int height) throws IOException {
        BufferedImage image = ImageIO.read(new File(oldPath));
        int type = image.getColorModel().getTransparency();
        BufferedImage img = new BufferedImage(width, height, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
        graphics2d.dispose();
        ImageIO.write(img, getFormatName(oldPath), new File(newPath));
    }

    /**
     * 裁剪图片
     * @param oldPath 原图
     * @param newPath 新图
     * @param x 左上角横坐标
     * @param y 左上角纵坐标
     * @param width 矩形宽
     * @param height 矩形高
     * @throws IOException
     */
    public static void cut(String oldPath, String newPath, int x, int y, int width, int height) throws IOException {
        FileInputStream is = new FileInputStream(oldPath);
        String formatName = getFormatName(oldPath);
        Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(formatName);
        ImageReader reader = it.next();
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        ImageIO.write(bi, formatName, new File(newPath));
    }

    /**
     * 旋转图片
     * @param oldPath 原图
     * @param newPath 新图
     * @param degree 旋转角度
     * @return
     */
    public static void rotate(String oldPath, String newPath, int degree) throws IOException {
        BufferedImage image = ImageIO.read(new File(oldPath));
        int w = image.getWidth();
        int h = image.getHeight();
        int type = image.getColorModel().getTransparency();
        BufferedImage img = new BufferedImage(w, h, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(image, 0, 0, null);
        graphics2d.dispose();
        ImageIO.write(img, getFormatName(oldPath), new File(newPath));
    }

    /**
     * 获取图片文件的formatName
     * @param path 图片路径
     * @return formatName
     * @throws IOException
     */
    public static String getFormatName(String path) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new File(path));
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if(!iter.hasNext()) {
            return null;
        }
        ImageReader reader = iter.next();
        iis.close();
        String formatName = reader.getFormatName();
        return formatName == null ? "png" : formatName; // 默认按png格式
    }

    /**
     * 获取图片的exif信息中的方向tag
     * @param path 图片路径
     * @return 方向标签内容
     * @throws IOException
     */
    public static String getOrientation(String path) throws IOException {
        File f = new File(path);
        Metadata metadata = null;
        try {
            metadata = JpegMetadataReader.readMetadata(f);
        } catch(JpegProcessingException e) {
            return null;
        }
        Directory exif = metadata.getDirectory(ExifIFD0Directory.class);
        if(exif == null || !exif.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) { // 检查是否Tag中包含了方向
            return null;
        }
        return exif.getDescription(ExifIFD0Directory.TAG_ORIENTATION);
    }


    /**
     * 获取网络图片
     * @param url 网络请求地址
     * @param path 保存至本地的路径
     * @return 成功或失败
     * @throws Exception
     */
    public static boolean getWebImg(String url, String path) throws Exception {
        HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) { // 连接失败
            return false;
        }
        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        File file = new File(path.substring(0, path.lastIndexOf("/")));
        if(!file.exists())
            file.mkdirs();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        bis.close();
        return true;
    }

}
