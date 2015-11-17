package com.mylaesoftware.util;

import java.util.Objects;
import java.util.function.Function;

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
        throw new UnsupportedOperationException("Left.right()");
    }

    @Override
    public <G> Either<G, None> mapLeft(Function<Value, G> mapper) {
        return new Left<>(mapper.apply(value));
    }

    @Override
    public <G> Either<Value, G> mapRight(Function<None, G> mapper) {
        return new Left<>(value);
    }

}
