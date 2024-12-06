package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "product_registry_events")
public abstract class ProductRegistryEventEntity {
  
  /**
   * The id of the Object.
   */
	private ObjectId id;

  /**
   * The id of the event.
   */
  private String eventId;

  /**
   * The type of the event.
   */
  private String eventType = getEventType();

  /**
   * The id of the aggregate root.
   */
  private String aggregateRootId;

  /**
   * The version of the event.
   */
  private long version;

  /**
   * The timestamp of the event.
   */
  private long timestamp;

  /**
   * @return The event type.
   */
  abstract String getEventType();

  /**
   * @return The version of the event.
   */
  public long getVersion(){
    return version;
  }

  /**
   * @return The timestamp of the event.
   */
  public long timestamp(){
    return timestamp;
  }

  /**
   * @return The id of the Object.
   */
  public ObjectId getId(){
    return id;
  }

  /**
   * @return The id of the event.
   */
  public String getEventId(){
    return eventId;
  }

  /**
   * @return The id of the aggregate root.
   */
  public String getAgregrateRootId(){
    return aggregateRootId;
  }
}
