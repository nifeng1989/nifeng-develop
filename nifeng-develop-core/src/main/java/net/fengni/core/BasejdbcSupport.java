package net.fengni.core;

import com.sun.javafx.beans.annotations.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/4.
 */
public abstract class BasejdbcSupport<T> implements RowMapper<T>, InitializingBean {
    protected String tableName;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public BasejdbcSupport() {
    }
    public T getEntity(final int id){
        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from ").append(tableName).append("where id=?");
        List<T> list= jdbcTemplate.query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        }, this);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    protected int insert(final String sql){
        final KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                return pstmt;
            }
        }, key);
        return key.getKey().intValue();
    }

    public List<T> list(final List<PropertyFilter> propertyFilterList,@NonNull final Page<T> page){
        if(page == null){
            return null;
        }
        StringBuilder sql = new StringBuilder(128);
        StringBuilder countSql;
        StringBuilder condition = new StringBuilder();
        sql.append("select * from ").append(tableName).append("where 1=1 ");
        Map<String,Object> map = new HashMap<String,Object>();
        if(propertyFilterList != null) {
            for (PropertyFilter propertyFilter : propertyFilterList) {
                propertyFilter.buildCondition(condition, map);
            }
            sql.append(condition);
        }
        sql.append(" limit ").append(page.getFirst()).append(",").append(page.getPageSize());
        List<T> list = namedParameterJdbcTemplate.query(sql.toString(), map, this);
        //计算总共有多少页
        countSql = new StringBuilder(128);
        countSql.append("select count(1) from ").append(tableName).append("where 1=1 ");
        countSql.append(condition);
        int count = namedParameterJdbcTemplate.queryForObject(countSql.toString(), map, Integer.class);
        page.setTotalCount(count);
        page.setResult(list);
        return list;
    }

    public List<T> list(List<PropertyFilter> propertyFilterList){
        StringBuilder sql = new StringBuilder(128);
        sql.append("select * from ").append(tableName).append("where 1=1 ");
        Map<String,Object> map = new HashMap<String,Object>();
        if(propertyFilterList != null) {
            for (PropertyFilter propertyFilter : propertyFilterList) {
                propertyFilter.buildCondition(sql, map);
            }
        }
        List<T> list = namedParameterJdbcTemplate.query(sql.toString(), map, this);      ;
        return list;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
    public void init() {
    }   
}

