package Projekt.appServer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "portfolio_assets")
public class PortfolioAsset {
    @EmbeddedId
    private PortfolioAssetId id;

    @MapsId("portfolioId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @MapsId("assetId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    public PortfolioAssetId getId() {
        return id;
    }

    public void setId(PortfolioAssetId id) {
        this.id = id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

}