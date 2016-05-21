package Bank;

public class test {

	/**
	 * @param args
	 */
	public static AccountSet accountset = new AccountSet();
	
	public static void main(String[] args) throws NotFoundException {
		// TODO Auto-generated method stub
        /*
		code continue
		*/
		Account creditCardAccount =new CreditCardAccount("01","jane","123");
		accountset.insert(creditCardAccount);
		Account debitCardAccount =new DebitCardAccount("02","greece","123");
		Account debitCardAccount2 =new DebitCardAccount("03","123","123");
		accountset.insert(debitCardAccount);
		creditCardAccount.depositMoney(1000);//success
		creditCardAccount.depositMoney(-1);//fail
        creditCardAccount.drawMoney(1000);//success
        creditCardAccount.drawMoney(-1);//fail
        creditCardAccount.depositMoney(1000);//success
        creditCardAccount.transferMoney("1", "greece", 1000, accountset);//success
        creditCardAccount.transferMoney("1", "greece", 1000, accountset);//fail

        debitCardAccount.depositMoney(1000);//success
        debitCardAccount.depositMoney(-1);//fail
        debitCardAccount.drawMoney(1000);//success
        debitCardAccount.drawMoney(-1);//fail
        debitCardAccount.depositMoney(1000);//success
        debitCardAccount.transferMoney("0", "jane", 1000, accountset);//success
        debitCardAccount.transferMoney("0", "jane", 1000, accountset);//fail

        System.out.println(accountset.isIn(creditCardAccount));//success
        System.out.println(accountset.isIn(debitCardAccount2));//fail
        System.out.println(accountset.queryByUserId(debitCardAccount.getUserId()));//success
        System.out.println(accountset.queryByUserId(creditCardAccount.getUserId()));//success
        System.out.println(accountset.queryByAccountId(debitCardAccount.getAccountId()));//success
        System.out.println(accountset.queryByAccountId(creditCardAccount.getAccountId()));//success

        System.out.println(creditCardAccount.getBalance());
        System.out.println(creditCardAccount.getIsActivate());
        System.out.println(creditCardAccount.getPwd());
        System.out.println(creditCardAccount.getBalance());
        System.out.println(debitCardAccount.getIsActivate());
        System.out.println(debitCardAccount.getPwd());

		
	}

}
