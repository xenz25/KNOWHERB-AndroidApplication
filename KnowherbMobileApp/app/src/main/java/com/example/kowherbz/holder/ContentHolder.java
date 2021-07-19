package com.example.kowherbz.holder;

import android.util.Pair;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * class Content Holder
 */
public class ContentHolder implements Serializable {
    //holds the contents and headers
    public static final int HYDROTERAPI = 0;
    public static final int HALAMAN = 1;
    public static final int SAKIT = 2;

    private final String header;
    private final String content;
    private final List<Pair<String, Integer>> hyperWordsInstruction;
    private final List<Pair<String, Integer>> hyperWordsHeader;

    public ContentHolder(String header, String content, List<Pair<String, Integer>> hyperWordsHeader, List<Pair<String, Integer>> hyperWordsInstruction) {
        this.header = header;
        this.content = content;
        this.hyperWordsHeader = hyperWordsHeader;
        this.hyperWordsInstruction = hyperWordsInstruction;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public List<Pair<String, Integer>> getHyperWordsHeader() {
        return hyperWordsHeader;
    }

    public boolean isHyperHeaderEmpty() {
        return hyperWordsHeader.isEmpty();
    }

    public List<Pair<String, Integer>> getHyperWordsInstruction() {
        return hyperWordsInstruction;
    }

    public boolean isHyperWordContentEmpty() {
        return hyperWordsInstruction.isEmpty();
    }

    @SafeVarargs
    public static List<Pair<String, Integer>> createPairs(Pair<String, Integer>... pairs) {
        return Arrays.asList(pairs.clone());
    }
}
