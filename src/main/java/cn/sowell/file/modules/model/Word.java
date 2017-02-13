package cn.sowell.file.modules.model;

public class Word extends File {

    private static final long serialVersionUID = Word.class.hashCode();

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

    public Word() {
        super();
    }

    public Word(File file) {
        super(file);
    }
}
