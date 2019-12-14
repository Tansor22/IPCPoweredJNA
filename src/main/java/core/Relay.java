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
    // shared data
    private SharedInt numR, numW, waitR, waitW;

    {
        // windows mutexes
        empty = Kernel32.INSTANCE.CreateMutex(null, false, "empty");
        read = Kernel32.INSTANCE.CreateMutex(null, false, "read");
        write = Kernel32.INSTANCE.CreateMutex(null, false, "write");
        // shared data
        numR = new SharedInt("numR");
        numW = new SharedInt("numW");
        waitR = new SharedInt("waitR");
        waitW = new SharedInt("waitW");
    }

    private void capture(WinNT.HANDLE mutex) {
        Kernel32.INSTANCE.WaitForSingleObject(mutex, WinBase.INFINITE);
    }

    private void release(WinNT.HANDLE mutex) {
        Kernel32.INSTANCE.ReleaseMutex(mutex);
    }

    public class RelayWriter extends Thread {
        private int iterations;
        public RelayWriter(Runnable r, int iterations) {
            super(r);
            this.iterations = iterations;
        }

        public void write() {
            int i =0;
            while (i++ < iterations) {
                // e.P()
                capture(empty);
                if (numW.get() > 0 || numR.get() > 0) {
                    // there is writers OR readers
                    incSharedInt(waitW);

                    //waiting for writing
                    release(empty);
                    capture(write);
                }
                incSharedInt(numW);
                relaySynchronize();
                // do the job
                run();
                // what is it
                capture(empty);
                decSharedInt(numW);
                relaySynchronize();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public class RelayReader extends Thread {
        private int iterations;
        public RelayReader(Runnable r, int iterations) {
            super(r);
            this.iterations = iterations;
        }
        public void read() {
            int i =0;
            while (i++ < iterations) {
                // e.P()
                capture(empty);
                if (numW.get() > 0) {
                    // there is writers
                    incSharedInt(waitR);

                    //waiting for writing
                    release(empty);
                    capture(read);
                }
                incSharedInt(numR);
                relaySynchronize();
                // do the job
                run();
                // what is it
                capture(empty);
                decSharedInt(numR);
                relaySynchronize();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void incSharedInt(SharedInt shared) {
        Integer value = shared.get();
        System.out.println(shared.getDataId() + " : " + value + " got");
        shared.set(++value);
        System.out.println("Increment it..");
    }

    private void decSharedInt(SharedInt shared) {
        Integer value = shared.get();
        System.out.println(shared.getDataId() + " : " + value + " got");
        shared.set(--value);
        System.out.println("Decrement it..");
    }

    public void relaySynchronize() {
        // there is writers or/and readers, and writers waiting
        if ((numW.get() > 0 || numR.get() > 0) && waitW.get() > 0) {
            // e.V() =>
            release(empty);
        } else if ((numW.get() == 0 && numR.get() == 0) && waitW.get() > 0) {
            // there is NO writers and readers, and writers waiting
            decSharedInt(waitW);
            // allow to write
            release(write);
        } else if (numW.get() == 0 && waitW.get() == 0 && waitR.get() > 0) {
            // there is NO writers and writers waiting and readers waiting absent
            decSharedInt(waitR);
            // allow to read
            release(read);
        } else release(empty);
    }
    public void close() {
        // ???
        Kernel32.INSTANCE.ReleaseMutex(empty);
        Kernel32.INSTANCE.ReleaseMutex(write);
        Kernel32.INSTANCE.ReleaseMutex(read);
        // closing data
        waitR.close();
        waitW.close();
        numR.close();
        numW.close();
    }
}
