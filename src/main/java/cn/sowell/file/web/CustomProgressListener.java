package cn.sowell.file.web;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.util.StringUtils;

import cn.sowell.file.common.util.ProgressUtil;
import cn.sowell.file.modules.Constants;

public class CustomProgressListener implements ProgressListener, Constants {
    
    ProgressUtil progressUtil = ProgressUtil.getInstance();

    private String skey;

    public CustomProgressListener(String skey) {
        super();
        this.skey = skey;
        if(!StringUtils.hasText(skey)) return;
        progressUtil.put(skey, new CustomProgress());
    }

    public void update(long bytesRead, long contentLength, int items) {
        if(!StringUtils.hasText(skey)) return;
        CustomProgress progress = progressUtil.get(skey);
        progress.setBytesRead(bytesRead);
        progress.setContentLength(contentLength);
        progress.setItems(items);
    }

}
