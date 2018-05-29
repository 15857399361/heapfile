import java.io.*;
import java.util.HashMap;


public class dbload {
    static int pid;
    static short pagesize;

    public static void main(String[] args) throws IOException {
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

        HashIndex hashIndex = new HashIndex(24);
        // int recordIndex = 0;
        pagesize = Short.parseShort(args[1]);
        String datafile = args[2];
        long start = System.currentTimeMillis();
        int recordCount = 0;
        HeapFile hf = new HeapFile(pagesize, "heap." + pagesize);

        InputStream is = new FileInputStream(datafile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // remove the fist line.
        reader.readLine();
        String str = null;
        // Create first header page and data page
        pid = 0;
        HeaderPage hpage = new HeaderPage(pid, pagesize);
        pid += 1;
        DataPage dpage = new DataPage(pid, pagesize);
        pid += 1;
        hpage = appendDataPage(hpage, dpage, hf);
        // Read each record
        while (true) {
            str = reader.readLine();
            if (str != null) {
                Record record = new Record(str);
                recordCount += 1;
                if (dpage.isFull(record)) {
                    DataPage new_dpage = new DataPage(pid, pagesize);
                    pid += 1;
                    hf.writePage(dpage);
                    dpage = new_dpage;
                    hpage = appendDataPage(hpage, dpage, hf);
                    // recordIndex = 0;
                }

                dpage.appendRecord(record);
                hashIndex.setIndex(record.bn_name, dpage.getId().getPageID());
                // recordIndex += 1;

            }
            else {
                hf.writePage(hpage);
                hf.writePage(dpage);
                break;
            }
        }

        ObjectOutput indexOut = new ObjectOutputStream(new FileOutputStream("index." + pagesize));
        indexOut.writeObject(hashIndex);
        indexOut.close();

        System.out.println("Number of records: " + recordCount);
        System.out.println("Number of pages: " + pid);
        long end = System.currentTimeMillis();
        long cost_time = end - start;
        System.out.println("Dump HeapFile cost " + cost_time + "ms.");
    }

    private static HeaderPage appendDataPage (HeaderPage hpage, DataPage dpage, HeapFile hf) throws IOException{
        if (hpage.isFull()) {
            HeaderPage new_hpage = new HeaderPage(pid, pagesize);
            pid += 1;
            hpage.setNextHeader(new_hpage);
            hf.writePage(hpage);
            hpage = new_hpage;
        }
        else {
            hpage.appendDataPage(dpage.getId());
        }
        return hpage;
    }
    private static void usage() {
        System.out.println("Usage:");
        System.out.println("java dbload -p $pagesize $datafile");
    }
}
