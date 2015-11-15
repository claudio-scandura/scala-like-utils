package com.mylaesoftware.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

/**
 * Created by claudioscandura on 14/11/2015.
 */
public final class Right<None, Value> extends Either<None, Value> {


    private final Value value;

    public Right(Value value) {
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
    public None left() {
        throw new NotImplementedException();
    }

    @Override
    public Value right() {
        return value;
    }
}
