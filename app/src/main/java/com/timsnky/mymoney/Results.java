package com.timsnky.mymoney;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Timsnky on 3/3/2015.
 */
public class Results extends Fragment {
    private ArrayList<Transaction> transactions = Expenses.transactions;
    private SimpleDateFormat dateFormatter;
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transactions, container, false);
        showResults();
        setupTransactionsView(rootView);
        return rootView;
    }

    private void showResults(){
        int i = 0;
        Log.d("My Money", "Size" + transactions.size());
        while (i < transactions.size()){
            Log.d("My Money", transactions.get(i).showTransaction());
            i++;
        }
    }

    private void setupTransactionsView(View rootView){
        int tranIndex = 0;

        dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.US);

        while(tranIndex < transactions.size()){
            Transaction transaction = transactions.get(tranIndex);
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.transactionsLayout);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View parent = inflater.inflate(R.layout.singletransaction, null);

            TextView dateTextView = (TextView) parent.findViewById(R.id.dateTextView);
            date = new Date(transaction.getTranDate());
            dateTextView.setText(dateFormatter.format(date));

            TextView detailsTextView = (TextView) parent.findViewById(R.id.detailsTextView);
            detailsTextView.setText(transaction.getTranDescription());

            TextView accountTextView = (TextView) parent.findViewById(R.id.accountTextView);
            accountTextView.setText(Integer.toString(transaction.getTranAccount()));

            if(transaction.getTranCategory() % 10 == 0){
                TextView debitTextView = (TextView) parent.findViewById(R.id.debitTextView);
                debitTextView.setText(Float.toString(transaction.getTranAmount()));
            }else{
                TextView creditTextView = (TextView) parent.findViewById(R.id.creditTextView);
                creditTextView.setText(Float.toString(transaction.getTranAmount()));
            }

            layout.addView(parent);

            tranIndex ++;
        }
    }
}
