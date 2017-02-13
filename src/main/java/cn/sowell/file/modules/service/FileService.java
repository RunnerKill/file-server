package cn.sowell.file.modules.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.sowell.file.modules.model.File;
import cn.sowell.file.modules.result.UploadResult;

public interface FileService {

    String FILE_SEPARATOR = "$";

    File findById(String id);
    
    List<File> listByIds(String[] ids);
    
    String insert(File file);

    List<String> insert(List<File> files);
    
    int delete(String id, HttpServletRequest request);
    
    int delete(String[] ids, HttpServletRequest request);
    
    UploadResult upload(HttpServletRequest request) throws Exception;

}