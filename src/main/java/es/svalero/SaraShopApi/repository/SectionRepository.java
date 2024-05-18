package es.svalero.SaraShopApi.repository;

import es.svalero.SaraShopApi.domain.Section;
import es.svalero.SaraShopApi.domain.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {
    List<Section> findAll();

    List<Section> findSectionByShopId(Long shopId);
}
