import java.util.*;
public class product {
    int proID;
    String proName;
    double Price;
    int Quantity;
    
    public product(int id, String name, double price, int Quantity) {
        this.proID = id;
        this.proName = name;
        this.Price = price;
        this.Quantity = Quantity;
    }
    public product() {
        this(0, "undefined", 0.0, 0);
    }

    public double getTotal() {
        return Price * Quantity;

    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of products :");
        int n = sc.nextInt(); 
        product[] p = new product[2+n];
        p[0] = new product(1,"Laptop",70000,2);
        p[1] = new product(2,"keybord",300,2);
   
        
        for(int i = 2;i<2+n;i++){
            System.out.print("Enter the Product ID :");
            int proId = sc.nextInt();
            System.out.print("Enter the Product Name :");
            String proName = sc.next();
            System.out.print("Enter the Price :");
            double Price = sc.nextDouble();
            System.out.print("Enter the Quantity :");
            int Quantity = sc.nextInt();
            System.out.println("==================================");

            p[i] = new product(proId, proName, Price, Quantity);
        }    

            System.out.println("\n==================== Product Detail=========================");

            System.out.printf("%-7s %-15s %-10s %-13s %-15s\n","ID","Name","Price","Quantity","Total");
            System.out.println("=============================================================");

        for(int i=0; i<p.length; i++){
            System.out.printf("%-7s %-15s %-10.2f %-13s %-15.2f\n",p[i].proID,p[i].proName,p[i].Price,p[i].Quantity,p[i].getTotal());
            

        }
        System.out.println("==========================================================");
        double Total = 0;
        for(int i=0;i<p.length;i++){
            Total += p[i].getTotal();
        }
        System.out.println("Grand Total: " + Total); 
        
        if(Total > 10000){
            System.out.println("you get 10% Discount " + (0.1 * Total));
        }
        else if(Total>50000 && Total<20000){
            System.out.println("you get 20% Discount :"+(0.2*Total));
        }else{
            System.out.println("you will get 25% Discount :"+(2.5*Total));
        }

       sc.close();
    }
   

    
}
                