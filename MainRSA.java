
public class MainRSA {

    public static void main(String[] args){
        System.out.println("Hi there! This is the main method\ncalling generateKeys");
        generateKeys();
        
        String str = "hello" ;
        System.out.println("setting plaintext to: "+str);
        System.out.println("calling encrypt...");
        encrypt(str,n ,e);
        
        System.out.println("calling decrypt on encrypt output...");
        decrypt(encrypt(str,n ,e),d,n);
        System.out.println("making sure decryptedText and plaintext are equalsIgnoreCase...  ");
        if(str.equals(decrypt(encrypt(str,n ,e),d,n)))
            System.out.println("Yes! they are");
        else
            System.out.println("No! they aren't");
            
        System.out.println("bye now! --main method");
    }
}
