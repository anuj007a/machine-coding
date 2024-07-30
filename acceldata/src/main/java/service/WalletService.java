package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletService {

    private Map<Long, Accounts> accounts;

    public WalletService(){
        this.accounts = new HashMap<>();
    }


    public void createWallet(UserAccount userAccount){
        if( accounts.containsKey(userAccount.getMobile())){
            System.out.println("Wallet already exit with mobile " + userAccount.getMobile());
            return;
        }
        Accounts accounts1 = new Accounts();
        accounts1.setUserAccount(userAccount);

        // account details
        AccountDetails accountDetails =  new AccountDetails();
        accountDetails.setAccountNumber(generateAccountNumber());
        accountDetails.setBalance(100.00);
        accounts1.setAccountDetails(accountDetails);

        AccountStatement accountStatement = logTransaction(100.00,"Initial balance",null, null,TransactionType.CREDIT);
        List<AccountStatement> a = new ArrayList<>();
        a.add(accountStatement);
        accounts1.setAccountStatement(a);
        accounts.put(userAccount.getMobile(), accounts1);
    }

    public boolean tranferBalance(long senderMobile, long receiverMobile, double amount){
        if(!accounts.containsKey(senderMobile)){
            System.out.println("Sender account doesn't exit with mobile " + senderMobile );
            return false;
        }
        if(!accounts.containsKey(receiverMobile)){
            System.out.println("Receiver account doesn't exit with mobile " + receiverMobile );
            return false;
        }

        Accounts senderDetails = accounts.get(senderMobile);
        AccountDetails sendeAccountDetails = senderDetails.getAccountDetails();
        if(sendeAccountDetails.getBalance()<amount){
            System.out.println("Not enough balance in user account " + receiverMobile );
            return false;
        }
        double b = sendeAccountDetails.getBalance();
        b = b - amount;
        sendeAccountDetails.setBalance(b);


        Accounts receiverDetails = accounts.get(receiverMobile);
        AccountDetails receiverAccountDetails = receiverDetails.getAccountDetails();
        receiverAccountDetails.setBalance(receiverAccountDetails.getBalance()+amount);

        List<AccountStatement> s1 = senderDetails.getAccountStatement();
        s1.add(logTransaction( amount, "Transfer amount", senderDetails.getUserAccount().getName(),null ,TransactionType.CREDIT ));
        List<AccountStatement> r1 = receiverDetails.getAccountStatement();
        r1.add(logTransaction( amount, "Transfer amount",null , receiverDetails.getUserAccount().getName(),TransactionType.DEBIT ));

        senderDetails.setAccountStatement(s1);
        receiverDetails.setAccountStatement(r1);

return true;

    }

    public List<AccountStatement> getStatmenet(long mobile){
        if(!accounts.containsKey(mobile)){
            System.out.println("Account doesn't exit with mobile " + mobile );
            return null;
        }
        return accounts.get(mobile).getAccountStatement();
    }

    private int generateAccountNumber(){
            return accounts.size()+1;
    }

    /*
     private Long epoch;
    private TransactionType transactionType;
    private double amount;
    private String remark;
    private String creditFromDetails;
    private String debitFromDetails;

     */
    private AccountStatement logTransaction(double amount,  String remark, String creditFromDetails, String debitFromDetails, TransactionType transactionType){
        AccountStatement accountStatement = new AccountStatement();
        accountStatement.setAmount(amount);
        accountStatement.setRemark(remark);
        accountStatement.setCreditFromDetails(creditFromDetails);
        accountStatement.setDebitFromDetails(debitFromDetails);
        accountStatement.setTransactionType(transactionType);
        return accountStatement;

    }

}
