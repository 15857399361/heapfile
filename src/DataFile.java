import java.util.*;
import java.io.*;

public interface DataFile {

    public Page readPage(PageID id);

    public void writePage(Page p) throws IOException;

}
