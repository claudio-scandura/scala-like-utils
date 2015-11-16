package com.mylaesoftware.exhandling;

import com.mylaesoftware.util.Either;

import java.util.function.Function;

/**
 * Created by claudioscandura on 15/11/2015.
 */
public interface RecoverableTry<T> {

    RecoverableTry<T> recoverWith(Function<Throwable, T> recoveryAction, Class<? extends Throwable>... forExceptionType);

    Either<Throwable, T> result();
}
