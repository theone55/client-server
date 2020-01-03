package com.justfors.protocol;

import java.time.Instant;

public class TransferData {

    private Instant createDate;
    private String data;
    private Status status;
    private String user;
    private String token;
    private Boolean isAuthorized;

    public Instant getCreateDate() {
        return createDate;
    }

    public String getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public Boolean getAuthorized() {
        return isAuthorized;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuthorized(Boolean authorized) {
        isAuthorized = authorized;
    }

    @Override
    public String toString() {
        return "TransferData{" +
                "createDate=" + createDate +
                ", data='" + data + '\'' +
                ", status='" + status + '\'' +
                ", user='" + user + '\'' +
                ", token='" + token + '\'' +
                ", isAuthorized=" + isAuthorized +
                '}';
    }

}
