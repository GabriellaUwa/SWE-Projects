package edu.qc.seclass.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class TipCalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        final EditText partySize = findViewById(R.id.partySizeValue);

        final EditText amount = findViewById(R.id.checkAmountValue);

        final Calculate calculate = new Calculate(); // abstracted calculator class to do computation

        final Button button = findViewById(R.id.buttonCompute);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String party = partySize.getText().toString();
                String amt = amount.getText().toString();

                if (amt.matches("") || party.matches("")){
                    Toast.makeText(v.getContext(), "Input cannot be empty!", Toast.LENGTH_SHORT).show();
                }else{

                int pSize = Integer.parseInt(party.toString());
                int am = Integer.parseInt(amt.toString());

                    calculate.calculate(am, pSize); //assigns cost per person, void method

                    final TextView fifteen = (TextView) findViewById(R.id.fifteenPercentTipValue);
                    fifteen.setText(Integer.toString(calculate.getFifteen()));

                    final TextView twenty = (TextView) findViewById(R.id.twentyPercentTipValue);
                    twenty.setText(Integer.toString(calculate.getTwenty()));

                    final TextView twentyFive = (TextView) findViewById(R.id.twentyfivePercentTipValue);
                    twentyFive.setText(Integer.toString(calculate.getTwentyfive()));

                    final TextView fifteenTotal = (TextView) findViewById(R.id.fifteenPercentTotalValue);
                    fifteenTotal.setText(Integer.toString(calculate.getTotalFifteen()));

                    final TextView twentyTotal = (TextView) findViewById(R.id.twentyPercentTotalValue);
                    twentyTotal.setText(Integer.toString(calculate.getTotalTwenty()));

                    final TextView twentyFiveTotal = (TextView) findViewById(R.id.twentyfivePercentTotalValue);
                    twentyFiveTotal.setText(Integer.toString(calculate.getTotalTwentyFive()));
                }
            }
        });
    }
}
