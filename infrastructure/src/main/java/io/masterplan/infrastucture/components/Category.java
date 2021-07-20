package io.masterplan.infrastucture.components;

import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import javafx.scene.paint.Color;

import java.net.URI;

public final class Category extends TodoElement {

    private final Observable<URI> _backgroundImage = new Observable<>( null );
    public final IReadOnlyObservable<URI> backgroundImage = _backgroundImage;

    private final Observable<Color> _backgroundColor = new Observable<>( null );
    public final IReadOnlyObservable<Color> backgroundColor = _backgroundColor;

    public Category() {
        super();
    }

    public Category(String name) {
        super(name);
    }


    /* getters and setters */

    public URI getBackgroundImageURI() { return this._backgroundImage.getValue(); }
    public void setBackgroundImageURI(URI uri) { this._backgroundImage.setValue(uri); }

    public Color getBackgroundColor() { return this._backgroundColor.getValue(); }
    public void setBackgroundColor(Color color) { this._backgroundColor.setValue(color); }

}
