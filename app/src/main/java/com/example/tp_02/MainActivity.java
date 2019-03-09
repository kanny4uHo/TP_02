package com.example.tp_02;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String ARRAY_KEY = "arrayKey";
    public static final String TAG = "MyLogs";
    RecyclerView recyclerView;
    Button addBtn;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.my_list);
        addBtn = findViewById(R.id.add_button);
        editText = findViewById(R.id.editText);

        ArrayList<Integer> integers;
        if (savedInstanceState != null && savedInstanceState.containsKey(ARRAY_KEY)) {
            integers = savedInstanceState.getIntegerArrayList(ARRAY_KEY);
        } else {
            integers = new ArrayList<>();
            fillList(integers);
        }

        recyclerView.setAdapter(new MyAdapter(integers));
        addBtn.setOnClickListener(this);
    }

    void fillList(List<Integer> toFill) {
        for (int i = 0; i < 100; i++) {
            toFill.add(i + 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                String text = editText.getText().toString();
                if (text.isEmpty())
                    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        int number = Integer.parseInt(text);
                        ((MyAdapter) recyclerView.getAdapter()).getDataList().add(number);
                        int lastPosition = ((MyAdapter) recyclerView.getAdapter()).getDataList().size();
                        recyclerView.getAdapter().notifyItemInserted(lastPosition);
                        recyclerView.scrollToPosition(lastPosition - 1);
                        editText.setText("");
                        Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Wrong format", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(ARRAY_KEY, new ArrayList<>(((MyAdapter) recyclerView.getAdapter()).getDataList()));
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text);
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick: " + v.getClass().getName() + " " + position);
            //TODO: normal listener for view
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<Integer> mData;

        MyAdapter(List<Integer> data) {
            super();
            this.mData = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View v = inflater.inflate(R.layout.list_element, viewGroup, false);
            return new MyViewHolder(v);
        }

        List<Integer> getDataList() {
            if (mData != null)
                return mData;
            else
                return new ArrayList<>();
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            int number = mData.get(position);
            myViewHolder.mTextView.setText(String.valueOf(number));

            if (isOdd(number)) {
                myViewHolder.mTextView.setTextColor(getResources().getColor(R.color.myBlue));
                myViewHolder.mTextView.setBackgroundColor(getResources().getColor(R.color.myLightBlue));
            } else {
                myViewHolder.mTextView.setTextColor(getResources().getColor(R.color.myRed));
                myViewHolder.mTextView.setBackgroundColor(getResources().getColor(R.color.myLightRed));
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        /*Check if number is odd*/
        boolean isOdd(int number) {
            return number % 2 == 1;
        }
    }
}
