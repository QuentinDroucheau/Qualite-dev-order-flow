package org.ormi.priv.tfa.orderflow.lib.publishedlanguage.error;

import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistryMessage;

public record ProductRegistryError(String errorMessage) implements ProductRegistryMessage {
}