public class Book
{
    private long id;
    private String name;
    private String author;
    private int stock;
    private int total;
    public Book() {}
    public Book(long id, String name, String author, int stock, int total)
    {
        this();
        this.id = id;
        this.name = name;
        this.author = author;
        this.stock = stock;
        this.total = total;
    }

    public boolean lend()
    {
        if(stock == 0)
            return false;
        stock--;
        return true;
    }

    public boolean giveBack()
    {
        stock++;
        return true;
    }

    public String toString()
    {
        return this.id + " " + this.name + " " + this.author + " " + this.stock + " " + this.total;
    }
}
