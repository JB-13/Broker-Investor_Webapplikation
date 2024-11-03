package Projekt.appServer.api.dto;

import Projekt.appServer.model.Investor;

public class InvestorDtoOut {
    int id;
    String firstname;
    String lastname;
    String username;

    public InvestorDtoOut(Investor person)
    {
        this.id = person.getId();
        this.firstname = person.getFirstname();
        this.lastname = person.getLastname();
        this.username = person.getUsername();
    }

    public int getId()
    {
        return id;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getUsername()
    {
        return username;
    }
}
