package com.example.kowherbz.utility;

import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.kowherbz.R;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RawResourceIDs {

    private static TreeMap<String, Integer> rawResourceMapSakitAtLunas;
    private static TreeMap<String, Integer> rawResourceMapHydroterapi;
    private static TreeMap<String, Integer> rawResourceMapHalaman;

    public static String[] getAllSakitLowerCaseName(Context context) {
        return context.getResources().getStringArray(R.array.sakit_lower_case);
    }

    public static String[] getAllHydrotherapyLowerCaseName(Context context) {
        return context.getResources().getStringArray(R.array.hydroterapi_lower_case);
    }

    public static String[] getAllHalamanLowerCaseName(Context context) {
        return context.getResources().getStringArray(R.array.plants_lower_case);
    }

    /**
     * Returns the id of raw resource identified by the name of the resource
     * @param context context
     * @param name raw resource name
     * @return id of raw resource
     */
    public static int getRawResourceID(Context context, String name){
        return context.getResources().getIdentifier(
                name, "raw", context.getPackageName()
        );
    }

    /**
     * Run at Start Up - initialize the raw ids of required list contents
     *
     * @param context context for retrieval
     */
    public static void init(Context context) {
        rawResourceMapSakitAtLunas = new TreeMap<>();
        rawResourceMapHydroterapi = new TreeMap<>();
        rawResourceMapHalaman = new TreeMap<>();

        String[] itemNamesSakitAtLunas = RawResourceIDs.getAllSakitLowerCaseName(context);
        String[] itemNamesHydro = RawResourceIDs.getAllHydrotherapyLowerCaseName(context);
        String[] itemNamesHalaman = RawResourceIDs.getAllHalamanLowerCaseName(context);

        //fills the list for sakit at lunas
        for (String name : itemNamesSakitAtLunas) {
            rawResourceMapSakitAtLunas.put(name.trim(), getRawResourceID(context, name));
        }

        //fills the list for hydroterapi
        for (String name : itemNamesHydro) {
            rawResourceMapHydroterapi.put(name.trim(), getRawResourceID(context, name));
        }

        //fills the list for halaman
        for (String name : itemNamesHalaman) {
            rawResourceMapHalaman.put(name.trim(), getRawResourceID(context, name));
        }
    }

    /**
     * Illness
     * @param name the identifier to look up - KEY
     * @return the ID of identifier provided
     */
    public static int findInMapSakitAtLunas(String name) {
        return rawResourceMapSakitAtLunas.containsKey(name) ? rawResourceMapSakitAtLunas.get(name) : -1;
    }

    /**
     * Hydroterapi
     * @param name the identifier to look up - KEY
     * @return the ID of identifier provided
     */
    public static int findInMapHydrotearapi(String name) {
        return rawResourceMapHydroterapi.containsKey(name) ? rawResourceMapHydroterapi.get(name) : -1;
    }

    /**
     * Plants
     * @param name the identifier to look up - KEY
     * @return the ID of identifier provided
     */
    public static int findInMapHalaman(String name) {
        return rawResourceMapHalaman.containsKey(name) ? rawResourceMapHalaman.get(name) : -1;
    }

    /**
     * @return the list of raw resource map for Sakit with names and IDS
     */
    public static TreeMap<String, Integer> getRawResourceMapSakitAtLunas() {
        return rawResourceMapSakitAtLunas;
    }

    /**
     * @return the list of raw resource map for Sakit with names and IDS
     */
    public static TreeMap<String, Integer> getRawResourceMapHydroterapi() {
        return rawResourceMapHydroterapi;
    }

    /**
     * @return the list of raw resource map for Halaman with names and IDS
     */
    public static TreeMap<String, Integer> getRawResourceMapHalaman() {
        return rawResourceMapHalaman;
    }
}
