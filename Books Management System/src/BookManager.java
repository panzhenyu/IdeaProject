import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BookManager
{
    private RandomAccessFile book_info;
    private List<Book> books;
    private static long bookID;
    private static long bookNum;
    final private int BLOCKSIZE;
    private class PrimaryKey implements Comparable<PrimaryKey>
    {
        String bookName;
        String author;
        public PrimaryKey() {}
        public PrimaryKey(String bookName, String author)
        {
            this();
            this.bookName = bookName;
            this.author = author;
        }
        public int compareTo(PrimaryKey o)
        {
            return (bookName+author).compareTo(o.bookName+o.author);
        }
    }
    public BookManager() { BLOCKSIZE = 4096; }

    public BookManager(int blocksize)
    {
        books = new LinkedList<>();
        bookID = 0;
        bookNum = 0;
        BLOCKSIZE = blocksize;
    }

    public boolean importBook(DataPackage datapkg)
    {
        try {
            book_info = new RandomAccessFile("book_info.txt", "rw");
            book_info.seek(book_info.length());
            Iterator ite = datapkg.getData();
            while(ite.hasNext())
                book_info.writeBytes(ite.next().toString());
            book_info.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public boolean append(Book b)
    {
        try {
            book_info = new RandomAccessFile("book_info.txt", "rw");
            book_info.seek(book_info.length());
            book_info.writeBytes(b.toString() + "\n");
            book_info.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
}

