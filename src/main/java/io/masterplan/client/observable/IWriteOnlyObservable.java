package io.masterplan.client.observable;

public interface IWriteOnlyObservable<T> extends IObservable<T> {

    public void setValue(T newValue);

}
