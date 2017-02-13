package cn.sowell.file.common.dao.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

import cn.sowell.file.common.util.DateFormatUtil;



/**
 * mySQL DECIMAL字段映射java的Long类型时日期格式转换
 * @author Xiaojie.Xu
 */
public class DecimalHandler implements TypeHandler<String> {

    // 指定日期格式
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";

    private static DateFormatUtil dateFormatUtil = new DateFormatUtil(DEFAULT_PATTERN);

    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (!StringUtils.hasText(parameter)||DEFAULT_PATTERN.equalsIgnoreCase(dateFormatUtil.getDateFormatMsg(parameter))) {
            ps.setLong(i, dateFormatUtil.toLong(parameter));
        } else {
            ps.setLong(i, (new DateFormatUtil(dateFormatUtil.getDateFormatMsg(parameter))).toLong(parameter));
        }
//        Long time = dateFormatUtil.toLong(parameter);
//        ps.setLong(i, time);
    }

    public String getResult(ResultSet rs, String columnName) throws SQLException {
        Long time = rs.getLong(columnName);
        return dateFormatUtil.toString(time);
    }

    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        Long time = rs.getLong(columnIndex);
        return dateFormatUtil.toString(time);
    }

    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Long time = cs.getLong(columnIndex);
        return dateFormatUtil.toString(time);
    }

}
