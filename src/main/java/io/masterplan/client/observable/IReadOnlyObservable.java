package io.masterplan.client.observable;

public interface IReadOnlyObservable<T> extends IObservable<T> {
    public T getValue();
}
