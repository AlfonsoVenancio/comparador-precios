package io.venanc.comparadorprecios;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    String[] arrayUnitWeights = {"gramos", "kilogramos"};
    String[] arrayUnitVolumes = {"mililitros", "litros"};
    String[] arrayTypes = {"Peso", "Volumen", "Por unidad"};
    private List<TableRow> rowsOptions;
    private TableLayout tableOptions;
    private int idSet;
    private float screenDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner spinnerTypes = findViewById(R.id.spinnerType);
        final Spinner spinnerUnits = findViewById(R.id.spinnerUnit);
        final Spinner spinnerTest = findViewById(R.id.spinnerPrueba);
        final Button buttonPlus = findViewById(R.id.button);
        final Button buttonMinus = findViewById(R.id.button2);
        tableOptions = findViewById(R.id.tableOptions);
        rowsOptions  = new ArrayList<>();
        idSet = 1;
        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
        final TableRow firstRow = findViewById(R.id.firstRow);
        final TableRow secondRow = findViewById(R.id.secondRow);
        firstRow.setId(idSet++);
        secondRow.setId(idSet++);
        rowsOptions.add(firstRow);
        rowsOptions.add(secondRow);
        //Poblate of the types spinner
        poblateSpinner(spinnerTypes,arrayTypes);
        //Types Spinners on selected item
        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String)spinnerTypes.getSelectedItem()){
                    case "Peso":
                        poblateSpinner(spinnerUnits, arrayUnitWeights);
                        poblateSpinner(spinnerTest,arrayUnitWeights);
                        spinnerUnits.setEnabled(true);
                        break;
                    case "Volumen":
                        poblateSpinner(spinnerUnits, arrayUnitVolumes);
                        poblateSpinner(spinnerTest,arrayUnitVolumes);
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

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraseRow();
            }
        });

    }

    protected void poblateSpinner(Spinner spinnerToPoblate, String[] arrayToUse){
        List<String> listAux = Arrays.asList(arrayToUse);
        ArrayAdapter<String> adapterAux = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAux);
        adapterAux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToPoblate.setAdapter(adapterAux);
    }

    protected void addRow(){
        TableRow rowAux = new TableRow(this);
        rowAux.setId(idSet);
        EditText editAux = new EditText(this);
        Spinner spinnerAux = new Spinner(this);
        TextView textAux =  new TextView(this);
        EditText editAux2 = new EditText(this);
        textAux.setText(String.valueOf(idSet++));

        //Attributes of the first EditText of each row
        editAux.setText("1");
        editAux.setGravity(Gravity.CENTER);
        editAux.setEms(2);
        editAux.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editAux.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(4)
        });

        //Attributes of the Spinner of each row
        spinnerAux.setPadding((int)(10*screenDensity),0,0,0);
        spinnerAux.setGravity(Gravity.CENTER);
        //spinnerAux.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));

        //Attributes of the TextView of each row
        textAux.setText("$");
        //textAux.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        textAux.setPadding((int)(10*screenDensity),0,0,0);
        textAux.setTextSize(15);
        textAux.setTextColor(Color.BLACK);

        //Attributes of the second EditText of each row
        editAux2.setText("Precio");
        editAux2.setGravity(Gravity.CENTER);
        editAux2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editAux2.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(9)
        });



        rowAux.setGravity(Gravity.CENTER);
        rowAux.addView(editAux);

        rowAux.addView(spinnerAux);
        rowAux.addView(textAux);
        rowAux.addView(editAux2);

        rowsOptions.add(rowAux);
        tableOptions.addView(rowAux);
    }

    protected boolean eraseRow(){
        if(rowsOptions.size() <= 2){
            Snackbar.make(findViewById(R.id.main_layout),"Debes de tener al menos dos opciones",Toast.LENGTH_SHORT ).show();
            return false;
        }
        rowsOptions.remove(rowsOptions.size()-1);
        tableOptions.removeView(findViewById(--idSet));
        return true;
    }

}
