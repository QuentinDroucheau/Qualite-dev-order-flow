package org.ormi.priv.tfa.orderflow.product.registry.service.producer;

import java.util.concurrent.CompletionStage;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistered;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistryEvent;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRemoved;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductUpdated;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.config.ProductRegistryEventChannelName;
import org.ormi.priv.tfa.orderflow.product.registry.service.producer.exception.EventProduceException;
import org.ormi.priv.tfa.orderflow.product.registry.service.producer.exception.ProducerCloseException;
import org.ormi.priv.tfa.orderflow.product.registry.service.producer.exception.ProducerCreateException;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.pulsar.PulsarClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * The product registry event producer.
 * Produces events to the product registry event channel so it can be watched on
 * other services.
 */
@ApplicationScoped
public class ProductRegistryEventEmitter {

  /**
   * The Pulsar client service.
   */
  @Inject
  private PulsarClientService pulsarClients;

  /**
   * Event emitter to send events to the read model.
   */
  @Channel("product-registry-event")
  private Emitter<ProductRegistryEvent> eventEmitter;

  /**
   * Project the event.
   * 
   * @param event - the event to project
   * @return void
   */
  public void emit(ProductRegistryEvent event) throws IllegalStateException {
    Log.debug("Projecting event: " + event.toString());
    if (event instanceof ProductRegistered registered) {
      emitRegisteredProduct(registered);
    } else if (event instanceof ProductUpdated updated) {
      projectUpdatedProduct(updated);
    } else if (event instanceof ProductRemoved removed) {
      emitRemovedProduct(removed);
    }
  }

  /**
   * Emit a product registered event.
   * 
   * @param registered - the event to emit
   * @throws IllegalStateException
   * @return void
   */
  void emitRegisteredProduct(ProductRegistered registered) throws IllegalStateException {
    eventEmitter.send(registered);
  }

  /**
   * Emit a product updated event.
   * 
   * @param updated - the event to emit
   * @throws IllegalStateException
   * @return void
   */
  void projectUpdatedProduct(ProductUpdated updated) throws IllegalStateException {
    eventEmitter.send(updated);
  }

  /**
   * Emit a product removed event.
   * 
   * @param removed - the event to project
   * @throws IllegalStateException
   * @return void
   */
  void emitRemovedProduct(ProductRemoved removed) throws IllegalStateException {
    eventEmitter.send(removed);
  }

  /**
   * Produce the given event with the given correlation id.
   * 
   * @param correlationId - the correlation id
   * @param event         - the event
   * @return void
   */
  public void sink(String correlationId, ProductRegistryEvent event) {
    // Get the producer for the correlation id
    getEventSinkByCorrelationId(correlationId)
        .thenAccept((producer) -> {
          // Sink the event
            producer
                .newMessage()
                .value(event)
                .property("correlationId", correlationId)
                .sendAsync()
                .whenComplete((msgId, ex) -> {
                    if (ex != null) {
                        throw new EventProduceException("Failed to produce event for correlation id: " + correlationId, ex);
                    }
                    Log.debug(String.format("Sinked event with correlation id{%s} in msg{%s}", correlationId, msgId));
                    try {
                        producer.close();
                    } catch (PulsarClientException e) {
                        throw new ProducerCloseException("Failed to close producer", e);
                    }
                });
        });
  }

  /**
   * Create a producer for the given correlation id.
   * 
   * @param correlationId - the correlation id
   * @return the producer
   */
  private CompletionStage<Producer<ProductRegistryEvent>> getEventSinkByCorrelationId(String correlationId) {
    // Define the channel name, topic and schema definition
    final String channelName = ProductRegistryEventChannelName.PRODUCT_REGISTRY_EVENT.toString();
    final String topic = channelName + "-" + correlationId;
    // Create and return the producer
    return pulsarClients.getClient(channelName)
        .newProducer(Schema.JSON(ProductRegistryEvent.class))
        .producerName(topic)
        .topic(topic)
        .createAsync()
        .exceptionally(ex -> {
          throw new ProducerCreateException("Failed to create producer for correlation id: " + correlationId, ex);
        });
  }
}
