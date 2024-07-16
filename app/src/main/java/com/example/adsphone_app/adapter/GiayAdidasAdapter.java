package com.example.adsphone_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adsphone_app.R;
import com.example.adsphone_app.activity.ChiTietActivity;
import com.example.adsphone_app.interfece.ItemClickListener;
import com.example.adsphone_app.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class GiayAdidasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private  static final int VIEW_TYPY_DATA = 0;
    private  static final int VIEW_TYPY_LOADING = 1;

    public GiayAdidasAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPY_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adidas, parent, false);
            return new ViewHolder(view);
        }else {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return  new loadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            SanPhamMoi sanPhamMoi = array.get(position);
            viewHolder.tensp.setText(sanPhamMoi.getTensp().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            viewHolder.giasp.setText(decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+ " VND");
            viewHolder.mota.setText(sanPhamMoi.getMota());
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(viewHolder.hinhanh);
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick){
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet",sanPhamMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            loadingViewHolder loadingViewHolder = (loadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPY_LOADING:VIEW_TYPY_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class loadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public loadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progreebar);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp, giasp, mota;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.item_adidas_ten);
            giasp = itemView.findViewById(R.id.item_adidas_gia);
            mota = itemView.findViewById(R.id.item_adidas_mota);
            hinhanh = itemView.findViewById(R.id.item_adidas_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

}
