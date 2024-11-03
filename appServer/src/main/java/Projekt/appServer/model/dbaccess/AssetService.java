package Projekt.appServer.model.dbaccess;

import Projekt.appServer.model.Broker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Projekt.appServer.model.Asset;
import Projekt.appServer.model.dbaccess.repos.AssetRepository;

import java.util.List;

@Service
@Transactional
public class AssetService {
    private AssetRepository assetRepository;


    @Autowired
    public AssetService(AssetRepository assetRepository)
    {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAsset()
    {
        return assetRepository.getAll();
    }

    public List<Asset> getAssetByName(String name)
    {
        return  assetRepository.getAssetByName(name);
    }

    public List<Asset> getAssetByKind(String kind)
    {
        return  assetRepository.getAssetByKind(kind);
    }

    public List<Asset> getAssetByBrokerId(int brokerid)
    {
        return  assetRepository.getAssetByBrokerId(brokerid);
    }

    public Asset getAssetById(int id)
    {
        return assetRepository.getAssetById(id);
    }

    public Asset createAsset(Broker broker, String name, String kind)
    {
        return assetRepository.createAsset(broker, name, kind);
    }

    public void deleteAsset(int id)
    {
        assetRepository.deleteAsset(id);
    }


}
