package com.mylaesoftware.errorhandling;

import com.mylaesoftware.util.PartialFunction;
import org.junit.Test;

import static com.mylaesoftware.errorhandling.Try.Try;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "ThrowableResultOfMethodCallIgnored"})
public class TryTest {

    @Test
    public void try_shouldReturnResultProvidedBySupplier_whenNoExceptionIsThrown() throws Exception {
        Try<Long> result = Try(
                () -> 12L
        );

        assertTrue(result.isSuccess());
        assertEquals(new Long(12L), result.get());
    }

    @Test
    public void try_shouldReturnException_whenExceptionIsThrownBySupplier() throws Exception {
        RuntimeException exception = new RuntimeException();
        Try<Long> result = Try(() -> {
            throw exception;
        });

        assertTrue(result.isFailure());
        assertEquals(exception, result.failed().get());
    }

    @Test
    public void try_shouldMapExceptionToObject_whenExceptionIsCoveredByThePartialFunction() throws Exception {
        PartialFunction<Throwable, Long> pf = new PartialFunction<>();

        Try<Long> result = Try(this::failingLongSupplier)
                .recover(pf.caseOf(e -> e instanceof RuntimeException, e -> 23L));

        assertTrue(result.isSuccess());
        assertThat(result.get(), equalTo(23L));
    }

    @Test
    public void try_shouldMapExceptionToObjectUsingFirstInsertedMatchingPartialFunction() throws Exception {
        PartialFunction<Throwable, Long> pf = new PartialFunction<>();

        Try<Long> result = Try(this::failingLongSupplier)
                .recover(pf.caseOf(e -> e instanceof RuntimeException, n -> 42L))
                .recover(pf.caseOf(e -> e instanceof RuntimeException, n -> 23L));

        assertTrue(result.isSuccess());
        assertThat(result.get(), equalTo(42L));
    }

    @Test
    public void try_shouldNotMapExceptionToObject_whenExceptionIsNotCoveredByTheRecoverClause() throws Exception {
        PartialFunction<Throwable, Long> pf = new PartialFunction<>();

        Try<Long> result = Try(this::failingLongSupplier)
                .recover(pf.caseOf(e -> e instanceof IllegalArgumentException, n -> 23L));

        assertTrue(result.isFailure());
        assertThat(result.failed().get().getClass(), equalTo(RuntimeException.class));
    }

    private Long failingLongSupplier() {
        throw new RuntimeException();
    }


}