package com.mylaesoftware.utils;

import com.mylaesoftware.exhandling.ExceptionHandling;
import com.mylaesoftware.util.Right;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class RightTest {


    @Test(expected = NullPointerException.class)
    public void constructor_shouldComplain_whenValueIsNull() throws Exception {
        new Right(null);
    }

    @Test
    public void constructor_shouldCreateRightObject() throws Exception {
        String expectedValue = "Hello!";
        Right<String> right = new Right(expectedValue);

        assertThat(right.right(), equalTo(expectedValue));
    }

    @Test
    public void right_shouldImplementEitherInterfaceCorrectly() throws Exception {
        String expectedValue = "Hello!";
        Right<String> right = new Right(expectedValue);

        assertTrue(right.isRight());
        assertFalse(right.isLeft());

        Throwable actualException = ExceptionHandling.Try(() -> right.left()).left();
        assertEquals(NotImplementedException.class, actualException.getClass());
    }
}