package cn.sowell.file.modules.dao;


import org.springframework.stereotype.Repository;

import cn.sowell.file.modules.model.Image;

@Repository
public interface ImageDao {

    int insert(Image criteria);
    
}
