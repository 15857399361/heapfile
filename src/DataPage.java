import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;

public class DataPage implements Page {

    private PageID pageID;
    private short pageSize;
    private short records_count;
    public List<Record> records;


    // Create an empty PageID
    public DataPage(int pid, short pageSize) {
        this.pageID = new PageID(pid, (short) 1);
        this.pageSize = pageSize;
        this.records_count = 0;
        this.records = new ArrayList<>();
    }

    // Create a Page loaded from byte stream
    public DataPage(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        byte[] pid = new byte[PageID.size()];
        dis.read(pid, 0, PageID.size());
        this.pageID = new PageID(pid);
        this.pageSize = dis.readShort();
        this.records_count = dis.readShort();

        short start = (short) (PageID.size() + Short.BYTES * 2);
        this.records = new ArrayList<>();
        for (short i = 0; i < this.records_count; i++) {
            Record record = new Record(Arrays.copyOfRange(data, start, this.pageSize));
            start = (short) (start + record.lenth());
            this.records.add(record);
        }

    }

    @Override
    public PageID getId() {
        return this.pageID;
    }

    public boolean isFull(Record record) {
        short lenth = (short)(PageID.size() + Short.BYTES * 2);
        for (Record rcd : this.records) {
            lenth += rcd.lenth();
        }
        return (lenth + record.lenth() > this.pageSize);
    }

    public void appendRecord(Record record) {
        this.records.add(record);
        this.records_count += 1;
    }

    @Override
    public byte[] serialize() {
        byte[] result = new byte[this.pageSize];
        System.arraycopy(this.pageID.serialize(), 0, result, 0, PageID.size());
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.pageSize).array(),
                0, result, PageID.size(), Short.BYTES);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.records_count).array(),
                0, result, PageID.size() + Short.BYTES, Short.BYTES);
        short start = (short) (PageID.size() + Short.BYTES * 2);
        for (Record rcd : this.records)  {
            System.arraycopy(rcd.serialize(),0, result, start, rcd.lenth());
            start += rcd.lenth();
        }
        return result;
    }
}
