package core;

import com.sun.jna.Pointer;

import static core.Kernel32.*;
import static core.Kernel32.Constants.*;

public class Kernel32Utils {
    public static Pointer createOrOpenSemaphore(String semaphoreName, int startStatus, int maxCount) {
        Pointer handle = KERNEL32.OpenSemaphoreW(SEMAPHORE_ALL_ACCESS, true, semaphoreName);
        if (handle == null) {
            System.out.println(semaphoreName + " was not open, creating ... ");
            handle = KERNEL32.CreateSemaphoreW(NULL, startStatus, maxCount, semaphoreName);
        }
        return handle;
    }

    public static Pointer createOrOpenFileMapping(String name) {
        Pointer memory;
        memory = KERNEL32.OpenFileMappingW(FILE_MAP_ALL_ACCESS, false, name);
        if (memory == null) {
            memory = KERNEL32.CreateFileMappingW(INVALID_HANDLE_VALUE_2, NULL, PAGE_READWRITE, 0, 4096, name);
        }
        if (memory == null)
            System.out.println("Error : Couldn't create file mapping");
        return memory;
    }

    public static boolean hasError() {
        return KERNEL32.GetLastError() != 0;
    }
}
