package com.example.kowherbz.encryption;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;

public class JsonAESEncryptorModifier {
    private static final String pass_key = "knowherb-encrypted-data-key-xenon25";

    public static String getPass_key() {
        return pass_key;
    }

    public static int getRawId(Context context, String identifier){
        return context.getResources().getIdentifier(
                identifier, "raw", context.getPackageName()
        );
    }

    public static void encryptDataFromJSONAndParseBack(Context context, String file_name){
        try {
            int resource_id = getRawId(context, file_name);
            String dataToEncrypt = grabJsonStringDataFromRaw(context, resource_id);
            String encrypted = AESEncryptor.encrypt(dataToEncrypt, pass_key);
            writeEncryptedDataToJsonEncrypted(context, encrypted, file_name);
        } catch (Exception ignored){}
    }

    public static String grabJsonStringDataFromRaw(Context context, int rawResourceID){
        String text = "";
        try {
            InputStream reader = context.getResources().openRawResource(rawResourceID);
            byte[] buffer = new byte[reader.available()];
            while(reader.read(buffer) != -1){
                text = new String(buffer);
            }
        } catch (Exception ignored){}
        return text;
    }

    private static void writeEncryptedDataToJsonEncrypted(Context context, String encryptedData, String file_name){
        file_name+=".json";
        File fileName = new File(context.getFilesDir()+"/"+file_name);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            bufferedWriter.write(encryptedData);
            bufferedWriter.close();
        } catch (Exception ignored){}
    }

    public static String decodeEncryptedJsonString(Context context, String file_name){
        file_name+=".json";
        File fileName = new File(context.getFilesDir()+"/"+file_name);
        String text;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while((text = bufferedReader.readLine()) != null){
                stringBuilder.append(text);
            }
        } catch (Exception ignored){}
        return AESEncryptor.decrypt(stringBuilder.toString(), pass_key);
    }
}
