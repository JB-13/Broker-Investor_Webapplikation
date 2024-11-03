package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer>, AssetRepositoryJPA {

    @Query("select a from Asset a")
    public List<Asset> getAll();

    @Query("select a from Asset a where a.name = ?1")
    public List<Asset> getAssetByName(String name);

    @Query("select a from Asset a where a.kind = ?1")
    public List<Asset> getAssetByKind(String kind);

    @Query("select a from Asset a where a.broker.id = ?1")
    public List<Asset> getAssetByBrokerId(int brokerid);

    @Query("select a from Asset a where a.id = ?1")
    public Asset getAssetById(int id);

}
