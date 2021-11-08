public class JoinData implements Protocol {

    static final short LOGIN_FAILED = -10; //login access
    static final short JOIN_FAILED =-20;// join the Queue
    static final short LOGIN_ACCESS = 10; //login access
    static final short JOIN_ACCESS =20;// join the Queue
    private boolean flag = false;
    private short protocol;

    public JoinData(boolean flag,short protocol) {
        this.flag =flag;
        this.protocol= protocol;
    }

    @Override
    public short getProtocol() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public void setMessage(String message) {

    }

    @Override
    public void setName(String name) {

    }
}
