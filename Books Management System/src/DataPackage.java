import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DataPackage
{
    private ArrayList<Book> books;
    public DataPackage()
    {
        books = new ArrayList<>();
    }

    public boolean importFile(String file)
    {
        try {
            File fp = new File(file);
            BufferedReader reader = new BufferedReader(new FileReader(fp));
            while(reader.ready());
            reader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public Iterator getData()
    {
        return books.iterator();
    }
}
