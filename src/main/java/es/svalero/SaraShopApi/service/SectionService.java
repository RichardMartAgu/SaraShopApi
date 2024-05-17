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
        logger.info("Ini saveSection " + section);
        sectionRepository.save(section);
        logger.info("End saveSection " + section);
        return section;
    }

    public void removeSection(long sectionId) throws SectionNotFoundException {
        logger.info("Ini removeSection ID: " + sectionId);
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new SectionNotFoundException(sectionId));
        logger.info("End removeSection section: " + section);
        sectionRepository.delete(section);
    }

    public void modifySection(Section newsection, long sectionId) throws SectionNotFoundException {
        logger.info("Ini modifySection ID: " + sectionId);
        Optional<Section> section = sectionRepository.findById(sectionId);
        if (section.isPresent()) {
            Section existingsection = section.get();
            existingsection.setName(newsection.getName());
            existingsection.setDescription(newsection.getDescription());
            existingsection.setCreation_date(newsection.getCreation_date());
            existingsection.setAvailable(newsection.isAvailable());



            sectionRepository.save(existingsection);
        } else {
            throw new SectionNotFoundException(sectionId);
        }
        logger.info("End modifySection section: " + section);
    }
}

