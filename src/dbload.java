import java.io.*;

public class dbload {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Arguments Error!");
            usage();
            return;
        }

        if (!args[0].equals("-p")) {
            System.out.println("Unknown argument: " + args[0]);
            usage();
            return;
        }

        short pagesize = Short.parseShort(args[1]);
        String datafile = args[2];
        long start = System.currentTimeMillis();
        HeapFile hf = new HeapFile(pagesize, datafile);
        hf.save();
        System.out.println("Number of records: " + hf.recordCount);
        System.out.println("Number of pages: " + hf.pageCount);
        long end = System.currentTimeMillis();
        long costtime = end - start;
        System.out.println("Dump HeapFile cost " + costtime + "ms.");
    }


    private static void usage() {
        System.out.println("Usage:");
        System.out.println("java dbload -p $pagesize $datafile");
    }
}
