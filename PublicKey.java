public class PublicKey {

    private long n;
    private long e;

    public PublicKey(long n, long e) {
        this.n = n;
        this.e = e;
    }

    public long getN() {
        return n;
    }

    public long getE() {
        return e;
    }
    
}
