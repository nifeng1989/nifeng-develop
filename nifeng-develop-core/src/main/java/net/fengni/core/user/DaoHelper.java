package net.fengni.core.user;

import net.fengni.core.CommonUtil;
import net.fengni.core.PackageScanner;
import net.fengni.core.anotation.Column;
import net.fengni.core.anotation.Entity;
import net.fengni.core.anotation.Id;
import net.fengni.core.dto.FieldTable;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by fengni on 2015/9/24.
 */
public class DaoHelper {
    public static Map<String,String> entityMap = new HashMap<String, String>();
    public static Map<String,String> insertMap = new HashMap<String, String>();
    public static Map<String,String> updataMap = new HashMap<String, String>();
    public static Map<String,List<FieldTable>> map = new HashMap<String, List<FieldTable>>();
    public static Map<String,String> primaryKey = new HashMap<String, String>();
    public  void build(List<String> packages) {
        Set<Class<?>> classSet = PackageScanner.getClasses(Entity.class, packages);  //得到目标包下所有的action类
        try {
            for (Class cls : classSet) {
                Entity entity =  (Entity)cls.getAnnotation(Entity.class);
                String table = entity.value();
                if(CommonUtil.EMPTY_STRING.equals(table)){
                    table = cls.getSimpleName();
                }
                entityMap.put(cls.getName(), table);
                Field [] fields =  cls.getDeclaredFields();
                List<FieldTable> fieldList = new ArrayList<FieldTable>();
                StringBuilder insertSb = new StringBuilder();
                StringBuilder values = new StringBuilder();
                insertSb.append("insert into ").append(entityMap.get(cls.getName())).append("(");
                StringBuilder updateSb = new StringBuilder();
                StringBuilder conditionSb = new StringBuilder();
                updateSb.append("update ").append(table).append(" set ");
                for(Field field:fields){
                    if(field.isAnnotationPresent(Column.class)){
                        Column column = field.getAnnotation(Column.class);
                        String columnName = column.name();
                        if(CommonUtil.EMPTY_STRING.equals(columnName)){
                            columnName = field.getName();
                        }
                        fieldList.add(new FieldTable(field.getName(),field.getName()));
                        if(field.isAnnotationPresent(Id.class)){
                            primaryKey.put(cls.getName(), field.getName());
                            conditionSb.append(" where ").append(columnName).append("=:").append(columnName);
                        }else {
                            insertSb.append(columnName).append(",");
                            values.append(":").append(columnName).append(",");
                            updateSb.append(columnName).append("=:").append(columnName).append(",");
                        }
                    }
                }
                insertSb.delete(insertSb.length() - 1, insertSb.length());
                values.delete(values.length()-1,values.length());
                insertSb.append(") values (");
                insertSb.append(values).append(")");
                insertMap.put(cls.getName(), insertSb.toString());
                updateSb.delete(updateSb.length()-1,updateSb.length());
                updateSb.append(conditionSb);
                updataMap.put(cls.getName(), updateSb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
