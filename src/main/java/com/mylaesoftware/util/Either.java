package com.mylaesoftware.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<Left, Right> {

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract Left left();

    public abstract Right right();

    public final Either<Left, Right> asEither() {return this;}

    public final <G> Optional<G> mapLeft(Function<Left, G> mapper) {
        return isLeft() ? Optional.of(mapper.apply(left())) : Optional.empty();
    }

    public final <G> Optional<G> mapRight(Function<Right, G> mapper) {
        return isRight() ? Optional.of(mapper.apply(right())) : Optional.empty();
    }

    public final <G> G fold(Function<Left, G> leftMapper, Function<Right, G> rightMapper) {
        return isRight() ? rightMapper.apply(right()) : leftMapper.apply(left());
    }

    public void accept(Consumer<Left> leftConsumer, Consumer<Right> rightConsumer) {
        mapLeft(l -> leftConsumer);
        mapRight(r -> rightConsumer);
    }


    public final Optional<Right> toOptional() {
        return isRight() ? Optional.of(right()) : Optional.empty();
    }

}