package Projekt.appServer.api;

import Projekt.appServer.model.*;
import Projekt.appServer.model.dbaccess.AssetService;
import Projekt.appServer.model.dbaccess.BrokerService;
import Projekt.appServer.model.dbaccess.InvestorService;
import Projekt.appServer.model.dbaccess.PortfolioService;
import Projekt.appServer.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import Projekt.appServer.api.dto.*;
import Projekt.appServer.api.security.AccessManager;
import Projekt.appServer.api.security.AccessToken;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/investor")
public class InvestorController {
    private final InvestorService investorService;
    private final PortfolioService portfolioService;
    private final BrokerService brokerService;
    private final AssetService assetService;
    private final AccessManager<Investor> accessManager;

    @Autowired
    public InvestorController(InvestorService investorService, PortfolioService portfolioService, BrokerService brokerService, AssetService assetService, AccessManager<Investor> accessManager) {
        this.investorService = investorService;
        this.portfolioService = portfolioService;
        this.brokerService = brokerService;
        this.assetService = assetService;
        this.accessManager = accessManager;
    }

    @PostMapping
    public ResponseEntity<?> registerInvestor(@RequestBody InvestorCreateDtoIn investorCreateDtoIn) {
        {
            try {
                Investor investor = investorService.createInvestor(investorCreateDtoIn.getFirstname(),
                        investorCreateDtoIn.getLastname(),
                        investorCreateDtoIn.getUsername(),
                        investorCreateDtoIn.getPassword());

                AccessToken accessToken = accessManager.createUserToken(investor);

                LoginDtoOut returnData = new LoginDtoOut( investor.getId(), investor.getUsername(), accessToken);
                return ResponseEntity.ok(returnData);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
            }
        }
    }


    @GetMapping
    public List<Investor> getAllInvestors() {
        return investorService.getAllInvestor();
    }

    @GetMapping("/{id}")
    public Investor getInvestorById(@PathVariable int id) {
        return investorService.getInvestorById(id);
    }







    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteInvestor(@RequestHeader AccessToken accessToken, @PathVariable("id") int investorId)
    {
        checkAnmeldung( accessToken );
        checkBerechtigung( accessToken, investorId );

        try {
            List<Portfolio> portfolios = portfolioService.getPortfolioByInvestorId(investorId);
            if (!portfolios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"Please delete all portfolios before deleting the account.\"}");
            }
            accessManager.removeUserToken( accessToken );
            investorService.deleteInvestor(investorId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }



    @GetMapping("/brokers")
    public ResponseEntity<List<BrokerDtoOut>> getAllBrokers(@RequestHeader AccessToken accessToken) {
        checkAnmeldung(accessToken);

        try {
            List<Broker> brokers = brokerService.getAllBroker();
            List<BrokerDtoOut> brokerDtos = brokers.stream().map(BrokerDtoOut::new).collect(Collectors.toList());
            return ResponseEntity.ok(brokerDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


    @GetMapping("/{investorId}/portfolios")
    public ResponseEntity<?> getPortfoliosByInvestorId(@RequestHeader AccessToken accessToken, @PathVariable int investorId) {
        checkAnmeldung(accessToken);
        checkBerechtigung( accessToken, investorId );

        try {
            Investor investor = investorService.getInvestorById(investorId);
            if (investor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Investor not found");
            }

            List<Portfolio> portfolios = portfolioService.getPortfolioByInvestorId(investorId);
            List<PortfolioDtoOut> portfolioDtoOutList = portfolios.stream()
                    .map(PortfolioDtoOut::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(portfolioDtoOutList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching portfolios: " + e.getMessage());
        }
    }



    @PostMapping("/{investorId}/portfolios")
    public ResponseEntity<?> createPortfolio(@RequestHeader AccessToken accessToken, @PathVariable int investorId, @RequestBody PortfolioCreateDtoIn portfolioCreateDtoIn) {
        checkAnmeldung(accessToken);
        checkBerechtigung( accessToken, investorId );

        try {
            Investor investor = investorService.getInvestorById(investorId);
            Portfolio portfolio = portfolioService.createPortfolio(investor, portfolioCreateDtoIn.getBroker(), portfolioCreateDtoIn.getAssets());
            PortfolioDtoOut portfolioDtoOut = new PortfolioDtoOut(portfolio);
            return ResponseEntity.status(HttpStatus.CREATED).body(portfolioDtoOut);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create portfolio");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    @DeleteMapping("/{investorId}/portfolios/{portfolioId}")
    public ResponseEntity<String> deletePortfolio(@RequestHeader AccessToken accessToken, @PathVariable int investorId, @PathVariable int portfolioId) {
        checkAnmeldung(accessToken);
        checkBerechtigung( accessToken, investorId );

        try {
            Investor investor = investorService.getInvestorById(investorId);
            if (investor == null) {
                return ResponseEntity.notFound().build();
            }

            Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);
            if (portfolio == null || portfolio.getInvestor().getId() != investorId) {
                return ResponseEntity.notFound().build();
            }

            portfolioService.deletePortfolio(portfolioId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Löschen des Assets: " + e.getMessage());
        }
    }



    private void checkAnmeldung(AccessToken accessToken) {
        if (!accessManager.hasAccess(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Investor nicht angemeldet");
        }
    }

    private void checkBerechtigung(AccessToken accessToken, int investorId) {
        if (accessManager.getInvestor(accessToken).getId() != investorId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Investor hat keine Berechtigung");
        }
    }
}
