package io.masterplan.infrastucture.observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ObservableManager {

    public static class ObservableListenerPair<T> {

        private final IObservable<T> observable;
        private final HashSet<IListener<T>> listeners = new HashSet<>();

        public ObservableListenerPair(IObservable<T> observable) {
            this.observable = observable;
        }

        public void addListener(IListener<T> listener) {
            listeners.add(listener);
        }

        public void removeListener(IListener<T> listener) {
            listeners.remove(listener);
        }

        public void removeAllListeners() {
            listeners.clear();
        }

        public void startListen() {
            for(IListener<T> listener : this.listeners)
                observable.startListen(listener);
        }

        public void stopListen() {
            for(IListener<T> listener : this.listeners)
                observable.stopListen(listener);
        }

        public boolean isListenersEmpty() {
            return listeners.size() == 0;
        }
    }


    private final Map<IObservable<?>, ObservableListenerPair<?>> observableToPair = new HashMap<>();
    private boolean listening = false;


    public ObservableManager() {  }


    public void startListen() {
        if(listening)
            return;
        else
            listening = true;

        for(ObservableListenerPair<?> pair : new ArrayList<>(observableToPair.values()))
            pair.startListen();
    }

    public void stopListen() {
        if(!listening)
            return;
        else
            listening = false;

        for(ObservableListenerPair<?> pair : new ArrayList<>(observableToPair.values()))
            pair.stopListen();
    }

    @SuppressWarnings("unchecked")
    private <T> ObservableListenerPair<T> getPair(IObservable<?> observable) {
        return (ObservableListenerPair<T>) observableToPair.get(observable);
    }

    public <T> void addListener(IObservable<T> observable, IListener<T> listener) {
        ObservableListenerPair<T> pair;

        if( (pair = getPair(observable)) == null) {
            pair = new ObservableListenerPair<>(observable);
            observableToPair.put(observable, pair);
        }

        pair.addListener(listener);

        if(listening)
            observable.startListen(listener);
    }

    public <T> void removeListener(IObservable<T> observable, IListener<T> listener) {

        ObservableListenerPair<T> pair = getPair(observable);

        if(pair == null)
            throw new IllegalArgumentException("ObservableManager.removeListener() - the given observable does not exist in this ObservableManager");

        pair.removeListener(listener);

        // if the pair isn't being used for any listeners, remove it from the map
        if(pair.isListenersEmpty())
            observableToPair.remove(observable);


        observable.stopListen(listener);
    }

    public void removeAllListeners(IObservable<?> observable) {
        ObservableListenerPair<?> pair = observableToPair.get(observable);

        if(listening)
            pair.stopListen();

        observableToPair.remove(observable);
    }

    public boolean isListening() { return listening; }
}
