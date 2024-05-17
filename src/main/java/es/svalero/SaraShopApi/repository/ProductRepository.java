package es.svalero.SaraShopApi.repository;

import es.svalero.SaraShopApi.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();

    List<Product> findProductBySectionId(Long sectionId);
}
