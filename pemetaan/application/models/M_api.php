<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class M_api extends CI_Model {

    //-------------------------------------- LOGIN ---------------------------------------------------   

    public function proses_login($user, $pass)
    {
        return $this->db->query("SELECT nik FROM penduduk WHERE username = '$user' AND password = MD5('$pass')");
    }

    public function proses_login_admin($username, $password)
    {
        return $this->db->query("SELECT id_admin FROM admin WHERE username = '$username' AND password = MD5('$password')");
    }

    //-------------------------------------- REGISTER ---------------------------------------------------   

    public function cek_nik_register($nik)
    {
        return $this->db->query("SELECT nik FROM penduduk WHERE nik = '$nik'");
    }
    public function cek_admin($id_admin)
    {
        return $this->db->query("SELECT id_admin FROM admin WHERE id_admin = '$id_admin'");
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


    //-------------------------------------- DASHBOARD --------------------------------------------------- 
    
    public function get_aduan_nik($nik, $status = NULL)
    {
        if ($status == NULL) {
            return $this->db->query("SELECT * FROM pengaduan WHERE nik = '$nik'");
        } else {
            return $this->db->query("SELECT * FROM pengaduan WHERE nik = '$nik' AND status = '$status' ORDER BY id_pengaduan DESC");
        }
    }

    public function get_nama_nik($nik)
    {
        return $this->db->query("SELECT nama FROM penduduk WHERE nik = '$nik'")->row()->nama;
    }

    public function get_admin($id_admin)
    {
        return $this->db->query("SELECT * FROM admin WHERE id_admin = '$id_admin'")->row();
    }

    //-------------------------------------- PROFILE --------------------------------------------------- 
    
    public function get_profile($nik)
    {
        return $this->db->query("SELECT * FROM penduduk, agama WHERE penduduk.id_agama = agama.id_agama AND nik = '$nik'")->row();
    }

    public function updatePenduduk()
    {
        $latitude = $_POST['latitude'];
        $longitude = $_POST['longitude'];
        $altitude = $_POST['altitude'];
        $alamat = $_POST['alamat'];
        $nik = $_POST['nik'];

        $this->db->query("UPDATE penduduk SET alamat = '$alamat', latitude = '$latitude', longitude = '$longitude', altitude = '$altitude' WHERE nik = '$nik'");
    }

    public function readData()
    {
        return $this->db->query("SELECT * FROM penduduk")->row();

    }

    //-------------------------------------- GET KELUHAN BY ID ---------------------------------------------------

    public function get_keluhan_id($id)
    {
        return $this->db->query("SELECT pengaduan.*, admin.nama_admin FROM pengaduan LEFT JOIN admin
                                    ON pengaduan.id_admin = admin.id_admin
                                    WHERE pengaduan.id_pengaduan = '$id'");
    }

    //-------------------------------------- TAMBAH KELUHAN ---------------------------------------------------

    public function addPenduduk($nik, $nama, $ttl, $tanggal)
    {
        // $tanggal = date('d-m-Y');
        $this->db->query("INSERT INTO penduduk (`nik`, `nama`, `tempat_lahir`, `tanggal_lahir`)
                            VALUES ('$nik', '$nama', '$ttl', $tanggal)");
    }
    //-------------------------------------- UBAH KELUHAN ---------------------------------------------------

    public function ubahPenduduk($nik, $nama, $alamat)
    {
        // $tanggal = date('d-m-Y');

        $this->db->query("UPDATE penduduk SET nama = '$nama', alamat = '$alamat' 
                            WHERE nik = '$nik'");
    }

    //-------------------------------------- UBAH ---------------------------------------------------

    public function hapusPenduduk($nik)
    {
        $this->db->query("DELETE FROM penduduk WHERE nik = '$nik'");
    }

    public function get_users()
    {
        return $this->db->query("SELECT nik, nama FROM penduduk");
    }


}