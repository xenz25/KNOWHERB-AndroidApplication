package com.example.kowherbz.utility;

import android.content.Context;

import com.example.kowherbz.encryption.AESEncryptor;
import com.example.kowherbz.encryption.JsonAESEncryptorModifier;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.PlantContentHolder;
import com.google.gson.Gson;

import java.io.InputStream;

public class JsonGrabber {

    public static ParentContentHolder grabJsonDataFromRaw(Context context, int rawResourceID){
        Gson gson = new Gson();
        String text = "";
        try {
            InputStream reader = context.getResources().openRawResource(rawResourceID);
            byte[] buffer = new byte[reader.available()];
            while(reader.read(buffer) != -1){
                text = new String(buffer);
            }
        } catch (Exception ignored){}
        text = AESEncryptor.decrypt(text, JsonAESEncryptorModifier.getPass_key());
        return gson.fromJson(text, ParentContentHolder.class);
    }

    public static PlantContentHolder grabJsonDataFromRawPlant(Context context, int rawResourceID){
        Gson gson = new Gson();
        String text = "";
        try {
            InputStream reader = context.getResources().openRawResource(rawResourceID);
            byte[] buffer = new byte[reader.available()];
            while(reader.read(buffer) != -1){
                text = new String(buffer);
            }
        } catch (Exception ignored){}
        text = AESEncryptor.decrypt(text, JsonAESEncryptorModifier.getPass_key());
        return gson.fromJson(text, PlantContentHolder.class);
    }
}
