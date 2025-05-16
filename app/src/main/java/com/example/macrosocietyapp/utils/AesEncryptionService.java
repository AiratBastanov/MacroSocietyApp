package com.example.macrosocietyapp.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionService {
    private static final String key = "bastanov_1234567";
    private static final String iv = "societyiv_123456";

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

            // URL-safe Base64 (NO_WRAP = без переносов строк)
            return Base64.encodeToString(encrypted, Base64.URL_SAFE | Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            byte[] decoded = Base64.decode(cipherText, Base64.URL_SAFE | Base64.NO_WRAP);
            byte[] decrypted = cipher.doFinal(decoded);

            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


