package cn.sowell.file.modules.result;

import java.util.List;

import cn.sowell.file.modules.model.File;

public class SyncResult extends Result {

    private List<File> insertFiles;

    private List<File> deleteFiles;

    public List<File> getInsertFiles() {
        return insertFiles;
    }

    public void setInsertFiles(List<File> insertFiles) {
        this.insertFiles = insertFiles;
    }

    public List<File> getDeleteFiles() {
        return deleteFiles;
    }

    public void setDeleteFiles(List<File> deleteFiles) {
        this.deleteFiles = deleteFiles;
    }
}
