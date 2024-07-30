package model;

import java.util.List;

public class Accounts {
    private UserAccount userAccount;
    private AccountDetails accountDetails;
    private List<AccountStatement> accountStatement;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    public List<AccountStatement> getAccountStatement() {
        return accountStatement;
    }

    public void setAccountStatement(List<AccountStatement> accountStatement) {
        this.accountStatement = accountStatement;
    }
}