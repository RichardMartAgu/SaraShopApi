package es.svalero.SaraShopApi.exceptions;

public class SectionNotFoundException extends Exception {
    public SectionNotFoundException() {
        super();
    }

    public SectionNotFoundException(String message) {
        super(message);
    }

    public SectionNotFoundException(long id) {
        super("La sección con id " + id + " no existe");
    }
}
