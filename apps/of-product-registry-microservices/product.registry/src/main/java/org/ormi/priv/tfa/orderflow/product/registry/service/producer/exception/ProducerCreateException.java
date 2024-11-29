package org.ormi.priv.tfa.orderflow.product.registry.service.producer.exception;

public class ProducerCreateException extends RuntimeException{
    public ProducerCreateException(String message, Throwable cause){
        super(message, cause);
    }
}
