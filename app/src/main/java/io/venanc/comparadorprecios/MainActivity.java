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
import android.widget.ImageButton;
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
    private List<Spinner> spinnerList;
    private List<EditText> quantitiesList;
    private List<EditText> pricesList;
    private Spinner spinnerTypes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //INIT OF ALL VIEWS
        final TableRow firstRow = findViewById(R.id.firstRow);
        final TableRow secondRow = findViewById(R.id.secondRow);
        final Spinner spinnerTypes = findViewById(R.id.spinnerType);
        final Spinner firstSpinner = findViewById(R.id.firstSpinner);
        final Spinner secondSpinner = findViewById(R.id.secondSpinner);
        final ImageButton buttonPlus = findViewById(R.id.buttonPlus);
        final ImageButton buttonMinus = findViewById(R.id.buttonMinus);
        //INIT OF ALL VARIABLES
        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
        idSet = 1;
        //LISTS
        rowsOptions  = new ArrayList<>();
        firstRow.setId(idSet++);
        secondRow.setId(idSet++);
        rowsOptions.add(firstRow);
        rowsOptions.add(secondRow);
        quantitiesList = new ArrayList<EditText>();
        spinnerList = new ArrayList<Spinner>();
        spinnerList.add(firstSpinner);
        spinnerList.add(secondSpinner);
        pricesList = new ArrayList<EditText>();
        tableOptions = findViewById(R.id.tableOptions);
        populateSpinner(spinnerTypes,arrayTypes);
        spinnerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String)spinnerTypes.getSelectedItem()){
                    case "Peso":
                        populateAllSpinners(spinnerList, arrayUnitWeights);
                        break;
                    case "Volumen":
                        populateAllSpinners(spinnerList, arrayUnitVolumes);
                        break;
                    default:
                        Snackbar.make(findViewById(R.id.main_layout),"Todavia no esta soportado :(",Snackbar.LENGTH_SHORT).show();
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
                Spinner spinnerTopopulate = spinnerList.get(spinnerList.size()-1);
                switch ((String)spinnerTypes.getSelectedItem()){
                    case "Peso":
                        populateSpinner(spinnerTopopulate,arrayUnitWeights);
                        break;
                    case "Volumen":
                        populateSpinner(spinnerTopopulate,arrayUnitVolumes);
                        break;
                    default:
                        Snackbar.make(findViewById(R.id.main_layout),"Todavia no esta soportado :(",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraseRow();
            }
        });
    }

    protected void populateSpinner(Spinner spinnerToPopulate, String[] arrayToUse){
        List<String> listAux = Arrays.asList(arrayToUse);
        ArrayAdapter<String> adapterAux = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAux);
        adapterAux.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToPopulate.setAdapter(adapterAux);
    }

    protected void addRow(){
        TableRow rowAux = new TableRow(this);
        EditText editQuantityAux = new EditText(this);
        Spinner spinnerAux = new Spinner(this);
        TextView textAux =  new TextView(this);
        EditText editPriceAux = new EditText(this);
        //Attributes of the whole row
        rowAux.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowAux.setGravity(Gravity.CENTER);
        rowAux.setId(idSet++);
        //Attributes of the first EditText of each row
        editQuantityAux.setText("1");
        editQuantityAux.setGravity(Gravity.CENTER);
        editQuantityAux.setEms(2);
        editQuantityAux.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editQuantityAux.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        quantitiesList.add(editQuantityAux);
        //Attributes of the Spinner of each row
        spinnerAux.setPadding((int)(10*screenDensity),0,0,0);
        spinnerAux.setGravity(Gravity.CENTER);
        spinnerList.add(spinnerAux);
        //Attributes of the TextView of each row
        textAux.setText("$");
        textAux.setPadding((int)(10*screenDensity),0,0,0);
        textAux.setTextSize(15);
        textAux.setTextColor(Color.BLACK);
        //Attributes of the second EditText of each row
        editPriceAux.setText("Precio");
        editPriceAux.setGravity(Gravity.CENTER);
        editPriceAux.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editPriceAux.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        pricesList.add(editPriceAux);
        rowAux.addView(editQuantityAux);
        rowAux.addView(spinnerAux);
        rowAux.addView(textAux);
        rowAux.addView(editPriceAux);
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

    protected void populateAllSpinners(List<Spinner> spinnerList, String[] arrayToUse){
        for(Spinner spinnerOption : spinnerList){
            populateSpinner(spinnerOption, arrayToUse);
        }
    }

}
