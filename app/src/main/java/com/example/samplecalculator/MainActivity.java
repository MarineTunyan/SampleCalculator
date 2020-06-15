package com.example.samplecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class MainActivity extends AppCompatActivity {
    private TextView result, input;
    private boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        input = findViewById(R.id.input);
        input.setText("0");
    }

    public void onClick(View v) {
        if (firstClick) {
            input.setText("");
            firstClick = false;
        }

        Button button = (Button) v;
        String temporary = "";
        if (button.getText().toString().equals("•")) {
            if (input.getText().toString().length() > 0) {
                temporary = input.getText().toString() + '.';
            }
        } else {
            temporary = input.getText().toString() + button.getText().toString();
        }
        input.setText(temporary);
    }

    public void clear(View v) {
        input.setText("0");
        firstClick = true;
        result.setText("");
    }

    public void calculate(View v) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino"); //libraryname
        String currentText = input.getText().toString().trim().replace('X', '*')
                .replace('÷', '/').replace("%", "/100").replace("√", "sqrt(");
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < currentText.length(); i++) {
            resultBuilder.append(currentText.charAt(i));
        }
        if (currentText.contains("sqrt(")) {
            resultBuilder.delete(0, resultBuilder.length());
            int pos = currentText.indexOf("sqrt(");
            int nextPosPlus = currentText.indexOf("+", pos);
            int nextPosMinus = currentText.indexOf("-");
            int nextPosDivide = currentText.indexOf("/");
            int nextPosMultiply = currentText.indexOf("*");
            int min = currentText.length() - 1; //amenamotik nshany
            if (min > nextPosPlus && nextPosPlus != -1) {
                min = nextPosPlus;
            }
            if (min > nextPosMinus && nextPosMinus != -1) {
                min = nextPosMinus;
            }
            if (min > nextPosDivide && nextPosDivide != -1) {
                min = nextPosDivide;
            }
            if (min > nextPosMultiply && nextPosMultiply != -1) {
                min = nextPosMultiply;
            }

            for (int i = 0; i < currentText.length(); i++) {
                if (i == pos) {
                    if (min == currentText.length() - 1) {
                        resultBuilder.append(Math.sqrt(Double.parseDouble(currentText.substring(pos + 5, min + 1))));
                        i = min;

                    } else {
                        resultBuilder.append(Math.sqrt(Double.parseDouble(currentText.substring(pos + 5, min))));
                        i = min - 1;
                    }
                } else {
                    resultBuilder.append(currentText.charAt(i));
                }
            }

        }
        double calculateResult = 0;
        currentText = resultBuilder.toString();
        try {
            calculateResult = (double) engine.eval(currentText);
            if (calculateResult % 1 == 0)
                result.setText(String.valueOf((int) calculateResult));
            else result.setText(String.valueOf(calculateResult));

        } catch (
                Exception e) {

            Toast.makeText(this, "Exception Raised", Toast.LENGTH_SHORT).show();
        }
    }
}
