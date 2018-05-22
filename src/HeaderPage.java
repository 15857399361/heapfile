import java.nio.ByteBuffer;
import java.io.*;
import java.lang.Math;

public class HeaderPage implements Page {

    private PageID pageID;
    private short pageSize;
    private short dataPageNum;
    public PageID[] dataPages;
    private PageID nextHeader;


    // Create an empty Page
    public HeaderPage(int pid, short pageSize) {
        this.pageID = new PageID(pid, (short) 0);
        this.pageSize = pageSize;
        this.dataPages = new PageID[this.maxDataPageNum()];
    }

    // Create a Page loaded from byte stream
    public HeaderPage(byte[] data) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        byte[] pid = new byte[PageID.size()];
        dis.read(pid, 0, PageID.size());
        this.pageID = new PageID(pid);
        this.pageSize = dis.readShort();
        this.dataPageNum = dis.readShort();

        this.dataPages = new PageID[this.maxDataPageNum()];
        for (int i=0; i<this.dataPageNum; i++) {
            dis.read(pid, 0, PageID.size());
            this.dataPages[i] = new PageID(pid);
        }

        dis.read(pid, 0, PageID.size());
        PageID tempID = new PageID(pid);
        if (tempID.getPageType() == 0) {
            this.nextHeader = new PageID(pid);
        }
        else {
            this.nextHeader = null;
        }

    }

    public int maxDataPageNum() {
        return (int) Math.floor((this.pageSize - PageID.size() * 2 - Short.BYTES * 2)/PageID.size());
    }

    public void setNextHeader(HeaderPage hpage) {
        this.nextHeader = hpage.getId();
    }

    public PageID getNextHeader()
    {
        return this.nextHeader;
    }

    @Override
    public PageID getId() {
        return this.pageID;
    }

    public boolean isFull() {
        return (this.dataPageNum == this.maxDataPageNum());
    }

    public void appendDataPage(PageID pid) {
        if (this.isFull()) {
            System.out.println("This Page is full, please create a new page.");
            return;
        }
        for (int i=0; i<this.maxDataPageNum(); i++) {
            if (this.dataPages[i] == null) {
                this.dataPages[i] = pid;
                this.dataPageNum += 1;
                return;
            }
        }
    }

    @Override
    public byte[] serialize() {
        byte[] result = new byte[this.pageSize];
        System.arraycopy(this.pageID.serialize(), 0, result, 0, PageID.size());
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.pageSize).array(),
                0, result, PageID.size(), Short.BYTES);
        System.arraycopy(ByteBuffer.allocate(2).putShort(this.dataPageNum).array(),
                0, result, PageID.size() + Short.BYTES, Short.BYTES);
        short start = (short) (PageID.size() + Short.BYTES * 2);
        for (int i=0; i<this.dataPageNum; i++) {
            System.arraycopy(this.dataPages[i].serialize(),0, result, start, PageID.size());
            start += PageID.size();
        }
        try {
            System.arraycopy(this.nextHeader.serialize(), 0, result, start, PageID.size());
        }
        catch (NullPointerException e) {
            PageID pid = new PageID(0, (short) -1);
            System.arraycopy(pid.serialize(), 0, result, start, PageID.size());
        }
        return result;
    }
}
