package com.xxf.blockcanary.internal;

public class CpuInfo {
    private String cpu;
    private String app;
    private String user;
    private String system;
    private String ioWait;


    public CpuInfo() {
    }

    public CpuInfo(String app) {
        this.app = app;
    }

    public CpuInfo(String cpu, String app, String user, String system, String ioWait) {
        this.cpu = cpu;
        this.app = app;
        this.user = user;
        this.system = system;
        this.ioWait = ioWait;
    }


    public String getCpu() {
        return cpu;
    }

    public String getApp() {
        return app;
    }

    public String getUser() {
        return user;
    }

    public String getSystem() {
        return system;
    }

    public String getIoWait() {
        return ioWait;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (cpu != null) {
            stringBuilder.append("cpu:").append(cpu).append("% ");
        }
        if (app != null) {
            stringBuilder.append("app:").append(app).append("% ");
        }
        if (user != null) {
            stringBuilder.append("user:").append(user).append("% ");
        }
        if (system != null) {
            stringBuilder.append("system:").append(system).append("% ");
        }
        if (ioWait != null) {
            stringBuilder.append("ioWait:").append(ioWait).append("% ");
        }
        return stringBuilder.toString();
    }
}
