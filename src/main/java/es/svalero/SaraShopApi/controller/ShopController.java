package es.svalero.SaraShopApi.controller;

import es.svalero.SaraShopApi.domain.ErrorResponse;
import es.svalero.SaraShopApi.domain.Product;
import es.svalero.SaraShopApi.domain.Shop;
import es.svalero.SaraShopApi.exceptions.SectionNotFoundException;
import es.svalero.SaraShopApi.exceptions.ShopNotFoundException;
import es.svalero.SaraShopApi.service.ShopService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ShopController {
    @Autowired
    private ShopService shopService;
    private Logger logger = LoggerFactory.getLogger(ShopController.class);

    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> getAll() {
        List<Shop> brandsList = shopService.findAll();
        return new ResponseEntity<>(brandsList, HttpStatus.OK);
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<Shop> getShop(@PathVariable long shopId) throws ShopNotFoundException {
        logger.info("ini GET/shop/" + shopId);
        Optional<Shop> optionalShop = shopService.findById(shopId);
        Shop shop = optionalShop.orElseThrow(() -> new ShopNotFoundException(shopId));
        logger.info("end GET/shop/" + shopId);
        return new ResponseEntity<>(shop, HttpStatus.OK);
    }

    @PostMapping("/shop")
    public ResponseEntity<Shop> saveShop(@Valid @RequestBody Shop shop) {
        logger.info("ini Post /shop" + shop);
        Shop newShop = shopService.saveShop(shop);
        logger.info("end Post /shop CREATED: {}", newShop);
        return new ResponseEntity<>(newShop, HttpStatus.CREATED);
    }


    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<Void> deleteShop(@PathVariable long shopId) throws ShopNotFoundException {
        logger.info("ini DELETE /shop/" + shopId);
        shopService.removeShop(shopId);
        logger.info("end DELETE /shop/" + shopId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/shop/{shopId}")
    public ResponseEntity<Void> modifyShop(@Valid @RequestBody Shop shop, @PathVariable long shopId) throws ShopNotFoundException {
        logger.info("ini PUT /shop/" + shopId);
        shopService.modifyShop(shop, shopId);
        logger.info("end PUT /shop/" + shopId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ShopNotFoundException.class)
    public ResponseEntity<ErrorResponse> ShopNotFoundException(ShopNotFoundException pnfe) {
        logger.error("Shop not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("Shop validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

