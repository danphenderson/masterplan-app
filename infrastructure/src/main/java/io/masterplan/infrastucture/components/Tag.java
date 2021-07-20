package io.masterplan.infrastucture.components;

import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import javafx.scene.paint.Color;


public final class Tag {

    private final Observable<String> _name = new Observable<>("");
    public final IReadOnlyObservable<String> name = _name;

    private final Observable<Color> _color = new Observable<>(Color.TRANSPARENT);
    public final IReadOnlyObservable<Color> color = _color;


    public Tag() { }

    public Tag(String name, Color color) {
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

    public void setColor(Color color) {
        if(color == null)
            throw new IllegalArgumentException("Tag.setColor() - name can not be null");

        this._color.setValue(color);
    }

    public Color getColor() {
        return _color.getValue();
    }

}
