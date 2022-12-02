<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class M_pengaduan extends CI_Model {

	public $variable;

	public function __construct()
	{
		parent::__construct();
		
	}

	public function get_all()
	{
		$q = $this->db->query("SELECT * FROM penduduk, pengaduan
								WHERE penduduk.nik = pengaduan.nik ORDER BY id_pengaduan DESC
								");	
		return $q;
	}

	public function get_nik($nik)
	{
		$q = $this->db->query("SELECT * FROM penduduk, pengaduan
								WHERE penduduk.nik = pengaduan.nik
								AND penduduk.nik = '$nik' 
								ORDER BY id_pengaduan DESC
								");	
		return $q;
	}

	public function get_id($id_pengaduan)
	{
		// $q = $this->db->query("SELECT * FROM pengaduan, admin, penduduk
		// 						WHERE pengaduan.id_admin = admin.id_admin
		// 						AND pengaduan.nik = penduduk.nik
		// 						AND pengaduan.id_pengaduan = '$id_pengaduan' ");

		$q = $this->db->query("SELECT * FROM pengaduan LEFT JOIN admin
								ON pengaduan.id_admin = admin.id_admin
								JOIN penduduk ON pengaduan.nik = penduduk.nik
								WHERE pengaduan.id_pengaduan = '$id_pengaduan' ");
		return $q;
	}

	public function tambah($file)
	{
		$user 		= $this->session->userdata('userdata_desa');
		$nik 		= $user['nik'];
		$tanggal 	= date('d-m-Y');
		$pengaduan 	= $this->input->post('pengaduan');
		$status 	= "0";

		$data = array(
			'nik'    	=> $nik,
            'tanggal'   => $tanggal,
			'pengaduan'	=> $pengaduan,
			'file'		=> $file,
            'status' 	=> $status
				);

		$this->db->insert('pengaduan', $data);
	}

	public function edit_proses($id_pengaduan, $file)
	{
		$data = array(
			'pengaduan' => $this->input->post('pengaduan'),
			'file' => $file
		);

		$this->db->where('id_pengaduan', $id_pengaduan);
		$this->db->update('pengaduan', $data);
	}

}

/* End of file M_pengaduan.php */
/* Location: ./application/models/M_pengaduan.php */ 