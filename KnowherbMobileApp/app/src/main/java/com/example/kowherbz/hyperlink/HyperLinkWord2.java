package com.example.kowherbz.hyperlink;

import android.util.Pair;

public class HyperLinkWord2 {
    private final int startIndex;
    private final int endIndex;
    private final Pair<String, Integer> searchPair;

    public HyperLinkWord2(int startIndex, int endIndex, Pair<String, Integer> searchSet) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.searchPair = searchSet;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public Pair<String, Integer> getSearchPair() {
        return searchPair;
    }
}