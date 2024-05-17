package es.svalero.SaraShopApi.exceptions;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(long id) {
        super("El producto con id " + id + " no existe");
    }
}