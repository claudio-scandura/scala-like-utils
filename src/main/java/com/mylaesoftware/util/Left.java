package com.mylaesoftware.util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

public final class Left<Value, None> extends Either<Value, None> {

    private final Value value;


    public Left(Value value) {
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
    public Value left() {
        return value;
    }

    @Override
    public None right() {
        throw new NotImplementedException();
    }

}
