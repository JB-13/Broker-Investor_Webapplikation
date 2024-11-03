package Projekt.appServer.api;

import Projekt.appServer.api.dto.LoginDtoIn;
import Projekt.appServer.api.dto.LoginDtoOut;
import Projekt.appServer.api.security.AccessManager;
import Projekt.appServer.api.security.AccessToken;
import Projekt.appServer.model.Broker;
import Projekt.appServer.model.Investor;
import Projekt.appServer.model.dbaccess.BrokerService;
import Projekt.appServer.model.dbaccess.InvestorService;
import Projekt.appServer.util.PasswordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("access")
public class AccessController {
    private final AccessManager accessManager;
    private final BrokerService brokerService;
    private final InvestorService investorService;

    @Autowired
    public AccessController(AccessManager accessManager, BrokerService brokerService, InvestorService investorService) {
        this.accessManager = accessManager;
        this.brokerService = brokerService;
        this.investorService = investorService;
    }

    @PostMapping("/broker")
    public ResponseEntity<LoginDtoOut> loginBroker(@RequestBody LoginDtoIn loginDtoIn) {
        String username = loginDtoIn.getUsername();
        String password = loginDtoIn.getPassword();

        Broker broker = brokerService.getBrokerByUsername(username);

        if (broker == null || !PasswordTools.checkPassword(password, broker.getPasswordHash(), broker.getPasswordSalt())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AccessToken accessToken = accessManager.createUserToken(broker);
        LoginDtoOut returnData = new LoginDtoOut(broker.getId(), broker.getUsername(), accessToken);

        return ResponseEntity.ok(returnData);
    }

    @PostMapping("/investor")
    public ResponseEntity<LoginDtoOut> loginInvestor(@RequestBody LoginDtoIn loginDtoIn) {
        String username = loginDtoIn.getUsername();
        String password = loginDtoIn.getPassword();

        Investor investor = investorService.getInvestorByUsername(username);

        if (investor == null || !PasswordTools.checkPassword(password, investor.getPasswordHash(), investor.getPasswordSalt())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AccessToken accessToken = accessManager.createUserToken(investor);
        LoginDtoOut returnData = new LoginDtoOut(investor.getId(), investor.getUsername(), accessToken);

        return ResponseEntity.ok(returnData);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> logout(@RequestHeader AccessToken accessToken)
    {
        return ResponseEntity.ok(accessManager.removeUserToken( accessToken ));
    }
}

