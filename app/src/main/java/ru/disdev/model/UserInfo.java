package ru.disdev.model;

/**
 * Created by DisDev on 24.07.2016.
 */
public class UserInfo {
    private String id;
    private String email;
    private String name;
    private long groupId;
    private boolean canEditThisGroup;
    private boolean canMakeNewPost;

    public UserInfo() {

    }

    void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public boolean isCanMakeNewPost() {
        return canMakeNewPost;
    }

    public void setCanMakeNewPost(boolean canMakeNewPost) {
        this.canMakeNewPost = canMakeNewPost;
    }

    public boolean isCanEditThisGroup() {
        return canEditThisGroup;
    }

    public void setCanEditThisGroup(boolean canEditThisGroup) {
        this.canEditThisGroup = canEditThisGroup;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        UserInfo user = new UserInfo();

        public Builder setId(String id) {
            user.setId(id);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setName(String name) {
            user.setName(name);
            return this;
        }

        public Builder setGroupId(long groupId) {
            user.setGroupId(groupId);
            return this;
        }

        public Builder setCanEditThisGroup(boolean canEditThisGroup) {
            user.setCanEditThisGroup(canEditThisGroup);
            return this;
        }

        public Builder setCanMakeNewPost(boolean canMakeNewPost) {
            user.setCanMakeNewPost(canMakeNewPost);
            return this;
        }

        public UserInfo build() {
            return user;
        }
    }
}
