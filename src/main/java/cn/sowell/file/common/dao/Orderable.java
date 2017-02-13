package cn.sowell.file.common.dao;


/**
 * 快速排序接口，支持orderBy属性的0.desc、1.asc等方式
 * @author Xiaojie.Xu
 */
public interface Orderable {
    public String[] getOrderArray();
}
