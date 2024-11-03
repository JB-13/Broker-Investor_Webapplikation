package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Investor;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Projekt.appServer.util.PasswordTools;

@Service
public class InvestorRepositoryJPAImpl implements InvestorRepositoryJPA {
    EntityManager entityManager;

    @Autowired
    public InvestorRepositoryJPAImpl(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public Investor createInvestor(String firstname, String lastname, String username, String password)
    {
        byte[] passwordSalt = PasswordTools.generateSalt();
        byte[] passwordHash = PasswordTools.generatePasswordHash(password, passwordSalt);

        Investor investor = new Investor(firstname, lastname, username, passwordSalt, passwordHash);

        entityManager.persist(investor);
        entityManager.flush();
        entityManager.refresh(investor);

        return investor;
    }




    @Override
    public void deleteInvestor(int id)
    {
        Investor investor = entityManager.find(Investor.class, id);
        entityManager.remove(investor);
        return;
    }
}
