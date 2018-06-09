public class Synchronized
{

	static int n1 = 4;
	static int n2 = 4;
	static boolean lock = false;
	public static void main(String[] args) throws InterruptedException
	{
		Thread t1 = new Thread(new myThread1());
		Thread t2 = new Thread(new myThread2());
		t1.start();
		t2.start();
		while(true)
		{
			Thread.sleep(2000);
			System.out.println("n1=" + n1 + " n2=" + n2);
			if(n1+n2 != 8)
				System.out.println("not synchronized with n1+n2=" + (n1+n2));
			else
				System.out.println("synchronized!");
		}
	}
	static class myThread1 implements Runnable
	{
		public void run()
        {
            while(true)
            {
                synchronized (Synchronized.class) {
                    Synchronized.n1--;
                    Synchronized.n2++;
                    if (n1 + n2 != 8)
                        System.out.println("not synchronized with n1+n2=" + (n1 + n2));
                    else
                        System.out.println("synchronized!");
                }
            }
		}
	}
	static class myThread2 implements Runnable
	{
		public void run()
		{
			while(true)
			{
			    synchronized (Synchronized.class) {
                    Synchronized.n1 += 3;
                    Synchronized.n2 -= 3;
                    if (n1 + n2 != 8)
                        System.out.println("not synchronized with n1+n2=" + (n1 + n2));
                    else
                        System.out.println("synchronized!");
                }
			}
		}
	}
}