package cn.sowell.file.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.sowell.file.web.CustomProgress;

/**
 * 进度缓存
 * @author Xiaojie.Xu
 */
public class ProgressUtil {
    
    private static final long VALID_TIME = 24 * 60 * 60 * 1000; // 有效时长，默认1天
    
    private Map<String, CustomProgress> cache = new HashMap<String, CustomProgress>();

    private static ProgressUtil progressUtil = new ProgressUtil();

    private ProgressUtil() {}

    public static ProgressUtil getInstance() {
        return progressUtil;
    }
    
    public void put(final String key, CustomProgress progress) {
        cache.put(key, progress);
        new Timer().schedule(new TimerTask() { // define schedule task
            @Override
            public void run() {
                cache.remove(key);
            }
        }, VALID_TIME);
    }
    
    public CustomProgress get(String key) {
        return cache.get(key);
    }

}
