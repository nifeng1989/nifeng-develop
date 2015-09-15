package com.nifeng.core.user.config;

/**
 * Created by Administrator on 2015/9/3.
 */
public class UserConfig {
    public enum  UserGrade{
        bad(-1,"bad"),
        normal(0,"nomal"),
        good(1,"good"),
        better(2,"better"),
        best(3,"best");
        private int value;
        private String desc;
        UserGrade(int value,String desc){
            this.value=value;
            this.desc=desc;
        }
        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
