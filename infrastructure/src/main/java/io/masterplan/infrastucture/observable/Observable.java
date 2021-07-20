package io.masterplan.infrastucture.observable;

import java.util.ArrayList;
import java.util.HashSet;

public class Observable<T> implements IObservable<T>, IReadOnlyObservable<T>, IWriteOnlyObservable<T>
{
    private final HashSet<IListener<T>> listeners = new HashSet<>();
    private T value = null;

    public Observable() {  }
    public Observable(T value) { this.value = value; }

    @Override
    public void startListen(IListener<T> listener) {
        listeners.add(listener);
        listener.onChange(value);
    }

    @Override
    public void stopListen(IListener<T> listener) {
        listeners.remove(listener);
    }

    public T getValue() { return value; }

    public void setValue(T value) {
        this.value = value;
        for(IListener<T> listener : new ArrayList<>(listeners))
            listener.onChange(value);
    }
}
