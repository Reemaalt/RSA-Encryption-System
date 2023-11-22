//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Cs281_Project {
    static Scanner input;

    public Cs281_Project() {
    }

    public static void main(String[] var0) {
        Cs281_Project var1 = new Cs281_Project();

        label30:
        while(true) {
            System.out.println("Please choose an option:");
            System.out.println("(1) Linear Congruential Generator (LCG)");
            System.out.println("(2) Miller-Rabin Primality Test");
            System.out.println("(3) RSA Key Generation");
            System.out.println("(4) Encryption and decryption and modular arithmetic operations");
            System.out.println("(5) Exit");

            while(true) {
                int var2;
                try {
                    var2 = input.nextInt();
                } catch (InputMismatchException var6) {
                    System.out.println("re-enter operation:");
                    input.next();
                    continue;
                }

                switch(var2) {
                    case 1:
                    case 3:
                    case 4:
                        continue label30;
                    case 2:
                        System.out.println("Enter number");
                        long var3 = input.nextLong();
                        System.out.println("Enter the number of iterations (k)");
                        int var5 = input.nextInt();
                        if (var1.millerRabinTest(var3, var5)) {
                            System.out.println(var3 + " is prime ");
                        } else {
                            System.out.println(var3 + " is composite");
                        }
                        continue label30;
                    case 5:
                        return;
                    default:
                        System.out.println();
                        continue label30;
                }
            }
        }
    }

    public boolean millerRabinTest(long var1, int var3) {
        if (var1 != 0L && var1 != 1L) {
            if (var1 == 2L) {
                return true;
            } else if (var1 % 2L == 0L) {
                return false;
            } else {
                long var4;
                for(var4 = var1 - 1L; var4 % 2L == 0L; var4 /= 2L) {
                }

                Random var6 = new Random();

                for(int var7 = 0; var7 < var3; ++var7) {
                    long var8 = Math.abs(var6.nextLong());
                    long var10 = var8 % (var1 - 1L) + 1L;
                    long var12 = var4;

                    long var14;
                    for(var14 = this.modPow(var10, var4, var1); var12 != var1 - 1L && var14 != 1L && var14 != var1 - 1L; var12 *= 2L) {
                        var14 = this.mulMod(var14, var14, var1);
                    }

                    if (var14 != var1 - 1L && var12 % 2L == 0L) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public long modPow(long var1, long var3, long var5) {
        long var7 = 1L;

        for(int var9 = 0; (long)var9 < var3; ++var9) {
            var7 *= var1;
            var7 %= var5;
        }

        return var7 % var5;
    }

    public long mulMod(long var1, long var3, long var5) {
        return BigInteger.valueOf(var1).multiply(BigInteger.valueOf(var3)).mod(BigInteger.valueOf(var5)).longValue();
    }

    static {
        input = new Scanner(System.in);
    }
}
