//needed imports
import java.security.SecureRandom;

public class RSAEncryption {

    private static final SecureRandom random = new SecureRandom();

    public static boolean singleTest(long n, int a) {
        long exp = n - 1;//assuming n is prime n-1 is even

        while (exp % 2 == 0) //trying to make exp an odd number
        {
            exp >>= 1;
        }

        if (power(a, exp, n) == 1) //modular exponintion is a^exp ≡1(modn)
        {
            return true;//it's composite
        }

        while (exp < n - 1) {
            if (power(a, exp, n) == n - 1) {
                return true;
            }
            exp <<= 1;
        }

        return false;
    }

    public static boolean millerRabinTest(long n, int k) {
        for (int i = 0; i < k; i++) {
            int a = getRandomInt(2, (int) (n - 1));
            if (!singleTest(n, a)) {
                return false;
            }
        }
        return true;
    }
    

          // Constants for the LCG formula
    private static final int A = 1664525;
    private static final int C = 1013904223;
    private static final long M = (int) Math.pow(2, 32);

    public static int[] LCG(int seed, int quantity) {
        int[] result = new int[quantity];

        for (int i = 0; i < quantity; i++) {
            seed = (int) ((A * (long) seed + C) % M & Integer.MAX_VALUE);
            result[i] = seed;
        }

        return result; 
    }

    public static int[] isprime (int[]a , int q ){
      int[]b =new int[q]; 
     for (int i=0; i<q;i++){
        b[i]=a[i];
     }
       int[]ans=new int[q];
       int k=0;
     for(int j=0; j<q; j++){

      if( millerRabinTest(b[j], 40)){
        ans[k++]=b[j];
    }
    }
     
       return ans; 
    }

    public static KeyPair generateKeys() {
        SecureRandom random = new SecureRandom();

        // Step 1: Choose two large prime numbers, p and q i need to use the lcg method and then cheak that use Miller-Rabin Primality Test
       
        int[] a =LCG(1024, 2);  
        int[] b= isprime (a,4) ;
        long p = b[0];
        long q = b[1];

        // Step 2: Compute n = p * q
        long n = p * q;

        // Step 3: Compute the totient of n, φ(n) = (p-1)(q-1)
        long phi = (p - 1) * (q - 1);

        // Step 4: Choose public exponent e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1 
        long e;
        do {
            e = random.nextLong() & Long.MAX_VALUE % phi; // Ensure 1 < e < φ(n)
        } while (e <= 1 || gcd(e, phi) != 1);

        // Step 5: Compute private exponent d using the extendedEuclideanAlgorithm
        long d = extendedEuclideanAlgorithm(e, phi);

        // Return the public and private keys
        return new KeyPair(e, d);
    } 

    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long extendedEuclideanAlgorithm(long e, long m) {
        long a = e;
        long b = m;
        long x0 = 1, x1 = 0, temp;
        while (b != 0) {
            long q = a / b;
            temp = a % b;
            a = b;
            b = temp;
            temp = x0 - q * x1;
            x0 = x1;
            x1 = temp;
        }
        return x0 < 0 ? x0 + m : x0;
    }
    
    private static long power(long base, long exp, long mod)
    {
        long result = 1;
        base = base % mod;

        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            exp >>= 1;
            base = (base * base) % mod;
        }

        return result;
    }
    private static int getRandomInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
    }
}

