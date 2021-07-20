package io.masterplan.infrastucture.components.task;

import java.util.Calendar;

// TODO: determine flexible interface
public interface IDateGenerator {

    /**
     *
     * @return
     */
    int getIndex();

    /**
     *
     * @param index
     */
    void setIndex(int index);

    /**
     *
     * @return
     */
    Calendar getDate();

    /**
     *
     * @return
     */
    boolean hasNext();

    /**
     *
     * @return
     */
    Calendar getNext();

    /**
     *
     * @return
     */
    boolean hasPrev();

    /**
     *
     * @return
     */
    Calendar getPrev();

}
