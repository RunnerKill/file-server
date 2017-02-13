package cn.sowell.file.web;

import com.google.gson.Gson;

/**
 * 上传进度类
 * @author Xiaojie.Xu
 */
public class CustomProgress {

    /**
     * bytesRead 到目前为止读取文件的比特数
     */
    private long bytesRead;

    /**
     * contentLength 文件总大小
     */
    private long contentLength;

    /**
     * items 目前正在读取第几个文件
     */
    private int items;

    public long getBytesRead() {
        return bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public int getItems() {
        return items;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
