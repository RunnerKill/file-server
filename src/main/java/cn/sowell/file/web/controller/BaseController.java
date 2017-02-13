package cn.sowell.file.web.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import cn.sowell.file.common.util.CacheUtil;
import cn.sowell.file.common.util.ProgressUtil;
import cn.sowell.file.modules.Constants;

/**
 * Controller层通用常量及方法
 * @author Xiaojie.Xu
 */
public abstract class BaseController implements Constants {

    protected static final Gson gsonUtil = new Gson();
    
    protected static final CacheUtil cacheUtil = CacheUtil.getInstance();
    
    protected static final ProgressUtil progressUtil = ProgressUtil.getInstance();

    protected String success(Object data) {
        return tracker(200, "success", data);
    }
    
    protected String failed(String msg) {
        return tracker(300, msg, null);
    }
    
    protected String error(String msg) {
        return tracker(500, msg, null);
    }
    
    private String tracker(int code, String msg, Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", code);
        result.put("msg", msg);
        result.put("data", data);
        return gsonUtil.toJson(result);
    }
    
}
