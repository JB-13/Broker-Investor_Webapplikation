package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class PortfolioRepositoryJPAImpl implements PortfolioRepositoryJPA {
    EntityManager entityManager;

    @Autowired
    public PortfolioRepositoryJPAImpl(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public Portfolio createPortfolio(Investor investor, Broker broker, Set<Asset> assets) {
        Portfolio portfolio = new Portfolio(investor, broker, assets);
        portfolio.setCreationDate(Instant.now());

        entityManager.persist(portfolio);
        entityManager.flush();

        return portfolio;
    }





    @Override
    public void deletePortfolio(int id)
    {
        Portfolio portfolio = entityManager.find(Portfolio.class, id);
        if (portfolio != null) {
            entityManager.remove(portfolio);
        }
    }


}
