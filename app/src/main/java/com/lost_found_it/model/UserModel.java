package com.lost_found_it.model;

import java.io.Serializable;

public class UserModel extends StatusResponse {
    private Data data;
    public static class Data implements Serializable{
        private User user;
        private String access_token;
        private String firebase_token;

        public User getUser() {
            return user;
        }

        public String getAccess_token() {
            return access_token;
        }

        public String getFirebase_token() {
            return firebase_token;
        }

        public void setFirebase_token(String firebase_token) {
            this.firebase_token = firebase_token;
        }
    }

    public static class User implements Serializable{
        private String id;
        private String first_name;
        private String last_name;
        private String phone_code;
        private String phone;
        private String email;
        private String image;

        public String getId() {
            return id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getImage() {
            return image;
        }
    }

    public Data getData() {
        return data;
    }
}
