package Util;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import static java.lang.Integer.parseInt;

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


    public static byte[] encrypt (String plainText, PrivateKey privateKey ) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(plainText.getBytes()) ;
        return cipherText;
    }

    public static String decrypt (byte[] cipherTextArray, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);
        return new String(decryptedTextArray);
    }

    public static PublicKey getDecryptionKey(int clientId) {
        String publicKeyPath = getPublicKeyPath(clientId) + ".pub";
        Path path = Paths.get(publicKeyPath);
        PublicKey publicKey = null;
        try {
            byte[] bytes = Files.readAllBytes(path);
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(ks);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return  publicKey;
    }

    public static boolean generateDirectories() {
        String publicPath = FileUtil.getPublicFolder();
        String privatePath = FileUtil.getPrivateFolder();
        File publicDir = new File(publicPath);
        boolean isCreated = publicDir.mkdirs();
        File privateDir = new File(privatePath);
        isCreated = isCreated && privateDir.mkdirs();

        String CoinsFolder = FileUtil.getStorage() + "/Coins";
        File coinsFolder = new File(CoinsFolder);
        isCreated = coinsFolder.mkdirs();
        return (publicDir.exists() && privateDir.exists() && coinsFolder.exists());
    }

    public static void generateKeyValuePairs() {
        try {
            for(int i = 1 ; i <= Constants.NUM_MINERS ; i++) {
                SecureRandom secureRandom = new SecureRandom();
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048, secureRandom);
                KeyPair kp = kpg.generateKeyPair();
                Key pub = kp.getPublic();
                Key pvt = kp.getPrivate();

                String outFile = FileUtil.getPrivateKeyPath(i);

                FileOutputStream out = new FileOutputStream(outFile + ".key");
                out.write(pvt.getEncoded());
                out.close();

                outFile = FileUtil.getPublicKeyPath(i);

                out = new FileOutputStream(outFile + ".pub");
                out.write(pub.getEncoded());
                out.close();
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    public static int getBalance(String clientId) {
        String path = getStorage() + "/Coins/client" + clientId + ".txt";
        File file = new File(path);
        if(!file.exists() || !file.isFile()){
            return 0;
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = fileReader.read()) != -1){
                sb.append((char)ch);
            }
            fileReader.close();
            int balance = 0;
            if(sb.length() == 0){
                balance = 0;
            } else {
                balance = parseInt(sb.toString());
            }
            return balance;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
