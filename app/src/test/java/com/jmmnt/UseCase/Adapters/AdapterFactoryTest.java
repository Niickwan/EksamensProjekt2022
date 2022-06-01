package com.jmmnt.UseCase.Adapters;

import static org.junit.Assert.*;

import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.Entities.TestingRCD;

import org.junit.Test;

public class AdapterFactoryTest {

    @Test
    public void setObjectType() {
        AdapterFactory af = new AdapterFactory();
        assertTrue(af.setObjectType("Assignment") instanceof Assignment);

        assertNull(af.setObjectType(""));
    }
}