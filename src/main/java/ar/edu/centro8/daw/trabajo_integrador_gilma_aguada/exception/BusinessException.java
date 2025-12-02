// src/main/java/ar/edu/centro8/daw/trabajo_integrador_gilma_aguada/exception/BusinessException.java
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}