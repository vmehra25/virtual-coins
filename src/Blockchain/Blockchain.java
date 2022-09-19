package Blockchain;

import Miner.Miner;
import Miner.Client;
import Transaction.Transaction;
import Transaction.PayloadTransaction;
import Util.BlockChainUtility;
import Util.FileUtil;
import Util.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Integer.parseInt;

public class Blockchain {
    private List<Block> blockList;
    private int zeroCount;
    public static final int LOWER_TIME_BOUND = 150;
    public static final int UPPER_TIME_BOUND = 400;
    public static final int REWARD_COINS = 100;

    public Blockchain(){
        blockList = new ArrayList<Block>();
    }

    public List<Block> getBlockList() {
        return new ArrayList<>(blockList);
    }

    public void addBlocks(int numBlocks, int _zeroCount) {
        Client.invoke();
        zeroCount = _zeroCount;
        Block minedBlock = null;
        long startTime = System.currentTimeMillis();
        while (minedBlock == null) {
            minedBlock = addSingleBlock(zeroCount, new ArrayList<Transaction>());
        }
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime;
        BlockChainUtility.printBlock(minedBlock, diff);
        blockList.add(minedBlock);

        updateZeroCount(diff);

        for(int i = 1 ; i < numBlocks ; i++){
            List<PayloadTransaction> encryptedTransactions = Client.retrieveTransactions();
            List<Transaction> transactionsList = getDecryptedTransactions(encryptedTransactions);
            transactionsList = approveValidTransactions(transactionsList);
            minedBlock = null;
            Client.invoke();
            startTime = System.currentTimeMillis();
            while (minedBlock == null) {
                minedBlock = addSingleBlock(zeroCount, transactionsList);
            }
            endTime = System.currentTimeMillis();
            diff = endTime - startTime;
            if (updateBalance(minedBlock)) {
                blockList.add(minedBlock);
                BlockChainUtility.printBlock(minedBlock, diff);
                updateZeroCount(diff);
            }
        }

    }

    private List<Transaction> approveValidTransactions(List<Transaction> transactionsList) {
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction transaction: transactionsList){
            String senderId = transaction.getSenderId();
            String recieverId = transaction.getReceiverId();
            int rid = Integer.parseInt(recieverId);
            int coins = transaction.getCoins();
            int balance = FileUtil.getBalance(senderId);
            if(balance >= coins && rid >= 1 && rid <= Constants.NUM_MINERS) {
                list.add(transaction);
            }
        }
        return list;
    }

    private void updateZeroCount(long diff) {
        if(diff >= LOWER_TIME_BOUND && diff <= UPPER_TIME_BOUND) {
            System.out.println("Zeros remain the same");
        } else if(diff < LOWER_TIME_BOUND) {
            zeroCount++;
            System.out.println("Zeros were increased to " + zeroCount);
        } else {
            zeroCount--;
            System.out.println("Zeros were decreased to " + zeroCount);
        }
    }

    private List<Transaction> getDecryptedTransactions(List<PayloadTransaction> encryptedTransactions) {
        List<Transaction> list = new ArrayList<>();
        for(PayloadTransaction payloadTransaction: encryptedTransactions) {
            int clientId = payloadTransaction.getSenderId();
            byte[] encryptedTransaction = payloadTransaction.getEncryptedTransaction();
            PublicKey key = FileUtil.getDecryptionKey(clientId);
            try {
                String trans = FileUtil.decrypt(encryptedTransaction, key);
                String[] data = trans.split(" ");
                if(data[0].equals(String.valueOf(clientId))){
                    String senderId = data[0];
                    String receiverId = data[1];
                    int coins = Integer.parseInt(data[2]);
                    list.add(new Transaction(senderId, receiverId, coins));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void updateBalance(String clientId, int coins) {
        String curPath = FileUtil.getStorage();
        String folderName = "/Coins";
        String clientBalanceFile = "client" + clientId + ".txt";
        String filePath = curPath + "/" + folderName + "/" + clientBalanceFile;

        File minerBalanceFile = new File(filePath);
        try {
            if(!minerBalanceFile.exists() && minerBalanceFile.createNewFile()){

            } else if(minerBalanceFile.exists() && minerBalanceFile.isFile()){

            } else {
                return;
            }

            FileReader fileReader = new FileReader(filePath);
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = fileReader.read()) != -1){
                sb.append((char)ch);
            }
            fileReader.close();
            int balance = 0;
            if(sb.length() == 0){
                balance = coins;
            } else {
                balance = parseInt(sb.toString());
                balance += coins;
            }
            String balanceString = String.valueOf(balance);

            FileWriter fileWriter = new FileWriter(filePath);
            for(int i = 0 ; i < balanceString.length() ; i++){
                fileWriter.write(balanceString.charAt(i));
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean updateBalance(Block minedBlock) {
        updateBalance(minedBlock.getMinerId(), REWARD_COINS);
        for(Transaction transaction: minedBlock.getTransactionsList()) {
            updateBalance(transaction.getSenderId(), transaction.getCoins() * -1);
            updateBalance(transaction.getReceiverId(), transaction.getCoins());
        }
        return true;
    }

    private Block addSingleBlock(int zeroCount, List<Transaction> transactions) {
        transactions = getVerifiedTransactions(transactions);

        ExecutorService threadService = Executors.newFixedThreadPool(16);
        CompletionService<Block> completionService = new ExecutorCompletionService<>(threadService);
        Block curBlock = null;

        if(blockList.size() > 0){
            Block block = blockList.get(blockList.size() - 1);
            int _id = block.getId() + 1;
            long _timestamp = new Date().getTime();
            String _hashOfPrevBlock = block.getBlockHash();
            curBlock = new Block("", _timestamp, _id, _hashOfPrevBlock, 0, transactions);
        }else{
            curBlock = new Block("", new Date().getTime(), 0, "0", 0, transactions);
        }

        for (int i = 1 ; i <= 16 ; i++){
            completionService.submit(new Miner(i, curBlock, zeroCount));
        }

        try {
            Future<Block> nextBlock = completionService.poll(65, TimeUnit.SECONDS);
            Block ans = nextBlock == null ? null : nextBlock.get();
            threadService.shutdownNow();
            return ans;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            threadService.shutdownNow();
        }

        return null;
    }

    private List<Transaction> getVerifiedTransactions(List<Transaction> transactions) {
        List<Transaction> list = new ArrayList<>();
        for (Transaction transaction: transactions) {
            String sender = transaction.getSenderId();
            String receiver = transaction.getReceiverId();
            int coins = transaction.getCoins();

            String curPath = Path.of("").toAbsolutePath().toString() + "/src";
            String folderName = "Storage/Coins";
            String clientBalance = "client" + sender + ".txt";
            String filePath = curPath + "/" + folderName + "/" + clientBalance;

            File minerBalanceFile = new File(filePath);
            if(!minerBalanceFile.exists() || !minerBalanceFile.isFile()){
                continue;
            }
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(filePath);
                int ch;
                StringBuilder sb = new StringBuilder();
                while ((ch = fileReader.read()) != -1){
                    sb.append((char)ch);
                }
                fileReader.close();
                int balance = Integer.parseInt(sb.toString());
                int receiverId = Integer.parseInt(receiver);
                if(balance >= coins && receiverId >= 1 && receiverId <= 16 && coins != 0) {
                    list.add(transaction);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
