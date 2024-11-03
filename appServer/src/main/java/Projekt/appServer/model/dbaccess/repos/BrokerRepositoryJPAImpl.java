package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Broker;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Projekt.appServer.util.PasswordTools;

@Service
public class BrokerRepositoryJPAImpl implements BrokerRepositoryJPA {
    EntityManager entityManager;

    @Autowired
    public BrokerRepositoryJPAImpl(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public Broker createBroker(String username, String company, String password)
    {
        byte[] passwordSalt = PasswordTools.generateSalt();
        byte[] passwordHash = PasswordTools.generatePasswordHash(password, passwordSalt);

        Broker broker = new Broker(username, company, passwordSalt, passwordHash);

        entityManager.persist(broker);
        entityManager.flush();
        entityManager.refresh(broker);

        return broker;
    }




    @Override
    public void deleteBroker(int id)
    {
        Broker broker = entityManager.find(Broker.class, id);
        entityManager.remove(broker);
        return;
    }

}
