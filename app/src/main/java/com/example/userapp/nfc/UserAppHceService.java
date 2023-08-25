package com.example.userapp.nfc;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import com.example.userapp.token.TokenManager;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class UserAppHceService extends HostApduService {
    private static final String RESPONSE_SUCCESS = "9000";

    public synchronized static void setUserId(Integer userId) {
        if(userId!=null)
            UserId = userId;
    }

    private static Integer UserId=0;
   private static byte []  selectCommand = {(byte)0x00, (byte)0xA4, (byte)0x04,(byte) 0x00,(byte) 0x07, (byte)0xF0,0x01,0x02,0x03,0x04,0x05,0x06,0x00};
   private  static                  byte[] getData = {
           (byte) 0x00,  // CLA
           (byte) 0xB0,  // INS (Read Binary)
           (byte) 0x00,  // P1 (Offset High Byte)
           (byte) 0x00,  // P2 (Offset Low Byte)
           (byte) 0x04   // Le (Expected Length of Response Data)
   };
    ByteBuffer byteBuffer = ByteBuffer.allocate(4);
   @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
            if(Arrays.equals(commandApdu,selectCommand))
                return hexStringToByteArray(RESPONSE_SUCCESS);
            else if (Arrays.equals(getData,commandApdu)&& TokenManager.getInstance()!=null){

                    byteBuffer.putInt(UserId);
                    return byteBuffer.array();

                }
            else return hexStringToByteArray("6700");
    }

    @Override
    public void onDeactivated(int reason) {
        // Not needed for this example
    }

    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
