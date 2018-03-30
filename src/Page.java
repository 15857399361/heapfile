import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;

public class Page implements Serializable {
    public short pageSize;
    public short records_count;
    public short currentSize;
    public List<Record> records;

    public Page(short pageSize) {
        this.pageSize = pageSize;
        this.records_count = 0;
        this.currentSize = Short.BYTES * 2;
        this.records = new ArrayList<>();
    }

    public Page(byte[] page) {
        this.pageSize = ByteBuffer.wrap(Arrays.copyOfRange(page, 0, 2)).getShort();
        this.records_count = ByteBuffer.wrap(Arrays.copyOfRange(page, 2, 4)).getShort();
        this.currentSize = Short.BYTES * 2;
        short start = 4;
        this.records = new ArrayList<>();
        for (short i = 0; i < this.records_count; i++) {
            Record record = new Record(Arrays.copyOfRange(page, start, this.pageSize));
            start = (short) (start + record.lenth());
            this.records.add(record);
        }

    }

    public boolean add_record(Record record) {
        if (this.currentSize + record.lenth() > pageSize) {
            return false;
        }
        else {
            this.currentSize += record.lenth();
            this.records_count += 1;
            this.records.add(record);
            return true;
        }
    }

    public byte[] serializer() {
        byte[] result = new byte[this.pageSize];
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.pageSize).array(),
                0, result, 0, 2);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.records_count).array(),
                0, result, 2, 2);
        short start = 4;
        for (Record record: this.records) {
            System.arraycopy(record.serializer(),0, result, start, record.lenth());
            start += record.lenth();
        }
        return result;
    }
}