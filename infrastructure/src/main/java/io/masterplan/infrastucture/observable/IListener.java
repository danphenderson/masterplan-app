package io.masterplan.infrastucture.observable;

public interface IListener<ChangeInfo> {

    void onChange(ChangeInfo changeInfo);

}
