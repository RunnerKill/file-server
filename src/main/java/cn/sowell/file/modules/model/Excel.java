package cn.sowell.file.modules.model;

public class Excel extends File {

    private static final long serialVersionUID = Excel.class.hashCode();

    private String previewPath;

    private String version;

    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Excel() {
        super();
    }

    public Excel(File file) {
        super(file);
    }
}
