package Projekt.appServer.api.dto;

import Projekt.appServer.model.Asset;
import Projekt.appServer.model.Broker;
import Projekt.appServer.model.Investor;

import java.time.Instant;
import java.util.Set;

public class PortfolioCreateDtoIn {
    Investor investor;
    Broker broker;
    Set<Asset> assets;
    Instant creationDate;

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public Set<Asset> getAssets() { return assets; }

    public void setAssets(Set<Asset> assets) { this.assets = assets; }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

}
