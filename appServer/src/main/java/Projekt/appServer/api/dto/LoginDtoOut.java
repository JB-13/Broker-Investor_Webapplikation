package Projekt.appServer.api.dto;

import Projekt.appServer.api.security.AccessToken;

public class LoginDtoOut
{
    private final int id;
    private final String username;
    private final AccessToken credential;

    public LoginDtoOut(int id, String username, AccessToken accessToken)
    {
        this.id = id;
        this.username = username;
        this.credential = accessToken;
    }



    public int getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public AccessToken getCredential()
    {
        return credential;
    }
}
