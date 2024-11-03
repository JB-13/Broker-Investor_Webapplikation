package Projekt.appServer.model.dbaccess;

import Projekt.appServer.model.*;
import Projekt.appServer.model.dbaccess.repos.AssetRepository;
import Projekt.appServer.model.dbaccess.repos.BrokerRepository;
import Projekt.appServer.model.dbaccess.repos.InvestorRepository;
import Projekt.appServer.model.dbaccess.repos.PortfolioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PortfolioService {
    private final InvestorRepository investorRepository;
    private final BrokerRepository brokerRepository;
    private final AssetRepository assetRepository;
    private PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, InvestorRepository investorRepository, BrokerRepository brokerRepository, AssetRepository assetRepository)
    {
        this.portfolioRepository = portfolioRepository;
        this.investorRepository = investorRepository;
        this.brokerRepository = brokerRepository;
        this.assetRepository = assetRepository;
    }

    public List<Portfolio> getAllPortfolio()
    {
        return portfolioRepository.getAll();
    }

    public List<Portfolio> getPortfolioByCreationDate(Instant creationDate)
    {
        return  portfolioRepository.getPortfolioByCreationDate(creationDate);
    }

    public List<Portfolio> getPortfolioByInvestorId(int investorId) {
        return  portfolioRepository.getPortfolioByInvestorId(investorId);
    }

    public List<Portfolio> getPortfolioByBrokerId(int brokerid)
    {
        return  portfolioRepository.getPortfolioByBrokerId(brokerid);
    }

    public Portfolio getPortfolioById(int id)
    {
        return portfolioRepository.getPortfolioById(id);
    }

    public Portfolio createPortfolio(Investor investor, Broker broker, Set<Asset> assets) {

        return portfolioRepository.createPortfolio(investor, broker, assets);
    }


    public void deletePortfolio(int id)
    {
        portfolioRepository.deletePortfolio(id);
    }
}
