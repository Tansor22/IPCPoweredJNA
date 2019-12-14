package core;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SharedInt extends SharedData<Integer> {

    public SharedInt(String dataId) {
        super(dataId, Data.INT);
    }

    @Override
    public void set(Integer data) {
        byte[] bytesToWrite = data.toString().getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytesToWrite);
        try {
            // clears file
            channel.truncate(0);
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private int fromAscii(byte[] bytes, int length) {
        // => +1 for -
        if (length > dataType.getSize() + 1) throw new RuntimeException("Too big to fit in " + dataType);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            if (bytes[i] == (byte) 45) // -
                sb.append('-');
            else
                sb.append(Character.getNumericValue(bytes[i]));
        }
        return Integer.parseInt(sb.toString());
    }

    @Override
    public Integer get() {
        ByteBuffer buffer = ByteBuffer.allocate(dataType.getSize() + 1);
        try {
            channel.position(0);
            int bytesRead = channel.read(buffer);
            if (bytesRead != -1)
                return fromAscii(buffer.array(), bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
