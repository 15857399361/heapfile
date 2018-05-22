import java.io.Serializable;

public class Pair<A, B> implements Serializable {
    public final A first;
    public final B second;

    public Pair(A a, B b) {
        this.first = a;
        this.second = b;
    }

    public A getFirst() {
        return this.first;
    }

    public B getSecond() {
        return this.second;
    }
}