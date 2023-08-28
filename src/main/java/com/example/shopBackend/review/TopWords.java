package com.example.shopBackend.review;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to include data for top words.
 */
public class TopWords {
    private List<String> topPos = new ArrayList<>();
    private List<String> topNeg = new ArrayList<>();

    public TopWords(List<String> topPos, List<String> topNeg) {
        this.topPos = topPos;
        this.topNeg = topNeg;
    }

    public TopWords() {};

    public List<String> getTopPos() {
        return topPos;
    }

    public void setTopPos(List<String> topPos) {
        this.topPos = topPos;
    }

    public List<String> getTopNeg() {
        return topNeg;
    }

    public void setTopNeg(List<String> topNeg) {
        this.topNeg = topNeg;
    }
}
