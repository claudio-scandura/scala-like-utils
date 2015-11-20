package com.mylaesoftware.errorhandling;

import com.mylaesoftware.util.Either;
import com.mylaesoftware.util.Left;
import com.mylaesoftware.util.PartialFunction;
import com.mylaesoftware.util.Right;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Claudio Scandura on 17/11/2015.
 * cl.scandura@gmail.com
 */
public class Try<T>  {

    private final Either<Throwable, T> either;

    Try(Either<Throwable, T> either) {
        this.either = either;
    }

    public static <T> Try<T> Try(Supplier<T> supplier) {
        try {
            return new Try<>(new Right<>(supplier.get()));
        } catch (Throwable exception) {
            return new Try<>(new Left<>(exception));
        }
    }


    boolean isFailure() {
        return either.isLeft();
    }

    boolean isSuccess() {
        return either.isRight();
    }

    public Try<Throwable> failed() {
        return new Try<>(new Right<>(either.left()));
    }


    public T orElseGet(Supplier<T> _default) {
        return toOptional().orElseGet(_default);
    }

    public T get() {
        return either.right();
    }

    public void accept(Consumer<T> consumer) {
        either.accept(l -> {}, consumer);
    }

    public Optional<T> toOptional() {
       return either.rightOptional();
    }

    public Try<T> recover(PartialFunction<Throwable, T> recovery) {
        if (isFailure()) {
            return recovery.lift()
                    .apply(either.left())
                    .map(t -> new Try<>(new Right<>(t)))
                    .orElse(this);
        }
        return this;
    }

    <G> Try<G> map(Function<T, G> mapper) {
        return new Try<>(either.mapRight(mapper));
    }

}
