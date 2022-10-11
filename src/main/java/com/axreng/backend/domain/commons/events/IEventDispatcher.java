package com.axreng.backend.domain.commons.events;


public interface IEventDispatcher {
    void notify(IEvent event);
    <T extends IEvent> void subscribe(Class<T> event, IEventHandler<T> handler);
    <T extends IEvent> void unsubscribe(Class<T> event, IEventHandler<T> handler);
}
