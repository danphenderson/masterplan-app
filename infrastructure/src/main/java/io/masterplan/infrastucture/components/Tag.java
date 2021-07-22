package io.masterplan.infrastucture.components;

import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;


public final class Tag {

    private final Observable<String> _name = new Observable<>("");
    public final IReadOnlyObservable<String> name = _name;

    private final Observable<Colour> _color = new Observable<>(new Colour(255, 255, 255, 255));
    public final IReadOnlyObservable<Colour> color = _color;


    public Tag() { }

    public Tag(String name, Colour color) {
        if(name == null)
            throw new IllegalArgumentException("name cannot be null");
    }


    public void setName(String name) {
        if(name == null)
            throw new IllegalArgumentException("Tag.setName() - name can not be null");

        this._name.setValue(name);
    }

    public String getName() {
        return _name.getValue();
    }

    public void setColor(Colour color) {
        if(color == null)
            throw new IllegalArgumentException("Tag.setColor() - name can not be null");

        this._color.setValue(color);
    }

    public Colour getColor() {
        return _color.getValue();
    }

}
