import java.util.*;

public class PageID {

    // Each PageID has 6 bytes (4 bytes Int pageID and 2 bytes Short pageType)
    private int pageID;
    // pageType: 0 - HeaderPage
    //           1 - DataPage
    private short pageType;

    public PageID(int pid, short ptype) {
        this.pageID = pid;
        this.pageType = ptype;
    }

    public PageID(byte[] data) {
        this.pageID = (int) (data[0] << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF));
        this.pageType = (short) (data[4] << 8 | (data[5] & 0xFF));
    }

    public int getPageID() {
        return pageID;
    }

    public short getPageType() {
        return pageType;
    }

    public byte[] serialize() {
        byte data[] = new byte[6];

        data[0] = (byte) (this.pageID >> 24);
        data[1] = (byte) (this.pageID >> 16);
        data[2] = (byte) (this.pageID >> 8);
        data[3] = (byte) (this.pageID);

        data[4] = (byte) (this.pageType >> 8);
        data[5] = (byte) (this.pageType);

        return data;
    }

    public static int size() {
        return Integer.BYTES + Short.BYTES;
    }
}
