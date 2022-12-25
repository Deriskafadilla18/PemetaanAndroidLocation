<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Api extends CI_Controller {


    public function __construct()
	{
		parent::__construct();
        $this->load->model('M_api');
    }

    //-------------------------------------- LOGIN ---------------------------------------------------  

    public function login()
    {
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            if (isset($_POST['user']) && isset($_POST['pass'])) {
                
                $user_login = $this->M_api->proses_login($_POST['user'], $_POST['pass']);
                $result['nik']   = null;

                if ($user_login->num_rows() == 1) {
                    $result['value'] = "1";
                    $result['pesan'] = "sukses login!";
                    $result['nik']   = $user_login->row()->nik;
                } else {
                    $result['value'] = "0";
                    $result['pesan'] = "username / password salah!";
                }
            } else {
                $result['value'] = "0";
                $result['pesan'] = "beberapa inputan masih kosong!";
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }

    public function login_admin()
    {
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            if (isset($_POST['user']) && isset($_POST['pass'])) {
                
                $user_login = $this->M_api->proses_login_admin($_POST['user'], $_POST['pass']);
                $result['id_admin']   = null;

                if ($user_login->num_rows() == 1) {
                    $result['value'] = "1";
                    $result['pesan'] = "sukses login!";
                    $result['id_admin']   = $user_login->row()->id_admin;
                } else {
                    $result['value'] = "0";
                    $result['pesan'] = "username / password salah!";
                }
            } else {
                $result['value'] = "0";
                $result['pesan'] = "beberapa inputan masih kosong!";
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }

    //-------------------------------------- REGISTER ---------------------------------------------------  

    public function register()
    {
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            if (isset($_POST['nik']) && isset($_POST['user']) && isset($_POST['pass']) && isset($_POST['cpass'])) {
                if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() == 0) {
                    $result['value'] = "0";
                    $result['pesan'] = "nik tidak terdaftar!";
                } else if ($this->M_api->cek_if_register($_POST['nik'])->num_rows() == 1) {
                    $result['value'] = "0";
                    $result['pesan'] = "nik sudah ter registrasi!";
                } else if ($this->M_api->cek_user_exist_register($_POST['user'])->num_rows() == 1) {
                    $result['value'] = "0";
                    $result['pesan'] = "username sudah terdaftar!";
                } else {
                    $this->M_api->proses_register();
                    $result['value'] = "1";
                    $result['pesan'] = "registrasi berhasil!";
                }
            } else {
                $result['value'] = "0";
                $result['pesan'] = "beberapa inputan masih kosong!";
            }            
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }


    //-------------------------------------- DASHBOARD ---------------------------------------------------  

    public function get_data()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {

            $result['hasil'] = null;

            if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {
                $result['value'] = "1";
                $result['pesan'] = "tampil data!";
                $result['hasil'] = [
                    'nama'      => $this->M_api->get_nama_nik($_POST['nik']),
                    'total_kel' => $this->M_api->get_nama_nik($_POST['nik'])->num_rows(),
                    'belum_kel' => $this->M_api->get_nama_nik($_POST['nik'], "0")->num_rows(),
                    'sudah_kel' => $this->M_api->get_nama_nik($_POST['nik'], "1")->num_rows(),
                    'tolak_kel' => $this->M_api->get_nama_nik($_POST['nik'], "2")->num_rows()
                ];
            } else {
                $result['value'] = "0";
                $result['pesan'] = "nik tidak terdaftar!";
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }


     //-------------------------------------- BY ID ---------------------------------------------------

    public function profile()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {

            $result['hasil'] = null;

            if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {

                $result['value'] = "1";
                $result['pesan'] = "response ok!";
                $result['hasil'] = [
                    'nik'   => $this->M_api->get_profile($_POST['nik'])->nik,
                    'nama'  => $this->M_api->get_profile($_POST['nik'])->nama,
                    'ttl'   => $this->M_api->get_profile($_POST['nik'])->tempat_lahir . ", " . $this->M_api->get_profile($_POST['nik'])->tanggal_lahir,
                    'jk'    => $this->M_api->get_profile($_POST['nik'])->jk == "L" ? "Laki-laki" : "Perempuan",
                    'agama' => $this->M_api->get_profile($_POST['nik'])->nama_agama
                ];
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }

    public function admin()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {

            $result['hasil'] = null;

            if ($this->M_api->cek_admin($_POST['id_admin'])->num_rows() != 0) {

                $result['value'] = "1";
                $result['pesan'] = "response ok!";
                $result['hasil'] = [
                    'id_admin'   => $this->M_api->get_admin($_POST['id_admin'])->id_admin,
                    'nama_admin'  => $this->M_api->get_admin($_POST['id_admin'])->nama_admin,
                    'tempat_lahir'   => $this->M_api->get_admin($_POST['id_admin'])->tempat_lahir . ", " . $this->M_api->get_admin($_POST['id_admin'])->tanggal_lahir,
                    'tanggal_lahir'  => $this->M_api->get_admin($_POST['id_admin'])->tanggal_lahir,
                    'jk'    => $this->M_api->get_admin($_POST['id_admin'])->jk == "L" ? "Laki-laki" : "Perempuan",
                    'username'=> $this->M_api->get_admin($_POST['id_admin'])->username,
                    'password'=> $this->M_api->get_admin($_POST['id_admin'])->password
                ];
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }

     //-------------------------------------- UPDATE PENDUDUK ---------------------------------------------------

    public function index_ubah()
    {
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            if (isset($_POST['nik']) && isset($_POST['alamat']) && isset($_POST['latitude']) && isset($_POST['longitude']) && isset($_POST['altitude'])) {
                if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() == 0) {
                    $result['value'] = "0";
                    $result['pesan'] = "nik tidak terdaftar!";
                } else {
                    $this->M_api->updatePenduduk();
                    $result['value'] = "1";
                    $result['pesan'] = "data berhasil diubah";
                }
            } else {
                $result['value'] = "0";
                $result['pesan'] = "beberapa inputan masih kosong!";
            }            
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }

    public function readDataAdmin()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'GET') {

            $result['hasil'] = null;

            if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {

                $result['value'] = "1";
                $result['pesan'] = "response ok!";
                $result['hasil'] = [
                    'nik'   => $this->M_api->readData($_POST['nik'])->nik,
                    'nama'  => $this->M_api->readData($_POST['nik'])->nama
                ];
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }

    //-------------------------------------- GET Recycler Penduduk ---------------------------------------------------

    public function recycler()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {

            $result['hasil'] = null;

            if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {

                $result['value'] = "1";
                $result['pesan'] = "response ok!";
                $result['hasil'] = $this->M_api->get_aduan_nik($_POST['nik'], "0")->result_array();
                
            } else {
                $result['value'] = "0";
                $result['pesan'] = "nik tidak terdaftar!";
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }
    


    // public function keluhan_diterima()
    // {
    //     if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    //         $result['hasil'] = null;

    //         if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {

    //             $result['value'] = "1";
    //             $result['pesan'] = "response ok!";
    //             $result['hasil'] = $this->M_api->get_aduan_nik($_POST['nik'], "1")->result_array();
                
    //         } else {
    //             $result['value'] = "0";
    //             $result['pesan'] = "nik tidak terdaftar!";
    //         }
    //     } else {
    //         $result['value'] = "0";
    //         $result['pesan'] = "invalid request method!";
    //     }

    //     echo json_encode($result);
    // }


    // public function keluhan_ditolak()
    // {
    //     if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    //         $result['hasil'] = null;

    //         if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {

    //             $result['value'] = "1";
    //             $result['pesan'] = "response ok!";
    //             $result['hasil'] = $this->M_api->get_aduan_nik($_POST['nik'], "2")->result_array();
                
    //         } else {
    //             $result['value'] = "0";
    //             $result['pesan'] = "nik tidak terdaftar!";
    //         }
    //     } else {
    //         $result['value'] = "0";
    //         $result['pesan'] = "invalid request method!";
    //     }

    //     echo json_encode($result);
    // }


    
    // public function get_keluhan_id()
    // {
    //     if ($_SERVER['REQUEST_METHOD'] == 'POST') {
            
    //         $result['hasil'] = [];

    //         if ($this->M_api->get_keluhan_id($_POST['id_keluhan'])->num_rows() != 0) {
                
    //             $id_keluhan = $this->M_api->get_keluhan_id($_POST['id_keluhan'])->row()->id_pengaduan;
    //             $isi_keluhan = $this->M_api->get_keluhan_id($_POST['id_keluhan'])->row()->pengaduan;
    //             $tgl_keluhan = $this->M_api->get_keluhan_id($_POST['id_keluhan'])->row()->tanggal;
    //             $status_verif = $this->M_api->get_keluhan_id($_POST['id_keluhan'])->row()->status;
    //             $verifikasi = $this->M_api->get_keluhan_id($_POST['id_keluhan'])->row()->nama_admin;

    //             if ($status_verif == 0) { $status_verif = "Belum Diverifikasi"; } 
    //             else if ($status_verif == 1) { $status_verif = "Sudah Diverifikasi"; } 
    //             else { $status_verif = "Verifikasi Ditolak"; }

    //             if ($verifikasi == null) { $verifikasi = "-"; }

    //             $result['value'] = "1";
    //             $result['pesan'] = "response ok";
    //             $result['hasil'] = [
    //                 'id_keluhan'  => $id_keluhan,
    //                 'isi_keluhan' => $isi_keluhan,
    //                 'tgl_keluhan' => $tgl_keluhan,
    //                 'status_verif'=> $status_verif,
    //                 'verifikasi'  => $verifikasi
    //             ];
    //         } else {
    //             $result['value'] = "0";
    //             $result['pesan'] = "id keluhan tidak tersedia!";
    //         }
    //     } else {
    //         $result['value'] = "0";
    //         $result['pesan'] = "invalid request method!";
    //     }

    //     echo json_encode($result);

    // }

    //-------------------------------------- TAMBAH PENDUDUK ------------------------------------------------

    public function add_penduduk()
    {
                if (isset($_POST['nik'])) {
                    $this->M_api->addPenduduk($_POST['nik'], $_POST['nama'], $_POST['tempat_lahir'], $_POST['tanggal_lahir']);
                    $result['value'] = "1";
                    $result['pesan'] = "data penduduk berhasil disimpan!";
                } else {
                    $result['value'] = "0";
                    $result['pesan'] = "beberapa inputan masih kosong!";
                }

        echo json_encode($result);
    }


    //-------------------------------------- UBAH PENDUDUK ------------------------------------------------

    public function ubah_penduduk()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {

            if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {
                if (isset($_POST['nama'])) {
                    $this->M_api->ubahPenduduk($_POST['nik'], $_POST['nama'], $_POST['alamat']);
                    $result['value'] = "1";
                    $result['pesan'] = "Data Penduduk berhasil diubah!";
                } else {
                    $result['value'] = "0";
                    $result['pesan'] = "beberapa inputan masih kosong!";
                }
            } else {
                $result['value'] = "0";
                $result['pesan'] = "NIK Penduduk tidak tersedia!";
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    } 
    
    //-------------------------------------- HAPUS Penduduk ------------------------------------------------

    public function hapus_penduduk()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {

            if ($this->M_api->cek_nik_register($_POST['nik'])->num_rows() != 0) {
        
                $this->M_api->hapusPenduduk($_POST['nik']);
                $result['value'] = "1";
                $result['pesan'] = "Data Penduduk berhasil dihapus!";
            
            } else {
                $result['value'] = "0";
                $result['pesan'] = "NIK Penduduk tidak tersedia!";
            }
        } else {
            $result['value'] = "0";
            $result['pesan'] = "invalid request method!";
        }

        echo json_encode($result);
    }   


    public function get_users()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'GET') {
                $result['value'] = "1";
                $result['pesan'] = "response ok!";
                $result['hasil'] = $this->M_api->get_users()->result_array();
                
            } else {
                $result['value'] = "0";
                $result['pesan'] = "invalid request method!";
            }

        echo json_encode($result);
    }
}