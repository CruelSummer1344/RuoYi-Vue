package com.tour.web.controller.system;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalTime;

@MappedTypes(LocalTime.class)
@MappedJdbcTypes(JdbcType.TIME)
public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    LocalTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTime(i, Time.valueOf(parameter));
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) 
            throws SQLException {
        Time time = rs.getTime(columnName);
        return time != null ? time.toLocalTime() : null;
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Time time = rs.getTime(columnIndex);
        return time != null ? time.toLocalTime() : null;
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Time time = cs.getTime(columnIndex);
        return time != null ? time.toLocalTime() : null;
    }
}