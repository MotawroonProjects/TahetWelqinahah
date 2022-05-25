package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class MessagesDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private String id;
        private String ad_id;
        private List<MessageModel> messages;

        public List<MessageModel> getMessages() {
            return messages;
        }

        public String getId() {
            return id;
        }

        public String getAd_id() {
            return ad_id;
        }
    }
}
