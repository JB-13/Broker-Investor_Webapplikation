package Projekt.appServer.model.dbaccess;

import Projekt.appServer.model.dbaccess.repos.PortfolioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Projekt.appServer.model.Broker;
import Projekt.appServer.model.dbaccess.repos.BrokerRepository;

import java.util.List;

@Service
@Transactional
public class BrokerService {
    private final PortfolioRepository portfolioRepository;
    private BrokerRepository brokerRepository;

    @Autowired
    public BrokerService(BrokerRepository brokerRepository, PortfolioRepository portfolioRepository)
    {
        this.brokerRepository = brokerRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public List<Broker> getAllBroker()
    {
        return brokerRepository.getAll();
    }

    public Broker getBrokerByUsername(String username)
    {
        return  brokerRepository.getBrokerByUsername(username);
    }

    public Broker getBrokerById(int id)
    {
        return brokerRepository.getBrokerById(id);
    }



    public Broker createBroker(String username, String company, String password)
    {

        return brokerRepository.createBroker(username, company, password);
    }

    public boolean isAssetInPortfolio(int assetId) {
        return portfolioRepository.existsByAssetId(assetId);
    }

    public void deleteBroker(int id)
    {
        brokerRepository.deleteBroker(id);
    }

}
