package io.masterplan.infrastucture.observable;

public interface IReadOnlyObservable<T> extends IObservable<T> {
    public T getValue();
}
