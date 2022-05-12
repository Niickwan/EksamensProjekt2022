package com.jmmnt.UseCase;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneralUseCaseTest {
    GeneralUseCase gUC = new GeneralUseCase();
    @Test
    public void checkIfStringMatchesInput() {
        boolean res1 = gUC.checkIfStringMatchesInput("Hans","an");
        assertTrue(res1);
        boolean res2 = gUC.checkIfStringMatchesInput("Hans","hans");
        assertTrue(res2);
        boolean res3 = gUC.checkIfStringMatchesInput("Hans", "Hans");
        assertTrue(res3);
        boolean res4 = gUC.checkIfStringMatchesInput("Hans","hanl");
        assertFalse(res4);
        boolean res5 = gUC.checkIfStringMatchesInput("Hans", "l");
        assertFalse(res5);
        boolean res6 = gUC.checkIfStringMatchesInput("Hans", "4");
        assertFalse(res6);
    }
}