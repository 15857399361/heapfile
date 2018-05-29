import java.io.*;
import java.util.*;
import java.text.*;

public class dbquery {
    static short pagesize;


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 3) {
            System.out.println("Arguments Error!");
            usage();
            return;
        }

        String text = args[0];
        pagesize = Short.parseShort(args[1]);

        long start = System.currentTimeMillis();
        query(text, args[2]);
        long end = System.currentTimeMillis();
        long costtime = end - start;
        System.out.println("Query HeapFile totally cost " + costtime + "ms.");
    }

    private static void query(String text, String useIndex) throws IOException, ClassNotFoundException {
        HeapFile hf = new HeapFile(pagesize, "heap." + pagesize);
        if (useIndex.equals("True")) {
            long start = System.currentTimeMillis();
            ObjectInput indexIn = new ObjectInputStream(new FileInputStream("index." + pagesize));
            HashIndex hashIndex = (HashIndex) indexIn.readObject();
            indexIn.close();
            long readIndex = System.currentTimeMillis();
            ArrayList<Integer> pids = hashIndex.getIndex(text);
            System.out.println("Load Index cost " + (readIndex - start) + "ms.");
            for (int pid : pids) {
                DataPage dpage = (DataPage) hf.readPage(new PageID(pid, (short) 1));
                for (Record record : dpage.records) {
                    if (record.bn_name.equals(text)) {
                        printRecord(record);
                    }
                }
            }
            long query = System.currentTimeMillis();
            System.out.println("Query Page cost " + (query - readIndex) + "ms.");
        }
        else {
            PageID hpid = new PageID(0, (short) 0);
            while (true) {
                HeaderPage hpage = (HeaderPage) hf.readPage(hpid);
                for (PageID dpid : hpage.dataPages) {
                    DataPage dpage = (DataPage) hf.readPage(dpid);
                    for (Record record : dpage.records) {
                        if (record.bn_name.equals(text)) {
                            printRecord(record);
                            return;
                        }
                    }
                }
                hpid = hpage.getNextHeader();
                if (hpid == null) {
                    break;
                }
            }
            System.out.println("Can't find " + text);
        }

    }

    private static void printRecord(Record record) {
        System.out.println(
                record.register_name + "\t"
                        + record.bn_name + "\t"
                        + record.bn_status + "\t"
                        + record.bn_reg_dt + "\t"
                        + record.bn_cancel_dt + "\t"
                        + record.bn_renew_dt + "\t"
                        + record.bn_state_num + "\t"
                        + record.bn_state_of_reg + "\t"
                        + record.bn_abn
        );
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("java dbquery $text $pagesize");
    }
}
