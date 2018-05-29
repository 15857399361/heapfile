import java.util.*;
import java.io.*;

public interface Page {

    public PageID getId();

    public byte[] serialize();
}
