package com.example.kowherbz.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.kowherbz.holder.HydroterapiItemHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.holder.SakitAtLunasItemHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class FavoritesUtils {
    public static final String ITEM_ADDED_TO_FAVORITES = "Idinagdag sa mga paborito";
    public static final String ITEM_REMOVED_TO_FAVORITES = "Naalis sa mga paborito";

    public static final int REMOVED = 0;
    public static final int ADD = 1;

    public static LinkedHashSet<String> sortSet(LinkedHashSet<String> set){
        List<String> lst = new ArrayList<>(set);
        Collections.sort(lst);
        set.clear();
        set.addAll(lst);
        return set;
    }

    public static List<String> convertHashSetToListString(LinkedHashSet<String> set){
        return new ArrayList<>(set);
    }

    public static class Plants {
        public static final String PLANT_FAV_PREFERENCE = "plant_favorite_preference";
        public static final String PLANT_PREF_CREATED = "plant_pref_created";

        public static TreeMap<String, PlantContentHolder> favPlantList;
        /*public static LinkedHashSet<String> plantSearchList = new LinkedHashSet<>();*/

        //initialize list of favorites plants
        public static void initPreference(Context context) {
            SharedPreferences fav_pref = SharedPreferenceUtility.getSharedPref(context, PLANT_FAV_PREFERENCE);

            if (!fav_pref.contains(PLANT_PREF_CREATED)) {
                //create shared preference
                SharedPreferences.Editor editor = fav_pref.edit();
                //start if the shared pref for favorite plants does not exist
                for (String item : RawResourceIDs.getRawResourceMapHalaman().keySet()) {
                    editor.putBoolean(item, false);
                }
                editor.putBoolean(PLANT_PREF_CREATED, true);
                editor.apply();
            }
            initializeList(context);
        }

        public static void initializeList(Context context) {
            SharedPreferences fav_pref = SharedPreferenceUtility.getSharedPref(context, PLANT_FAV_PREFERENCE);
            favPlantList = new TreeMap<>();
            //plantSearchList = new LinkedHashSet<>();
            List<PlantContentHolder> items = new ArrayList<>(ClassPackageMaker.getHalamanList());

            for (PlantContentHolder item : items) {
                String key = item.getLower_case_name();
                if (fav_pref.getBoolean(key, false)) {
                    favPlantList.put(key, item);
                    //plantSearchList.add(item.getPlant_name());
                }
            }
        }

        public static boolean isEmpty() {
            return Plants.favPlantList.isEmpty();
        }

        private static void updatePlantItemOnFavorites(Context context, String key, int tag) {
            SharedPreferences.Editor editor = SharedPreferenceUtility.getSharedPref(context, PLANT_FAV_PREFERENCE).edit();
            editor.putBoolean(key, tag == ADD);
            editor.apply();
        }

        public static boolean isPlantFavorite(Context context, String key) {
            return SharedPreferenceUtility.getSharedPref(context, PLANT_FAV_PREFERENCE).getBoolean(key, false);
        }

        public static TreeMap<String, PlantContentHolder> getFavPlantList() {
            return favPlantList;
        }

        /*public static LinkedHashSet<String> getPlantSearchList() {
            return sortSet(plantSearchList);
        }*/

        /**
         * Use this method to add a specific plants to favorites
         *
         * @param context            context of activity
         * @param plantContentHolder the item to add
         */
        public static void addToFavorites(Context context, PlantContentHolder plantContentHolder) {
            favPlantList.put(plantContentHolder.getLower_case_name(), plantContentHolder);
            //plantSearchList.add(plantContentHolder.getPlant_name());
            updatePlantItemOnFavorites(context, plantContentHolder.getLower_case_name(), ADD);
        }

        /**
         * Use this method insed HydroterapiInstruction View Activity during removing favorites
         *
         * @param context context of activity
         * @param key     the item key to be removed
         */
        public static void removeToFavorites(Context context, String key) {
            //TODO
            //plantSearchList.remove(Objects.requireNonNull(favPlantList.get(key)).getPlant_name());
            favPlantList.remove(key);
            updatePlantItemOnFavorites(context, key, REMOVED);
        }

        public static boolean toggleFavState(Context context, PlantContentHolder plantContentHolder, ToastUtils mainToast, boolean displayToast) {
            boolean state = false;
            String key = plantContentHolder.getLower_case_name();
            if (FavoritesUtils.Plants.isPlantFavorite(context, key)) {
                FavoritesUtils.Plants.removeToFavorites(context, key);
                if (displayToast)
                    mainToast.displayOnToastShort(FavoritesUtils.ITEM_REMOVED_TO_FAVORITES);
            } else {
                state = true;
                FavoritesUtils.Plants.addToFavorites(context, plantContentHolder);
                if (displayToast)
                    mainToast.displayOnToastShort(FavoritesUtils.ITEM_ADDED_TO_FAVORITES);
            }
            return state;
        }
    }

    /**
     * Hydrotherapy Class That manages the favorites
     */
    public static class Hydrotherapy {

        public static final String HYDROTHERAPY_FAV_PREFERENCE = "hydroterapi_favorite_preference";
        public static final String HYDROTHERAPY_PREF_CREATED = "ihydroterapi_pref_created";

        public static TreeMap<String, ParentContentHolder> favHydrotherapyList;
        public static LinkedHashSet<String> hydroSearchList = new LinkedHashSet<>();

        //initialize list of favorites hydroterapi
        public static void initPreference(Context context) {
            SharedPreferences fav_pref = SharedPreferenceUtility.getSharedPref(context, HYDROTHERAPY_FAV_PREFERENCE);

            if (!fav_pref.contains(HYDROTHERAPY_PREF_CREATED)) {
                //create shared preference
                SharedPreferences.Editor editor = fav_pref.edit();
                //start if the shared pref for favorite hyfroterapi does not exist
                for (String item : RawResourceIDs.getRawResourceMapHydroterapi().keySet()) {
                    editor.putBoolean(item, false);
                }
                editor.putBoolean(HYDROTHERAPY_PREF_CREATED, true);
                editor.apply();
            }
            initializeFavList(context);
        }

        public static TreeMap<String, ParentContentHolder> getFavHydrotherapyList() {
            return favHydrotherapyList;
        }

        public static LinkedHashSet<String> getHydroSearchList() {
            return sortSet(hydroSearchList);
        }

        public static void initializeFavList(Context context) {
            SharedPreferences fav_pref = SharedPreferenceUtility.getSharedPref(context, HYDROTHERAPY_FAV_PREFERENCE);
            favHydrotherapyList = new TreeMap<>();
            List<ParentContentHolder> items = new ArrayList<>(ClassPackageMaker.getHydroterapiList());

            for (ParentContentHolder item : items) {
                String key = item.getLower_case_name();
                if (fav_pref.getBoolean(key, false)) {
                    favHydrotherapyList.put(key, item);
                    hydroSearchList.add(item.getTitleHolder().getTitleOfSickness());
                }
            }
        }

        public static boolean isEmpty() {
            return Hydrotherapy.favHydrotherapyList.isEmpty();
        }

        private static void updateHydrotherapyItemOnFavorites(Context context, String key, int tag) {
            SharedPreferences.Editor editor = SharedPreferenceUtility.getSharedPref(context, HYDROTHERAPY_FAV_PREFERENCE).edit();
            editor.putBoolean(key, tag == ADD);
            editor.apply();
        }

        public static boolean isHydrotherapyFavorite(Context context, String key) {
            return SharedPreferenceUtility.getSharedPref(context, HYDROTHERAPY_FAV_PREFERENCE).getBoolean(key, false);
        }

        /**
         * Use this method inside HydroterapiInstruction View Activity during adding favorites
         *
         * @param context             context of activity
         * @param parentContentHolder the item to add
         */
        public static void addToFavorites(Context context, ParentContentHolder parentContentHolder) {
            favHydrotherapyList.put(parentContentHolder.getLower_case_name(), parentContentHolder);
            hydroSearchList.add(parentContentHolder.getTitleHolder().getTitleOfSickness());
            updateHydrotherapyItemOnFavorites(context, parentContentHolder.getLower_case_name(), ADD);
        }

        /**
         * Use this method inside HydroterapiInstruction View Activity during removing favorites
         *
         * @param context context of activity
         * @param key     the item key to be removed
         */
        public static void removeToFavorites(Context context, String key) {
            //hydroSearchList.remove(Objects.requireNonNull(favHydrotherapyList.get(key)).getTitleHolder().getTitleOfSickness());
            favHydrotherapyList.remove(key);
            updateHydrotherapyItemOnFavorites(context, key, REMOVED);
        }
    }

    /**
     * Illness Class That manages the favorites
     */
    public static class Illness {

        public static final String ILLNESS_FAV_PREFERENCE = "illness_favorite_preference";
        public static final String ILLNESS_PREF_CREATED = "illness_pref_created";

        public static TreeMap<String, ParentContentHolder> favIllnessList;
        public static LinkedHashSet<String> illnessSearchList = new LinkedHashSet<>();

        //initialize list of favorites illness
        public static void initPreference(Context context) {
            SharedPreferences fav_pref = SharedPreferenceUtility.getSharedPref(context, ILLNESS_FAV_PREFERENCE);

            if (!fav_pref.contains(ILLNESS_PREF_CREATED)) {
                //create shared preference
                SharedPreferences.Editor editor = fav_pref.edit();
                //start if the shared pref for favorite illness does not exist
                for (String item : RawResourceIDs.getRawResourceMapSakitAtLunas().keySet()) {
                    editor.putBoolean(item, false);
                }
                editor.putBoolean(ILLNESS_PREF_CREATED, true);
                editor.apply();
            }
            initializeFavList(context);
        }

        public static TreeMap<String, ParentContentHolder> getFavIllnessList() {
            return favIllnessList;
        }

        public static LinkedHashSet<String> getIllnessSearchList() {
            return sortSet(illnessSearchList);
        }

        public static void initializeFavList(Context context) {
            SharedPreferences fav_pref = SharedPreferenceUtility.getSharedPref(context, ILLNESS_FAV_PREFERENCE);
            favIllnessList = new TreeMap<>();
            List<ParentContentHolder> items = new ArrayList<>(ClassPackageMaker.getSakitAtLunasList());

            for (ParentContentHolder current : items) {
                String key = current.getLower_case_name();
                if (fav_pref.getBoolean(key, false)) {
                    favIllnessList.put(key, current);
                    illnessSearchList.add(current.getTitleHolder().getTitleOfSickness());
                }
            }
        }

        public static boolean isEmpty() {
            return Illness.favIllnessList.isEmpty();
        }

        private static void updateItemOnFavoritesIllness(Context context, String key, int tag) {
            SharedPreferences.Editor editor = SharedPreferenceUtility.getSharedPref(context, ILLNESS_FAV_PREFERENCE).edit();
            editor.putBoolean(key, tag == ADD);
            editor.apply();
        }

        public static boolean isIllnessFavorite(Context context, String key) {
            return SharedPreferenceUtility.getSharedPref(context, ILLNESS_FAV_PREFERENCE).getBoolean(key, false);
        }


        /**
         * Use this method inside InstructionView Activity during adding favorites
         *
         * @param context             context of activity
         * @param parentContentHolder the item to add
         */
        public static void addToFavorites(Context context, ParentContentHolder parentContentHolder) {
            favIllnessList.put(parentContentHolder.getLower_case_name(), parentContentHolder);
            illnessSearchList.add(parentContentHolder.getTitleHolder().getTitleOfSickness());
            updateItemOnFavoritesIllness(context, parentContentHolder.getLower_case_name(), ADD);
        }

        /**
         * Use this method inside Instruction View Activity during removing favorites
         *
         * @param context context of activity
         * @param key     the item key to be removed
         */
        public static void removeToFavorites(Context context, String key) {
            illnessSearchList.remove(Objects.requireNonNull(favIllnessList.get(key)).getTitleHolder().getTitleOfSickness());
            favIllnessList.remove(key);
            updateItemOnFavoritesIllness(context, key, REMOVED);
        }
    }
}
