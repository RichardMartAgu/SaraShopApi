package es.svalero.SaraShopApi.repository;

import es.svalero.SaraShopApi.domain.Product;
import es.svalero.SaraShopApi.domain.Section;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();

    List<Product> findProductBySectionId(Optional<Section> brand);
}
