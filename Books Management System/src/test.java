import java.io.*;
import java.util.Iterator;

public class test
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        RandomAccessFile file = new RandomAccessFile("test.txt", "rw");
//        byte[] buff = new byte[4096];
//        file.readFully(buff, 0, (int)file.length());
//        String[] s = new String(buff).split("\n");
//        for(String ss : s)
//            System.out.print(ss + "\n");
//        System.out.println(file.readLine());
//        System.out.println(file.length());
//        file.close();

        BufferedReader reader = new BufferedReader(new FileReader(new File("test.txt")));
        Iterator itr = reader.lines().iterator();
        while(itr.hasNext())
            System.out.println(itr.next());

//        RandomAccessFile file = new RandomAccessFile("test.txt", "rw");
//        file.seek(file.length());
//        long count = 1000000000;
//        while(count-- > 0)
//            file.writeBytes("hello world \n");
    }
}
