
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
        long[] encryptedMessage = RSAEncryption.encrypt(message, publicKey, keyPair.getN());

        // Display the encrypted message
        System.out.println("Encrypted Message: " + RSAEncryption.Array_to_String(encryptedMessage));

        // Step 3: Decrypt the message using the private key
        String decryptedMessage = RSAEncryption.decrypt(encryptedMessage, privateKey, keyPair.getN());
        

        // Display the decrypted message
        System.out.println("Decrypted Message: " + decryptedMessage);
    }
    
}
