package Transaction;

public class PayloadTransaction {
    private final byte[] encryptedTransaction;
    private final int senderId;

    public PayloadTransaction(byte[] encryptedTransaction, int senderId) {
        this.encryptedTransaction = encryptedTransaction;
        this.senderId = senderId;
    }

    public byte[] getEncryptedTransaction() {
        return encryptedTransaction;
    }

    public int getSenderId() {
        return senderId;
    }
}
