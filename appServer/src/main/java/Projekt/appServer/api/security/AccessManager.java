package Projekt.appServer.api.security;

import Projekt.appServer.model.Broker;
import Projekt.appServer.model.Investor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccessManager<User> {
    private Map<AccessToken, User> accessList = new ConcurrentHashMap<>();

    public AccessToken createUserToken(User user) {
        String uuid = UUID.randomUUID().toString();
        AccessToken accessToken = new AccessToken(uuid);

        accessList.put(accessToken, user);

        return accessToken;
    }

    public boolean removeUserToken(AccessToken accessToken) {
        System.out.println("Trying to remove token: " + accessToken.getAccessToken()); // Debugging-Ausgabe
        return accessList.remove(accessToken) != null;
    }

    public boolean hasAccess(AccessToken accessToken) {
        return accessList.containsKey(accessToken);
    }

    public User getUser(AccessToken accessToken) {
        return accessList.get(accessToken);
    }

    public Investor getInvestor(AccessToken accessToken) {
        return (Investor) accessList.get(accessToken);
    }

    public Broker getBroker(AccessToken accessToken) {
        return (Broker) accessList.get(accessToken);
    }
}
