package com.mylaesoftware.utils;

import com.mylaesoftware.util.Either;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class EitherTest {

    private class CustomRight extends Either<Object, String> {


        private final String value;

        public CustomRight(String value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public Throwable left() {
            throw new NotImplementedException();
        }

        @Override
        public String right() {
            return value;
        }
    }

    private class CustomLeft extends Either<RuntimeException, Object> {


        private final RuntimeException exception;

        public CustomLeft(RuntimeException exception) {
            this.exception = exception;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public RuntimeException left() {
            return exception;
        }

        @Override
        public String right() {
            throw new NotImplementedException();
        }
    }

    @Test
    public void asOptional_shouldReturnAnOptionalContainingTheRighValue_whenEitherIsRight() throws Exception {
        String expectedResult = "Hello dude!";
        Either<?, String> rightEither = new CustomRight(expectedResult);

        Optional<String> result = rightEither.toOptional();
        assertThat(result, equalTo(Optional.of(expectedResult)));
    }

    @Test
    public void asOptional_shouldReturnEmpty_whenEitherIsLeft() throws Exception {
        Either<RuntimeException, ?> leftEither = new CustomLeft(new RuntimeException("Damn!"));

        Optional<?> result = leftEither.toOptional();
        assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    public void fold_shouldCallSuccessFunction_whenEitherIsRight() throws Exception {
        Either<?, String> rightEither = new CustomRight("Hello dude!");

        Integer result = rightEither.fold(
                (f) -> 321,
                (s) -> 123
        );

        assertThat(result, equalTo(123));
    }

    @Test
    public void fold_shouldCallFailureFunction_whenEitherIsLeft() throws Exception {
        Either<RuntimeException, ?> leftEither = new CustomLeft(new RuntimeException());

        Integer result = leftEither.fold(
                (f) -> 321,
                (s) -> 123
        );

        assertThat(result, equalTo(321));
    }

    @Test
    public void accept_shouldCallSuccessFunction_whenEitherIsRight() throws Exception {
        Either<?, String> rightEither = new CustomRight("Hello dude!");

        rightEither.accept(
                (l) -> fail("Accept on a Either.Right should not call the failure consumer."),
                (r) -> doNothing()
        );
    }

    @Test
    public void accept_shouldCallFailurteFunction_whenEitherIsLeft() throws Exception {
        Either<RuntimeException, ?> leftEither = new CustomLeft(new RuntimeException());

        leftEither.accept(
                (l) -> doNothing(),
                (r) -> fail("Accept on a Either.Left should not call the failure consumer.")
        );
    }

    @Test
    public void mapLeft_shouldApplyMapperToLeftValue_whenEitherIsLeft() throws Exception {
        String expectedMessage = "some bad error..";
        Either<RuntimeException, ?> leftEither = new CustomLeft(new RuntimeException(expectedMessage));

        Optional<String> result = leftEither.mapLeft(Throwable::getMessage);

        assertTrue(result.isPresent());
        assertThat(result.get(), equalTo(expectedMessage));
    }

    @Test
    public void mapLeft_shouldReturnOptionalEmpty_whenEitherIsNotLeft() throws Exception {
        Either<Object, String> nonLeftEither = new CustomRight("This is a right Either");

        Optional<String> result = nonLeftEither.mapLeft(Object::toString);

        assertFalse(result.isPresent());
    }

    @Test
    public void mapRight_shouldApplyMapperToRightValue_whenEitherIsRight() throws Exception {
        String expectedResult = "some lowercase string";
        Either<?, String> customRight = new CustomRight(expectedResult);

        Optional<String> result = customRight.mapRight(String::toUpperCase);

        assertTrue(result.isPresent());
        assertThat(result.get(), equalTo(expectedResult.toUpperCase()));
    }

    @Test
    public void mapRight_shouldReturnOptionalEmpty_whenEitherIsNotRight() throws Exception {
        Either<RuntimeException, Object> nonRightEither = new CustomLeft(new RuntimeException());

        Optional<String> result = nonRightEither.mapRight(Object::toString);

        assertFalse(result.isPresent());
    }

    private static void doNothing() {

    }
}