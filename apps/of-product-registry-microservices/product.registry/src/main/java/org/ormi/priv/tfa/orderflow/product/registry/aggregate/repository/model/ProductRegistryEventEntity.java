package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "product_registry_events")
public abstract class ProductRegistryEventEntity {
	public ObjectId id;
  public String eventId;
  public String eventType = getEventType();
  public String aggregateRootId;
  public long version;
  public long timestamp;

  abstract String getEventType();
}
