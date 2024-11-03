package Projekt.appServer.api;

import Projekt.appServer.model.Asset;
import Projekt.appServer.model.dbaccess.AssetService;
import Projekt.appServer.model.dbaccess.BrokerService;
import Projekt.appServer.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Projekt.appServer.api.dto.*;
import Projekt.appServer.api.security.AccessManager;
import Projekt.appServer.api.security.AccessToken;
import Projekt.appServer.model.Broker;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/broker")
public class BrokerController {
    private final BrokerService brokerService;
    private final AssetService assetService;
    private final AccessManager<Broker> accessManager;

    @Autowired
    public BrokerController(BrokerService brokerService, AssetService assetService, AccessManager<Broker> accessManager) {
        this.brokerService = brokerService;
        this.assetService = assetService;
        this.accessManager = accessManager;
    }

    @PostMapping
    public ResponseEntity<?> registerBroker(@RequestBody BrokerCreateDtoIn brokerCreateDtoIn) {
        {
            try {
                Broker broker = brokerService.createBroker(brokerCreateDtoIn.getUsername(),
                        brokerCreateDtoIn.getCompany(),
                        brokerCreateDtoIn.getPassword());

                AccessToken accessToken = accessManager.createUserToken(broker);

                LoginDtoOut returnData = new LoginDtoOut( broker.getId(), broker.getUsername(), accessToken);
                return ResponseEntity.ok(returnData);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
            }
        }
    }




    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBroker(@RequestHeader AccessToken accessToken, @PathVariable("id") int brokerId)
    {
        checkAnmeldung( accessToken );
        checkBerechtigung( accessToken, brokerId );

        try {
            List<Asset> assets = assetService.getAssetByBrokerId(brokerId);
            if (!assets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"Please delete all assets before deleting the account.\"}");
            }
            accessManager.removeUserToken( accessToken );
            brokerService.deleteBroker(brokerId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        }
    }


    @GetMapping
    public List<Broker> getAllBrokers() {
        return brokerService.getAllBroker();
    }

    @GetMapping("/{id}")
    public Broker getBrokerById(@PathVariable int id) {
        return brokerService.getBrokerById(id);
    }






    @PostMapping("/{id}/assets")
    public ResponseEntity<?> createAsset(@RequestHeader AccessToken accessToken, @PathVariable int id, @RequestBody AssetCreateDtoIn assetCreateDtoIn) {
        checkAnmeldung(accessToken);
        checkBerechtigung(accessToken, id);

        try {
            Broker broker = brokerService.getBrokerById(id);
            if (broker == null) {
                return ResponseEntity.notFound().build();
            }

            Asset createdAsset = assetService.createAsset(broker, assetCreateDtoIn.getName(), assetCreateDtoIn.getKind());
            AssetDtoOut assetDtoOut = new AssetDtoOut(createdAsset);
            return ResponseEntity.status(HttpStatus.CREATED).body(assetDtoOut);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/{id}/assets")
    public ResponseEntity<?> getAssetsByBrokerId(@PathVariable int id) {

        try {
            Broker broker = brokerService.getBrokerById(id);
            if (broker == null) {
                return ResponseEntity.notFound().build();
            }

            List<Asset> assets = assetService.getAssetByBrokerId(id);
            List<AssetDtoOut> assetDtoOutList = assets.stream().map(AssetDtoOut::new).toList();
            return ResponseEntity.ok(assetDtoOutList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }
    }



    @GetMapping("/assets/{assetId}/in-portfolio")
    public ResponseEntity<Boolean> isAssetInPortfolio(@RequestHeader AccessToken accessToken, @PathVariable int assetId) {
        checkAnmeldung(accessToken);

        try {
            boolean isInPortfolio = brokerService.isAssetInPortfolio(assetId);
            return ResponseEntity.ok(isInPortfolio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }


    @DeleteMapping("/{brokerId}/assets/{assetId}")
    public ResponseEntity<?> deleteAsset(@RequestHeader AccessToken accessToken, @PathVariable int brokerId, @PathVariable int assetId) {
        checkAnmeldung(accessToken);
        checkBerechtigung(accessToken, brokerId);

        try {
            boolean isInPortfolio = brokerService.isAssetInPortfolio(assetId);
            if (isInPortfolio) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Asset cannot be deleted because it is part of a portfolio."));
            }

            assetService.deleteAsset(assetId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Asset successfully deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Internal server error: " + e.getMessage()));
        }
    }




    private void checkAnmeldung(AccessToken accessToken)
    {
        if( !accessManager.hasAccess(accessToken) )
        {
            throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Broker nicht angemeldet");
        }
    }



    private void checkBerechtigung(AccessToken accessToken, int brokerId )
    {
        if( accessManager.getBroker(accessToken).getId() != brokerId )
        {
            throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Broker hat keine Berechtigung");
        }
    }




}

