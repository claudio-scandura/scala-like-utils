package com.mylaesoftware.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

public final class Left<E> extends Either<E, Object> {

    private final E value;


    public Left(E value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public E left() {
        return value;
    }

    @Override
    public Object right() {
        throw new NotImplementedException();
    }

}
