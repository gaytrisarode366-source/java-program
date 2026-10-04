import java.util.*;

public class ass1{
    public static int sum(int a ,int b){
        return a+b;

    }
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the First number:");
        int num1 = sc.nextInt();
        System.out.print("Enter the second number");
        int num2 = sc.nextInt();

        int result = sum(num1, num2);
        System.out.println("Addition is:"+result);

    }
}