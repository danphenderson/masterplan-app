package io.masterplan.infrastucture.components;

import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;



import java.net.URI;

public final class Category extends TodoElement {

    private final Observable<URI> _backgroundImage = new Observable<>( null );
    public final IReadOnlyObservable<URI> backgroundImage = _backgroundImage;

    private final Observable<Colour> _backgroundColor = new Observable<>( new Colour(255,255,255, 255) );
    public final IReadOnlyObservable<Colour> backgroundColor = _backgroundColor;

    public Category() {
        super();
    }

    public Category(String name) {
        super(name);
    }


    /* getters and setters */

    public URI getBackgroundImageURI() { return this._backgroundImage.getValue(); }
    public void setBackgroundImageURI(URI uri) { this._backgroundImage.setValue(uri); }

    public Colour getBackgroundColor() { return this._backgroundColor.getValue(); }
    public void setBackgroundColor(Colour color) { this._backgroundColor.setValue(color); }

}
