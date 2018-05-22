import java.io.*;

public class test {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException  {
        PageID pageID = new PageID(1, (short)0);
        byte[] data = pageID.serialize();
        System.out.println(data);
        PageID pageID1 = new PageID(data);
        System.out.println(pageID1.getPageID());
        System.out.println(pageID1.getPageType());
        HeaderPage hpage = new HeaderPage(1, (short) 1024);
        hpage.getId();
    }


    private static void usage() {
        System.out.println("Usage:");
        System.out.println("java dbquery $text $pagesize");
    }
}
