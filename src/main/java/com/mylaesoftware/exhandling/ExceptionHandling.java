package com.mylaesoftware.exhandling;


import com.mylaesoftware.util.Either;
import com.mylaesoftware.util.Left;
import com.mylaesoftware.util.Right;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionHandling {

    public static <E extends Throwable, T> Either<E, T> Try(Supplier<T> supplier) {
        try {
            return new Right(supplier.get());
        } catch (Throwable t) {
            return new Left(t);
        }
    }

    public static <T> RecoverableTry<T> RecoverableTry(Supplier<T> supplier) {
        return new RecoverableTryImpl(Try(supplier));
    }

    private static class RecoverableTryImpl<T> implements RecoverableTry<T> {

        private final Either<Throwable, T> bodyExecutionResult;
        private final Map<Class<? extends Throwable>, Function<Throwable, T>> recoveryStrategies;

        RecoverableTryImpl(Either<Throwable, T> bodyExecutionResult) {
            this.bodyExecutionResult = bodyExecutionResult;
            this.recoveryStrategies = new LinkedHashMap<>();
        }

        @Override
        public RecoverableTry<T> recoverWith(Function<Throwable, T> recoveryAction, Class<? extends Throwable>... forExceptionType) {
            for (Class<? extends Throwable> exceptionType : forExceptionType) {
                recoveryStrategies.put(exceptionType, recoveryAction);
            }
            return this;
        }

        @Override
        public Either<Throwable, T> go() {
            if (bodyExecutionResult.isLeft()) {
                Function<Throwable, T> recovery = recoveryStrategies.get(bodyExecutionResult.left().getClass());
                if (recovery != null) {
                    return new Right(recovery.apply(bodyExecutionResult.left()));
                }
            }
            return bodyExecutionResult;
        }
    }
}
