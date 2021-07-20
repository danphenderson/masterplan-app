package io.masterplan.infrastucture.observable;

public interface IValueChangePairListener<T> {

    void onChange(T oldVal, T newVal);
    
}
