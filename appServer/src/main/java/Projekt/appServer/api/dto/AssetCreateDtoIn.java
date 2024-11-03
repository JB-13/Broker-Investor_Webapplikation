package Projekt.appServer.api.dto;

import Projekt.appServer.model.Broker;


public class AssetCreateDtoIn {
    Broker broker;
    String kind;
    String name;

    public Broker getBroker()
    {
        return broker;
    }

    public void setBroker(Broker broker)
    {
        this.broker = broker;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind(String kind)
    {
        this.kind = kind;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
