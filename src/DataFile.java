import java.util.*;
import java.io.*;

public interface DataFile {

    public Page readPage(PageID id);

    public void writePage(Page p) throws IOException;

    //public ArrayList<Page> insertTuple(TransactionId tid, Tuple t) throws DbException, IOException, TransactionAbortedException;

    //public DbFileIterator iterator(TransactionId tid);

    // public int getId();
}
