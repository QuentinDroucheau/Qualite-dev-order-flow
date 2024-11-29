package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

public class ProductRemovedEventEntity extends ProductRegistryEventEntity {
  static final String EVENT_TYPE = "ProductRemoved";

  /**
   * Payload for the event.
   */
  public record Payload(String productId) {
  }

  /**
   * The payload for the event.
   */
  public Payload payload;

  @Override
  /**
   * @return The event type
   */
  public String getEventType() {
    return EVENT_TYPE;
  }
  
}
