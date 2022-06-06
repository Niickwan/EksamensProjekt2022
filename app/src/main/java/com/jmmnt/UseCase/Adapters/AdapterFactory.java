package com.jmmnt.UseCase.Adapters;

import android.content.Context;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.Entities.TestingRCD;
import java.util.List;

public class AdapterFactory {

    /**
     *
     * @param tag      - for identifying a certain object of a specific adapter class.
     * @param dataList - a list containing entities such as Questions, Assignments etc.
     * @param context  -
     * @return         -  returns a new object of an adapter class.
     */
    public Object setAdapterType(String tag, List<?> dataList, Context context) {
        if (dataList == null || tag.isEmpty())
            return null;
        if (tag.equalsIgnoreCase("question"))
            return new QuestionsViewAdapter((List<Questions>) dataList);
        if (tag.equalsIgnoreCase("circuitDetails"))
            return new CircuitDetailsViewAdapter((List<CircuitDetails>) dataList);
        if (tag.equalsIgnoreCase("RCD"))
            return new RCDViewAdapter((List<TestingRCD>) dataList);
        if (tag.equalsIgnoreCase("ShortCircuitCurrent"))
            return new ShortCircuitCurrentAndVoltageDropViewAdapter((List<ShortCircuitCurrentAndVoltageDrop>) dataList);
        if (tag.equalsIgnoreCase("Assignment"))
            return new OrderViewAdapter((List<Assignment>) dataList, context);
        return null;
    }

    /**
     *
     * @param tag - for identifying a certain object of a class.
     * @return - returns a new object of a closs
     */
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
        if (tag.equalsIgnoreCase("Assignment"))
            return new Assignment();
        return null;
    }

}



