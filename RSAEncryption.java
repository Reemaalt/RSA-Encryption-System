//needed imports

import java.security.SecureRandom;
import java.util.Arrays;

public class RSAEncryption {

    public static void main(String[] args){
        System.out.println("Hi there! This is the main method\ncalling generateKeys");
        KeyPair keyPair = generateKeys();
        
        String str = "hello" ;
        System.out.println("setting plaintext to: "+str);
        System.out.println("calling encrypt...");
        long[] encryptedMessage = encrypt(str, keyPair.getPublicKey(), getN());
        
        System.out.println("calling decrypt on encrypt output...");
        String decryptedMessage = decrypt(encryptedMessage, keyPair.getPrivateKey(), getN());
        System.out.println("making sure decryptedText and plaintext are equalsIgnoreCase...  ");
        if(str.equalsIgnoreCase(decryptedMessage))
            System.out.println("Yes! they are");
        else
            System.out.println("No! they aren't");
            
            System.out.println("bye now! --main method");
    }


private static final SecureRandom random = new SecureRandom();
private static long n;
private static long e;
private static long d;
 // Getter method for n
    public static long getN() {
        return n;
    }

//---------------------------------------------------------------------------//Linear Congruential Generator
 public static int[] LCG(int seed, int quantity) {
        System.out.println("Hi there! This is LCG method, I am called with\n" +
        "(seed="+seed +"  quantity="+quantity+")");
        final int A = 1664525;
        final int C = 1013904223;
        final long M = 32767;
        System.out.println("and I have those initialized local variables:\n" +
        "(A = "+A+"  C = "+C+"  M = "+M+")");
        int[] result = new int[quantity];
        
        for (int i = 0; i < quantity; i++) {
            seed = (int) ((A * (long) seed + C) % M & Integer.MAX_VALUE);
            result[i] = seed;
        }
        System.out.println("I generated "+quantity+" random numbers, and I made them all positives!");
        for(int i =0 ; i<9 ; i++){
            System.out.print(result[i]+",");
        }
        System.out.println("...");
        System.out.println("bye now! --LCG method");
        return result;  
    }

//---------------------------------------------------------------------------//Miller-Rabin Primality Test
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
    
    public static long[] getPrimes(int[] candidates, int quantity) {
    long[] primes = new long[quantity];
    int count = 0;

    for (int candidate : candidates) {
        if (millerRabinTest(candidate, 40)) {
            primes[count++] = candidate;
            if (count == quantity) {
                break;  // Found enough primes, exit loop
            }
        }
    }

    return Arrays.copyOf(primes, count);  // Trim the array to the actual count
}

//---------------------------------------------------------------------------// RSA Key Generation
    public static KeyPair generateKeys() {
       
        // Step 1: Choose two large prime numbers, p and q i need to use the lcg method and then cheak that use Miller-Rabin Primality Test
       
        System.out.println("Hi there! This is generateKeys method");
        System.out.println("I will be generating 100 random numbers using LCG method\n" +
        "calling LCG...");
        int[] a =LCG(5, 100); 
        System.out.println("back to generateKeys, now I will examin the random numbers and assign p to "
                + "the first number that passes millerRabinTest");
        System.out.println("q to the second number (if it is not equal to p");
        long[] b= getPrimes (a,40) ;
        long p = b[6];
        long q = b[7];
        System.out.println("p is "+p+" the 7th element in the random list");
        System.out.println("q is "+q+" the 8th element in the random list");

        // Step 2: Compute n = p * q
         n = p * q;
  

        // Step 3: Compute the totient of n, φ(n) = (p-1)(q-1)
        long phi = (p - 1) * (q - 1);
        System.out.println("I calculated phi:" + phi);

        // Step 4: Choose public exponent e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1
        
        do {
            e = generateRandomNumberInRange(2, phi - 1);
        } while (gcd(e, phi) != 1);
        System.out.println("I set e:"+e);

        // Step 5: Compute private exponent d using the extendedEuclideanAlgorithm
        d = extendedEuclideanAlgorithm(e, phi);
        System.out.println("I called extendedEuclideanAlgorithm, and got d to be:"+d);

        

        System.out.println("finally, I am creating an instance of KeyPair class as:\n"
        + "KeyPair(new PublicKey(n, e), new PrivateKey(n, d))\n"
        + "and returning it. Bye now! --generateKeys method");

   // Return the public and private keys
        return new KeyPair(e, d);
    } 
    
//---------------------------------------------------------------------------//Encryption and decryption and modular arithmetic operations
    static long[] encrypt(String message, long e, long n) {
        System.out.println("Hi there! This is encrypt method");
        long[] intArray = string_to_intArray(message);
        System.out.println("converting my string to int:");
       
        for(int i = 0 ; i < intArray.length ; i++){
            System.out.print(intArray[i]+"   ");
        }
        
        System.out.println("\n encryptedValues:");
        
        
        long[] encrypted = new long[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            encrypted[i] = modularExponentiation(intArray[i], e, n);
        }
        for(int i = 0 ; i < encrypted.length ; i++)
        System.out.print(encrypted[i]+"   ");
        System.out.println("\nbye now! --encrypt method");
        return encrypted;
    }

    static String decrypt(long[] ciphertext, long d, long n) {
        System.out.println("Hi there! This is decrypt method");

        long[] decryptedInts = new long[ciphertext.length];

        for (int i = 0; i < ciphertext.length; i++) {
        System.out.println("decryptedValues:");
        
        for(int j = 0 ; j<decryptedInts.length ; j++)
            System.out.print(decryptedInts[i]+"   ");

        System.out.println();
        }
        String decrypted =   Array_to_String(decryptedInts);
        System.out.println("ArrayToString: "+ decrypted);
        System.out.println("bye now! --decrypt method");
       
        return decrypted;

    }

    static long[] string_to_intArray(String str) {
    long[] intArray = new long[str.length()];
    for (int i = 0; i < str.length(); i++) {
        char current = str.charAt(i);
        if (current >= 'a' && current <= 'z') {
            intArray[i] = current - 'a' + 1;
        } else if (current >= 'A' && current <= 'Z') {
            intArray[i] = current - 'A' + 27; // Adjust for the uppercase letters
        } else {
            intArray[i] = 0;  // Use 0 for non-alphabetic characters
        }
        
    }
    return intArray;
}

    static String Array_to_String(long[] intArray) {
    char[] charArray = new char[intArray.length];
    for (int i = 0; i < intArray.length; i++) {
        long current = intArray[i];  
        if (current >= 1 && current <= 26) {
            charArray[i] = (char) (current + 'a' - 1);
        } else if (current >= 27 && current <= 52) {
            charArray[i] = (char) (current + 'A' - 27);
        }else {
            charArray[i] = '-';
        }
    }
    return new String(charArray);
}

//other methods 
    static long modularExponentiation(long base, long exponent, long modulus) {
        long result = 1;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
         base = (base * base) % modulus;
        exponent >>= 1; // Use bitwise shift instead of division 
        }

        return result ;
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

    private static long generateRandomNumberInRange(long min, long max) {
        return min + (long) (random.nextDouble() * (max - min + 1));
    }
    
}

