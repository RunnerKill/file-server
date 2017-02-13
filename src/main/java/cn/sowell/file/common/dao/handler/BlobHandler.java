package cn.sowell.file.common.dao.handler;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * mySQL BLOB字段映射java的String时作编码转换
 * @author Xiaojie.Xu
 */
public class BlobHandler extends BaseTypeHandler<String> {

    // 指定字符集
    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis;
        try {
            // 把String转化成byte流
            byte[] bytes = parameter.getBytes(DEFAULT_CHARSET);
            bis = new ByteArrayInputStream(bytes);
            ps.setBinaryStream(i, bis, bytes.length);
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if(null != blob) {
            returnValue = blob.getBytes(1, (int)blob.length());
            try {
                // 把byte转化成string
                return new String(returnValue, DEFAULT_CHARSET);
            } catch(UnsupportedEncodingException e) {
                throw new RuntimeException("Blob Encoding Error!");
            }
        }
        return null;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Blob blob = rs.getBlob(columnIndex);
        byte[] returnValue = null;
        if(null != blob) {
            returnValue = blob.getBytes(1, (int)blob.length());
            try {
                // 把byte转化成string
                return new String(returnValue, DEFAULT_CHARSET);
            } catch(UnsupportedEncodingException e) {
                throw new RuntimeException("Blob Encoding Error!");
            }
        }
        return null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        if(null != blob) {
            returnValue = blob.getBytes(1, (int)blob.length());
            try {
                return new String(returnValue, DEFAULT_CHARSET);
            } catch(UnsupportedEncodingException e) {
                throw new RuntimeException("Blob Encoding Error!");
            }
        }
        return null;
    }

}
