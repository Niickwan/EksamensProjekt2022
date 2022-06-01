package com.jmmnt.UseCase;

import static org.junit.Assert.*;

import android.app.Activity;
import android.widget.EditText;

import com.jmmnt.R;

import org.junit.Test;

public class GeneralUseCaseTest extends Activity {

    GeneralUseCase gUC = GeneralUseCase.getInstance();

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

    @Test
    public void testIsInputMatchingTrue() {
        boolean test = gUC.isInputMatching("123","123");
        assertTrue(test);
    }

    @Test
    public void testIsInputMatchingFalse() {
        boolean test = gUC.isInputMatching("John","123");
        assertFalse(test);
    }

    @Test
    public void testIsFieldsEmpty() { //TODO FROSÆT HER
        EditText firstname = findViewById(R.id.editFirstName_et);
        EditText surname = findViewById(R.id.editSurname_et);
        EditText phone = findViewById(R.id.editPhoneNumber_et);

        firstname.setText("Arne");
        surname.setText("John");
        phone.setText("44332211");

        EditText[] editText = new EditText[3];
        editText[0] = firstname;
        editText[1] = surname;
        editText[2] = phone;

        boolean test = gUC.isFieldsEmpty(editText);

        assertFalse(test);

    }

    @Test
    public void round() {
        double d1 = 2.334636;
        assertEquals(2.3, gUC.round(d1,1), 0.005);
        assertEquals(2.33, gUC.round(d1,2), 0.005);
        assertEquals(2.335, gUC.round(d1,3), 0.005);
        assertEquals(2.3346, gUC.round(d1,4), 0.005);
    }
}