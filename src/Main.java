import Blockchain.Blockchain;
import Util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Main {
    private static final int NUM_MINERS = 16;

    public static void main(String[] args) {
        initializeAll();
        Blockchain blockchain = new Blockchain();
        blockchain.addBlocks(10, 4);
        System.exit(0);

        /*
        * Private key format: PKCS#8
        * Public key format: X.509
        * */
    }

    private static void initializeAll() {
        if(generateDirectories())
            generateKeyValuePairs();
        else
            System.exit(0);
    }

    private static boolean generateDirectories() {
        String publicPath = FileUtil.getPublicFolder();
        String privatePath = FileUtil.getPrivateFolder();
        File publicDir = new File(publicPath);
        boolean isCreated = publicDir.mkdir();
        File privateDir = new File(privatePath);
        isCreated = isCreated && privateDir.mkdir();

        String CoinsFolder = FileUtil.getStorage() + "/Coins";
        File coinsFolder = new File(CoinsFolder);
        isCreated = coinsFolder.mkdir();
        return (publicDir.exists() && privateDir.exists() && coinsFolder.exists());
    }

    private static void generateKeyValuePairs() {
        try {
            for(int i = 1 ; i <= NUM_MINERS ; i++) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
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
}
