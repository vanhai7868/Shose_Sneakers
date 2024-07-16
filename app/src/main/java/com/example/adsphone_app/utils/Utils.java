package com.example.adsphone_app.utils;

import com.example.adsphone_app.model.GioHang;
import com.example.adsphone_app.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    //ipconfig mạng

//    public static final String BASE_URL="http://192.168.1.11/banhang/";
    public static final String BASE_URL="http://192.168.1.25/banhang/";

    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();
    public static User user_current = new User();



    public static String statusOrder(int status){
        String result = "";
        switch (status){
            case 0:
                result = "Đơn hàng đang xử lí";
                break;
            case 1:
                result = "Đơn hàng đang tiếp nhận";
                break;
            case 2:
                result = "Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Đơn hàng giao thành công";
                break;
            case 4:
                result = "Đơn hàng đã được hủy";
                break;
            default:
                result = "...";
        }

        return result;
    }
}
