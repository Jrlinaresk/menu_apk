package com.palgao.menu.modules.Notifications.entity;

// Notification.java
public class Notification {

    private String _id;
    private String userId;
    private String title;
    private String content;
    private String icon;
    private String type;
    private String group;
    private String status;
    private String createdAt;

    public Notification() {
    }

    public Notification(String _id, String userId, String title, String content, String icon, String type, String group, String status, String createdAt) {
        this._id = _id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.type = type;
        this.group = group;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
