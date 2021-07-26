package my.alumni.klu.Notifications;

public class token {
    private String stoken;
    public token(String stoken){
        this.stoken=stoken;
    }
    public token(){

    }

    public String getToken() {
        return stoken;
    }

    public void setToken(String stoken) {
        this.stoken = stoken;
    }
}
