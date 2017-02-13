package cn.sowell.file.modules.result;

import com.google.gson.Gson;

/**
 * 返回结果
 * @author Xiaojie.Xu
 */
public abstract class Result {

    protected Gson gsonUtil = new Gson();

    public String toJson() {
        return gsonUtil.toJson(this);
    }

}
