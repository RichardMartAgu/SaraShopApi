package es.svalero.SaraShopApi.service;


import es.svalero.SaraShopApi.controller.ProductController;
import es.svalero.SaraShopApi.domain.Product;
import es.svalero.SaraShopApi.domain.Section;
import es.svalero.SaraShopApi.exceptions.ProductNotFoundException;
import es.svalero.SaraShopApi.exceptions.SectionNotFoundException;
import es.svalero.SaraShopApi.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    public List<Product> findAll() {
        logger.info("Do Product findAll");
        return productRepository.findAll();
    }

    public Optional<Product> findById(long id) {
        logger.info("Do Product findById " + id);
        return productRepository.findById(id);
    }

    public List<Product> findProductBySectionId(long sectionId) throws SectionNotFoundException {
        logger.info("Ini findProductBySection " + sectionId);
        Optional<Section> sectionOptional = sectionService.findById(sectionId);
        if (sectionOptional.isPresent()) {
            logger.info("End findProductBySection " + sectionId);
            return productRepository.findProductBySectionId(sectionId);
        } else {
            throw new SectionNotFoundException();
        }
    }

    public Product saveProduct(Product product) {
        logger.info("Ini saveProduct " + product);
        productRepository.save(product);
        logger.info("End saveProduct " + product);
        return product;
    }

    public void removeProduct(long productId) throws ProductNotFoundException {
        logger.info("Ini removeProduct ID: " + productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        logger.info("End removeProduct Product: " + product);
        productRepository.delete(product);
    }

    public void modifyProduct(Product newProduct, long productId) throws ProductNotFoundException {
        logger.info("Ini modifyProduct ID: " + productId);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setName(newProduct.getName());
            existingProduct.setDescription(newProduct.getDescription());
            existingProduct.setCreation_date(newProduct.getCreation_date());
            existingProduct.setStock(newProduct.isStock());

            productRepository.save(existingProduct);
        } else {
            throw new ProductNotFoundException(productId);
        }
        logger.info("End modifyProduct Product: " + product);
    }

}

