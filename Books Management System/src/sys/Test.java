package sys;

import java.io.*;
import java.util.Iterator;

public class Test
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        RandomAccessFile file = new RandomAccessFile("tet.txt", "rw");
        file.write("112".getBytes());
        file.close();
//        byte[] buff = new byte[4096];
//        file.readFully(buff, 0, (int)file.length());
//        String[] s = new String(buff).split("\n");
//        for(String ss : s)
//            System.out.print(ss + "\n");
//        System.out.println(file.readLine());
//        System.out.println(file.length());
//        file.close();

//        BufferedReader reader = new BufferedReader(new FileReader(new File("sys.test.txt")));
//        Iterator itr = reader.lines().iterator();
//        while(itr.hasNext())
//        {
//            String[] ss = itr.next().toString().split(" ");
//            for(String s:ss)
//                System.out.print(s + " ");
//            System.out.println();
//        }

//        RandomAccessFile file = new RandomAccessFile("sys.test.txt", "rw");
//        file.seek(file.length());
//        long count = 1000000000;
//        while(count-- > 0)
//            file.writeBytes("hello world \n");

//        System.out.println("    ".split(" ").length);
    }
}
