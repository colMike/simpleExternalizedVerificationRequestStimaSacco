package com.michael.mbugua.verificationrequest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {
        try {
            secretKey = new SecretKeySpec(myKey.getBytes("UTF-8"), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        setKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public static String decrypt(String strToDecrypt, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        setKey(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    public static String HmacHash(String content, String key, String encodingMAC)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        // Sample HmacSHA512,HmacSHA256
        Mac enctype = null;
        String result = null;
        enctype = Mac.getInstance(encodingMAC);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), encodingMAC);
        enctype.init(keySpec);
        byte[] mac_data = enctype.doFinal(content.getBytes("UTF-8"));
        result = convertToHex(mac_data);
        return result;
    }

    public static String convertToHex(byte[] raw) {
        int substring = 1;
        int length = 16;
        int size = 200;
        StringBuilder stringBuilder = new StringBuilder(size);
        for (int i = 0; i < raw.length; i++) {

            stringBuilder.append(Integer.toString((raw[i] & 0xff) + 0x100, length).substring(substring));
        }
        return stringBuilder.toString();
    }

    public static String generateSecretKey() {
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        int count = 20;

        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }

    public static String generateEncryptionKey() {
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        int count = 16;

        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }

    public static void main(String args[]) {
        try {
            String encKey = "504HIZUQLBHANT2Q";
            String secretKey = "504HIZUQLBHANT2Q";

            String rawmsg = "{\r\n" + "	\"header\": {\r\n" + "		\"type\": \"verify\",\r\n"
                    + "		\"version\": 100,\r\n" + "		\"token\": \"string\",\r\n"
                    + "		\"requestId\": \"string\",\r\n"
                    + "		\"apiKey\": \"7d7846b815451182d38f87e47ebd0db4319a57e1cfcb3e00ec240bc4c2c937b3\"\r\n"
                    + "	},\r\n" + "	\"body\": {\r\n" + "		\"search\": {\r\n"
                    + "			\"criteria\": \"string\",\r\n" + "			\"key\": 100\r\n" + "		},\r\n"
                    + "		\"biometrics\": {\r\n" + "			\"fingerprints\": {\r\n"
                    + "				\"fingerprints\": [{\r\n" + "					\"position\": 100,\r\n"
                    + "					\"image\": {\r\n" + "						\"format\": \"string\",\r\n"
                    + "						\"resolutionDpi\": 100,\r\n"
                    + "						\"data\": \"string\"\r\n" + "					}\r\n" + "				}]\r\n"
                    + "			}\r\n" + "		}\r\n" + "	}\r\n" + "}";




            System.out.println("Secret Key:" + AES.generateSecretKey());
            System.out.println("Encryption Key:" + AES.generateEncryptionKey());
            String encmsg = "kox0g5HjAHBgn26S0+p0dI7q0VV7CvAPOqI+J3BwFZHh2KPCwjBRsv5PGEbtL2IS8+WFUrRJlacyjaQ+7ENPwi5nbEFVSWZqyeIbVMLi0YulGHPAqqOmCIZ405LBnI7NAUZ49Qu8NISnwEVGI5jX9LkWxrqKIRbTpe5zrDEMPbI=";

            System.out.println("Enc Message:" + AES.encrypt(rawmsg, encKey));
//		System.out.println("Dec Message:" + AES.decrypt(
//				"kGnWVTOe+SSWI1kAyLt8QgHJAy+hlwkz5bZTBfR+8v14pc1indscEoPg6EdAvgd1wUu3ObaLUwu/mvyn616Rl9vSR92eR7cFfqsXiLaYsk4/GzBFNz5yXav/pmyxc7XPzXwf+DBRS50A1mgzijEV2+Sxw1VcoiwmI05fBGpDCev5ljiy3ZB3y1UxblMA7Sz8HPYpRUJaXI6sa1ZK1lHHyRz4hwd9WgTnzI+GFEmyYaee1dD2axvztK5fqGBUzyRy1DY8lR8tU2tY1ESJTnLak+sJo3Gp9/Cb6ayGeRzYwgloIWqW/KwuMvh5h1kzEcAx5Gv828nLUriJ+8VUFHxjdJN/5RyzKchJCLXfEbNz+BR1Y+Vzcd5Hlw8Vv5thqG5eQsYfKreKxcJqtrSxhsxUgg==",
//				encKey)); //
            System.out.println("Hmac Enc Message:" + AES.HmacHash(AES.encrypt(rawmsg,encKey),secretKey, "HmacSHA256"));
//		if (!AES.HmacHash(rawmsg, secretKey, "HmacSHA256").equalsIgnoreCase("")) {
//			String test = "Paul";
//			System.out.println("Testing: " + test);
//		}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}