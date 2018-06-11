package sys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import utils.BplusTree;
import utils.DataPackage;

public class BookManager
{
    private List<Book> bookBuff;
    private static long bookID;
    private static long bookNum;
    final private int BLOCK_SIZE;
    final private int RECORD_SIZE;
    final private String BOOK_INFO = "book_info.txt";
    final private String BOOK_PRIMARY_KEY_IDX = "book_primary_key_idx.txt";
    private BplusTree<PrimaryKey, Long> bookidx;

    private static class PrimaryKey extends Object implements Comparable<PrimaryKey>
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

        @Override
        public String toString()
        {
            return bookName + ":" + author;
        }

        @Override
        public boolean equals(Object o)
        {
            return o.toString().equals(this.toString());
        }
    }

    public BookManager()
    {
        this(4096, 64, 63);
    }

    public BookManager(int blocksize, int recordsize, int bookidxRank)
    {
        bookBuff = new LinkedList<>();
        bookID = 0;
        bookNum = 0;
        BLOCK_SIZE = blocksize;
        RECORD_SIZE = recordsize;
        bookidx = new BplusTree<>(bookidxRank);
        bookidx.read(BOOK_PRIMARY_KEY_IDX, BLOCK_SIZE);
    }

    public boolean importBook(String filename)
    {
        try {
            RandomAccessFile book_info = new RandomAccessFile(BOOK_INFO, "rw");
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
            RandomAccessFile book_info = new RandomAccessFile(BOOK_INFO, "rw");
            book_info.seek(book_info.length());
            book_info.writeBytes(b.toString() + "\n");
            book_info.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public boolean delete(Book b)
    {
        try {
            RandomAccessFile book_info = new RandomAccessFile(BOOK_INFO, "rw");
            Long addr = locate(b.getName(), b.getAuthor());
            if(addr == null)
                return false;
            book_info.seek((addr-1) * RECORD_SIZE);
            String var = "";
            for(int i=0;i<RECORD_SIZE;i++)
                var += " ";
            book_info.write(var.getBytes());
            bookNum--;
            book_info.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean borrow(Book b)
    {
        try {
            RandomAccessFile book_info = new RandomAccessFile(BOOK_INFO, "rw");
            if(!b.borrow())
                return false;
            Long addr = locate(b.getName(), b.getAuthor());
            if(addr == null)
                return false;
            book_info.seek((addr-1) * RECORD_SIZE);
            book_info.write(b.toString().getBytes());
            book_info.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean giveBack(Book b)
    {
        try {
            RandomAccessFile book_info = new RandomAccessFile(BOOK_INFO, "rw");
            if(!b.giveBack())
                return false;
            Long addr = locate(b.getName(), b.getAuthor());
            if(addr == null)
                return false;
            book_info.seek((addr-1) * RECORD_SIZE);
            book_info.write(b.toString().getBytes());
            book_info.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean save()
    {
        try {
            return true;
        } catch(Error e) {
            return false;
        }
    }

//    public  BookManager load(String filename)
//    {
//
//    }

//    public DataPackage find(String bookname, String author)
//    {
//        utils.utils.DataPackage full = fullMatch(bookname, author);
//        utils.utils.DataPackage fuzzy = fuzzyMatch(bookname, author);
//
//    }
//

    private Long locate(String bookname ,String author) throws IOException
    {
        PrimaryKey pk = new PrimaryKey(bookname, author);
        if(!bookidx.contains(pk))
            return null;
        return bookidx.getFlag(pk);
    }

    private Book fullMatch(String bookname, String author) throws IOException
    {
        PrimaryKey pk = new PrimaryKey(bookname, author);
        if(!bookidx.contains(pk))
            return null;
        Long flag = bookidx.getFlag(pk);
        RandomAccessFile book_info = new RandomAccessFile(BOOK_INFO, "rw");
        book_info.seek((flag - 1) * RECORD_SIZE);
        byte[] record = new byte[RECORD_SIZE];
        book_info.read(record);
        String book = new String(record);
        String[] info = book.split(" ");
        book_info.close();
        return new Book(Long.parseLong(info[0]), info[1], info[2], Integer.parseInt(info[3]), Integer.parseInt(info[4]));
    }
//
//    private utils.utils.DataPackage fuzzyMatch(String bookname, String author)
//    {
//
//    }

    public static void main(String[] args) throws IOException
    {
        BookManager m = new BookManager();
        m.importBook("test.txt");
        m.bookidx.write(m.BOOK_PRIMARY_KEY_IDX, m.BLOCK_SIZE);
        m.bookidx.show();
        System.out.println(m.fullMatch("bk1", "au1"));
        m.borrow(new Book(1, "bk1", "au1", 1, 1));
    }
}
