package com.michael.attackpoint.log.addentry.pickers;

/**
 * Created by michael on 4/5/16.
 */
public interface PickerContract {

    interface Picker<T> {
        void set(T item);
    }

    interface Manager<T> {

        void update(T item);

        T get();

    }
}
