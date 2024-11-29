package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "product_registry_events")
public abstract class ProductRegistryEventEntity {
  
  /**
   * The id of the Object.
   */
	public ObjectId id;

  /**
   * The id of the event.
   */
  public String eventId;

  /**
   * The type of the event.
   */
  public String eventType = getEventType();

  /**
   * The id of the aggregate root.
   */
  public String aggregateRootId;

  /**
   * The version of the event.
   */
  public long version;

  /**
   * The timestamp of the event.
   */
  public long timestamp;

  /**
   * @return The event type.
   */
  abstract String getEventType();
}
