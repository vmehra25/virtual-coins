package Miner;

import Blockchain.Block;
import Util.Constants;
import Util.Hash;

import java.util.Random;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {
    private int id;
    private Block block;
    private int zeros;

    public Miner(int id, Block block, int zeros) {
        this.id = id;
        this.block = block;
        this.zeros = zeros;
    }

    @Override
    public Block call() throws Exception {
        Random random = new Random();
        String candidateHash = "";
        Block guess = new Block(""+id, block.getTimeStamp(), block.getId(), block.getHashOfPrevBlock(),0,block.getTransactionsList());
        do {
            int magicNum = random.nextInt(Constants.RANDOM_BOUND);
            guess.setMagicNumber(magicNum);
            candidateHash = guess.getBlockHash();
        } while(!Hash.checkHashHasValidZeros(candidateHash, zeros));
        return guess;
    }
}
