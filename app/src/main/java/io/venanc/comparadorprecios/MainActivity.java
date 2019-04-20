package io.venanc.comparadorprecios;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String[] arrayUnitWeights = {"gramos", "kilogramos", "onzas", "libras"};
    double[] arrayWeightsConversion = {1e-3,1,0.02834952,0.4535924}; // kilograms
    String[] arrayUnitVolumes = {"mililitros", "litros", "galón USA", "galón UK"};
    double[] arrayVolumesConversion = {1e-6,1e-3,3.7853e-3,4.5460e-3}; // m^3
    Map<String,Double> mapWeightsConversion;
    Map<String,Double> mapVolumesConversion;
    String[] arrayTypes = {"Peso", "Volumen"};


    private List<TableRow> rowsOptions;
    private TableLayout tableOptions;
    private int idSet;
    private float screenDensity;
    private List<Spinner> spinnerList;
    private List<EditText> quantitiesList;
    private List<TextView> signList;
    private List<EditText> pricesList;
    private Spinner spinnerTypes;
    private List<Integer> rowsToColor;
    private boolean haveEmpty;

    private AlphaAnimation buttonClicAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //INIT OF ALL VIEWS
        final TableRow firstRow = findViewById(R.id.firstRow);
        final TableRow secondRow = findViewById(R.id.secondRow);
        final EditText firstQuantity = findViewById(R.id.firstQuantity);
        final EditText secondQuantity = findViewById(R.id.secondQuantity);
        final Spinner spinnerTypes = findViewById(R.id.spinnerType);
        final Spinner firstSpinner = findViewById(R.id.firstSpinner);
        final Spinner secondSpinner = findViewById(R.id.secondSpinner);
        final TextView firstSign = findViewById(R.id.firstSign);
        final TextView secondSign = findViewById(R.id.secondSign);
        final EditText firstPrice = findViewById(R.id.firstPrice);
        final EditText secondPrice = findViewById(R.id.secondPrice);
        final ImageButton buttonPlus = findViewById(R.id.buttonPlus);
        final ImageButton buttonMinus = findViewById(R.id.buttonMinus);
        final Button compareButton = findViewById(R.id.compareButton);
        final Button cleanButton = findViewById(R.id.cleanButton);
        //INIT OF ALL VARIABLES
        mapWeightsConversion = new HashMap<String,Double>();
        mapVolumesConversion = new HashMap<String,Double>();
        for(int i = 0; i<arrayUnitWeights.length; i++) mapWeightsConversion.put(arrayUnitWeights[i], arrayWeightsConversion[i]);
        for(int i = 0; i<arrayUnitVolumes.length; i++) mapVolumesConversion.put(arrayUnitVolumes[i], arrayVolumesConversion[i]);
        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
        idSet = 0;
        buttonClicAnimation = new AlphaAnimation(1F, 0.8F);
        buttonClicAnimation.setDuration(30);
        //LISTS
        rowsOptions  = new ArrayList<>();
        firstRow.setId(idSet++);
        secondRow.setId(idSet++);
        rowsOptions.add(firstRow);
        rowsOptions.add(secondRow);
        quantitiesList = new ArrayList<EditText>();
        quantitiesList.add(firstQuantity);
        quantitiesList.add(secondQuantity);
        spinnerList = new ArrayList<Spinner>();
        spinnerList.add(firstSpinner);
        spinnerList.add(secondSpinner);
        signList = new ArrayList<TextView>();
        signList.add(firstSign);
        signList.add(secondSign);
        pricesList = new ArrayList<EditText>();
        pricesList.add(firstPrice);
        pricesList.add(secondPrice);
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
                v.startAnimation(buttonClicAnimation);
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
                v.startAnimation(buttonClicAnimation);
                eraseRow();
            }
        });
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClicAnimation);
                rowsToColor = getBestOption();
                if(rowsToColor==null){
                    Snackbar.make(findViewById(R.id.main_layout),"Debes de llenar los campos vacios",Toast.LENGTH_SHORT ).show();
                    return;
                }
                for(Integer rowToColor : rowsToColor) {
                    TableRow rowAux = findViewById(rowToColor);
                    rowAux.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    quantitiesList.get(rowToColor).setTextColor(Color.WHITE);
                    ((TextView) spinnerList.get(rowToColor).getChildAt(0)).setTextColor(Color.WHITE);
                    signList.get(rowToColor).setTextColor(Color.WHITE);
                    pricesList.get(rowToColor).setTextColor(Color.WHITE);
                }
            }
        });
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClicAnimation);
                rowsToColor = null;
                haveEmpty = false;
                for(EditText editTextAux : quantitiesList){
                    editTextAux.setText("");
                    editTextAux.setTextColor(Color.BLACK);
                    editTextAux.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
                for(EditText editTextAux : pricesList){
                    editTextAux.setText("");
                    editTextAux.setTextColor(Color.BLACK);
                    editTextAux.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                }
                for(Spinner spinnerAux : spinnerList){
                    spinnerAux.setSelection(0);
                    ((TextView)spinnerAux.getChildAt(0)).setTextColor(Color.BLACK);
                }
                for(TextView textViewAux : signList){
                    textViewAux.setTextColor(Color.BLACK);
                }
                for(TableRow tableRowAux : rowsOptions){
                    tableRowAux.setBackgroundColor(0);
                }
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
        TextInputEditText editQuantityAux = new TextInputEditText(this);
        Spinner spinnerAux = new Spinner(this);
        TextView textAux =  new TextView(this);
        TextInputEditText editPriceAux = new TextInputEditText(this);
        //Attributes of the whole row
        rowAux.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowAux.setGravity(Gravity.CENTER);
        rowAux.setId(idSet++);
        //Attributes of the first EditText of each row
        editQuantityAux.setHint("Cantidad");
        editQuantityAux.setGravity(Gravity.CENTER);
        editQuantityAux.setEms(4);
        editQuantityAux.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editQuantityAux.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        editQuantityAux.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
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
        signList.add(textAux);
        //Attributes of the second EditText of each row
        editPriceAux.setHint("Precio");
        editPriceAux.setGravity(Gravity.CENTER);
        editPriceAux.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editPriceAux.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        editPriceAux.setEms(4);
        editPriceAux.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
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
        rowsOptions.remove(--idSet);
        quantitiesList.remove(idSet);
        pricesList.remove(idSet);
        signList.remove(idSet);
        spinnerList.remove(idSet);
        tableOptions.removeView(findViewById(idSet));
        return true;
    }

    protected void populateAllSpinners(List<Spinner> spinnerList, String[] arrayToUse){
        for(Spinner spinnerOption : spinnerList){
            populateSpinner(spinnerOption, arrayToUse);
        }
    }

    protected List<Integer> getBestOption(){
        List<Integer> bestOptionIndex = new ArrayList<Integer>();
        double priceRelation;
        double bestOption = -1;
        haveEmpty = false;
        Spinner spinnerAuxType = findViewById(R.id.spinnerType);
        String selectedType = (String)(spinnerAuxType.getSelectedItem());
        for (int i = 0; i< idSet; i++){
            EditText quantityAux = quantitiesList.get(i);
            EditText priceAux = pricesList.get(i);
            if(quantityAux.getText().toString().isEmpty()){
                quantityAux.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                haveEmpty = true;
            }else{
                quantityAux.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
            if (priceAux.getText().toString().isEmpty()){
                priceAux.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                haveEmpty = true;
            }else{
                priceAux.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if(haveEmpty){
            return null;
        }


        for(int i = 0; i < idSet; i++){
            EditText quantityAux = quantitiesList.get(i);
            Spinner spinnerAux = spinnerList.get(i);
            EditText priceAux = pricesList.get(i);
            switch (selectedType){
                case "Peso":
                    priceRelation = Double.parseDouble(quantityAux.getText().toString()) * mapWeightsConversion.get(spinnerAux.getSelectedItem()) / Double.parseDouble(priceAux.getText().toString());
                    if(priceRelation==bestOption) bestOptionIndex.add(i);
                    else if(priceRelation>bestOption){
                        bestOptionIndex.clear();
                        bestOptionIndex.add(i);
                    }
                    bestOption = Math.max(bestOption,priceRelation);
                    break;
                case "Volumen":
                    priceRelation = Double.parseDouble(quantityAux.getText().toString()) * mapVolumesConversion.get(spinnerAux.getSelectedItem()) / Double.parseDouble(priceAux.getText().toString());
                    if(priceRelation==bestOption) bestOptionIndex.add(i);
                    else if(priceRelation>bestOption){
                        bestOptionIndex.clear();
                        bestOptionIndex.add(i);
                    }
                    bestOption = Math.max(bestOption,priceRelation);
                    break;
                default:
                    priceRelation = 9999999;
                    if(!bestOptionIndex.contains(1)) bestOptionIndex.add(1);
                    bestOption = 99999999;
            }
        }
        return bestOptionIndex;
    }
}
