package Projekt.appServer.api.dto;

import Projekt.appServer.model.Asset;
import Projekt.appServer.model.Broker;


public class AssetDtoOut {
    int id;
    Broker broker;
    String kind;
    String name;

    public AssetDtoOut(Asset asset)
    {
        this.id = asset.getId();
        this.broker = asset.getBroker();
        this.kind = asset.getKind();
        this.name = asset.getName();
    }

    public int getId()
    {
        return id;
    }

    public Broker getBroker()
    {
        return broker;
    }

    public String getKind()
    {
        return kind;
    }

    public String getName() { return name; }

}
