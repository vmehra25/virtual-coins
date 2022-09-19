import Blockchain.Blockchain;
import Util.Constants;

import static Util.FileUtil.generateDirectories;
import static Util.FileUtil.generateKeyValuePairs;

public class Main {

    public static void main(String[] args) {
        initializeAll();
        Blockchain blockchain = new Blockchain();
        blockchain.addBlocks(Constants.NUM_BLOCKS, Constants.ZERO_COUNT);
        System.exit(0);
    }

    private static void initializeAll() {
        if(generateDirectories())
            generateKeyValuePairs();
        else
            System.exit(0);
    }
}
