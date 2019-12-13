package runners;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;

public class MutexDemo {

    private static int shared;
    public static void main(String[] args) {
        Thread[] threads = new Thread[10];
        WinNT.HANDLE mutex = Kernel32.INSTANCE.CreateMutex(null, false, "mt");
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {

                //capture
                Kernel32.INSTANCE.WaitForSingleObject(mutex, WinBase.INFINITE);
                System.out.println("Shared before: " + shared);
                shared++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Shared after: " + shared);
                // release
                Kernel32.INSTANCE.ReleaseMutex(mutex);
            });
            threads[i].setName("Thread " + i);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

    }
}
