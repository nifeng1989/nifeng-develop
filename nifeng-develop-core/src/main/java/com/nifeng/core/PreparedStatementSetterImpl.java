package com.nifeng.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.rowset.serial.SerialArray;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2015/9/4.
 */
public class PreparedStatementSetterImpl implements PreparedStatementSetter {
    private List<PropertyFilter> propertyFilterList;
    private Page page;
    PreparedStatementSetterImpl(List<PropertyFilter> propertyFilterList,Page page){
        this.propertyFilterList = propertyFilterList;
        this.page = page;
    }
    @Override
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        int index=1;
        for(int i=0;i<propertyFilterList.size();i++){
            PropertyFilter propertyFilter = propertyFilterList.get(i);
            Object value = propertyFilter.getValue();
            if(value instanceof Integer){
                preparedStatement.setInt(index,(Integer) value);
            }else if (value instanceof Long){
                preparedStatement.setLong(index,(Long) value);
            }else if (value instanceof Float){
                preparedStatement.setFloat(index,(Float) value);
            }else if (value instanceof Double){
                preparedStatement.setDouble(index,(Double) value);
            }else if (value instanceof Date){
                preparedStatement.setDate(index,(java.sql.Date) value);
            }else if (value instanceof String){
                if(propertyFilter.getMatchType() == PropertyFilter.MatchType.IS){
                    continue;
                }else if(propertyFilter.getMatchType() == PropertyFilter.MatchType.IN ||
                        propertyFilter.getMatchType() == PropertyFilter.MatchType.NOT_IN){
                    String [] values = StringUtils.split((String) value, ",");
                    preparedStatement.setObject(index,value);

                    MapSqlParameterSource parems = new MapSqlParameterSource();
                }

                preparedStatement.setString(index,(String) value);
            }
            index++;
        }
    }
}
