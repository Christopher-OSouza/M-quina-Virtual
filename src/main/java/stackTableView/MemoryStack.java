package stackTableView;

public class MemoryStack {
    public String value;
    public int address;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public MemoryStack(String value, int address) {
        this.value = value;
        this.address = address;
    }
}
