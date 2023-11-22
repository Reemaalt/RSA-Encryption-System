//needed imports
import java.security.SecureRandom;

public class RSAEncryption {

    

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

    public static KeyPair generateKeys() {
        SecureRandom random = new SecureRandom();

        // Step 1: Choose two large prime numbers, p and q i need to use the lcg method and then cheak that use Miller-Rabin Primality Test
        int[] a =LCG(1024, 2);      
        long p = a[0];
        long q = a[1];

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
    
}
