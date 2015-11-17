package com.mylaesoftware.util;

import java.util.Objects;
import java.util.function.Function;

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
        throw new UnsupportedOperationException("Right.left()");
    }

    @Override
    public Value right() {
        return value;
    }

    @Override
    public <G> Either<G, Value> mapLeft(Function<None, G> mapper) {
        return new Right<>(value);
    }

    @Override
    public <G> Either<None, G> mapRight(Function<Value, G> mapper) {
        return new Right<>(mapper.apply(value));
    }
}
