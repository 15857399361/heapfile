import java.util.*;
import java.io.*;

public interface Page {

    public PageID getId();

    // public TransactionId isDirty();

    // public void markDirty(boolean dirty, TransactionId tid);

    public byte[] serialize();

    // public Page getBeforeImage();

    // public void setBeforeImage();

    // public Iterator<Tuple> iterator();
}
