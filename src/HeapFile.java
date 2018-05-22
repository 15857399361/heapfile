import java.util.*;
import java.io.*;

public class HeapFile implements DataFile {
    private short pageSize;
    private String file;

    public HeapFile(short pageSize, String file) {
        this.pageSize = pageSize;
        this.file = file;
    }
    public Page readPage(PageID pid) {
        try {
            RandomAccessFile f = new RandomAccessFile(this.file,"r");
            int offset = this.pageSize * pid.getPageID();
            byte[] data = new byte[this.pageSize];
            if (offset + this.pageSize > f.length()) {
                System.err.println("Page offset exceeds max size, error!");
                System.exit(1);
            }
            f.seek(offset);
            f.readFully(data);
            f.close();
            if (pid.getPageType() == 0) {
                return new HeaderPage(data);
            }
            else {
                return new DataPage(data);
            }
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
            throw new IllegalArgumentException();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    public void writePage(Page page) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(this.file, "rw");
        PageID pid = page.getId();
        int offset = this.pageSize * pid.getPageID();
        raf.seek(offset);
        raf.write(page.serialize(), 0, this.pageSize);
        raf.close();
    }

}
