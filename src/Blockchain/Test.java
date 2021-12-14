package Blockchain;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

import static Util.FileUtil.decrypt;
import static Util.FileUtil.encrypt;

public class Test {
    public static void main(String[] args) {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        Key pvt = kp.getPrivate();


        String plainText = 1 + " " + "chair.namenskalsdn ams dkas mdkla   sm kdmas";


        // Encryption
        byte[] cipherTextArray = new byte[0];
        try {
            cipherTextArray = encrypt(plainText, kp.getPublic());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
        System.out.println("Encrypted Text : "+encryptedText);


        // Decryption
        String decryptedText = null;
        try {
            decryptedText = decrypt(cipherTextArray, kp.getPrivate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DeCrypted Text : "+decryptedText);
    }






    private class Chair {
        public int id;
        public String name;
        public Chair(int _id, String _name) {
            id = id;
            name = _name;
        }
    }
}





/*
* I am trying to encrypt and decrypt an object in java using public and private keys,
tried to create a single string from the object and then encrypt and then decrypt. But facing BadPaddingException.

My approach -
```
public class Chair {
    public int id;
    public String name;
    public Chair(int _id, String _name) {
        id = id;
        name = _name;
    }
}


public class Main {

    public static void main(String[] args) {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        Key pvt = kp.getPrivate();

        Chair chair = new Chair(1, "chair1");
        String plainText = chair.id + " " + chair.name;


        // Encryption
        byte[] cipherTextArray = encrypt(plainText, publicKey);
        String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
        System.out.println("Encrypted Text : "+encryptedText);


        // Decryption
        String decryptedText = decrypt(cipherTextArray, privateKey);
        System.out.println("DeCrypted Text : "+decryptedText);

        String[] data = decryptedText.split(" ");
        Chair decryptedChair = new Chair(Integer.parseInt(data[0]), data[1]);
    }

    public static byte[] encrypt (String plainText, PublicKey publicKey ) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes()) ;
        return cipherText;
    }

    public static String decrypt (byte[] cipherTextArray, PrivateKey privateKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);
        return new String(decryptedTextArray);
    }

}

```

I want to know what is the correct way to encrypt and decrypt an object.
* */