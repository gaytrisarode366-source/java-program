//check balance ,perform operation credit,debbit
import java.util.*;
public class ass4 {
    private static double bal;
    public static void checkBalance(){
        System.out.println("Current Balance :$"+bal);
    }
    public static void Credited(double amt){
        if(amt<=0){
            throw new IllegalArgumentException("Deposite ammount must be greater than Zero");
        }
        bal = bal+amt;
        System.out.println("$" + amt + " deposited successfully.");
    }
    public static void withdrawl(double amt){
        if(amt<=0){
            throw new IllegalArgumentException("Withdrawal amount must be greater then zero");
        } 
        if(amt > bal){
            throw new ArithmeticException("Insufficient funds! Available balance: $" + bal);
        }
        bal = bal-amt;
        System.out.println("₹" + amt + " Withdraw successfully.");
    }
    public static void main(String[] args){
        Scanner sc =new Scanner(System.in);
        System.out.print("Enter the balance :");
        bal = sc.nextDouble();
        int choice;
        boolean running = true;
        System.out.println("Welcome to ATM Machine");
        System.out.println("Initial Balance :$"+bal);
        while(running){
            System.out.println("================= ATM MACHINE ==================");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposite Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter your choice :");
            try{
                choice=sc.nextInt();
                switch (choice) {
                    case 1:
                        checkBalance();
                        break;
                    case 2:
                        System.out.print("Enter ammount to deposite: ");
                        double depo_Amt=sc.nextDouble();
                        Credited(depo_Amt);
                        break; 
                    case 3:
                        System.out.print("Enter ammount to withdrawl: ");
                        double withd_Amt=sc.nextDouble();
                        withdrawl(withd_Amt);
                        break;
                    case 4:
                        running=false;
                        System.out.println("Thank you for using ATM");    
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid choice!");     
                }
            }catch(ArithmeticException e){
                System.out.println("Error:"+e.getMessage());
            }catch(IllegalArgumentException e){
                System.out.println("Error :"+e.getMessage());
            }catch(Exception e){
                System.out.println("Invalid input! Please enter a number :");
                sc.nextLine();
            }finally{
                
                System.out.println("Transaction Completed");
            }
        }
        sc.close();
    }
}

