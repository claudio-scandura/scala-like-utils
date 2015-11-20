package com.mylaesoftware.utils;

import com.mylaesoftware.util.Right;
import org.junit.Test;

import static com.mylaesoftware.errorhandling.Try.Try;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class RightTest {



    @Test(expected = NullPointerException.class)
    public void constructor_shouldComplain_whenValueIsNull() throws Exception {
        new Right<>(null);
    }

    @Test
    public void constructor_shouldCreateRightObject() throws Exception {
        String expectedValue = "Hello!";
        Right<?, String> right = new Right<>(expectedValue);

        assertThat(right.right(), equalTo(expectedValue));
    }

    @Test
    public void right_shouldImplementEitherInterfaceCorrectly() throws Exception {
        String expectedValue = "Hello!";
        Right<?, String> right = new Right<>(expectedValue);

        assertTrue(right.isRight());
        assertFalse(right.isLeft());

        Throwable actualException = Try(right::left).failed().get();
        assertEquals(UnsupportedOperationException.class, actualException.getClass());
    }
}