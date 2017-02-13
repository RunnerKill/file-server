package cn.sowell.file.modules.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.sowell.file.modules.model.File;

@Repository
public interface FileDao {
    
    File getOne(String id);
    
    List<File> getListByIds(String[] ids);

    int insert(File file);
    
    int insertMultiply(File[] files);
    
    int delete(String id);
    
    int deleteMultiply(String[] ids);

}
