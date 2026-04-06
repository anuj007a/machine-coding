package com.wraith;

import com.wraith.model.LedgerEntry;
import com.wraith.model.User;
import com.wraith.repository.LedgerRepository;
import com.wraith.repository.UserRepository;
import com.wraith.service.impl.WalletServiceImpl;

import java.util.List;

public class WalletServiceDoublePaymentDriver {
    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        LedgerRepository ledgerRepo = new LedgerRepository();
        WalletServiceImpl service = new WalletServiceImpl(userRepo, ledgerRepo);
        userRepo.save(new User("1", "Alice", "A1"));
        userRepo.save(new User("2", "Bob", "B1"));
        userRepo.save(new User("3", "Charlie", "C1"));
        service.deposit("1", 10000.0);
        service.transfer("1", "2", 3000.0);
        service.transfer("2", "3", 2000.0);
        service.withdraw("3", 500.0);
        System.out.println(service.getBalance("1")); // 7000.0
        System.out.println(service.getBalance("2"));
        System.out.println(service.getBalance("3"));
        try {
            service.transfer("1", "2", 8000.0); // Should fail
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); // Insufficient balance
        }
        List<LedgerEntry> userStatement = service.getUserStatements("1");
        System.out.println(userStatement);
        /*
        1. Create users: Alice, Bob, Charlie

2. Alice deposits 10,000       → Alice balance: 10,000
3. Bob deposits 5,000          → Bob balance: 5,000
4. Alice transfers 3,000 to Bob → Alice: 7,000 | Bob: 8,000
5. Bob transfers 2,000 to Charlie → Bob: 6,000 | Charlie: 2,000
6. Charlie withdraws 500       → Charlie: 1,500

7. Alice transfers 8,000 to Bob
   → Should FAIL (Alice only has 7,000)
   → No ledger entries created

8. Verify user balances:
   → Alice (7,000) + Bob (6,000) + Charlie (1,500) = 14,500
   → = Total deposited (15,000) - Total withdrawn (500) ✓

9. Pull Alice's statement. It should look something like:

   | Type   | Amount | Counterparty | Running Balance |
   | CREDIT | 10,000 | ???          | 10,000          |
   | DEBIT  | 3,000  | Bob          | 7,000
         */
    }
}