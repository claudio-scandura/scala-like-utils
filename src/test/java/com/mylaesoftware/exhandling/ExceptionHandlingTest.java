package com.mylaesoftware.exhandling;

import org.junit.Test;

import java.util.function.Supplier;

import static com.mylaesoftware.exhandling.ExceptionHandling.RecoverableTry;
import static com.mylaesoftware.exhandling.ExceptionHandling.newTry;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "ThrowableResultOfMethodCallIgnored"})
public class ExceptionHandlingTest {

    @Test
    public void try_shouldReturnResultProvidedBySupplier_whenNoExceptionIsThrown() throws Exception {
        Try<Long> result = newTry(
                () -> 12L
        );

        assertTrue(result.isSuccess());
        assertEquals(new Long(12L), result.get());
    }

    @Test
    public void try_shouldReturnException_whenExceptionIsThrownBySupplier() throws Exception {
        RuntimeException exception = new RuntimeException();
        Try<Long> result = newTry(() -> {
            throw exception;
        });

        assertTrue(result.isFailure());
        assertEquals(exception, result.failed().get());
    }

    @Test
    public void recoverableTry_shouldMapExceptionToObject_whenExceptionIsCoveredByTheRecoverClause() throws Exception {

        Try<Long> result = RecoverableTry(this::failingLongSupplier)
                .recoverWith(e -> 23L, RuntimeException.class)
                .result();

        assertTrue(result.isSuccess());
        assertThat(result.get(), equalTo(23L));
    }

    @Test
    public void recoverableTry_shouldMapExceptionToObjectUsingLastInsertedMatchingRecoverStrategyFirst() throws Exception {

        Try<Long> result = RecoverableTry(this::failingLongSupplier)
                .recoverWith(e -> 23L, RuntimeException.class)
                .recoverWith(e -> 42L, RuntimeException.class)
                .result();

        assertTrue(result.isSuccess());
        assertThat(result.get(), equalTo(42L));
    }

    @Test
    public void recoverableTry_shouldMapMoreThenOneException_whenRecoveryStrategyAppliesToMoreExceptionTypes() throws Exception {

        Supplier<Long> anotherFailingSupplier = () -> {
            throw new IllegalArgumentException();
        };

        Try<Long> result = RecoverableTry(anotherFailingSupplier)
                .recoverWith(e -> 23L, RuntimeException.class, IllegalArgumentException.class)
                .result();

        assertTrue(result.isSuccess());
        assertThat(result.get(), equalTo(23L));
    }

    @Test
    public void recoverableTry_shouldNotMapExceptionToObject_whenExceptionIsNotCoveredByTheRecoverClause() throws Exception {

        Try<Long> result = RecoverableTry(this::failingLongSupplier)
                .recoverWith(e -> 23L, IllegalArgumentException.class)
                .result();

        assertTrue(result.isFailure());
        assertThat(result.failed().get().getClass(), equalTo(RuntimeException.class));
    }

    private Long failingLongSupplier() {
        throw new RuntimeException();
    }


}