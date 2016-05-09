package net.fengni.core.dto;

/**
 * Created by fengni on 2015/9/24.
 */
public class FieldTable {
    private String field;
    private String table;

    public FieldTable(String field, String table) {
        this.field = field;
        this.table = table;
    }

    public FieldTable() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
