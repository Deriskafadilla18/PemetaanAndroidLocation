package com.example.android;

public class Data {

    private String nik, nama;

    Data(String nik, String nama) {
        this.setNik(nik);
        this.setNama(nama);
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
