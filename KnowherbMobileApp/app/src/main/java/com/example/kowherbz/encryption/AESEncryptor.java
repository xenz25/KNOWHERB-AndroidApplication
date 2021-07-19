package com.example.kowherbz.encryption;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptor {
    private static final String AES = "AES";

    public static String encrypt(String data, String password) {
        try{
            SecretKeySpec keySpec = generateKey(password);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedByteVal = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedByteVal, Base64.DEFAULT);
        } catch (Exception ignore){

        }
        return "null";
    }

    public static String decrypt(String encryptedData, String password){
        try{
            SecretKeySpec keySpec = generateKey(password);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedValBase64 = Base64.decode(encryptedData, Base64.DEFAULT);
            byte[] decryptedByteVal = cipher.doFinal(decodedValBase64);
            return new String(decryptedByteVal);
        } catch (Exception ignore){

        }
        return "null";
    }

    public static SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passByte = password.getBytes(StandardCharsets.UTF_8);
        digest.update(passByte, 0, passByte.length);
        byte[] keyByte = digest.digest();
        return new SecretKeySpec(keyByte, AES);
    }

}
