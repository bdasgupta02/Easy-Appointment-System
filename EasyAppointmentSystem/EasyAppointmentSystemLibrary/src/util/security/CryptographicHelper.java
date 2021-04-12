package util.security;

import java.security.MessageDigest;


public class CryptographicHelper {
    
    private static CryptographicHelper cryptographicHelper = null;   
    private static final String MD5_ALGORITHM_NAME = "MD5";
    
    public CryptographicHelper() {
    }
    
    public static CryptographicHelper getInstance() {
        if (cryptographicHelper == null) {
            cryptographicHelper = new CryptographicHelper();
        }
        
        return cryptographicHelper;
    }
    
    public byte[] doMD5Hashing(String stringToHash) {
        MessageDigest md = null;
        
        try {
            md = MessageDigest.getInstance(MD5_ALGORITHM_NAME);
            return md.digest(stringToHash.getBytes());
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public String byteArrayToHexString(byte[] bytes) {
        int lo = 0;
        int hi = 0;
        String hexString = "";

        for (int i = 0; i < bytes.length; i++) {
            lo = bytes[i];
            lo = lo & 0xff;
            hi = lo >> 4;
            lo = lo & 0xf;
            
            hexString += numToString(hi);
            hexString += numToString(lo);                                    
        }
        
        return hexString;
    }
    
    private String numToString(int num) {
        if (num == 0) return "0";
        else if (num == 1) return "1";
        else if (num == 2) return "2";
        else if (num == 3) return "3";
        else if (num == 4) return "4";
        else if (num == 5) return "5";
        else if (num == 6) return "6";
        else if (num == 7) return "7";
        else if (num == 8) return "8";
        else if (num == 9) return "9";
        else if (num == 10) return "a";
        else if (num == 11) return "b";
        else if (num == 12) return "c";
        else if (num == 13) return "d";
        else if (num == 14) return "e";
        else if (num == 15) return "f";
        else return "";            
    }
    
}
