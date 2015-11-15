package com.mylaesoftware.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

/**
 * Created by claudioscandura on 14/11/2015.
 */
public final class Right<T> extends Either<Object, T> {


    private final T value;

    public Right(T value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public Object left() {
        throw new NotImplementedException();
    }

    @Override
    public T right() {
        return value;
    }
}
