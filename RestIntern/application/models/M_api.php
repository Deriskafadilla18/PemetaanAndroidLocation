<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class M_api extends CI_Model {

    //-------------------------------------- LOGIN ---------------------------------------------------   

    public function proses_login($user, $pass)
    {
        return $this->db->query("SELECT nik FROM penduduk WHERE username = '$user' AND password = MD5('$pass')");
    }

    //-------------------------------------- REGISTER ---------------------------------------------------   

    public function cek_nik_register($nik)
    {
        return $this->db->query("SELECT nik FROM penduduk WHERE nik = '$nik'");
    }

    public function cek_if_register($nik)
    {
        return $this->db->query("SELECT * FROM penduduk WHERE nik = '$nik' AND username IS NOT NULL");
    }

    public function cek_user_exist_register($user)
    {
        return $this->db->query("SELECT username FROM penduduk WHERE username = '$user'");
    }

    public function proses_register()
    {
        $user = $_POST['user'];
        $pass = md5($_POST['pass']);
        $nik = $_POST['nik'];

        $this->db->query("UPDATE penduduk SET username = '$user', password = '$pass' WHERE nik = '$nik'");
    }

    //-------------------------------------- PROFILE --------------------------------------------------- 
    
    public function get_profile($nik)
    {
        return $this->db->query("SELECT * FROM penduduk, agama WHERE penduduk.id_agama = agama.id_agama AND nik = '$nik'")->row();
    }

    //-------------------------------------- UBAH PROFILE ---------------------------------------------------

    // public function ubah_keluhan($id, $keluhan)
    // {
    //     $tanggal = date('d-m-Y');

    //     $this->db->query("UPDATE pengaduan SET pengaduan = '$keluhan', tanggal = '$tanggal' 
    //                         WHERE id_pengaduan = '$id'");
    // }

}