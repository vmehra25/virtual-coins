import Blockchain.Blockchain;
import Util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;

import static Util.FileUtil.generateDirectories;
import static Util.FileUtil.generateKeyValuePairs;

public class Main {

    public static void main(String[] args) {
        initializeAll();
        Blockchain blockchain = new Blockchain();
        blockchain.addBlocks(10, 4);
        System.exit(0);
    }

    private static void initializeAll() {
        if(generateDirectories())
            generateKeyValuePairs();
        else
            System.exit(0);
    }
}
