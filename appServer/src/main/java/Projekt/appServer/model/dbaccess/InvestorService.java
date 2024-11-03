package Projekt.appServer.model.dbaccess;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Projekt.appServer.model.Investor;
import Projekt.appServer.model.dbaccess.repos.InvestorRepository;

import java.util.List;

@Service
@Transactional
public class InvestorService {
    private InvestorRepository investorRepository;

    @Autowired
    public InvestorService(InvestorRepository investorRepository)
    {
        this.investorRepository = investorRepository;
    }

    public List<Investor> getAllInvestor()
    {
        return investorRepository.getAll();
    }

    public Investor getInvestorByUsername(String username)
    {
        return  investorRepository.getInvestorByUsername(username);
    }

    public Investor getInvestorById(int id)
    {

        return investorRepository.getInvestorById(id);
    }

    public Investor createInvestor(String firstname, String lastname, String username, String password)
    {
        return investorRepository.createInvestor(firstname, lastname, username, password);
    }

    public void deleteInvestor(int id)
    {
        investorRepository.deleteInvestor(id);
    }

}
