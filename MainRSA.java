//import java.security.KeyPair;

public class MainRSA {

    public static void main(String[] args) {
        // Step 1: Generate RSA keys
        KeyPair keyPair = RSAEncryption.generateKeys();
        long publicKey = keyPair.getPublicKey();
        long privateKey = keyPair.getPrivateKey();

        //Display public and private keys
        System.out.println("Public Key: " + publicKey);
        System.out.println("Private Key: " + privateKey);

        // Step 2: Encrypt a message using the public key
        String message = "Hello, RSA!";
        int[] encryptedMessage = RSAEncryption.encrypt(message, publicKey, RSAEncryption.n);

        // Display the encrypted message
        System.out.println("Encrypted Message: " + RSAEncryption.IntArray_to_String(encryptedMessage));

        // Step 3: Decrypt the message using the private key
        int[] decryptedIntArray = RSAEncryption.decrypt(encryptedMessage, privateKey, RSAEncryption.n);
        String decryptedMessage = RSAEncryption.IntArray_to_String(decryptedIntArray);

        // Display the decrypted message
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
    
}
