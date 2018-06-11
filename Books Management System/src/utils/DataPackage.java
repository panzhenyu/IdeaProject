package utils;

import sys.Book;

import java.util.ArrayList;
import java.util.Iterator;

public class DataPackage
{
    private ArrayList<Book> books;
    public DataPackage()
    {
        books = new ArrayList<>();
    }

    public DataPackage(ArrayList<Book> books)
    {
        this.books = books;
    }

    public Iterator getData()
    {
        return books.iterator();
    }

    public DataPackage mix(DataPackage o)
    {
        ArrayList<Book> newBooks = (ArrayList<Book>)this.books.clone();
        newBooks.addAll(o.books);
        return new DataPackage(newBooks);
    }
}
