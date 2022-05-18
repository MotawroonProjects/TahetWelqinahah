package com.lost_found_it.model;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private String id;
        private String terms_condition;
        private String privacy_policy;
        private String about;
        private String facebook;
        private String instagram;
        private String twitter;
        private String snapchat;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public String getTerms_condition() {
            return terms_condition;
        }

        public String getPrivacy_policy() {
            return privacy_policy;
        }

        public String getAbout() {
            return about;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getInstagram() {
            return instagram;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getSnapchat() {
            return snapchat;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
