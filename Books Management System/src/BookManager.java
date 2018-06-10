import java.io.BufferedReader;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import utils.BplusTree;

public class BookManager
{
    private RandomAccessFile book_info;
    private List<Book> books;
    private static long bookID;
    private static long bookNum;
    final private int BLOCK_SIZE;
    final private int RECORD_SIZE;
    private BplusTree<PrimaryKey, Long> bookidx;

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

        public PrimaryKey(String objStr) throws Error
        {
            String[] var = objStr.split(":");
            if(var.length < 2)
                throw new Error("key format error");
            this.bookName = var[0];
            this.author = var[1];
        }

        public int compareTo(PrimaryKey o)
        {
            return (bookName+":"+author).compareTo(o.bookName+":"+o.author);
        }

        public String toString()
        {
            return bookName + ":" + author;
        }
    }

    public BookManager()
    {
        this(4096, 64, 63);
    }

    public BookManager(int blocksize, int recordsize, int bookidxRank)
    {
        books = new LinkedList<>();
        bookID = 0;
        bookNum = 0;
        BLOCK_SIZE = blocksize;
        RECORD_SIZE = recordsize;
        bookidx = new BplusTree<>(bookidxRank);
    }

    public boolean importBook(String filename)
    {
        try {
            book_info = new RandomAccessFile("book_info.txt", "rw");
            book_info.seek(book_info.length());
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
            Iterator ite = reader.lines().iterator();
            String tmp;
            String[] bookInfo;
            while(ite.hasNext())
            {
                bookInfo = ite.next().toString().split(" ");
                if(bookInfo.length<2 || !bookidx.insert(new PrimaryKey(bookInfo[0], bookInfo[1]), bookID+1))
                    continue;
                bookID++;
                bookNum++;
                tmp = bookID + " " + bookInfo[0] + " " + bookInfo[1] + " " + bookInfo[2] + " " + bookInfo[2];
                for(int i=tmp.length();i<=RECORD_SIZE;i++)
                    tmp += " ";
                book_info.write(tmp.getBytes());
            }
            reader.close();
            book_info.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public boolean append(Book b)
    {
        try {
            if(bookidx == null)
                BplusTree.read("book_primarykey_index.txt", BLOCK_SIZE, bookidx, PrimaryKey.class.getName());
            book_info = new RandomAccessFile("book_info.txt", "rw");
            book_info.seek(book_info.length());
            book_info.writeBytes(b.toString() + "\n");
            book_info.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String csname = PrimaryKey.class.getName();
        PrimaryKey o = (PrimaryKey)Class.forName(csname).newInstance();
        o.bookName = "111";
    }

    public boolean delete(Book b)
    {
        return true;
    }

    public boolean borrow(Book b)
    {
        return true;
    }

    public boolean giveBack(Book b)
    {
        return true;
    }

    public boolean save()
    {
        try {
            return true;
        } catch(Error e) {
            return false;
        }
    }

//    public static BookManager load(String filename)
//    {
//
//    }

//    public DataPackage find(String bookname, String author)
//    {
//        DataPackage full = fullMatch(bookname, author);
//        DataPackage fuzzy = fuzzyMatch(bookname, author);
//
//    }
//
//    private DataPackage fullMatch(String bookname, String author)
//    {
//
//    }
//
//    private DataPackage fuzzyMatch(String bookname, String author)
//    {
//
//    }

}

