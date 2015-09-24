package com.nifeng.core.user.model;

import com.nifeng.core.anotation.Column;
import com.nifeng.core.anotation.Entity;
import com.nifeng.core.anotation.Id;

/**
 * Created by Administrator on 2015/9/3.
 */
@Entity("p_user")
public class User {
    @Id
    @Column
    private int id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private int grade;//用户级别

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
