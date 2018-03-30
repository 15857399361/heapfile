import java.io.*;

public class dbquery {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Arguments Error!");
            usage();
            return;
        }

        String text = args[0];
        short pagesize = Short.parseShort(args[1]);
        HeapFile hf = new HeapFile(pagesize);
        long start = System.currentTimeMillis();
        hf.query(text);
        long end = System.currentTimeMillis();
        long costtime = end - start;
        System.out.println("Query HeapFile cost " + costtime + "ms.");
    }


    private static void usage() {
        System.out.println("Usage:");
        System.out.println("java dbquery $text $pagesize");
    }
}
