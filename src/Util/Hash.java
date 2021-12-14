package Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hash {
    public static boolean checkHashHasValidZeros(String hash, int n){
        if(n == 0){
            return true;
        }
        if(hash.length() <= n){
            return false;
        }
        for(int i = 0 ; i < n ; i++){
            if(hash.charAt(i) != '0'){
                return false;
            }
        }
        if(hash.charAt(n) == '0'){
            return false;
        }
        return true;
    }

    public static String getSha256Hash(String block){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(block.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash256 = new StringBuilder();
            for(byte element: hash){
                String curHash = Integer.toHexString(0xff & element);
                if(curHash.length() == 1){
                    hash256.append("0");
                }
                hash256.append(curHash);
            }
            return hash256.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
