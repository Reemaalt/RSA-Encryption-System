//needed imports

import java.security.SecureRandom;
import java.util.Arrays;

public class RSAEncryption {

    public static void main(String[] args) {
        System.out.println("Hi there! This is the main method\ncalling generateKeys");
        KeyPair keyPair = generateKeys();

        String str = "hello";
        System.out.println("setting plaintext to: " + str);
        System.out.println("calling encrypt...");
        long[] encryptedMessage = encrypt(str, keyPair.getPublicKey(), getN());

        System.out.println("calling decrypt on encrypt output...");
        String decryptedMessage = decrypt(encryptedMessage, keyPair.getPrivateKey(), getN());
        System.out.println("making sure decryptedText and plaintext are equalsIgnoreCase...  ");
        if (str.equalsIgnoreCase(decryptedMessage))
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
    public static int[] LCG(int seed, int quantity) {//seed is 5 quantity is 100
        System.out.println("Hi there! This is LCG method, I am called with\n" +
                "(seed=" + seed + "  quantity=" + quantity + ")");
        final int A = 1664525;
        final int C = 1013904223;
        final long M = 32767;
        int[] result = new int[quantity];

        for (int i = 0; i < quantity; i++) {
            seed = (int) ((A * (long) seed + C) % M & Integer.MAX_VALUE);
            result[i] = seed;
        }
        return result;
    }
    //---------------------------------------------------------------------------//Miller-Rabin Primality Test
    public static boolean probablyPrime(long n, long a) {
        //this test checks if n is probably prime
        //1) either a^exp ≡ 1 (mod n)
        //2) or a^2*r*exp ≡-1 (mod n)
        // and returns true
        //if it's composite, it returns false

        // Initialize exp with n - 1.
        long exp = n - 1;
        ////assuming n is prime, so n-1 (exp) is even.
        while (exp % 2 == 0) // Make exp an odd number by dividing by 2
        {
            exp >>= 1;// Bitwise right shift, equivalent to dividing by 2
        }
        //here we are taking the first term that has been divided by 2 many times
        // until the exp became odd
        if (modularExponentiation(a, exp, n) == 1) // Check if a^exp ≡ 1 (mod n)
        {
            return true;//it's probably prime
        }

        //here we are going through the other terms one by one checking if
        // a^2*r*exp ≡ 1 (mod n)
        // we are checking the next term by multiplying by 2.
        while (exp < n - 1) {
            if (modularExponentiation(a, exp, n) == n - 1) {
                return true;//a^exp ≡ n-1 (mod n) same as saying a^exp ≡ -1 (mod n)
            }
            exp <<= 1;// Bitwise left shift, equivalent to multiplying by 2.
        }
        return false;//composite
    }
    public static boolean millerRabinTest(long n, int k) {
        for (int i = 0; i < k; i++) {
            long a = generateRandomNumberInRange(2, (int) (n - 1));
            if (!probablyPrime(n, a)) {
                return false;//it's composite for sure in shaa allah
            }
        }
        return true;//here we did the test 40 times
        // so the possibility of error is 2^-80
        //since we did the test many times in shaa allah it's truly prime;)
    }

    //---------------------------------------------------------------------------// RSA Key Generation
    public static KeyPair generateKeys() {
        // Step 1: Choose two large prime numbers, p and q, I need to use the lcg method and then check that it's prime using Miller-Rabin Primality Test
        int seed = 5;
        int[] a;
        a = LCG(seed, 100);
        long[] primesGeneratedFromMillerRabinTest = getPrimes(a, 40);
        long p = primesGeneratedFromMillerRabinTest[0];
        long q = primesGeneratedFromMillerRabinTest[1];
        do {//these two loops are ensuring that p and q are different
            for (int i = 1; i < primesGeneratedFromMillerRabinTest.length; i++) {
                if (primesGeneratedFromMillerRabinTest[i] != p)//Ensuring that primesGeneratedFromMillerRabinTest[i] are not the same to the first element
                {
                    q = primesGeneratedFromMillerRabinTest[i];
                    break;
                }
                a = LCG(seed++, 100); //In a highly unlikely scenario where the first element in the list is identical to all other numbers.
                primesGeneratedFromMillerRabinTest = getPrimes(a, 40);
            }
        } while (p == q) ;

        // Step 2: Compute n = p * q
        n = p * q;

        // Step 3: Compute the totient of n, φ(n) = (p-1)(q-1)
        long phi = (p - 1) * (q - 1);
        System.out.println("I calculated phi:" + phi);

        // Step 4: Choose public exponent e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1
        do {
            e = generateRandomNumberInRange(2, phi - 1);
        } while (gcd(e, phi) != 1);
        System.out.println("I set e:" + e);

        // Step 5: Compute private exponent d using the extendedEuclideanAlgorithm
        d = extendedEuclideanAlgorithm(e, phi);
        System.out.println("I called extendedEuclideanAlgorithm, and got d to be:" + d);

        System.out.println("finally, I am creating an instance of KeyPair class as:\n"
                + "KeyPair(new PublicKey(n, e), new PrivateKey(n, d))\n"
                + "and returning it. Bye now! --generateKeys method");
        // Return the public and private keys
        return new KeyPair(e, d);
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

    //---------------------------------------------------------------------------//Encryption and decryption and modular arithmetic operations
    static long[] encrypt (String message,long e, long n){
        System.out.println("Hi there! This is encrypt method");
        long[] intArray = string_to_intArray(message);
        System.out.println("converting my string to int:");

        for (int i = 0; i < intArray.length; i++) {
            System.out.print(intArray[i] + "   ");
        }

        System.out.println("\n encryptedValues:");


        long[] encrypted = new long[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            encrypted[i] = modularExponentiation(intArray[i], e, n);
        }
        for (int i = 0; i < encrypted.length; i++)
            System.out.print(encrypted[i] + "   ");
        System.out.println("\nbye now! --encrypt method");
        return encrypted;
    }

    static String decrypt ( long[] ciphertext, long d, long n){
        System.out.println("Hi there! This is decrypt method");

        long[] decryptedInts = new long[ciphertext.length];
        for (int i = 0; i < ciphertext.length; i++) {
            decryptedInts[i] = (int) modularExponentiation(ciphertext[i], d, n);
        }
        System.out.println("decryptedValues:");
        for (int i = 0; i < decryptedInts.length; i++)
            System.out.print(decryptedInts[i] + "   ");
        System.out.println();
        String decrypted = Array_to_String(decryptedInts);
        System.out.println("ArrayToString: " + decrypted);
        System.out.println("bye now! --decrypt method");
        return decrypted;

    }

    static long[] string_to_intArray (String str){
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

    static String Array_to_String ( long[] intArray){
        char[] charArray = new char[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            long current = intArray[i];
            if (current >= 1 && current <= 26) {
                charArray[i] = (char) (current + 'a' - 1);
            } else if (current >= 27 && current <= 52) {
                charArray[i] = (char) (current + 'A' - 27);
            } else {
                charArray[i] = '-';
            }
        }
        return new String(charArray);
    }

    //other methods
    static long modularExponentiation ( long base, long exponent, long modulus){
        long result = 1;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            base = (base * base) % modulus;
            exponent >>= 1; // Use bitwise shift instead of division
        }

        return result;
    }

    private static long gcd ( long a, long b){
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long extendedEuclideanAlgorithm ( long e, long m){
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

    private static long generateRandomNumberInRange ( long min, long max)
    {
        return min + (long) (random.nextDouble() * (max - min + 1));
    }

//        private static int getRandomInt ( int min, int max)
//        {
//            return random.nextInt(max - min + 1) + min;
//        }
//


}



