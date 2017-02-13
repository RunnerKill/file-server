package cn.sowell.file.modules.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.sowell.file.common.util.FileUtil;
import cn.sowell.file.common.util.ImageUtil;
import cn.sowell.file.common.util.TransHtmlUtil;
import cn.sowell.file.common.util.UrlManegerUtil;
import cn.sowell.file.modules.Constants;
import cn.sowell.file.modules.dao.ExcelDao;
import cn.sowell.file.modules.dao.FileDao;
import cn.sowell.file.modules.dao.ImageDao;
import cn.sowell.file.modules.dao.WordDao;
import cn.sowell.file.modules.enums.Command;
import cn.sowell.file.modules.enums.FileType;
import cn.sowell.file.modules.model.Excel;
import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.model.Image;
import cn.sowell.file.modules.model.Word;
import cn.sowell.file.modules.result.UploadResult;
import cn.sowell.file.modules.service.FileService;

/**
 * 业务层
 * @author Xiaojie.Xu
 */
@Service
public class FileServiceImpl implements FileService, Constants {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private WordDao wordDao;

    @Autowired
    private ExcelDao excelDao;

    @Autowired
    private ImageDao imageDao;

    public File findById(String id) {
        return fileDao.getOne(id);
    }
    
    public List<File> listByIds(String[] ids) {
        if(ids.length < 1) return new ArrayList<File>();
        return fileDao.getListByIds(ids);
    }
    
    public String insert(File file) {
        file.setCreateTime(System.currentTimeMillis());
        file.setUpdateTime(System.currentTimeMillis());
        fileDao.insert(file);
        insertExtra(file);
        return file.getId();
    }

    public List<String> insert(List<File> files) {
        List<String> ids = new ArrayList<String>();
        if(files.size() < 1) return ids;
        for(File file: files) {
            file.setCreateTime(System.currentTimeMillis());
            file.setUpdateTime(System.currentTimeMillis());
            ids.add(file.getId());
            insertExtra(file);
        }
        fileDao.insertMultiply(files.toArray(new File[0]));
        return ids;
    }
    
    private void insertExtra(File file) {
        FileType type = file.getType();
        if(type == FileType.IMAGE) {
            imageDao.insert((Image)file);
        } else if(type == FileType.EXCEL) {
            excelDao.insert((Excel)file);
        } else if(type == FileType.WORD) {
            wordDao.insert((Word)file);
        }
    }
    
    public int delete(String id, HttpServletRequest request) {
        deleteDisk(id, request);
        return fileDao.delete(id);
    }

    public int delete(String[] ids, HttpServletRequest request) {
        if(ids.length < 1) return 0;
        for(String id : ids) {
            deleteDisk(id, request);
        }
        return fileDao.deleteMultiply(ids);
    }

    private boolean deleteDisk(String id, HttpServletRequest request) {
        if(request == null) return false;
        String fileDir = UrlManegerUtil.getServerPath(request, ROOT_DIR);
        File file = findById(id);
        if(file == null) return false;
        return FileUtils.deleteQuietly(new java.io.File(fileDir + file.getPath()));
    }

    public UploadResult upload(HttpServletRequest request) throws Exception {
        UploadResult result = new UploadResult(); // 返回结果
        List<File> files = new ArrayList<File>(); // 上传的文件
        String baseDir = UrlManegerUtil.getServerPath(request, Constants.ROOT_DIR);
        // 获取request中的普通表单项目或文本参数
        Map<String, Object> map = toMap(request);
        result.setValues(map);
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        // 获得文件
        List<MultipartFile> fileList = multipartRequest.getFiles("file");
        for(MultipartFile multipartFile: fileList) {
            // 创建文件目录
            Calendar c = Calendar.getInstance();
            String fileDir = String.format("%s/%04d%02d/", cast(map.get("project")), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
            String dirPath = baseDir + fileDir;
            java.io.File d = new java.io.File(dirPath);
            if(!d.exists())
                d.mkdirs();
            // 获取文件名、后缀
            String name = multipartFile.getOriginalFilename();
            String ext = null;
            name = name.substring(name.indexOf(FILE_SEPARATOR) + 1);
            int pos = name.lastIndexOf(".");
            if(pos > 0) {
                ext = name.substring(pos + 1, name.length()).toLowerCase();
                name = name.substring(0, pos);
            }
            String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
            String fileName = uuid + "." + ext;
            // 转存
            java.io.File uploadedFile = new java.io.File(dirPath + fileName);
            multipartFile.transferTo(uploadedFile);
            // 创建File对象
            File file = new File();
            file.setCommand(Command.INSERT);
            file.setId(uuid);
            file.setName(name);
            file.setExt(ext);
            file.setRemarks(cast(map.get("remarks")));
            file.setSize(FileUtil.getFileSizeString(multipartFile.getSize()));
            file.setPath(fileDir + fileName);
            file.setUploadTime(System.currentTimeMillis());
            file.setProject(cast(map.get("project")));
            file.setModule(cast(map.get("module")));
            files.add(createFile(baseDir, file));
        }
        result.setFiles(files);
        return result;
    }

    private static String cast(Object o) {
        return o == null ? null : o.toString();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(HttpServletRequest request) {
        Map<String, Object> paramMap = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        for(String key: paramMap.keySet()) {
            Object value = paramMap.get(key);
            if(value == null || value.equals("")) {
                value = "";
            } else if(value instanceof String[]) {
                String[] values = (String[])value;
                String tmpstr = "";
                for(int i = 0; i < values.length; i++) {
                    tmpstr = "," + values[i];
                }
                value = tmpstr.length() > 0 ? tmpstr.substring(1) : "";
            } else {
                value = value.toString();
            }
            returnMap.put(key, value);
        }
        return returnMap;
    }

    private static File createFile(String fileDir, File file) throws IOException {
        FileType type = file.getType();
        if(type == FileType.IMAGE) {
            String path = fileDir + file.getPath();
            java.awt.Image i = ImageIO.read(new java.io.File(path));
            Image image = new Image(file);
            image.setWidth(i.getWidth(null));
            image.setHeight(i.getHeight(null));
            image.setOrientation(ImageUtil.getOrientation(path));
            return image;
        }
        if(type == FileType.WORD) {
            String previewPath = Constants.ROOT_PREVIEW_DIR + "/" + TransHtmlUtil.getHtmlName(file.getPath());
            String version = file.getExt().equals("docx") ? "2007" : "2003";
            Word word = new Word(file);
            word.setPreviewPath(previewPath);
            word.setVersion(version);
            return word;
        }
        if(type == FileType.EXCEL) {
            String previewPath = Constants.ROOT_PREVIEW_DIR + "/" + TransHtmlUtil.getHtmlName(file.getPath());
            String version = file.getExt().equals("xlsx") ? "2007" : "2003";
            Excel excel = new Excel(file);
            excel.setPreviewPath(previewPath);
            excel.setVersion(version);
            return excel;
        }
        return file;
    }

}
