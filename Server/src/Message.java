import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private int size;
    private byte[] data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
