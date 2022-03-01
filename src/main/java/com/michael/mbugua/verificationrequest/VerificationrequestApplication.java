package com.michael.mbugua.verificationrequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class VerificationrequestApplication {

    static String token = "BSQz8Ve6yFheGmKfj1ttMldlLW4lmwWUAlEQxKAFlS8";


//    static String encKey = "@compulynx#54321";
//    static String apiKey = "API KEY";
//
    static String encKey = "504HIZUQLBHANT2Q";
    static String apiKey = "7d7846b815451182d38f87e47ebd0db4319a57e1cfcb3e00ec240bc4c2c937b3";

    public static void main(String[] args) throws IOException {
        postUser();
    }

    public static void postUser() throws IOException {


        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:9080/compas-bio-api/rest/api/Profits");
            httpPost.setHeader("Content-type", "application/json");


            httpPost.setHeader("type", "loadProfile");
            httpPost.setHeader("version", "2");
//            httpPost.setHeader("token", token);
            httpPost.setHeader("requestId", "jmuthui:f4be73e2-48e6-4e8a-bc8d-e8536483939f");
            httpPost.setHeader("apiKey", apiKey);
            httpPost.setHeader("entityId", "0");

//            String json = "{\n" +
//                    "\t\"header\": {\n" +
//                    "\t\t\"type\": \"loadProfile\",\n" +
//                    "\t\t\"version\": \"2\",\n" +
////                    "\t\t\"token\" " + ":" + "\""
////                    + token +
////                    "\"" +
////                    "," +
////                    "\n" +
//                    "\t\t\"requestId\": \"jmuthui:1da95c2d-0ac4-4482-855c-05cc5ab7984b\",\n" +
//                    "\t\t\"apiKey\" " + ":" + "\""
//                    + apiKey +
//                    "\"" +
//                    "," +
//                    "\n" +
//                    "\t\t\"entityId\": \"0\"\n" +
//                    "\t},\n" +
//                    "\t\"body\": {\n" +
//                    "\t\t\"search\": {\n" +
//                    "\t\t\t\"criteria\": \"list\"\n" +
//                    "\t\t}\n" +
//                    "\t}\n" +
//                    "}";

            String json = "{\n" +
                    "\t\"header\": {\n" +
                    "\t\t\"type\": \"verify\",\n" +
                    "\t\t\"version\": \"2\",\n" +
                    "\t\t\"requestId\": \"profits:1da95c2d-0ac4-4482-855c-05cc5ab7984b\",\n" +
                    "\t\t\"apiKey\" " + ":" + "\""
                    + apiKey +
                    "\"" +
                    "," +
                    "\n" +
                    "\t\t\"entityId\": \"0\"\n" +
                    "\t},\n" +
                    "    \"body\": {\n" +
                    "        \"search\": {\n" +
                    "            \"criteria\": \"idNumber\",\n" +
                    "            \"key\": \"30258124\"\n" +
                    "        },\n" +
                    "        \"bios\": {\n" +
                    "            \"fingerPrints\": [\n" +
                    "                {\n" +
                    "                    \"index\": 0,\n" +
                    "                    \"base64\" " + ":" + "\""
                                        + readFile() +
                                        "\"" +
                                        "\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        \"bioType\": \"FINGERPRINTS\"\n" +
                    "    }\n" +
                    "}";


            System.out.println("here is the json being sent");
            System.out.println(json);

            String encryptedBody = encryptBody(json);

            String generatedSignature = AES.HmacHash(encryptedBody, encKey, "HmacSHA256");

            httpPost.setHeader("signature", generatedSignature);


            StringEntity stringEntity = new StringEntity(encryptedBody);
            httpPost.setEntity(stringEntity);

            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    public static String encryptBody(String json) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        return AES.encrypt(json, encKey);
    }


    public static String readFile(){
        try{

            DataInputStream dis =
                    new DataInputStream (
                            new FileInputStream("src/main/resources/fingerprint.txt"));

            byte[] datainBytes = new byte[dis.available()];
            dis.readFully(datainBytes);
            dis.close();

            String content = new String(datainBytes, 0, datainBytes.length);

            return content;

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

}
