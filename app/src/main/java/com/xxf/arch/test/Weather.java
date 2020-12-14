package com.xxf.arch.test;

/**
 * @Description: TODO @XGode
 * @Author: XGod
 * @CreateDate: 2020/12/14 18:36
 */
public class Weather {

    /**
     * status : 201
     * message : APP被用户自己禁用，请在控制台解禁
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
