
import java.util.*;
import java.math.BigInteger;

public class Cs281_Project {
    
    static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        Cs281_Project obj = new Cs281_Project();
        while(true){
            System.out.println("Please choose an option:");
            System.out.println("(1) Linear Congruential Generator (LCG)");
            System.out.println("(2) Miller-Rabin Primality Test");
            System.out.println("(3) RSA Key Generation");
            System.out.println("(4) Encryption and decryption and modular arithmetic operations");
            System.out.println("(5) Exit");
            int option;
            while(true)
            {
                try
                {
                    option = input.nextInt();
                    break;
                }
                catch(InputMismatchException e){
                    System.out.println("re-enter operation:");
                    input.next();
                }
            }
            switch(option){
                case 1 : 
                    
                    //call method 1
                    break;
                case 2 :
                    System.out.println("Enter number");
                    long n = input.nextLong();
                    System.out.println("Enter the number of iterations (k)");
                    int k = input.nextInt();
                    if(obj.millerRabinTest(n, k))
                        System.out.println(n+" is prime ");  
                    
                    else
                        System.out.println(n+" is composite");
                    break;
                case 3 :
                    
                    //call method 3
                    break;
                case 4 :
                    
                    //call method 4
                    break;
                case 5 : 
                    // Ending program
                    return;
                default :
                    System.out.println(); 
                
            }
        }
    }
    
    ////////////////CASE 2 Miller-Rabin Primality Test //////////////////////////////////
    
    public boolean millerRabinTest(long n, int k)  // int k represents iteration ()
    {
        /** base case **/
        if (n == 0 || n == 1)
            return false;
        /** base case - 2 is prime **/
        if (n == 2)
            return true;
        /** an even number other than 2 is composite **/
        if (n % 2 == 0)
            return false;
 
        long s = n - 1;
        while (s % 2 == 0)
            s /= 2;
 
        Random rand = new Random();
        for (int i = 0 ; i < k ; i++)
        {
            long r = Math.abs(rand.nextLong());            
            long a = r % (n - 1) + 1, temp = s;
            long mod = modPow(a, temp, n);
            while (temp != n - 1 && mod != 1 && mod != n - 1)
            {
                mod = mulMod(mod, mod, n);
                temp *= 2;
            }
            if (mod != n - 1 && temp % 2 == 0)
                return false;
        }
        return true;        
    }
    /** Function to calculate (a ^ b) % c **/
    public long modPow(long a, long b, long c)
    {
        long res = 1;
        for (int i = 0; i < b; i++)
        {
            res *= a;
            res %= c; 
        }
        return res % c;
    }
    /** Function to calculate (a * b) % c **/
    public long mulMod(long a, long b, long mod) 
    {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }

}
