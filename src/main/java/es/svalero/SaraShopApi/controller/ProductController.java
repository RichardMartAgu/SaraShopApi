package es.svalero.SaraShopApi.controller;

import es.svalero.SaraShopApi.domain.ErrorResponse;
import es.svalero.SaraShopApi.domain.Product;
import es.svalero.SaraShopApi.exceptions.ProductNotFoundException;
import es.svalero.SaraShopApi.exceptions.SectionNotFoundException;
import es.svalero.SaraShopApi.service.ProductService;
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
public class ProductController {
    @Autowired
    private ProductService productService;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> productList = productService.findAll();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable long productId) throws ProductNotFoundException {
        logger.info("ini GET/product/" + productId);
        Optional<Product> optionalProduct = productService.findById(productId);
        Product product = optionalProduct.orElseThrow(() -> new ProductNotFoundException(productId));
        logger.info("end GET/product/" + productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/section/{sectionId}/products")
    public ResponseEntity<List<Product>> getProductBySectionId(@PathVariable long sectionId) {
        logger.info("ini GET/section/ " + sectionId + "/products");
        try {
            List<Product> product = productService.findProductBySectionId(sectionId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (SectionNotFoundException e) {
            logger.warn("SectionNotFoundException ID: " + sectionId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Brand not found with ID: " + sectionId, e);
        } finally {
            logger.info("end GET/section/ " + sectionId + "/products");
        }
    }

    @PostMapping("/product")
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) {
        logger.info("ini Post /product" + product);
        Product newProduct = productService.saveProduct(product);
        logger.info("end Post /product CREATED: {}", newProduct);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }


    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long productId) throws ProductNotFoundException {
        logger.info("ini DELETE /product/" + productId);
        productService.removeProduct(productId);
        logger.info("end DELETE /product/" + productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<Void> modifyProduct(@Valid @RequestBody Product product, @PathVariable long productId) throws ProductNotFoundException {
        logger.info("ini PUT /product/" + productId);
        productService.modifyProduct(product, productId);
        logger.info("end PUT /product/" + productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> productNotFoundException(ProductNotFoundException pnfe) {
        logger.error("product not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("product validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

