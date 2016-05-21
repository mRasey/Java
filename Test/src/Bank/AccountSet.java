package Bank;

import java.util.HashSet;
import java.util.Iterator;

public class AccountSet extends Exception{
//OVERVIEW:帐户的集合，不区别帐户类别
//
    private HashSet<Account> accounts = new HashSet<>();
   //constructors
   public AccountSet(){
       //EFFECTS:Initializes this to be empty
       accounts.clear();
   }

   //methods
   public void insert(Account a) {
      //MODIFIES:this
      //EFFECTS:Adds a to the elements of this
        accounts.add(a);
   }
   
   public void removeByAccountId(String id) {
      //MODIFIES:this
      //EFFECTS:remove the account(identified by String ID) from this
        Iterator<Account> iterator = accounts.iterator();
       while(iterator.hasNext()){
           Account account = iterator.next();
           if(account.getAccountId().equals(id)){
               iterator.remove();
               break;
           }
       }
   }
   
   public void removeByUserId(String id) {
      //MODIFIES:this
      //EFFECTS:remove all the accounts which are attached to user (identified by String ID) from this
       Iterator<Account> iterator = accounts.iterator();
       while(iterator.hasNext()){
           Account account = iterator.next();
           if(account.getUserId().equals(id)){
               iterator.remove();
               break;
           }
       }
   }
  
   public boolean isIn(Account a) {
      //EFFECTS:If a is in this returns true else returns false
        return accounts.contains(a);
   }
   
   public long size() {
      //EFFECTS:Returns the cardinality of this
        return accounts.size();
   }

   public Iterator elements() {
      //REQUIRES:this not be modified while the generator is in use
      //EFFECTS:Returns a generator that produces all elements of this(as Accounts),each exactly once in arbitrary order.
        return accounts.iterator();
   }

   public Account queryByAccountId(String accountId) throws NotFoundException {
      //EFFECTS:If an account identified by accountId is in this returns the finding account,else throws a NoFoundException.
       Iterator<Account> iterator = accounts.iterator();
       while(iterator.hasNext()){
           Account account = iterator.next();
           if(account.getAccountId().equals(accountId)){
               return account;
           }
       }
       throw new NotFoundException();
   }
      
   public AccountSet queryByUserId(String userId) throws NotFoundException {
      //EFFECTS:If a userId is in this returns all the finding accounts that are attached to the userId,else throws a NoFoundException.
       AccountSet accountSet = new AccountSet();
       Iterator<Account> iterator = accounts.iterator();
       while(iterator.hasNext()){
           Account account = iterator.next();
           if(account.getUserId().equals(userId)){
               accountSet.insert(account);
           }
       }
       if(accountSet.size() > 0)
           return accountSet;
       throw new NotFoundException();
   }

   public boolean reqOK(){
        return accounts.size() >= 0;
   }
    /*
    不变式：
    account.size() < 0
     */


}

class NotFoundException extends Throwable {
    public NotFoundException(){

    }
}
