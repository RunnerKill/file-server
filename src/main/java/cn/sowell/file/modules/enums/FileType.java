package cn.sowell.file.modules.enums;

import java.util.Arrays;

import com.google.gson.Gson;

public enum FileType {

    /**
     * 图片
     */
    IMAGE("image", new String[]{"jpg", "png", "gif", "jpeg", "bmp"}),
    
    /**
     * word文件
     */
    WORD("word", new String[]{"doc", "docx", "rtf"}),
    
    /**
     * excel文件
     */
    EXCEL("excel", new String[]{"xls", "xlsx", "rtf"}),
    
    /**
     * 音频
     */
    AUDIO("audio", new String[]{"mp3"}),
    
    /**
     * 视频
     */
    VIDEO("video", new String[]{"mp4"}),
    
    /**
     * 其它文件
     */
    OTHER("other", new String[]{});
    
    public String name;

    private String[] suffixs;

    private FileType(String name, String[] suffixs) {
        this.name = name;
        this.suffixs = suffixs;
    }
    
    /**
     * 获取文件类型
     * @param ext 文件扩展名
     * @return
     */
    public static FileType getType(String ext) {
        for(FileType t : FileType.values()) {
            if(t != FileType.OTHER && Arrays.asList(t.suffixs).contains(ext)) {
                return t;
            }
        }
        return FileType.OTHER;
    }
    
    /**
     * 枚举转json串
     * @return json对象字符串
     */
    public static String toJson() {
        String jsonstr = "";
        Gson gson = new Gson();
        for(FileType t : FileType.values()) {
            jsonstr += ",\"" + t.name + "\":" + gson.toJson(t.suffixs);
        }
        return jsonstr.length() > 0 ? "{" + jsonstr.substring(1) + "}" : "{}";
    }
}
