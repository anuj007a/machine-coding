import model.Accounts;
import model.UserAccount;
import service.WalletService;

public class TestWallet {

    public static void main(String[] args) {

        WalletService walletService = new WalletService();
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail("test_1@data.com");
        userAccount.setName("Anuj");
        userAccount.setMobile(9876543210l);
        userAccount.setUsername("anuj_1");
        walletService.createWallet(userAccount);
        walletService.getStatmenet(userAccount.getMobile());
    }
}
