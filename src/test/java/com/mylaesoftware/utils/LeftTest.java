package com.mylaesoftware.utils;

import com.mylaesoftware.exhandling.ExceptionHandling;
import com.mylaesoftware.util.Left;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class LeftTest {

    @Test(expected = NullPointerException.class)
    public void constructor_shouldComplain_whenValueIsNull() throws Exception {
        new Left<>(null);
    }

    @Test
    public void constructor_shouldCreateRightObject() throws Exception {
        RuntimeException expectedValue = new RuntimeException("Hello!");
        Left<RuntimeException, ?> left = new Left<>(expectedValue);

        assertThat(left.left(), equalTo(expectedValue));
    }

    @Test
    public void left_shouldImplementEitherInterfaceCorrectly() throws Exception {
        RuntimeException expectedValue = new RuntimeException("Hello!");
        Left<RuntimeException, ?> left = new Left<>(expectedValue);

        assertTrue(left.isLeft());
        assertFalse(left.isRight());

        Throwable actualException = ExceptionHandling.Try(left::right).left();
        assertEquals(UnsupportedOperationException.class, actualException.getClass());
    }

}