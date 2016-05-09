package net.fengni.core.user.model;

import net.fengni.core.anotation.Column;
import net.fengni.core.anotation.Entity;
import net.fengni.core.anotation.Id;

/**
 * Created by Administrator on 2015/9/3.
 */
@Entity("p_user")
public class User {
    @Id
    @Column
    private int aid;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private int grade;//用户级别

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
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
