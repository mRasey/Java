package Bank;


public class CreditCardAccount extends Account{
	//OVERVIEW:信用卡帐户有最高透支额度
	//消费不能超出额度
    private double overdraftLimit;

    public CreditCardAccount(String id,String name,String pwd){
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: constructor
        super(id,name,pwd);
        this.setBalance(0);
    }

    public void setMax(double money){
        //REQUIRES: none
        //MODIFIED: overdraftLimit
        //EFFECTS: set the overdraftLimit to money
        this.overdraftLimit=money;
    }

    public double getMax() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return overdraftLimit
        return this.overdraftLimit;
    }


    @Override
    public boolean drawMoney(double money) {
        //REQUIRES: none
        //MODIFIED: change the balance of this account
        //EFFECTS: sub money from the balance only if the money is bigger than zero and smaller than balance
        double balance=this.getBalance();
        if(!this.getIsActivate()) return false;
        if (balance + this.overdraftLimit < money)
            return false;
        this.setBalance(balance-money);
        return true;
    }

    @Override
    public boolean transferMoney(String AccountId,String name,double money,AccountSet accounts) throws NotFoundException {
        //REQUIRES: accountSet <> null
        //MODIFIED: balance
        //EFFECTS: move money from this account to the account which accountId
                // is equals accountId,if can move, return true, else return false.
        if(this.getBalance()<0)
            return false;
        else if (money - this.getBalance() > this.overdraftLimit)
            return false;
        else {

        }
        return super.transferMoney(AccountId, name, money, accounts);
    }

    public boolean reqOk() {
        /*
        code continue
        */
        if(!super.reqOk())
            return false;
        if(overdraftLimit < 0) return false;
        return true;
    }
    /*
    不变式：
    super.reqok() == true && overdraftLimit >= 0
     */
}