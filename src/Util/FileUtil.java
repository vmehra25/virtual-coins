package Util;

import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class FileUtil {
    public static String getPublicKeyPath(int clientID) {
        return getPublicFolder() + "/Client" + clientID;
    }

    public static String getPrivateKeyPath(int clientID) {
        return getPrivateFolder() + "/Client" + clientID;
    }

    public static String getPrivateFolder() {
        String privateFolder = "private";
        return getStorage() + "/" + privateFolder;
    }

    public static String getPublicFolder() {
        String publicFolder = "public";
        return getStorage() + "/" + publicFolder;
    }

    public static String getStorage() {
        String curPath = Path.of("").toAbsolutePath().toString() + "/src";
        String folderName = "Storage";
        return curPath + "/" + folderName;
    }


    public static byte[] encrypt (String plainText, PublicKey publicKey ) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes()) ;
        return cipherText;
    }

    public static String decrypt (byte[] cipherTextArray, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);
        return new String(decryptedTextArray);
    }

    public static PrivateKey getDecryptionKey(int clientId) {
        String privateKeyPath = getPrivateKeyPath(clientId) + ".key";
        Path path = Paths.get(privateKeyPath);
        PrivateKey privateKey = null;
        try {
            byte[] bytes = Files.readAllBytes(path);
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(ks);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return  privateKey;
    }
}
