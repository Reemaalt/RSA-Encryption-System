public class KeyPair {
    
    private final long publicKey;
    private final long privateKey;

        public KeyPair(long publicKey, long privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public long getPublicKey() {
            return publicKey;
        }

        public long getPrivateKey() {
            return privateKey;
        }
}

    
    

