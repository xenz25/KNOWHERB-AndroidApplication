package com.example.kowherbz.holder;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Pair;

import com.example.kowherbz.R;
import com.example.kowherbz.utility.StringIDs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlantContentHolder {
    private static transient TreeMap<String, Integer> NAME_LIST;

    //TODO Run at start up
    public static void generatePlantNameDictionary(Context context){
        NAME_LIST = new TreeMap<>();
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.scientific_name), R.string.scientific_name);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.english), R.string.english);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.spanish), R.string.spanish);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.tagalog), R.string.tagalog);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.cebuan), R.string.cebuan);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.ilocano), R.string.ilocano);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.pampango), R.string.pampango);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.visayan), R.string.visayan);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.cebu_visayan), R.string.cebu_visayan);
        NAME_LIST.put(StringIDs.getStringValue(context, R.string.bicol), R.string.bicol);
    }

    public static int findInNameListMap(String key){
        return NAME_LIST.containsKey(key) ? NAME_LIST.get(key) : -1;
    }

    private final String plant_name;
    private transient String lower_case_name;
    private final List<Pair<String, String>> common_names;

    public PlantContentHolder(String plant_name, List<Pair<String, String>> common_names) {
        this.plant_name = plant_name;
        this.common_names = common_names;
    }

    public void setLower_case_name(String lower_case_name) {
        this.lower_case_name = lower_case_name;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public String getLower_case_name() {
        return lower_case_name;
    }

    public List<Pair<String, String>> getCommon_names() {
        return common_names;
    }

    @SafeVarargs
    public static List<Pair<String, String>> generatePairs(Pair<String, String>... pairs){
        return Arrays.asList(pairs);
    }
}
