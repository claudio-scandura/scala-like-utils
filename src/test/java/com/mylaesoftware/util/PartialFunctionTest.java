package com.mylaesoftware.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Claudio Scandura on 20/11/2015.
 * cl.scandura@gmail.com
 */
public class PartialFunctionTest {

    @Test
    public void apply_shouldUseProvidedFunction() throws Exception {
        PartialFunction<Integer, String> pf = new PartialFunction<>();
        pf.caseOf(value -> value % 3 == 0, n -> n + " is a multiple of three!");

        assertThat(pf.apply(3), is("3 is a multiple of three!"));
        assertThat(pf.apply(15), is("15 is a multiple of three!"));
    }

    @Test
    public void apply_shouldUseProvidedFunction_whenMultipleCasesAreProvided() throws Exception {
        PartialFunction<String, Double> pf = new PartialFunction<>();
        pf.caseOf(s -> s.equals("hola"), n -> 34.34)
                .caseOf(s -> s.equals("ciao"), n -> 22.34);

        assertThat(pf.apply("ciao"), is(22.34));
    }

    @Test
    public void apply_shouldUseDefaultCase_whenThereIsNoCodomainDefinedForTheGivenInput_butThereIsDefaultCaseDefined() throws Exception {
        PartialFunction<String, Double> pf = new PartialFunction<>();
        pf.caseOf(s -> s.equals("hola"), n -> 34.34)
                .caseOf(s -> s.equals("ciao"), n -> 22.34)
                .caseDefault(v -> 0.0);

        assertThat(pf.apply("hello"), is(0.0));
    }

    @Test(expected = PartialFunction.ApplicationUndefinedException.class)
    public void apply_shouldThrowException_whenThereIsNoCodomainDefinedForTheGivenInput() throws Exception {
        PartialFunction<Integer, String> pf = new PartialFunction<>();
        pf.caseOf(i -> i == 3, n -> "Three!");

        pf.apply(4);
    }

    @Test
    public void isDefinedAt_shouldReturnTrue_whenFunctionIsDefinedForADomainValue() throws Exception {
        PartialFunction<Integer, String> pf = new PartialFunction<Integer, String>()
                .caseOf(i -> (i + 3) == 8, n -> "Yes");

        assertTrue(pf.isDefinedAt(5));
    }

    @Test
    public void isDefinedAt_shouldReturnFalse_whenFunctionIsDefinedForADomainValue() throws Exception {
        PartialFunction<Integer, String> pf = new PartialFunction<Integer, String>()
                .caseOf(i -> i + 3 == 8, n -> "Yes");

        assertFalse(pf.isDefinedAt(4));
    }

    @Test
    public void apply_shouldRespectOrderDefinedByCaseOfInvocations() throws Exception {
        PartialFunction<Integer, String> pf = new PartialFunction<Integer, String>()
                .caseOf(i -> i % 2 == 0, n -> "Yes")
                .caseOf(i -> i % 4 == 0, n -> "No");

        assertThat(pf.apply(12), is("Yes"));
    }

}