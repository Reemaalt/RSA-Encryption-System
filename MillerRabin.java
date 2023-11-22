import java.security.SecureRandom;
public class MillerRabin {

    private static final SecureRandom random = new SecureRandom();

    public static boolean composite(long n, int a)
    {//this tests checks if n is composite and returns true
        //if it's probably prime it returns false
        long exp = n - 1;//assuming n is prime n-1 is even

        while (exp % 2 == 0) //trying to make exp an odd number
        {
            exp >>= 1;
        }

        if (power(a, exp, n) == 1) //modular exponentiation is a^exp ≡ 1(mod n)
        {//≡ 1(mod n) ->> composite
            return true;//it's composite
        }

        while (exp < n - 1)
        {
            if (power(a, exp, n) == n - 1)//same as saying a^exp ≡ -1(mod n)
            {
                return true;
            }
            exp <<= 1;//doubling the exponent (squaring)
        }

        return false;
    }

    public static boolean millerRabinTest(long n, int k) {
        for (int i = 0; i < k; i++) //repeat test k times
        {
            int a = getRandomInt(2, (int) (n - 1));
            if (!composite(n, a)) {
                return false;
            }
        }
        return true;//after checking 40 times there is 2^-80 possibility of error
    }

    public static void main(String[] args)
    {
        System.out.println(millerRabinTest(341, 40));
    }

    private static int getRandomInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
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
}
