package org.ormi.priv.tfa.orderflow;

import io.smallrye.mutiny.Multi;
import io.quarkus.logging.Log;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.ormi.priv.tfa.orderflow.lib.event.sourcing.aggregate.Event;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.mapper.ProductRegistryEventDtoMapper;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.resource.exception.ProductRegistryEventStreamException;
import org.apache.pulsar.client.api.PulsarClientException;

public class ConsumeEmitEvent{

    @ConfigProperty(timeout = "api-gateway.timeout") 
    static int timeout;

    public static void consumeAndEmit(
        Consumer<T> consumer,
        Multi<T> em,
        Event event
    ){
        while(!em.isCancelled()){
            try{
                final var msg = Optional.ofNullable(consumer.receive(timeout, TimeUnit.MILLISECONDS));
                if(msg.isEmpty()){
                    // Complete the emitter if no event is received within the timeout. Free up resources.
                    Log.debug("No event received within timeout of " + timeout + " seconds.");
                    em.complete();
                }
                final ProductRegistryEvent evt = msg.get().getValue();
                Log.debug("Received event: " + evt);
                // Map event to DTO
                if(evt instanceof Event eventType){
                    Log.debug("Emitting DTO for event: " + eventType);
                    // Emit DTO for event
                    em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(eventType));
                }else{
                    // Fail the stream on unexpected event types
                    Throwable error = new ProductRegistryEventStreamException("Unexpected event type: " + evt.getClass().getName());
                    em.fail(error);
                    return;
                }
                // Acknowledge the message
                consumer.acknowledge(msg.get());
            } catch (PulsarClientException e) {
                Log.error("Failed to receive event from consumer.", e);
                em.fail(e);
                return;
            }
        }
    }
}