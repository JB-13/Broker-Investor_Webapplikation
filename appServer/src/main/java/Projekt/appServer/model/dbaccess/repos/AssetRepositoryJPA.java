package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Asset;
import Projekt.appServer.model.Broker;

public interface AssetRepositoryJPA {
    public Asset createAsset(Broker broker, String name, String kind);
    public void deleteAsset(int id);
}
