package cn.sowell.file.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.sowell.file.modules.model.File;

/**
 * 自定义缓存类
 * @author Xiaojie.Xu
 */
public class CacheUtil {
    
    private static final long VALID_TIME = 24 * 60 * 60 * 1000; // 有效时长，默认1天

    private Map<String, List<File>> cache = new HashMap<String, List<File>>();

    private static CacheUtil cacheUtil = new CacheUtil();

    private CacheUtil() {}

    public static CacheUtil getInstance() {
        return cacheUtil;
    }
    
    public List<File> get(String key) {
        return cache.containsKey(key) ? cache.get(key) : new ArrayList<File>();
    }
    
    public File get(String key, String id) {
        List<File> list = get(key);
        for(File f: list) {
            if(f.getId().equals(id)) return f;
        }
        return null;
    }
    
    public boolean put(final String key, List<File> files) {
        if(files == null || files.size() < 1) return false;
        if(!cache.containsKey(key)) {
            cache.put(key, new ArrayList<File>());
            new Timer().schedule(new TimerTask() { // define schedule task
                @Override
                public void run() {
                    clear(key);
                }
            }, VALID_TIME);
        }
        return cache.get(key).addAll(files);
    }
    
    public File remove(String key, String id) {
        List<File> list = get(key);
        Iterator<File> it = list.iterator();
        while(it.hasNext()) {
            File file = it.next();
            if(file.getId().equals(id)) {
                it.remove();
                return file;
            }
        }
        return null;
    }
    
    public boolean exist(String key) {
        return cache.containsKey(key);
    }

    public void clear(String key) {
        cache.remove(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public String toHtml(String key) {
        String html = ">>>" + key + "====================<br/>";
        for(File file : get(key)) {
            html += file.toHtml() + "<br/>";
        }
        return html;
    }
    
    public String toHtml() {
        String html = "";
        for(String key : cache.keySet()) {
            html += toHtml(key);
        }
        return html;
    }
}
