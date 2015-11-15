package com.mylaesoftware.exhandling;

import com.mylaesoftware.util.Either;
import org.junit.Test;

import java.util.function.Supplier;

import static com.mylaesoftware.exhandling.ExceptionHandling.RecoverableTry;
import static com.mylaesoftware.exhandling.ExceptionHandling.Try;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ExceptionHandlingTest {

    @Test
    public void try_shouldReturnResultProvidedBySupplier_whenNoExceptionIsThrown() throws Exception {
        Either<Throwable, Long> result = Try(
                () -> 12L
        );

        assertTrue(result.isRight());
        assertEquals(new Long(12L), result.right());
    }

    @Test
    public void try_shouldReturnException_whenExceptionIsThrownBySupplier() throws Exception {
        RuntimeException exception = new RuntimeException();
        Either<? extends Throwable, Long> result = Try(() -> {
            throw exception;
        });

        assertTrue(result.isLeft());
        assertEquals(exception, result.left());
    }

    @Test
    public void recoverableTry_shouldMapExceptionToObject_whenExceptionIsCoveredByTheRecoverClause() throws Exception {

        Either<Throwable, Long> result = RecoverableTry(this::failingLongSupplier)
                .recoverWith(e -> 23L, RuntimeException.class)
                .go();

        assertTrue(result.isRight());
        assertThat(result.right(), equalTo(23L));
    }

    @Test
    public void recoverableTry_shouldMapExceptionToObjectUsingLastInsertedMatchingRecoverStrategyFirst() throws Exception {

        Either<Throwable, Long> result = RecoverableTry(this::failingLongSupplier)
                .recoverWith(e -> 23L, RuntimeException.class)
                .recoverWith(e -> 42L, RuntimeException.class)
                .go();

        assertTrue(result.isRight());
        assertThat(result.right(), equalTo(42L));
    }

    @Test
    public void recoverableTry_shouldMapMoreThenOneException_whenRecoveryStrategyAppliesToMoreExceptionTypes() throws Exception {

        Supplier<Long> anotherFailingSupplier = () -> {
            throw new IllegalArgumentException();
        };

        Either<Throwable, Long> result = RecoverableTry(anotherFailingSupplier)
                .recoverWith(e -> 23L, RuntimeException.class, IllegalArgumentException.class)
                .go();

        assertTrue(result.isRight());
        assertThat(result.right(), equalTo(23L));
    }

    @Test
    public void recoverableTry_shouldNotMapExceptionToObject_whenExceptionIsNotCoveredByTheRecoverClause() throws Exception {

        Either<Throwable, Long> result = RecoverableTry(this::failingLongSupplier)
                .recoverWith(e -> 23L, IllegalArgumentException.class)
                .go();

        assertTrue(result.isLeft());
        assertThat(result.left().getClass(), equalTo(RuntimeException.class));
    }

    private Long failingLongSupplier() {
        throw new RuntimeException();
    }


}