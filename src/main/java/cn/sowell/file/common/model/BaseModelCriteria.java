package cn.sowell.file.common.model;

import java.util.List;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import cn.sowell.file.common.dao.Orderable;

/**
 * 参数实体类
 * @author Xiaojie.Xu
 */
public abstract class BaseModelCriteria implements Orderable {

    private Long createTime;

    private String createTimeStart;

    private String createTimeEnd;

    private Long updateTime;

    private String updateTimeStart;

    private String updateTimeEnd;

    private Integer curPage = 0;

    private Integer pageSize = 10;

    private String orderBy;

    private String[] ids;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTimeStart() {
        return updateTimeStart;
    }

    public void setUpdateTimeStart(String updateTimeStart) {
        this.updateTimeStart = updateTimeStart;
    }

    public String getUpdateTimeEnd() {
        return updateTimeEnd;
    }

    public void setUpdateTimeEnd(String updateTimeEnd) {
        this.updateTimeEnd = updateTimeEnd;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    /**
     * 获取分页/排序插件对象
     */
    public PageBounds getPageBounds() {
        List<Order> orders = Order.formString(orderBy);
        String[] orderArray = getOrderArray();
        if(orderArray != null) {
            for(Order order: orders) {
                String property = order.getProperty();
                if(property.matches("^\\d+$")) { // 非负整数
                    Integer index = Integer.valueOf(property);
                    if(index < orderArray.length) { // 在排序数组内
                        order.setProperty(orderArray[index]);
                    }
                }
            }
        }
        PageBounds page = new PageBounds();
        page.setOrders(orders);
        page.setPage(curPage);
        page.setLimit(pageSize);
        return page;
    }

}
