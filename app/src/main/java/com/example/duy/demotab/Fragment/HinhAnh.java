package com.example.duy.demotab.Fragment;

/**
 * Created by DUY on 4/30/2018.
 */

public class HinhAnh {
    private String Ten;
    private int Hinh;

    public HinhAnh(String ten, int hinh) {
        Ten = ten;
        Hinh = hinh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public int getHinh() {
        return Hinh;
    }

    public void setHinh(int hinh) {
        Hinh = hinh;
    }
}
