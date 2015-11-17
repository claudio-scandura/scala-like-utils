package com.mylaesoftware.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<Left, Right> {

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract Left left();

    public abstract Right right();

    public abstract <G> Either<G, Right> mapLeft(Function<Left, G> mapper);

    public abstract <G> Either<Left, G> mapRight(Function<Right, G> mapper);

    public final <G> G fold(Function<Left, G> leftMapper, Function<Right, G> rightMapper) {
        return isRight() ? rightMapper.apply(right()) : leftMapper.apply(left());
    }

    public final void accept(Consumer<Left> leftConsumer, Consumer<Right> rightConsumer) {
        fold(
                l -> {
                    leftConsumer.accept(l);
                    return null;
                },
                r -> {
                    rightConsumer.accept(r);
                    return null;
                }
        );

    }

    public final Optional<Right> rightOptional() {
        return isRight() ? Optional.of(right()) : Optional.empty();
    }

    public final Optional<Left> leftOptional() {
        return isLeft() ? Optional.of(left()) : Optional.empty();
    }

}