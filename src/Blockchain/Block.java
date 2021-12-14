package Blockchain;

import Transaction.Transaction;
import Util.Hash;

import java.util.List;

public class Block {
    private String minerId;
    private final long timeStamp;
    private final int id;
    private final String hashOfPrevBlock;
    private int magicNumber;
    private List<Transaction> transactionsList;
    private String curHash;

    public Block(String minerId, long timeStamp, int id, String hashOfPrevBlock, int magicNumber, List<Transaction> transactionsList) {
        this.minerId = minerId;
        this.timeStamp = timeStamp;
        this.id = id;
        this.hashOfPrevBlock = hashOfPrevBlock;
        this.magicNumber = magicNumber;
        this.transactionsList = transactionsList;
        curHash = getBlockHash();
    }

    public String getCurHash() {
        curHash = getBlockHash();
        return curHash;
    }

    public String getMinerId() {
        return minerId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getId() {
        return id;
    }

    public String getHashOfPrevBlock() {
        return hashOfPrevBlock;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public String getBlockHash() {
        return Hash.getSha256Hash(toString());
    }

    @Override
    public String toString() {
        return "Block{" +
                "minerId='" + minerId + '\'' +
                ", timeStamp=" + timeStamp +
                ", id=" + id +
                ", hashOfPrevBlock='" + hashOfPrevBlock + '\'' +
                ", magicNumber=" + magicNumber +
                ", transactionsList=" + transactionsList +
                '}';
    }

    public void setMagicNumber(int magicNum) {
        this.magicNumber = magicNum;
    }
}
