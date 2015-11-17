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

    public static <T> Try<T> newTry(Supplier<T> supplier) {
        return new Try(Try(supplier));
    }

    public static <T> RecoverableTry<T> RecoverableTry(Supplier<T> supplier) {
        return new RecoverableTryImpl<>(newTry(supplier));
    }

    private static class RecoverableTryImpl<T> implements RecoverableTry<T> {

        private final Try<T> bodyExecutionResult;
        private final Map<Class<? extends Throwable>, Function<Throwable, T>> recoveryStrategies;

        RecoverableTryImpl(Try<T> bodyExecutionResult) {
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

        //change this once the Try type works out
        @Override
        public Try<T> result() {
            if (bodyExecutionResult.isFailure()) {
                return recoverFromFailure(bodyExecutionResult.failed().get());
            }
            return bodyExecutionResult;
//            bodyExecutionResult.mapLeft(this::recoverFromFailure);
//            return bodyExecutionResult
//                    .mapLeft(this::recoverFromFailure)
//                    .orElse(bodyExecutionResult);
        }

        private Try<T> recoverFromFailure(Throwable throwable) {
            return Optional.ofNullable(recoveryStrategies.get(throwable.getClass()))
                    .map(function -> new Try<>(new Right<>(function.apply(throwable))))
                    .orElse(new Try<>(new Left<>(throwable)));
        }
    }
}
