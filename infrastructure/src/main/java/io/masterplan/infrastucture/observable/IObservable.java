package io.masterplan.infrastucture.observable;

public interface IObservable<T> {

    void startListen(IListener<T> listener);

    void stopListen(IListener<T> listener);

}
