package io.masterplan.client.observable;

public interface IObservable<T> {

    void startListen(IListener<T> listener);

    void stopListen(IListener<T> listener);

}
