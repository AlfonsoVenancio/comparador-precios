package io.venanc.comparadorprecios;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    String[] arrayUnitWeights = {"gramos", "kilogramos"};
    String[] arrayUnitVolumes = {"mililitros", "litros"};
    String[] arrayTypes = {"Peso", "Volumen", "Por unidad"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner spinnerTypes = findViewById(R.id.spinnerType);
        final Spinner spinnerUnits = findViewById(R.id.spinnerUnit);

        //Poblate of the types spinner
        poblateSpinner(spinnerTypes,arrayTypes);

        //Types Spinners on selected item
        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String)spinnerTypes.getSelectedItem()){
                    case "Peso":
                        poblateSpinner(spinnerUnits, arrayUnitWeights);
                        spinnerUnits.setEnabled(true);
                        break;
                    case "Volumen":
                        poblateSpinner(spinnerUnits, arrayUnitVolumes);
                        spinnerUnits.setEnabled(true);
                        break;
                    default:
                        Snackbar.make(findViewById(R.id.main_layout),"Todavia no esta soportado :(",Snackbar.LENGTH_SHORT).show();
                        spinnerUnits.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Snackbar.make(findViewById(R.id.main_layout),"Nada seleccionado",Snackbar.LENGTH_SHORT);
            }
        });
    }

    protected void poblateSpinner(Spinner spinnerToPoblate, String[] arrayToUse){
        List<String> listAux = Arrays.asList(arrayToUse);
        ArrayAdapter<String> adapterAux = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAux);
        adapterAux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToPoblate.setAdapter(adapterAux);
    }


}
