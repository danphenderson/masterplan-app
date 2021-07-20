package io.masterplan.infrastucture.components.task;

import io.masterplan.infrastucture.components.Completable;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;


import java.util.Calendar;


public final class Task extends TodoElement implements Completable{

    private final Observable<Boolean> _isCompleted = new Observable<>(false);
    public final IReadOnlyObservable<Boolean> isCompleted = _isCompleted;

    private final Observable<Calendar> _dueDate = new Observable<>(null);
    public final IReadOnlyObservable<Calendar> dueDate = _dueDate;

    private final Observable<Boolean> _isBookmarked = new Observable<>(false);
    public  final IReadOnlyObservable<Boolean> isBookmarked = _isBookmarked;


    public Task() {
        super();
    }
    public Task(String name) {
        super(name);
    }

    @Override
    public boolean isCompleted() {
        return _isCompleted.getValue();
    }

    @Override
    public void setCompleted(boolean isComplete) {
        this._isCompleted.setValue(isComplete);
    }

    public void setDueDate(Calendar dueDate) {
        this._dueDate.setValue(dueDate);
    }

    public Calendar getDueDate() {
        return _dueDate.getValue();
    }

    public void setBookmark(boolean isBookmarked) {
        this._isBookmarked.setValue(isBookmarked);
    }

    public boolean isBookmarked() {
        return this._isBookmarked.getValue();
    }

}
