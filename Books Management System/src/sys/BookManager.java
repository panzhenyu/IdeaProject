package sys;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import utils.BplusTree;
import utils.DataPackage;

public class BookManager
{
    private ArrayList<Book> bookBuff;
    private BplusTree<PrimaryKey, Long> bookidx;
    private static long bookID;
    private static long bookNum;
    final private int BLOCK_SIZE;
    final private int RECORD_SIZE;
    final private int BOOK_IDX_RANK;
    final private static String BOOK_INFO = "book_info.txt";
    final private static String BOOK_PRIMARY_KEY_IDX = "book_primary_key_idx.txt";
    final private static String MANAGER_CONFIG = "manage_config.txt";

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

    private BookManager()
    {
        this(0, 0, 4096, 64, 63);
    }

    private BookManager(long bookID, long bookNum, int blocksize, int recordsize, int bookidxRank)
    {
        this.bookID = bookID;
        this.bookNum = bookNum;
        this.BLOCK_SIZE = blocksize;
        this.RECORD_SIZE = recordsize;
        this.BOOK_IDX_RANK = bookidxRank;
        this.bookidx = new BplusTree<>(BOOK_IDX_RANK);
        this.bookidx.read(BOOK_PRIMARY_KEY_IDX, BLOCK_SIZE);
    }

    public String toString()
    {
        return bookID + " " + bookNum + " " + BLOCK_SIZE + " " + RECORD_SIZE + " " + BOOK_IDX_RANK;
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
                for(int i=tmp.length();i<RECORD_SIZE;i++)
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
            String var = b.toString();
            for(int i=var.length();i<RECORD_SIZE;i++)
                var += " ";
            book_info.write(var.getBytes());
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
            File fp = new File(MANAGER_CONFIG);
            if(!fp.exists())
                if(!fp.createNewFile())
                    return false;
            BufferedWriter writer = new BufferedWriter(new FileWriter(fp));
            writer.write(this.toString());
            writer.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public static BookManager load(String filename)
    {
        try {
            File fp = new File(MANAGER_CONFIG);
            if(!fp.exists())
                return new BookManager();
            else {
                BufferedReader reader = new BufferedReader(new FileReader(fp));
                String var = reader.readLine();
                String[] info = var.split(" ");
                BookManager m = new BookManager(Long.parseLong(info[0]),
                        Long.parseLong(info[1]),
                        Integer.parseInt(info[2]),
                        Integer.parseInt(info[3]),
                        Integer.parseInt(info[4]));
                reader.close();
                return m;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public DataPackage find(String bookname, String author) throws IOException
    {
        if(bookname==null && author==null)
            return getAll();
        else if(bookname == null)
            return getByAuthor(author);
        else if(author == null)
            return getByName(bookname);
        else {
            Book b = fullMatch(bookname, author);
            if (b != null) {
                bookBuff = new ArrayList<>();
                bookBuff.add(b);
                return new DataPackage(bookBuff);
            }
            return fuzzyMatch(bookname, author);
        }
    }


    private Long locate(String bookname ,String author) throws IOException
    {
        PrimaryKey pk = new PrimaryKey(bookname, author);
        if(!bookidx.contains(pk))
            return null;
        return bookidx.getFlag(pk);
    }

    private DataPackage getByAuthor(String author)
    {
        Iterator<Book> ite = getAll().getData();
        if(ite == null)
            return null;
        bookBuff = new ArrayList<>();
        Book var;
        while(ite.hasNext())
        {
            var = ite.next();
            if(var.getAuthor().equals(author))
                bookBuff.add(var);
        }
        if(bookBuff.size() == 0)
            return null;
        return new DataPackage(bookBuff);
    }

    private DataPackage getByName(String bookname)
    {
        Iterator<Book> ite = getAll().getData();
        if(ite == null)
            return null;
        bookBuff = new ArrayList<>();
        Book var;
        while(ite.hasNext())
        {
            var = ite.next();
            if(var.getName().equals(bookname))
                bookBuff.add(var);
        }
        if(bookBuff.size() == 0)
            return null;
        return new DataPackage(bookBuff);
    }

    private DataPackage getAll()
    {
        try {
            RandomAccessFile bookInfo = new RandomAccessFile(BOOK_INFO, "r");
            bookBuff = new ArrayList<>();
            long len = bookInfo.length();
            long num = len / RECORD_SIZE;
            byte[] buff = new byte[RECORD_SIZE];
            String var;
            String[] var1;
            while(num-- > 0) {
                bookInfo.read(buff);
                var = new String(buff);
                var1 = var.split(" ");
                bookBuff.add(new Book(Long.parseLong(var1[0]), var1[1], var1[2], Integer.parseInt(var1[3]), Integer.parseInt(var1[4])));
            }
            bookInfo.close();
            return new DataPackage(bookBuff);
        } catch(IOException e) {
            return null;
        }
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

    private DataPackage fuzzyMatch(String bookname, String author)
    {
        return null;
    }

    public static void main(String[] args) throws IOException
    {
//        BookManager m = new BookManager();
//        m.importBook("test.txt");
//        m.bookidx.write(m.BOOK_PRIMARY_KEY_IDX, m.BLOCK_SIZE);
//        m.bookidx.show();
//        System.out.println(m.fullMatch("bk1", "au1"));
//        m.save();

    }
}
