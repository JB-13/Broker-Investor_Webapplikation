package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Integer>, InvestorRepositoryJPA {
    @Query("select i from Investor i")
    public List<Investor> getAll();

    @Query("select i from Investor i where i.username = ?1")
    public Investor getInvestorByUsername(String username);

    @Query("select i from Investor i where i.id = ?1")
    public Investor getInvestorById(int id);
}
