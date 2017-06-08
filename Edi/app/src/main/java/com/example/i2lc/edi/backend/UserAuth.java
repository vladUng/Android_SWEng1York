package com.example.i2lc.edi.backend;

/**
 * Created by vlad on 06/04/2017.
 */

public class UserAuth {
    String userToLogin;
    String password;

    public UserAuth(String userToLogin, String password) {
        super();
        this.userToLogin = userToLogin;
        this.password = password;
    }

    public String getUserToLogin() { return userToLogin; }

    public String getPassword() { return password; }
}
