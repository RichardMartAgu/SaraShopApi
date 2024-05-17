package es.svalero.SaraShopApi.controller;

import es.svalero.SaraShopApi.domain.ErrorResponse;
import es.svalero.SaraShopApi.domain.Section;
import es.svalero.SaraShopApi.exceptions.SectionNotFoundException;
import es.svalero.SaraShopApi.service.SectionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class SectionController {


    @Autowired
    private SectionService sectionService;
    private Logger logger = LoggerFactory.getLogger(SectionController.class);

    @GetMapping("/sections")
    public ResponseEntity<List<Section>> getAll() {
        List<Section> sectionList = sectionService.findAll();
        return new ResponseEntity<>(sectionList, HttpStatus.OK);
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<Section> getSection(@PathVariable long sectionId) throws SectionNotFoundException {
        logger.info("ini GET/section/" + sectionId);
        Optional<Section> optionalSection = sectionService.findById(sectionId);
        Section section = optionalSection.orElseThrow(() -> new SectionNotFoundException(sectionId));
        logger.info("end GET/section/" + sectionId);
        return new ResponseEntity<>(section, HttpStatus.OK);
    }

    @PostMapping("/section")
    public ResponseEntity<Section> saveSection(@Valid @RequestBody Section section) {
        logger.info("ini Post /section" + section);
        Section newsection = sectionService.saveSection(section);
        logger.info("end Post /section CREATED: {}", newsection);
        return new ResponseEntity<>(newsection, HttpStatus.CREATED);
    }


    @DeleteMapping("/section/{sectionId}")
    public ResponseEntity<Void> deleteSection(@PathVariable long sectionId) throws SectionNotFoundException {
        logger.info("ini DELETE /section/" + sectionId);
        sectionService.removeSection(sectionId);
        logger.info("end DELETE /section/" + sectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/section/{sectionId}")
    public ResponseEntity<Void> modifySection(@Valid @RequestBody Section section, @PathVariable long sectionId) throws SectionNotFoundException {
        logger.info("ini PUT /section/" + sectionId);
        sectionService.modifySection(section, sectionId);
        logger.info("end PUT /section/" + sectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(SectionNotFoundException.class)
    public ResponseEntity<ErrorResponse> sectionNotFoundException(SectionNotFoundException pnfe) {
        logger.error("section not found. Details: {}", pnfe.getMessage());
        ErrorResponse errorResponse = ErrorResponse.generalError(404, pnfe.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.error("section validation Exception. Details: {}", manve.getMessage());
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }
}

