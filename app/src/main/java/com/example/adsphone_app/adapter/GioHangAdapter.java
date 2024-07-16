package com.example.adsphone_app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adsphone_app.EventBus.TinhTongEvent;
import com.example.adsphone_app.R;
import com.example.adsphone_app.interfece.IImageClickListener;
import com.example.adsphone_app.model.GioHang;
import com.example.adsphone_app.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.tensp.setText(gioHang.getTensp());
        holder.soluong.setText(gioHang.getSoluong() + " ");
        holder.size.setText( "Size "+gioHang.getSize());
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.imggiohang);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText(decimalFormat.format((gioHang.getGiasp()))+ " VND");
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.giasp2.setText(decimalFormat.format(gia));
        holder.checkgiohang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(true);
                    if (!Utils.mangmuahang.contains(gioHang)){
                        Utils.mangmuahang.add(gioHang);
                    }

                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }else {
                    Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(false);
                    for (int i = 0; i<Utils.mangmuahang.size(); i++){
                        if (Utils.mangmuahang.get(i).getIdsp() == gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });
        holder.checkgiohang.setChecked(gioHang.isChecked());

        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1){
                    if (gioHangList.get(pos).getSoluong() > 1){
                        int soluongmoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluongmoi);

                        holder.soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if (gioHangList.get(pos).getSoluong() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setIcon(R.drawable.warning);
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng không");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangmuahang.remove(gioHang);
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if(giatri == 2){
                    if (gioHangList.get(pos).getSoluong() < 10){
                        int soluongmoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHang.getGiasp();
                    holder.giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imggiohang;
        TextView tensp, giasp, soluong, giasp2, congsp, trusp, size;
        IImageClickListener listener;
        CheckBox checkgiohang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imggiohang = itemView.findViewById(R.id.item_giohang_image);
            tensp = itemView.findViewById(R.id.item_giohang_ten);
            giasp = itemView.findViewById(R.id.item_giohang_giasp1);
            giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            soluong = itemView.findViewById(R.id.item_giohang_soluong);
            size = itemView.findViewById(R.id.item_giohang_size);
            congsp = itemView.findViewById(R.id.item_giohang_cong);
            trusp = itemView.findViewById(R.id.item_giohang_tru);
            checkgiohang = itemView.findViewById(R.id.item_check_giohang);

            //click
            congsp.setOnClickListener(this);
            trusp.setOnClickListener(this);
        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (v == trusp){
                listener.onImageClick(v, getAdapterPosition(), 1);
                //1 tru
            }else if(v == congsp){
                //2 cong
                listener.onImageClick(v, getAdapterPosition(), 2);

            }
        }
    }
}
