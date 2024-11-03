package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Integer>, BrokerRepositoryJPA {
    @Query("select b from Broker b")
    public List<Broker> getAll();

    @Query("select b from Broker b where b.username = ?1")
    public Broker getBrokerByUsername(String username);

    @Query("select b from Broker b where b.id = ?1")
    public Broker getBrokerById(int id);
}
