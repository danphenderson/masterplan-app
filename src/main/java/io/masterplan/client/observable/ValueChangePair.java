package io.masterplan.client.observable;

public class ValueChangePair<T> {

    public final T oldVal;
    public final T newVal;

    public ValueChangePair(T oldVal, T newVal) {
        this.oldVal = oldVal;
        this.newVal = newVal;
    }

}
