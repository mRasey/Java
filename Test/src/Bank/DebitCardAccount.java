package Bank;


public class DebitCardAccount extends Account {
	//OVERVIEW:储蓄帐户与年存款利率有关
	//存款时计算利息
    private double annualInterestRate;
    
    
    public DebitCardAccount(String id,String name,String pwd) {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: constructor
    	super(id,name,pwd);
    	this.setBalance(10.0);
    }
    
    public void changeStatus() {
        //REQUIRES: none
        //MODIFIED: isActivate
        //EFFECTS: if balance < 10; set isActivate to false, else set isActivate to true
    	if(this.getBalance()<10)
    		this.setIsActivate(false);
    	else 
    		this.setIsActivate(true);
    }
	
        //override
    public boolean depositMoney(double money) {
        //REQUIRES: none
        //MODIFIED: the balance of this account
        //EFFECTS: add money to the balance only if the money is bigger than zero
    	if (money <= 0) 	
			return false;    	
    	double balance=this.getBalance();
		balance += money*(1+annualInterestRate);
		this.setBalance(balance);
		changeStatus();
		return true;
    
    	}
    //override   
    public boolean drawMoney(double money,String pwd) {
        //REQUIRES: none
        //MODIFIED: change the balance of this account
        //EFFECTS: sub money from the balance only if the money is bigger than zero and smaller than balance
    	if (money <= 0) 	
			    return false;
    	if(!this.getIsActivate()) return false;
       	if(!(this.getPwd().equals(pwd))) return false;
		if (this.getBalance()-10< money)
				return false;
		this.setBalance(this.getBalance()-money);
	    return true;
	}
    
    public boolean transferMoney(String AccountId,String name,double money,AccountSet accounts) throws NotFoundException {
        //REQUIRES: accountSet <> null
        //MODIFIED: balance
        //EFFECTS: move money from this account to the account which accountId
                // is equals accountId,if can move, return true, else return false.
    	if (this.getBalance()-10< money)
				return false;
    	else 
			return super.transferMoney(AccountId, name, money, accounts);
    }
    
	
    
    public void setRate(double interestRate){
        //REQUIRES: interestRate
        //MODIFIED: annualInterestRate
        //EFFECTS: change the annualInterestRate to interestRate
  		this.annualInterestRate=interestRate;
  	}
  	
  	public double getRate() {
        //REQUIRES: none
        //MODIFIED: none
        //EFFECTS: return the annualInterestRate
  		return this.annualInterestRate;
  	}
  	public boolean reqOk(){
  		/*
		code continue
		*/
        if(!super.reqOk())
            return false;
        if(annualInterestRate < 0) return false;
  		return true;
  	}
    /*
    不变式:
     super.reqOK() == true && annualInterestRate >= 0
     */
}

