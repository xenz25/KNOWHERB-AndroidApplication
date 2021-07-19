package com.example.kowherbz.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.kowherbz.R;
import com.example.kowherbz.adapter.HydroterapiListAdapter;
import com.example.kowherbz.holder.HydroterapiItemHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.holder.SakitAtLunasItemHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ClassPackageMaker {
    private static List<ParentContentHolder> sakitAtLunasList;
    private static List<ParentContentHolder> hydroterapiList;
    private static List<PlantContentHolder> halamanList;
    private static List<String> sakitAtLunasListTitles;
    private static List<String> hydroterapiListTitles;
    private static List<String> halamanListTitles;

    /**
     *
     * @return String List of Hydrotherapy Titles
     */
    public static List<String> getHydroterapiListTitles() {
        return hydroterapiListTitles;
    }

    /**
     *
     * @return String List of Sakit at Lunas Titles
     */
    public static List<String> getSakitAtLunasListTitles() {
        return sakitAtLunasListTitles;
    }

    /**
     *
     * @return String List of Sakit at Lunas Titles
     */
    public static List<String> getHalamanListTitles() {
        return halamanListTitles;
    }



    /**
     * Package all Views passed into List of Views
     * @param views any number of views to add on list
     * @return List<View> list of all views passed
     */
    public static List<View> compileToListOfViews(View... views){
        return Arrays.stream(views).collect(Collectors.toList());
    }

    /**
     *
     * @return sakit at lunas list for recycler view
     */
    public static List<ParentContentHolder> getSakitAtLunasList() {
        return sakitAtLunasList;
    }

    /**
     *
     * @return hydroterapi list for recycler view
     */
    public static List<ParentContentHolder> getHydroterapiList() {
        return hydroterapiList;
    }

    /**
     *
     * @return halaman list for recycler view
     */
    public static List<PlantContentHolder> getHalamanList() {
        return halamanList;
    }

    /**
     * this will initialize all important list once
     * @param context context to grab resources
     */
    public static void grabAllRequiredList (Context context){
        ClassPackageMaker.sakitAtLunasList = new ArrayList<>();
        ClassPackageMaker.hydroterapiList = new ArrayList<>();
        ClassPackageMaker.halamanList = new ArrayList<>();

        ClassPackageMaker.sakitAtLunasListTitles = new ArrayList<>();
        ClassPackageMaker.hydroterapiListTitles = new ArrayList<>();
        ClassPackageMaker.halamanListTitles = new ArrayList<>();

        TreeMap<String, Integer> contentListSakitAtLunas = RawResourceIDs.getRawResourceMapSakitAtLunas();
        TreeMap<String, Integer> contentListHydroterapi = RawResourceIDs.getRawResourceMapHydroterapi();
        TreeMap<String, Integer> contentListHalaman = RawResourceIDs.getRawResourceMapHalaman();


        //grab list for sakit at lunas
        for (Map.Entry<String, Integer> content : contentListSakitAtLunas.entrySet()){
            ParentContentHolder parentContentHolder =
                    JsonGrabber.grabJsonDataFromRaw(context, content.getValue());
            parentContentHolder.setLower_case_name(content.getKey());
            sakitAtLunasListTitles.add(parentContentHolder.getTitleHolder().getTitleOfSickness());
            ClassPackageMaker.sakitAtLunasList.add(parentContentHolder);
        }

        //grab list for hydroterapi
        for (Map.Entry<String, Integer> content : contentListHydroterapi.entrySet()){
            ParentContentHolder parentContentHolder =
                    JsonGrabber.grabJsonDataFromRaw(context, content.getValue());
            parentContentHolder.setLower_case_name(content.getKey());
            hydroterapiListTitles.add(parentContentHolder.getTitleHolder().getTitleOfSickness());
            ClassPackageMaker.hydroterapiList.add(parentContentHolder);
        }

        //grab list for halaman
        for (Map.Entry<String, Integer> content : contentListHalaman.entrySet()){
            PlantContentHolder plantContentHolder = JsonGrabber.grabJsonDataFromRawPlant(context, content.getValue());
            plantContentHolder.setLower_case_name(content.getKey());
            halamanListTitles.add(plantContentHolder.getPlant_name());
            ClassPackageMaker.halamanList.add(plantContentHolder);
        }
    }

    public static int getImageResourceID(Context context, String name){
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static String getLowerCaseNameHydroterapi(Context context, String identifier){
        int position = ClassPackageMaker.getHydroterapiListTitles().indexOf(identifier);
        return RawResourceIDs.getAllHydrotherapyLowerCaseName(context)[position];
    }

    public static String getLowerCaseNameSakitAtLunas(Context context, String identifier){
        int position = ClassPackageMaker.getSakitAtLunasListTitles().indexOf(identifier);
        return RawResourceIDs.getAllSakitLowerCaseName(context)[position];
    }

    public static String getLowerCaseNameHalaman(Context context, String identifier){
        int position = ClassPackageMaker.getHalamanListTitles().indexOf(identifier);
        return RawResourceIDs.getAllHalamanLowerCaseName(context)[position];
    }

    public static int getIndexByTitlePlant(String plant_name){
        return ClassPackageMaker.getHalamanListTitles().indexOf(plant_name);
    }

    public static boolean isPackageAvailable(int serial_ID, Activity context){
        return context.getIntent() != null && context.getIntent().getExtras() != null && context.getIntent().hasExtra(
                context.getResources().getString(serial_ID));
    }
}
