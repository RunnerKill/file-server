package cn.sowell.file.modules.dao;


import org.springframework.stereotype.Repository;

import cn.sowell.file.modules.model.Word;

@Repository
public interface WordDao {

    int insert(Word criteria);

}
