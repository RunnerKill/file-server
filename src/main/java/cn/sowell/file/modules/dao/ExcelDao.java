package cn.sowell.file.modules.dao;


import org.springframework.stereotype.Repository;

import cn.sowell.file.modules.model.Excel;

@Repository
public interface ExcelDao {

    int insert(Excel criteria);
    
}
