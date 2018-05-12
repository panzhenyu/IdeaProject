package userSystem;

import java.io.*;

public class Test
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
//        File fp = new File("/home/panda/Desktop/screenshot.png");
//        File op = new File("/home/panda/Desktop/backup.png");
//        BufferedInputStream in = new BufferedInputStream(new FileInputStream(fp));
//        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(op));
//        byte[] buff = new byte[2048];
//        while(in.read(buff) != -1)
//            out.write(buff);
//        out.write(buff);
        A a = new A(5);
        f(a);
        System.out.println(a.i);
    }
    static void f(A a)
    {
        a = new A(1);
    }
}
class A
{
    int i;
    A(int i)
    {
        this.i = i;
    }
}
