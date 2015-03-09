package zmq.pubsub.test;

public class MultiThreadExample {

    private static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println ("Current Thread: Name = " 
            		+ Thread.currentThread().getName() + ", ID = "
            		+ Thread.currentThread().getId());
			System.out.flush();
            //Thread.currentThread().
        }
    }
    
	public MultiThreadExample() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		final int THREAD_COUNT = 4;
		MyThread[] myThread = new MyThread[THREAD_COUNT];
		
		for (int i=0; i<THREAD_COUNT; ++i) {
			myThread[i] = new MyThread();
			System.out.println("Starting thread #" + i);
			System.out.flush();
		    myThread[i].start();
		}
	}

}
