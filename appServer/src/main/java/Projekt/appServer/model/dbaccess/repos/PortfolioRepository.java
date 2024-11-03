package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer>, PortfolioRepositoryJPA {
    @Query("select p from Portfolio p")
    public List<Portfolio> getAll();

    @Query("select p from Portfolio p where p.creationDate = ?1")
    public List<Portfolio> getPortfolioByCreationDate(Instant creationDate);

    @Query("select p from Portfolio p where p.investor.id = ?1")
    public List<Portfolio> getPortfolioByInvestorId(int investorid);

    @Query("select p from Portfolio p where p.broker.id = ?1")
    public List<Portfolio> getPortfolioByBrokerId(int brokerid);

    //Set<Asset>

    @Query("select p from Portfolio p where p.id = ?1")
    public Portfolio getPortfolioById(int id);

    @Query("select count(pa) > 0 from PortfolioAsset pa where pa.asset.id = ?1")
    boolean existsByAssetId(int assetId);
}
