import java.nio.ByteBuffer;
import java.util.*;
import java.text.*;
import java.io.*;

public class HeapFile {
    public short pageSize;
    public String file;
    public int pageCount;
    public int recordCount;
    public List<Page> pages;

    public HeapFile(short pageSize, String file) {
        this.pageSize = pageSize;
        this.pages = new ArrayList<>();
        this.file = file;
        this.pageCount = 0;
        this.recordCount = 0;
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            // remove the fist line.
            reader.readLine();
            String str = null;
            Page page = new Page(this.pageSize);
            this.pageCount += 1;
            while (true) {
                str = reader.readLine();
                if(str!=null) {
                    Record record = new Record(str);
                    this.recordCount += 1;
                    if (!page.add_record(record)) {
                        pages.add(page);
                        page = new Page(this.pageSize);
                        page.add_record(record);
                        this.pageCount += 1;
                    }

                }
                else
                    break;
            }
            pages.add(page);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HeapFile(short pageSize) {
        try (InputStream is = new FileInputStream("heap." + pageSize)) {
            BufferedInputStream reader = new BufferedInputStream(is, pageSize);
            int offset = Short.BYTES + Integer.BYTES * 2;
            byte[] byte_page_size = new byte[Short.BYTES];
            byte[] byte_page_count = new byte[Integer.BYTES];
            byte[] byte_record_count = new byte[Integer.BYTES];
            reader.read(byte_page_size, 0, Short.BYTES);
            reader.read(byte_page_count, 0, Integer.BYTES);
            reader.read(byte_record_count, 0, Integer.BYTES);
            this.pageSize = ByteBuffer.wrap(byte_page_size).getShort();
            this.pageCount = ByteBuffer.wrap(byte_page_count).getInt();
            this.recordCount = ByteBuffer.wrap(byte_record_count).getInt();
            this.pages = new ArrayList<>();
            byte[] byte_page = new byte[this.pageSize];
            while(reader.read(byte_page, 0, this.pageSize) != -1)
            {
                Page page = new Page(byte_page);
                this.pages.add(page);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream("heap." + this.pageSize, true)) {
            byte[] byte_page_size = ByteBuffer.allocate(Short.BYTES).putShort(this.pageSize).array();
            byte[] byte_page_count = ByteBuffer.allocate(Integer.BYTES).putInt(this.pageCount).array();
            byte[] byte_record_count = ByteBuffer.allocate(Integer.BYTES).putInt(this.recordCount).array();
            fos.write(byte_page_size);
            fos.write(byte_page_count);
            fos.write(byte_record_count);
            for (Page page: this.pages){
                fos.write(page.serializer());
            }
            // the number of records
            //loaded, number of pages used and the number of milliseconds to create the heap file

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void query(String text) {
        for (Page page: this.pages) {
            for (Record record: page.records) {
                if (record.bn_name.equals(text)) {
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
            }
        }
    }
}