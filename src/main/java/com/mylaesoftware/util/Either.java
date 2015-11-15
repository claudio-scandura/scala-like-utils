package com.mylaesoftware.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<Left, Right> {

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract Left left();

    public abstract Right right();

    public final <G> G fold(Function<Left, G> failure, Function<Right, G> success) {
        return isRight() ? success.apply(right()) : failure.apply(left());
    }

    public void accept(Consumer<Left> failure, Consumer<Right> success) {
        fold(
                l -> {
                    failure.accept(l);
                    return null;
                },
                r -> {
                    success.accept(r);
                    return null;
                }
        );

    }


    public final Optional<Right> toOptional() {
        return isRight() ? Optional.of(right()) : Optional.empty();
    }

}