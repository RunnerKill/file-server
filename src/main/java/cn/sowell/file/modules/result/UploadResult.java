package cn.sowell.file.modules.result;

import java.util.List;
import java.util.Map;

import cn.sowell.file.modules.model.File;

public class UploadResult extends Result {

    private List<File> files;

    private Map<String, Object> values;

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

}
