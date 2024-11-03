package Projekt.appServer.api.dto;

import Projekt.appServer.model.Broker;

public class BrokerDtoOut {
    int id;
    String username;
    String company;

    public BrokerDtoOut(Broker person)
    {
        this.id = person.getId();
        this.username = person.getUsername();
        this.company = person.getCompany();
    }

    public int getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getCompany()
    {
        return company;
    }
}
