package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    TextView displayInput, displayOutput;
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayInput = findViewById(R.id.display_input);
        displayOutput = findViewById(R.id.display_output);

        int[] btnIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9, R.id.button_plus, R.id.button_minus,
                R.id.button_multiply, R.id.button_division, R.id.button_dot,
                R.id.button_open_bracket, R.id.button_close_bracket
        };

        for (int id : btnIds) {
            MaterialButton btn = findViewById(id);
            btn.setOnClickListener(view -> {
                input += btn.getText().toString();
                displayInput.setText(input);
            });
        }

        findViewById(R.id.button_ac).setOnClickListener(view -> {
            input = "";
            displayInput.setText("0");
            displayOutput.setText("0");
        });

        findViewById(R.id.button_c).setOnClickListener(view -> {
            if (!input.isEmpty()) {
                input = input.substring(0, input.length() - 1);
                displayInput.setText(input);
            }
        });

        findViewById(R.id.button_equal).setOnClickListener(view -> {
            try {
                double result = eval(input);
                displayOutput.setText(String.valueOf(result));
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });
        findViewById(R.id.button_sin).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                double result = Math.sin(Math.toRadians(val));
                displayOutput.setText("sin(" + val + ") = " + result);
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });

        findViewById(R.id.button_cos).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                double result = Math.cos(Math.toRadians(val));
                displayOutput.setText("cos(" + val + ") = " + result);
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });
        findViewById(R.id.button_square).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                double result = val * val;
                displayOutput.setText(val + "² = " + result);
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });
        findViewById(R.id.button_mode).setOnClickListener(view -> {
            try {
                if (!input.isEmpty()) {
                    if (input.startsWith("-")) {
                        input = input.substring(1); // remove the minus sign
                    } else {
                        input = "-" + input; // add minus sign
                    }
                    displayInput.setText(input);
                }
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });


        findViewById(R.id.button_tan).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                double result = Math.tan(Math.toRadians(val));
                displayOutput.setText("tan(" + val + ") = " + result);
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });

        findViewById(R.id.button_sqrt).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                double result = Math.sqrt(val);
                displayOutput.setText("√(" + val + ") = " + result);
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });
        findViewById(R.id.button_log).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                if (val > 0) {
                    double result = Math.log10(val);
                    displayOutput.setText("log(" + val + ") = " + result);
                } else {
                    displayOutput.setText("Math Error");
                }
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });
        findViewById(R.id.button_LN).setOnClickListener(view -> {
            try {
                double val = Double.parseDouble(displayInput.getText().toString());
                if (val > 0) {
                    double result = Math.log(val);  // Natural log (base e)
                    displayOutput.setText("ln(" + val + ") = " + result);
                } else {
                    displayOutput.setText("Math Error");
                }
            } catch (Exception e) {
                displayOutput.setText("Error");
            }
        });


    }


    // Simple expression evaluator (supports +, -, *, /)
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while(true) {
                    if      (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while(true) {
                    if      (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                return x;
            }
        }.parse();
    }
}
