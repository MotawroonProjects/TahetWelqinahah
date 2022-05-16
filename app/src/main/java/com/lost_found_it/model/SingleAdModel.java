package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class SingleAdModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private AdModel ad;
        private List<AdModel> related;

        public AdModel getAd() {
            return ad;
        }

        public List<AdModel> getRelated() {
            return related;
        }
    }
}
