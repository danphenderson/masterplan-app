package io.masterplan.client.observable;

public interface IListener<ChangeInfo> {

    void onChange(ChangeInfo changeInfo);

}
