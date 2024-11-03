package Projekt.appServer.api.dto;

import Projekt.appServer.model.Asset;
import Projekt.appServer.model.Broker;
import Projekt.appServer.model.Investor;
import Projekt.appServer.model.Portfolio;

import java.time.Instant;
import java.util.Set;

public class PortfolioDtoOut {

    int id;
    Investor investor;
    Broker broker;
    Instant creationDate;
    Set<Asset> assets;

    public PortfolioDtoOut(Portfolio portfolio)
    {
        this.id = portfolio.getId();
        this.investor = portfolio.getInvestor();
        this.broker = portfolio.getBroker();
        this.creationDate = portfolio.getCreationDate();
        this.assets = portfolio.getAssets();
    }


    public int getId()
    {
        return id;
    }

    public Investor getInvestor() { return investor; }

    public Broker getBroker()
    {
        return broker;
    }

    public Instant getCreationDate()
    {
        return creationDate;
    }

    public Set<Asset> getAssets() { return assets; }

}
