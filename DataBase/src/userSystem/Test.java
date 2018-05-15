package userSystem;

import java.io.*;
import java.util.ArrayList;

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
        ArrayList<A> a = new ArrayList<>();
        A a1 = new A();
        A a2 = new A();
        A a3 = new A();
        a.add(a1);
        a.add(a2);
        a.add(a3);
        System.out.println(a.indexOf(new A()));                                       //answer is 1
    }
}
class A
{

}