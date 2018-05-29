
import java.io.*;
import java.util.ArrayList;

public class HashIndex implements Serializable{
    public int n;
    private ArrayList<Integer>[] buckets;

    public HashIndex(int n){
        this.n = n;
        this.buckets = (ArrayList<Integer>[]) new ArrayList[1 << n];
    }

    public ArrayList getIndex(String key) {
        int index = (this.n - 1) & HashIndex.hash(key);
        return this.buckets[index];
    }

    public void setIndex(String key, int pid) {
        int index = (this.n - 1) & HashIndex.hash(key);
        if (this.buckets[index] != null) {
            this.buckets[index].add(pid);
        }
        else {
            this.buckets[index] = new ArrayList<Integer>();
            this.buckets[index].add(pid);
        }

    }

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
