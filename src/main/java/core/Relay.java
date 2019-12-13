package core;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;


public class Relay {
    // use win API mutex for inter-processes synchronization
    // *e
    private WinNT.HANDLE empty;
    // *r
    private WinNT.HANDLE read;
    // *w
    private WinNT.HANDLE write;
    public int test;
    private int numR, numW, waitR, waitW;

    private void capture(WinNT.HANDLE mutex) {
        Kernel32.INSTANCE.WaitForSingleObject(mutex, WinBase.INFINITE);
    }

    private void release(WinNT.HANDLE mutex) {
        Kernel32.INSTANCE.ReleaseMutex(mutex);
    }

    public class RelayWriter extends Thread {
        public RelayWriter(Runnable r) {
            super(r);
        }

        public int getTest() {return test;}
        public void write() {
            while (true) {
                // e.P()
                capture(empty);
                if (numW > 0 || numR > 0) {
                    // there is writers OR readers
                    waitW++;

                    //waiting for writing
                    release(empty);
                    capture(write);
                }
                numW++;
                relaySynchronize();
                // do the job
                run();
                // what is it
                capture(empty);
                numW--;
                relaySynchronize();
            }
        }

    }
    public class RelayReader extends Thread {

        public void read() {
            while (true) {
                // e.P()
                capture(empty);
                if (numW > 0) {
                    // there is writers
                    waitR++;

                    //waiting for writing
                    release(empty);
                    capture(read);
                }
                numR++;
                relaySynchronize();
                // do the job
                run();
                // what is it
                capture(empty);
                numR--;
                relaySynchronize();
            }
        }

    }

    public void relaySynchronize() {
        // there is writers or/and readers, and writers waiting
        if ((numW > 0 || numR > 0) && waitW > 0) {
            // e.V() =>
            release(empty);
        } else if ((numW == 0 && numR == 0) && waitW > 0) {
            // there is NO writers and readers, and writers waiting
            waitW--;
            // allow to write
            release(write);
        } else if (numW == 0 && waitW == 0 && waitR > 0) {
            // there is NO writers and writers waiting and readers waiting absent
            waitR--;
            // allow to read
            release(read);
        } else release(empty);
    }
}
