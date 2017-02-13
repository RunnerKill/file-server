package cn.sowell.file.modules.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.sowell.file.common.util.DateFormatUtil;
import cn.sowell.file.modules.Constants;
import cn.sowell.file.modules.enums.Command;
import cn.sowell.file.modules.enums.FileType;
import cn.sowell.file.modules.model.base.BaseFile;

/**
 * 用于Cache/DB中存储
 * @author Xiaojie.Xu
 */
public class File extends BaseFile implements Serializable, Constants {

    private static final long serialVersionUID = File.class.hashCode();

    private String encodeName;

    private String webPath;
    
    private Command command = Command.NONE;

    private transient FileType type;

    public String getEncodeName() {
        return encodeName;
    }

    public void setEncodeName(String encodeName) {
        this.encodeName = encodeName;
    }

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public Command getCommand() {
        return command;
    }
    
    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public void setName(String name) {
        try {
            setEncodeName(URLEncoder.encode(name, "UTF-8"));
        } catch(UnsupportedEncodingException e) {
            System.err.println("file name encode error!");
        }
        super.setName(name);
    }

    @Override
    public void setPath(String path) {
        setWebPath(ROOT_DIR + "/" + path);
        super.setPath(path);
    }

    @Override
    public void setExt(String ext) {
        FileType ft = FileType.getType(ext);
        setType(ft);
        setFtype(ft.name);
        super.setExt(ext);
    }

    public String getUploadTimeStr() {
        DateFormatUtil dfu = new DateFormatUtil(DATE_PATTERN);
        return dfu.toString(getUploadTime());
    }
    
    public String toHtml() {
        String prefix = "&nbsp;&nbsp;";
        String suffix = "<br/>";
        return "{" + suffix +
         prefix + "id : " + getId() + ", " + suffix +
         prefix + "project : " + getProject() + ", " + suffix +
         prefix + "module : " + getModule() + ", " + suffix +
         prefix + "name : " + getName() + ", " + suffix +
         prefix + "ext : " + getExt() + ", " + suffix +
         prefix + "size : " + getSize() + ", " + suffix +
         prefix + "path : " + getPath() + ", " + suffix +
         prefix + "uploadTime : " + getUploadTimeStr() + suffix +
         prefix + "command : " + getCommand() + suffix +
        "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // 1.检测是否引用自同一对象
        if(this == obj)
            return true;
        // 2.检测otherObj是否为null
        if(obj == null)
            return false;
        // 3.比较this与otherObj是否属于同一个类
        if(getClass() != obj.getClass())
            return false;
        // 3.1.如果所有子类都拥有统一的语义，则使用instanceof
        if(!(obj instanceof File))
            return false;
        // 4.将otherObj转换为相应的类类型变量
        File other = (File)obj;
        // 5.比较对象中的所有域
        return getId().equals(other.getId());
    }

    public File() {
        super();
    }

    protected File(File file) { // 按base类中属性重新构造
        super();
        this.setCommand(file.getCommand());
        this.setId(file.getId());
        this.setName(file.getName());
        this.setExt(file.getExt());
        this.setFtype(file.getFtype());
        this.setRemarks(file.getRemarks());
        this.setSize(file.getSize());
        this.setPath(file.getPath());
        this.setProject(file.getProject());
        this.setModule(file.getModule());
        this.setUploadTime(file.getUploadTime());
        this.setCreateTime(file.getCreateTime());
        this.setUpdateTime(file.getUpdateTime());
    }
}
