
public class ass3{
    public static void main(String[] args){
        int n = 6;
        for(int i=3;i>=1;i--){
            for(int j=1;j<=i;j++){
                System.out.print(n*n + " ");  
                n--;      
            }
            System.out.println();
        }
    }  
}