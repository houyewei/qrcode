package com.houyewei.cameraxapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.BarcodeViewHolder> implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    class BarcodeViewHolder extends  RecyclerView.ViewHolder {
        private final TextView barcodeTextView;
        private LinearLayout linearLayout;
        private BarcodeViewHolder(View itemView) {
            super(itemView);
            barcodeTextView = itemView.findViewById(R.id.text_view_head);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }

    private final LayoutInflater mInflater;
    private List<BarcodeModel> mBarcodes;

    private Context context;

    private BarcodeViewModel viewModel;

    BarcodeAdapter(BarcodeViewModel viewModel, Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public BarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.barcode_item, parent, false);

        return new BarcodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeViewHolder holder, int position) {
        if(mBarcodes != null) {
            BarcodeModel current = mBarcodes.get(position);

            holder.barcodeTextView.setText(String.valueOf(current.getBarcode()));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity)context;
                    //BottomNavigationView navView = activity.findViewById(R.id.nav_view);
                    SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    //editor.putInt(activity.getString(R.string.current_position), position);
                    editor.commit();
                    //navView.setSelectedItemId(R.id.navigation_home);
                }
            });

            holder.linearLayout.setOnLongClickListener(this);

            holder.linearLayout.setOnGenericMotionListener(new View.OnGenericMotionListener() {
                @Override
                public boolean onGenericMotion(View v, MotionEvent event) {
                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if(mBarcodes != null)
            return mBarcodes.size();
        return 0;
    }
}
