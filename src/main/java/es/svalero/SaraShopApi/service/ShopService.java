package es.svalero.SaraShopApi.service;

import es.svalero.SaraShopApi.controller.ShopController;
import es.svalero.SaraShopApi.domain.Shop;
import es.svalero.SaraShopApi.exceptions.ShopNotFoundException;
import es.svalero.SaraShopApi.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;
    private Logger logger = LoggerFactory.getLogger(ShopController.class);


    public List<Shop> findAll() {
        logger.info("Do Shop findAll");
        return shopRepository.findAll();
    }


    public Optional<Shop> findById(long id) {
        logger.info("Do Shop findById " + id);
        return shopRepository.findById(id);
    }


    public Shop saveShop(Shop shop) {
        logger.info("Ini saveShop " + shop);
        shop.setOpen_date(LocalDate.now());
        shopRepository.save(shop);
        logger.info("End saveShop " + shop);
        return shop;
    }

    public void removeShop(long shopId) throws ShopNotFoundException {
        logger.info("Ini removeShop ID: " + shopId);
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new ShopNotFoundException(shopId));
        logger.info("End removeShop Shop: " + shop);
        shopRepository.delete(shop);
    }

    public void modifyShop(Shop shop, long shopId) throws ShopNotFoundException {
        logger.info("Ini modifyShop ID: " + shopId);
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop existingShop = shopOptional.get();
            existingShop.setName(shop.getName());
            existingShop.setAddress(shop.getAddress());
            existingShop.setPhone(shop.getPhone());
            existingShop.setOpen_date(shop.getOpen_date());
            existingShop.setOpen(shop.isOpen());


            shopRepository.save(existingShop);
        } else {
            throw new ShopNotFoundException(shopId);
        }
        logger.info("End modifyShop Shop: " + shopOptional);
    }

}