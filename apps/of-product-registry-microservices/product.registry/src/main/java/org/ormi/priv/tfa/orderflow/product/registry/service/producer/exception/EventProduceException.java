package org.ormi.priv.tfa.orderflow.product.registry.service.producer.exception;

public class EventProduceException extends RuntimeException{
    public EventProduceException(String message, Throwable cause){
        super(message, cause);
    }
}
