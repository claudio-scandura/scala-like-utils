package com.mylaesoftware.exhandling;


import com.mylaesoftware.util.Either;
import com.mylaesoftware.util.Left;
import com.mylaesoftware.util.Right;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionHandling {

    public static <T> Either<Throwable, T> Try(Supplier<T> supplier) {
        try {
            return new Right<>(supplier.get());
        } catch (Throwable exception) {
            return new Left<>(exception);
        }
    }

    public static <T> RecoverableTry<T> RecoverableTry(Supplier<T> supplier) {
        return new RecoverableTryImpl<>(Try(supplier));
    }

    private static class RecoverableTryImpl<T> implements RecoverableTry<T> {

        private final Either<Throwable, T> bodyExecutionResult;
        private final Map<Class<? extends Throwable>, Function<Throwable, T>> recoveryStrategies;

        RecoverableTryImpl(Either<Throwable, T> bodyExecutionResult) {
            this.bodyExecutionResult = bodyExecutionResult;
            this.recoveryStrategies = new HashMap<>();
        }

        @Override
        @SafeVarargs
        final public RecoverableTry<T> recoverWith(Function<Throwable, T> recoveryAction, Class<? extends Throwable>... forExceptionType) {
            for (Class<? extends Throwable> exceptionType : forExceptionType) {
                recoveryStrategies.put(exceptionType, recoveryAction);
            }
            return this;
        }

        @Override
        public Either<Throwable, T> result() {
            return bodyExecutionResult
                    .mapLeft(this::recoverFromFailure)
                    .orElse(bodyExecutionResult);
        }

        private Either<Throwable, T> recoverFromFailure(Throwable throwable) {
            return Optional.ofNullable(recoveryStrategies.get(throwable.getClass()))
                    .map(function -> new Right<Throwable, T>(function.apply(throwable)).asEither())
                    .orElse(new Left<Throwable, T>(throwable).asEither());
        }
    }
}
