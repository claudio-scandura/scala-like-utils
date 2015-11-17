package com.mylaesoftware.exhandling;

import com.mylaesoftware.util.Either;
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

    public Try(Either<Throwable, T> either) {
        this.either = either;
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


    /**
     * Returns the value from this `Success` or the given `default` argument if this is a `Failure`.
     * <p/>
     * ''Note:'': This will throw an exception if it is not a success and default throws an exception.
     */
    T orElseGet(Supplier<T> _default) {
        return null;
    }

    T get() {
        return either.right();
    }

    /**
     * Applies the given function `f` if this is a `Success`, otherwise returns `Unit` if this is a `Failure`.
     * <p/>
     * ''Note:'' If `f` throws, then this method may throw an exception.
     */
    void accept(Consumer<T> consumer) {
        either.accept(l -> {
            return;
        }, consumer);
    }

    Optional<T> toOptional() {
       return either.rightOptional();
    }


        /**
     * Maps the given function to the value from this `Success` or returns this if this is a `Failure`.
     */
    <G> Try<G> map(Function<T, G> mapper) {
        return new Try<>(either.mapRight(mapper));
    }

//    <G> Try<G> flatMap(Function<T, Try<G>> mapper) {
//        return mapRight(mapper);
//    }

//
//    /**
//     * Applies the given partial function to the value from this `Success` or returns this if this is a `Failure`.
//     */
//    def collect[U](pf: PartialFunction[T, U]): Try[U]
//
//    /**
//     * Converts this to a `Failure` if the predicate is not satisfied.
//     */
//    def filter(p: T => Boolean): Try[T]
//
//    /**
//     * Applies the given function `f` if this is a `Failure`, otherwise returns this if this is a `Success`.
//     * This is like `flatMap` for the exception.
//     */
//    def recoverWith[U >: T](@deprecatedName('f) pf: PartialFunction[Throwable, Try[U]]): Try[U]
//
//            /**
//             * Applies the given function `f` if this is a `Failure`, otherwise returns this if this is a `Success`.
//             * This is like map for the exception.
//            */
//            def recover[U >: T](@deprecatedName('f) pf: PartialFunction[Throwable, U]): Try[U]
//
//            /**
//             * Transforms a nested `Try`, ie, a `Try` of type `Try[Try[T]]`,
//             * into an un-nested `Try`, ie, a `Try` of type `Try[T]`.
//            */
//            def flatten[U](implicit ev: T <:< Try[U]): Try[U]
//
//    /**
//     * Inverts this `Try`. If this is a `Failure`, returns its exception wrapped in a `Success`.
//     * If this is a `Success`, returns a `Failure` containing an `UnsupportedOperationException`.
//     */
//    def failed: Try[Throwable]
//
//    /** Completes this `Try` by applying the function `f` to this if this is of type `Failure`, or conversely, by applying
//     *  `s` if this is a `Success`.
//     */
//    def transform[U](s: T => Try[U], f: Throwable => Try[U]): Try[U]
//

}
