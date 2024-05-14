package com.example.userapp.otp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTPGenerator {

    //X vrjeme cekanja u sekundama
    public final static long X = 60;
    private static  final String hmacSHA256Algorithm = "HmacSHA512";

     private String algorithm;
     private String key;
    private int returnDigits;
    private Mac mac;

    public TOTPGenerator(String key) throws NoSuchAlgorithmException, InvalidKeyException {
        this(hmacSHA256Algorithm,key,8);
    }

    private TOTPGenerator(String algorithm, String key, int returnDigits) throws NoSuchAlgorithmException, InvalidKeyException {
        this.algorithm = algorithm;
        this.key = key;
        this.returnDigits = returnDigits;
        SecretKeySpec secretKeySpec = new SecretKeySpec(hexStringToByteArray(key), algorithm);
        this.mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
    }



        private static final int[] DIGITS_POWER
                = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };

        public String generate ()  {






            long unixTime = System.currentTimeMillis() / 1000L;
            long T  = unixTime /X;

            String steps = Long.toHexString(T).toUpperCase();


            while (steps.length() < 16)
                steps = "0" + steps;
            String data = steps;

            return this.GenerateOTP(data);


        }


        //Data je u nasem slucaju vrijeme
        private synchronized String GenerateOTP(String data) {
            mac.reset();
            byte [] hash = mac.doFinal(data.getBytes());
            int offset = hash[hash.length - 1] & 0xf;

            int binary =
                    ((hash[offset] & 0x7f) << 24) |
                            ((hash[offset + 1] & 0xff) << 16) |
                            ((hash[offset + 2] & 0xff) << 8) |
                            (hash[offset + 3] & 0xff);



            int otp=   binary % DIGITS_POWER[returnDigits];

            String result = Integer.toString(otp);


            while (result.length() < returnDigits) {
                result = "0" + result;
            }
            return result;
        }


        private static String byteToHex(byte num) {
            char[] hexDigits = new char[2];
            hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((num & 0xF), 16);
            return new String(hexDigits);
        }

        private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }


}
