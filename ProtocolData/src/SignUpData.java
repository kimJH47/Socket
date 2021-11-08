public class SignUpData implements Protocol {

    private String ID;
    private String passWord;
    private String mail;
    private String name;
    private short protocol;

    public SignUpData(String ID, String passWord, String mail, String name,short protocol) {
        this.ID = ID;
        this.passWord = passWord;
        this.mail = mail;
        this.name = name;
        this.protocol=protocol;
    }
    @Override
    public String toString(){
        return String.format("%d %d %d %d",ID,passWord,mail,name);
    }
    @Override
    public short getProtocol() {
        return protocol;
    }

    @Override
    public String getName() {
        return name;
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
