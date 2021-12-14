package Util;

import Blockchain.Blockchain;
import Blockchain.Block;
import Transaction.Transaction;

public class BlockChainUtility {
    public static void printBlockChain(Blockchain blockchain) {
        for(Block block: blockchain.getBlockList()){
            printBlock(block);
        }
    }

    public static void printBlock(Block block) {
        System.out.println("#######################");
        System.out.println("Block Id:" + block.getId());
        System.out.println("Timestamp create:" + block.getTimeStamp());
        System.out.println("Created by Miner:" + block.getMinerId());
        System.out.println("Previous Hash:" + block.getHashOfPrevBlock());
        System.out.println("Block Hash:" + block.getBlockHash());
        System.out.println("Magic Number:" + block.getMagicNumber());
        for(Transaction transaction: block.getTransactionsList()) {
            printTransaction(transaction);
        }
        System.out.println();
    }

    private static void printTransaction(Transaction transaction) {
        System.out.println("Miner#" + transaction.getSenderId() + " sends Miner#" + transaction.getReceiverId() + " " + transaction.getCoins() + " virtual coins");
    }

    public static void printBlock(Block block, long timeTaken) {
        System.out.println();
        System.out.println("#######################");
        System.out.println("Block Id:" + block.getId());
        System.out.println("Timestamp create:" + block.getTimeStamp());
        System.out.println("Created by Miner:" + block.getMinerId());
        System.out.println("Previous Hash:" + block.getHashOfPrevBlock());
        System.out.println("Block Hash:" + block.getBlockHash());
        System.out.println("Magic Number:" + block.getMagicNumber());
        if (block.getTransactionsList().size() > 0) {
            System.out.println("Transactions");
        }
        for(Transaction transaction: block.getTransactionsList()) {
            printTransaction(transaction);
        }
        System.out.println("Time taken:" + timeTaken / 1000 + "." + (timeTaken % 1000) + " seconds" );
        System.out.println("#######################");
        System.out.println();
    }
}
