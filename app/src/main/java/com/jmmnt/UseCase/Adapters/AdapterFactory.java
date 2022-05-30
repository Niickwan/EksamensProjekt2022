package com.jmmnt.UseCase.Adapters;

import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.TestingRCD;

import java.util.List;

public class AdapterFactory {

    public Object setAdapterType(String tag, List<?> dataList) {

        if (dataList == null || tag.isEmpty())
            return null;
        if (tag.equalsIgnoreCase("question"))
            return new QuestionsViewAdapter((List<Questions>) dataList);
        if (tag.equalsIgnoreCase("circuitDetails"))
            return new CircuitDetailsViewAdapter((List<CircuitDetails>) dataList);
        if (tag.equalsIgnoreCase("RCD"))
            return new RCDViewAdapter((List<TestingRCD>)dataList);
        if (tag.equalsIgnoreCase("ShortCircuitCurrent"))
            return new ShortCircuitCurrentAndVoltageDropViewAdapter((List<ShortCircuitCurrentAndVoltageDrop>) dataList);
        return null;
    }

    public Object setObjectType(String tag) {
        if (tag.isEmpty())
            return null;
        if (tag.equalsIgnoreCase("question"))
            return new Questions();
        if (tag.equalsIgnoreCase("circuitDetails"))
            return new CircuitDetails();
        if (tag.equalsIgnoreCase("RCD"))
            return new TestingRCD();
        if (tag.equalsIgnoreCase("ShortCircuitCurrent"))
            return new ShortCircuitCurrentAndVoltageDrop();
        return null;
    }
}
