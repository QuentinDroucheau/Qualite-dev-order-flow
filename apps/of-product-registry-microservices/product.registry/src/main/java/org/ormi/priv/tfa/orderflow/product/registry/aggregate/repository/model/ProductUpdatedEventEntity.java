package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

public class ProductUpdatedEventEntity extends ProductRegistryEventEntity {
  static final String EVENT_TYPE = "ProductUpdated";

  /**
   * Payload for the event.
   */
  public record Payload(String productId, String name, String productDescription) {
  }

  /**
   * The payload for the event.
   */
  private Payload payload;


  @Override
  /**
   * @return The event type
   */
  public String getEventType() {
    return EVENT_TYPE;
  }

  /**
   * @return The payload for the event.
   */
  public Payload getPayload() {
    return payload;
  }
}
