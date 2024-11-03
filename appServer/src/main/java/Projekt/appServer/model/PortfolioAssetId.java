package Projekt.appServer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class PortfolioAssetId implements java.io.Serializable {
    private static final long serialVersionUID = 112205032309134829L; //serialisierte Version der Klasse bleibt kompatibel
    @Column(name = "portfolio_id", nullable = false)
    private Integer portfolioId;

    @Column(name = "asset_id", nullable = false)
    private Integer assetId;

    public Integer getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Integer portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PortfolioAssetId entity = (PortfolioAssetId) o;
        return Objects.equals(this.portfolioId, entity.portfolioId) &&
                Objects.equals(this.assetId, entity.assetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioId, assetId);
    }

}