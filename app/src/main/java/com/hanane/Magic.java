package com.hanane;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.Toast;


public class Magic extends Activity {
    public static final String LEVEL = "LEVEL";
    private static final String TAG = "MagicActivity";

    private HashMap<String, EditText> matrix = new HashMap<>();
    private HashMap<String, TextView> somme = new HashMap<>();
    private int niveau, solution[][] = getSolution();
    private long timeWhenStopped, timeWhenpause, elapsedMillis = 0;
    private EditText editHelp;
    private Button btnNewParty, btnSubmit, btnHelp;
    private Chronometer chronometer;
    private boolean newGameState;
    private String magic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            magic = savedInstanceState.getString("Magic");
        }
        setContentView(R.layout.activity_magic);
        initialize();
        checkFieldsForEmptyValues();
    }

    private void initialize(){
        Log.i(TAG, "initialize");
        // Champs de texts
        matrix.put("00", (EditText) findViewById(R.id.x00));
        matrix.put("01", (EditText) findViewById(R.id.x02));
        matrix.put("02", (EditText) findViewById(R.id.x04));
        matrix.put("10", (EditText) findViewById(R.id.x20));
        matrix.put("11", (EditText) findViewById(R.id.x22));
        matrix.put("12", (EditText) findViewById(R.id.x24));
        matrix.put("20", (EditText) findViewById(R.id.x40));
        matrix.put("21", (EditText) findViewById(R.id.x42));
        matrix.put("22", (EditText) findViewById(R.id.x44));

        // Somme
        somme.put("03", (TextView) findViewById(R.id.x06));
        somme.put("13", (TextView) findViewById(R.id.x26));
        somme.put("23", (TextView) findViewById(R.id.x46));
        somme.put("30", (TextView) findViewById(R.id.x60));
        somme.put("31", (TextView) findViewById(R.id.x62));
        somme.put("32", (TextView) findViewById(R.id.x64));

        //Les boutons
        btnHelp = findViewById(R.id.help);
        btnSubmit = findViewById(R.id.Submit);
        btnNewParty =findViewById(R.id.New);

        //Chronomètre
        chronometer = (Chronometer) findViewById(R.id.chronometer1);
        timeWhenStopped = 0;
        chronometer.start();
        niveau = getIntent().getIntExtra(Magic.LEVEL,0);
        game(solution, this.niveau);
        for (Map.Entry<String, EditText> E : this.matrix.entrySet()) {
            EditText Case = (EditText) E.getValue();
            Case.addTextChangedListener(MyTextwatcher);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("magic", solution);
        timeWhenStopped = chronometer.getBase();
        savedInstanceState.putLong("pausechroo",timeWhenpause);// quand on met en pause le chrono
        savedInstanceState.putLong("chrono",timeWhenStopped);
        savedInstanceState.putString("m", magic);
        savedInstanceState.putBoolean("NewgameSate", btnNewParty.isEnabled());
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.solution = (int[][]) savedInstanceState.getSerializable("magic");
            this.timeWhenStopped = (long) savedInstanceState.getLong("chrono");
            this.chronometer.setBase(timeWhenStopped);
            this.timeWhenpause = (long) savedInstanceState.getLong("pausechroo");
            magic = (String) savedInstanceState.getString("m");
            game(this.solution, this.niveau);
            newGameState = (boolean) savedInstanceState.getBoolean("NewgameSate");
            btnNewParty.setEnabled(newGameState);
        }
    }

    @Override
    public void onPause(){
        Log.i(TAG, "onPause");
        super.onPause();
        timeWhenpause = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
    }
    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        chronometer.setBase(SystemClock.elapsedRealtime() +  timeWhenpause);
        chronometer.start();
    }

    private void game(int n[][] , int level) {
        Log.i(TAG, "game");

        somme.get("03").setText(Integer.toString(n[0][0] + n[0][1] + n[0][2]));
        somme.get("13").setText(Integer.toString(n[1][0] + n[1][1] + n[1][2]));
        somme.get("23").setText(Integer.toString(n[2][0] + n[2][1] + n[2][2]));
        somme.get("30").setText(Integer.toString(n[0][0] + n[1][0] + n[2][0]));
        somme.get("31").setText(Integer.toString(n[0][1] + n[1][1] + n[2][1]));
        somme.get("32").setText(Integer.toString(n[0][2] + n[1][2] + n[2][2]));
        for (int i = 0; i < (9-3*level) ; i++){
            help();
        }
        btnNewParty.setEnabled(false);
    }

    private void newGame(View view){
        Log.i(TAG, "newGame");

        solution = getSolution();
        clearAllaDigits();
        game(this.solution, this.niveau);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
    public void helpMe(View view){
        Log.i(TAG, "helpMe");
        help();
    }
    public void submitMe(View view){
            Log.i(TAG, "submitMe");
            int a[][] = new int[4][4];
            /////// debut de la matrice des entier entre 1 et 9
            a[0][0] = Integer.parseInt(matrix.get("00").getText().toString());
            a[0][1] = Integer.parseInt(matrix.get("01").getText().toString());
            a[0][2] = Integer.parseInt(matrix.get("02").getText().toString());
            a[1][0] = Integer.parseInt(matrix.get("10").getText().toString());
            a[1][1] = Integer.parseInt(matrix.get("11").getText().toString());
            a[1][2] = Integer.parseInt(matrix.get("12").getText().toString());
            a[2][0] = Integer.parseInt(matrix.get("20").getText().toString());
            a[2][1] = Integer.parseInt(matrix.get("21").getText().toString());
            a[2][2] = Integer.parseInt(matrix.get("22").getText().toString());
            //////////////// // les sommes
            if (condition(a)) {
                congratulation();
                elapsedMillis = (SystemClock.elapsedRealtime() - chronometer.getBase()) % 1000;
                Toast.makeText(this, elapsedMillis+"", Toast.LENGTH_SHORT).show();
                btnNewParty.setEnabled(true);
                chronometer.stop();
            } else {
                gameOver( );
            }
    }
    public void congratulation(){
        Log.i(TAG, "congratulation");
        Toast.makeText(this, "congratulation", Toast.LENGTH_SHORT).show();
    }
    public void gameOver() {
        Log.i(TAG, "gameOver");
        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
    }

    public int[][] getSolution() {
        Log.i(TAG, "getSolution");

        Integer[] digit = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        ArrayList<Integer> Alldigit = new ArrayList<>(digit.length);
        Alldigit.addAll(Arrays.asList(digit));
        int[][] matrice = new int[3][3];
        for (int i = 0; i < matrice.length; i++)
            for (int j = 0; j < matrice[i].length; j++) {
                int index = rand.nextInt(Alldigit.size());
                matrice[i][j] = Alldigit.get(index);
                Alldigit.remove(index);
            }
        return matrice;
    }

    public void help(){
        Log.i(TAG, "btnHelp");
        for (Map.Entry<String, EditText> E : this.matrix.entrySet()) {
            EditText Case = (EditText) E.getValue();
            int line = Character.getNumericValue(E.getKey().charAt(0));
            int col = Character.getNumericValue(E.getKey().charAt(1));
            if (Case.getText().toString().equals("")) {
                Case.setText(Integer.toString(this.solution[line][col]));
                Case.setKeyListener(null);
                Case.setTextColor(getResources().getColor(R.color.coloredit));
                editHelp = Case;
                break;
            }
        }
    }
    public void clearAllaDigits(){
        Log.i(TAG, "clearAllaDigits");
        for (Map.Entry<String, EditText> E : this.matrix.entrySet()) {
            EditText Case = (EditText) E.getValue();
            Case.setText("");
            }
    }
    public boolean condition(int a[][]) {
        Log.i(TAG, "condition");
        return ((   a[0][0] + a[0][1] + a[0][2] == Integer.parseInt(somme.get("03").getText().toString()))
                && (a[1][0] + a[1][1] + a[1][2] == Integer.parseInt(somme.get("13").getText().toString()))
                && (a[2][0] + a[2][1] + a[2][2] == Integer.parseInt(somme.get("23").getText().toString()))
                && (a[0][0] + a[1][0] + a[2][0] == Integer.parseInt(somme.get("30").getText().toString()))
                && (a[0][1] + a[1][1] + a[2][1] == Integer.parseInt(somme.get("31").getText().toString()))
                && (a[0][2] + a[1][2] + a[2][2] == Integer.parseInt(somme.get("32").getText().toString())));
    }

    private void checkFieldsForEmptyValues(){
        Log.i(TAG, "checkFieldsForEmptyValues");
        for (Map.Entry<String, EditText> E : matrix.entrySet()){
            EditText Case = (EditText) E.getValue();
            if ("".equals(Case.getText().toString())){
                btnSubmit.setEnabled(false);
            }else{
                btnSubmit.setEnabled(true);
            }
        }
    }
    private TextWatcher MyTextwatcher =  new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i(TAG, "beforeTextChanged "+s.toString()+" "+start+" "+count+" "+after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG, "onTextChanged "+s.toString()+" "+start+" "+before+" "+count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i(TAG, "afterTextChanged");
            checkFieldsForEmptyValues();
        }
    };

}
