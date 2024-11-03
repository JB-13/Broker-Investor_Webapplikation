package Projekt.appServer.model.dbaccess.repos;

import Projekt.appServer.model.Broker;

public interface BrokerRepositoryJPA {
    public Broker createBroker(String username, String company, String password);
    public void deleteBroker(int id);
}
