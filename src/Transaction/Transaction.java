package Transaction;

public class Transaction {
    private String senderId;
    private String receiverId;
    private int coins;

    public Transaction(String senderId, String receiverId, int coins) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.coins = coins;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getCoins() {
        return coins;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", coins=" + coins +
                '}';
    }
}
