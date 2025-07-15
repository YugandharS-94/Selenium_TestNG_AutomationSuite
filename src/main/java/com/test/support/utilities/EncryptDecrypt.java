package com.test.support.utilities;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptDecrypt {
    private static final String KEY_FILE = "./src/main/resources/key/aes.key";
    public static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    // Save AES Key to file (Base64 encoded)
    public static void saveKey(SecretKey secretKey, String filePath) throws IOException {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(encodedKey);
        }
    }
    // Load AES Key from file
    public static SecretKey loadKey(String filePath) throws IOException {
        String encodedKey;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            encodedKey = br.readLine();
        }
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, loadKey(KEY_FILE));
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, loadKey(KEY_FILE));
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
////        SecretKey secretKey = EncryptDecrypt.generateKey();
////        System.out.println("Secret Key: " + secretKey.getEncoded());
////        saveKey(secretKey,KEY_FILE);
        String toBeEncrypted = "";
        String encrypted = EncryptDecrypt.encrypt(toBeEncrypted);
        System.out.println("Encrypted Text: "+ encrypted);
        String decrypted = EncryptDecrypt.decrypt(encrypted);
        System.out.println("Decrypted Text: "+ decrypted);
    }
}
