package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Asset;
import Projekt.appServer.model.Investor;
import Projekt.appServer.model.Portfolio;
import Projekt.appServer.model.Broker;

import java.util.List;
import java.util.Set;

public interface PortfolioRepositoryJPA {
    public Portfolio createPortfolio(Investor investor, Broker broker, Set<Asset> assets);
    public void deletePortfolio(int id);
}
