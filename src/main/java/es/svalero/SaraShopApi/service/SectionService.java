package es.svalero.SaraShopApi.service;

import es.svalero.SaraShopApi.controller.SectionController;
import es.svalero.SaraShopApi.domain.Section;
import es.svalero.SaraShopApi.exceptions.SectionNotFoundException;
import es.svalero.SaraShopApi.repository.SectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {


    @Autowired
    private SectionRepository sectionRepository;

    private Logger logger = LoggerFactory.getLogger(SectionController.class);

    public List<Section> findAll() {
        logger.info("Do section findAll");
        return sectionRepository.findAll();
    }

    public Optional<Section> findById(long id) {
        logger.info("Do section findById " + id);
        return sectionRepository.findById(id);
    }


    public Section saveSection(Section section) {
        logger.info("Ini savesection " + section);
        sectionRepository.save(section);
        logger.info("End savesection " + section);
        return section;
    }

    public void removeSection(long sectionId) throws SectionNotFoundException {
        logger.info("Ini removesection ID: " + sectionId);
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new SectionNotFoundException(sectionId));
        logger.info("End removesection section: " + section);
        sectionRepository.delete(section);
    }

    public void modifySection(Section newsection, long sectionId) throws SectionNotFoundException {
        logger.info("Ini modifysection ID: " + sectionId);
        Optional<Section> section = sectionRepository.findById(sectionId);
        if (section.isPresent()) {
            Section existingsection = section.get();
            existingsection.setName(newsection.getName());
            existingsection.setDescription(newsection.getDescription());
            existingsection.setCreation_date(newsection.getCreation_date());
            existingsection.setStock(newsection.isStock());



            sectionRepository.save(existingsection);
        } else {
            throw new SectionNotFoundException(sectionId);
        }
        logger.info("End modifysection section: " + section);
    }
}

