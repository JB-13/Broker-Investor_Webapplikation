package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Investor;

public interface InvestorRepositoryJPA {
    public Investor createInvestor(String firstname, String lastname, String username, String password);
    public void deleteInvestor(int id);
}
