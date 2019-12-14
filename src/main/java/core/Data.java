package core;

public enum Data {
    INT(Integer.BYTES);
    private int bytes;


    Data(int bytes) {
        this.bytes = bytes;
    }
    public int getSize() {
        return bytes;
    }
}
