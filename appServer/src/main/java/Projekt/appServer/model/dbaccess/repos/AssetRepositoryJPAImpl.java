package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Broker;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Projekt.appServer.model.Asset;

@Service
public class AssetRepositoryJPAImpl implements AssetRepositoryJPA{
    EntityManager entityManager;

    @Autowired
    public AssetRepositoryJPAImpl(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public Asset createAsset(Broker broker, String name, String kind)
    {
        Asset asset = new Asset(broker, name, kind);

        entityManager.persist(asset);
        entityManager.flush();
        entityManager.refresh(asset);

        return asset;
    }





    @Override
    public void deleteAsset(int id)
    {
        Asset asset = entityManager.find(Asset.class, id);
        entityManager.remove(asset);
        return;
    }
}
