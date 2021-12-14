package Miner;

import Transaction.PayloadTransaction;
import Util.FileUtil;

import java.io.File;
import java.io.FileReader;
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
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Client implements Callable<PayloadTransaction> {
    private final int clientId;
    public static final int NUM_CLIENTS = 16;
    private static CompletionService<PayloadTransaction> currentService;
    private static ExecutorService executorService;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Client(int _id){
        clientId = _id;
        String publicKeyPath = FileUtil.getPublicKeyPath(clientId) + ".pub";
        String privateKeyPath = FileUtil.getPrivateKeyPath(clientId) + ".key";
        try {
            Path path = Paths.get(privateKeyPath);
            byte[] bytes = Files.readAllBytes(path);
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(ks);

            path = Paths.get(publicKeyPath);
            bytes = Files.readAllBytes(path);

            X509EncodedKeySpec xks = new X509EncodedKeySpec(bytes);
            KeyFactory xkf = KeyFactory.getInstance("RSA");
            publicKey = xkf.generatePublic(xks);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public static void invoke() {
        ExecutorService service = Executors.newFixedThreadPool(16);
        CompletionService<PayloadTransaction> completionService = new ExecutorCompletionService<>(service);
        currentService = completionService;
        executorService = service;
        for(int i = 0 ; i < NUM_CLIENTS ; i++){
            completionService.submit(new Client(i+1));
        }
    }

    public static List<PayloadTransaction> retrieveTransactions(){
        Future<PayloadTransaction> transactions;
        List<PayloadTransaction> clientTransactions = new ArrayList<>();
        while((transactions = currentService.poll()) != null){
            try {
                clientTransactions.add(transactions.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
        currentService = null;
        executorService = null;
        return clientTransactions;
    }

    @Override
    public PayloadTransaction call() throws Exception {
        Random random = new Random();
        int otherClient = random.nextInt(16) + 1;
        int balance = getAvailableBalance();
        int coins = random.nextInt(balance);
        int randomSleeptime = (int)(random.nextDouble() * 5.0);
        randomSleeptime *= 1000;
        String plainTransaction = clientId + " " + otherClient + " " + coins;
        byte[] encryptedTransaction = FileUtil.encrypt(plainTransaction, publicKey);
        Thread.sleep(randomSleeptime);
        return new PayloadTransaction(encryptedTransaction, clientId);
    }

    private int getAvailableBalance() {
        String curPath = Path.of("").toAbsolutePath().toString() + "/src";
        String folderName = "Storage/Coins";
        String clientBalance = "client" + clientId + ".txt";
        String filePath = curPath + "/" + folderName + "/" + clientBalance;


        File minerBalanceFile = new File(filePath);
        if(!minerBalanceFile.exists() || !minerBalanceFile.isFile()){
            return 1;
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
            if(balance == 0){
                balance++;
            }
            return balance;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
