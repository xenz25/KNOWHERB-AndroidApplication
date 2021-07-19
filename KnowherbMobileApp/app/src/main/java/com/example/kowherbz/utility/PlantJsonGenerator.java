package com.example.kowherbz.utility;

import android.content.Context;
import android.util.Pair;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kowherbz.R;
import com.example.kowherbz.adapter.PlantListAdapter;
import com.example.kowherbz.holder.PlantContentHolder;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Use for creating JSON files for plants - not needed actually
 */
public class PlantJsonGenerator {
    private Context context;
    private ViewPager2 mainViewPager;

    private void setUpViewPager() {
        List<PlantContentHolder> plantContentHolderList = new ArrayList<>();

        //-------------
        PlantContentHolder plantContentHolder = new PlantContentHolder(
                context.getString(StringIDs.KANYA_PISTULA), PlantContentHolder.generatePairs(
                Pair.create(context.getString(R.string.scientific_name), "Cassia fistula L."),
                Pair.create(context.getString(R.string.english), "Golden shower, Pudding pipe tree"),
                Pair.create(context.getString(R.string.tagalog), "Kanya pistula"),
                Pair.create(context.getString(R.string.visayan), "Bitsula, Ibaban, Lombayong")
        ));
        String fname = "kanya_pistula";
        //----------

        generateJSONPlant(plantContentHolder, fname);
        PlantContentHolder read = readToDirs(fname);
        read.setLower_case_name(fname);
        plantContentHolderList.add(read);

        PlantListAdapter plantListAdapter = new PlantListAdapter();
        plantListAdapter.setContentHolderList(plantContentHolderList);
        mainViewPager.setAdapter(plantListAdapter);
        mainViewPager.setOffscreenPageLimit(3);
        mainViewPager.setClipToPadding(false);
        mainViewPager.setClipChildren(false);
        mainViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        mainViewPager.setPageTransformer(compositePageTransformer);
    }

    private void generateJSONPlant(PlantContentHolder plantContentHolder, String fileName){
        fileName += ".json";
        Gson gson = new Gson();
        String json = gson.toJson(plantContentHolder);
        File file = new File(context.getFilesDir() + "/" + fileName);

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (Exception ignored) {
        }
    }

    private PlantContentHolder readToDirs(String fileName) {
        fileName+=".json";
        Gson gson = new Gson();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader buff = new BufferedReader(new FileReader(context.getFilesDir() + "/" + fileName));
            String text;
            while ((text = buff.readLine()) != null) {
                stringBuilder.append(text);
            }
        } catch (Exception ignored) {
        }
        return gson.fromJson(stringBuilder.toString(), PlantContentHolder.class);
    }
}
