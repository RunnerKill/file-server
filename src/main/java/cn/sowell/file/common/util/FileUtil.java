package cn.sowell.file.common.util;

import java.io.File;

//import org.junit.Test;

public class FileUtil {

	 /**
     * 文件/目录是否存在
     * @param path
     * @return
     */
    public static boolean exist(String path) {
        return new File(path).exists();
    }

    /**
     * 单位换算：byte -> 文件大小
     * @param bytes
     * @return
     */
    public static String getFileSizeString(long bytes) {
        int index = 0;
        double size = bytes;
        while(size > 1024) {
            index++;
            size /= 1024;
        }
        size = (double)Math.round(size * 10);
        String sizeStr = "";
        if(size % 10 == 0) {
            sizeStr = (int)size / 10 + "";
        } else {
            sizeStr = size / 10.0 + "";
        }
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB"}; // 单位
        return sizeStr + units[index];
    }

//    @Test
//    public void testname() throws Exception {
//        // System.out.println(FileUtil.getFileSizeString(5*1024*1024));
//        // System.out.println(FileUtil.getFileSizeString(23424324));
//        // System.out.println(FileUtil.getFileSizeString(243));
//    }

}
