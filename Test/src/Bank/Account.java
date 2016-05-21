package Bank;

public class Account {

	private String AccountId;
	private String UserId;
	private String name;
	private String pwd;
	private double balance=0.0;
	private boolean isActivate=true;
	static int counter = 0;
		
		
    public Account(String id, String name, String pwd) {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: constructor
		this.AccountId = String.valueOf(counter++);
		this.UserId=id;
		this.name=name;
		this.pwd=pwd;
		this.isActivate=true;
    }
		

	public String getAccountId() {
		//REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the id of this account
		return AccountId;
	}

	public void setAccountId(String accountId) {
        //REQUIRES: accountId <> null
        //MODIFIED: accountId of this class
        //EFFECTS: change accountId to accountId
		this.AccountId = accountId;
	}

	public String getUserId() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the id of user
		return UserId;
	}

	public void setUserId(String id) {
        //REQUIRES: id <> null
        //MODIFIED: userId of this class
        //EFFECTS: change userId to id
		this.UserId = id;
	}
	
	public String getName() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the name of this account
		return name;
	}

	public void setName(String id) {
        //REQUIRES: id <> null
        //MODIFIED: the name of this account
        //EFFECTS: change the name of account to name
		this.name = name;
	}
	public String getPwd() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the pwd of this class
		return pwd;
	}

	public void setPwd(String pwd) {
        //REQUIRES: pwd <> null
        //MODIFIED: the pwd of this class
        //EFFECTS: change pwd of this class to pwd
		this.pwd = pwd;
	}

	
	public double getBalance() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the balance of this class
		return this.balance;
	}

	public void setBalance(double balance) {
        //REQUIRES: balance <> null
        //MODIFIED: the balance of this class
        //EFFECTS: change the balance of this class to balance
		this.balance = balance;
	}

	public boolean getIsActivate() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the isActivate of this class
		return isActivate;
	}

	public void setIsActivate(boolean isActivate) {
        //REQUIRES: isActivate <> null
        //MODIFIED: isActivate of this class
        //EFFECTS: change isActivate of this class to isActivate
		this.isActivate = isActivate;
	}
	
		
	public boolean depositMoney(double money) {
        //REQUIRES: none
        //MODIFIED: the balance of this account
        //EFFECTS: add money to the balance only if the money is bigger than zero
		if (money <= 0) 	
			return false;
		this.balance += money;
		return true;
	}

	public boolean drawMoney(double money) {
        //REQUIRES: none
        //MODIFIED: change the balance of this account
        //EFFECTS: sub money from the balance only if the money is bigger than zero and smaller than balance
		if (money <= 0) 	
			    return false;
		if(!this.getIsActivate()) return false;
		if (this.balance< money)
				return false;
		this.balance -= money;
	    return true;
	}

    public boolean transferMoney(String AccountId, String name, double money, AccountSet accounts) throws NotFoundException {
        //REQUIRES: accountSet <> null
        //MODIFIED: balance
        //EFFECTS: move money from this account to the account which accountId
            // is equals accountId,if can move, return true, else return false.
        if (!this.getIsActivate())
            return false;
        Account target = accounts.queryByAccountId(AccountId);
        if (target != null && target.getName().equals(name)) {
            this.balance -= money;
            target.setBalance(target.getBalance() + money);
            return true;
        }
        return false;
    }

    public String toString(){
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the accountId and UserId and name and isActivate
	  	return ("AccountId:"+AccountId+"UserId:"+UserId+"Name:"+name+"isActivate:"+isActivate);
	}
	public boolean reqOk(){
        if (UserId == null || pwd == null || AccountId == null || name == null)
            return false;
        if(balance < 0 || counter < 0) return false;
		return true;	
	}
    /*
    不变式：
    UserId <> null && pwd <> null && AccountId <> null && name <> null && balance >= 0
    && counter >= 0
     */


}
