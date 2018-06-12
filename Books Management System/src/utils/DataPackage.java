package utils;

import sys.Book;

import java.util.ArrayList;
import java.util.Iterator;

public class DataPackage
{
    private ArrayList<Book> books;

    public DataPackage(ArrayList<Book> books)
    {
        this.books = books;
    }

    public Iterator<Book> getData()
    {
        return books.iterator();
    }

    public ArrayList<Book> getBooks()
    {
        return books;
    }
}
