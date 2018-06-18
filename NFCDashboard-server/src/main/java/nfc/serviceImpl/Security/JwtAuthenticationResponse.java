package nfc.serviceImpl.Security;

import java.io.Serializable;
import java.util.Date;
import nfc.messages.BaseResponse;
import nfc.model.ViewModel.UserModelLogin;

public class JwtAuthenticationResponse extends BaseResponse implements Serializable{
    private UserModelLogin user;
    private final String token;
    private Date expired;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public JwtAuthenticationResponse(String token, UserModelLogin user) {
            this.token = token;
            this.user = user;
    }

    public String getToken() {
            return this.token;
    }

    public UserModelLogin getUser() {
        return user;
    }

    public void setUser(UserModelLogin user) {
        this.user = user;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
         
}
