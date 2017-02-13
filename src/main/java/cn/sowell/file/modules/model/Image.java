package cn.sowell.file.modules.model;

public class Image extends File {

    private static final long serialVersionUID = Image.class.hashCode();

    private int width;

    private int height;

    private String orientation;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public Image() {
        super();
    }

    public Image(File file) {
        super(file);
    }

}
