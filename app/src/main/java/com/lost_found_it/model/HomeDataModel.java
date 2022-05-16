package com.lost_found_it.model;

import java.io.Serializable;
import java.util.List;

public class HomeDataModel extends StatusResponse implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private List<AdModel> ads;
        private List<AdModel> slider;

        public List<AdModel> getAds() {
            return ads;
        }

        public List<AdModel> getSlider() {
            return slider;
        }
    }
}
