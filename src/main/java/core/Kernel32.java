package core;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Kernel32 extends Library /*StdCallLibrary ??? */ {
    class Constants {
        public static final int SEMAPHORE_ALL_ACCESS = 0x1F0003;
        public static final int INFINITE = 0xFFFFFFFF;
        public static final Pointer NULL = null;
        public static final int FILE_MAP_ALL_ACCESS = 0xf001f;
        public static final int FILE_MAP_ALL_ACCESS_2 = 0xFFFFFFFF;
        public static final int INVALID_HANDLE_VALUE = -1;
        public static final int PAGE_READWRITE = 0x04;
        public static final Pointer INVALID_HANDLE_VALUE_2 = Pointer.createConstant(0xFFFFFFFF);

    }

    Kernel32 KERNEL32 = (Kernel32) Native.loadLibrary("Kernel32", Kernel32.class);

    Pointer OpenSemaphoreW(int dwDesiredAccess, boolean bInheritHandle, String lpName);

    Pointer CreateSemaphoreW(Pointer lpSemaphoreAttributes, long lInitialCount, long lMaximumCount, String lpName);

    boolean ReleaseSemaphore(Pointer hSemaphore, long lReleaseCount, Pointer lpPreviousCount);

    Pointer OpenFileMappingW(int dwDesiredAccess, boolean bInheritHandle, String lpName);

    Pointer CreateFileMappingW(Pointer hFile, Pointer lpFileMappingAttributes, int flProtect, int dwMaximumSizeHigh, int dwMaximumSizeLow, String lpName);

    Pointer MapViewOfFile(Pointer hFileMappingObject, int dwDesiredAccess, int dwFileOffsetHigh, int dwFileOffsetLow, int dwNumberOfBytesToMap);

    int WaitForSingleObject(Pointer hHandle, int dwMilliseconds);
    void CopyMemory(char[] Destination, char[] Source, int Length);

    int GetLastError();
}
