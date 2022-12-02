-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 24, 2022 at 08:07 AM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 7.4.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `internship1`
--

-- --------------------------------------------------------

--
-- Table structure for table `agama`
--

CREATE TABLE `agama` (
  `id_agama` varchar(10) NOT NULL,
  `nama_agama` varchar(30) DEFAULT NULL,
  `status` int(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `agama`
--

INSERT INTO `agama` (`id_agama`, `nama_agama`, `status`) VALUES
('A1', 'Islam', 1),
('A2', 'Kristen', 1),
('A3', 'Hindu', 1),
('A4', 'Budha', 1),
('A5', 'Konghuchu', 1);

-- --------------------------------------------------------

--
-- Table structure for table `penduduk`
--

CREATE TABLE `penduduk` (
  `nik` varchar(30) NOT NULL,
  `nama` varchar(150) DEFAULT NULL,
  `tempat_lahir` varchar(50) DEFAULT NULL,
  `tanggal_lahir` varchar(50) DEFAULT NULL,
  `jk` enum('L','P') DEFAULT NULL,
  `id_agama` varchar(10) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` text DEFAULT NULL,
  `lokasi` varchar(100) NOT NULL,
  `latitude` int(100) NOT NULL,
  `longitude` int(100) NOT NULL,
  `altitude` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `penduduk`
--

INSERT INTO `penduduk` (`nik`, `nama`, `tempat_lahir`, `tanggal_lahir`, `jk`, `id_agama`, `username`, `password`, `lokasi`, `latitude`, `longitude`, `altitude`) VALUES
('3513052504220004', 'Deriska Fadilla Musdalifa', 'Blitar', '18-08-2001', 'P', 'A1', 'deriska', '49a297ef9b871821c539231cac078ca0', '', 0, 0, 0),
('3513060210190002', 'Aki', 'Blitar', '15-07-1999', 'L', 'A1', 'aki', '6fd394b2f8f7e2438ca7f0a87a6db994', '', 0, 0, 0),
('3513060804770001', 'Hasyim', 'Blitar', '02-06-2020', 'L', 'A1', 'hasyim', '74a686f8fa52d8ac4d19d570ab6711bd', '', 0, 0, 0),
('3513060804970002', 'Sania ', 'Blitar', '22-05-1996', 'P', 'A1', 'sania', 'e63aa50cb57c494b8b77d8d342e66624', '', 0, 0, 0),
('3513062121210002', 'Arif', 'Blitar', '14-08-1998', 'L', 'A1', NULL, NULL, '', 0, 0, 0),
('3513070804170002', 'Faizal', 'Blitar', '06-08-1898', 'L', 'A1', NULL, NULL, '', 0, 0, 0),
('3513070804440004', 'Reni Rusdiana', 'Blitar', '02-01-2001', 'P', 'A1', NULL, NULL, '', 0, 0, 0),
('3513070804980003', 'Ahmad', 'Blitar', '26-03-1995', 'L', 'A1', NULL, NULL, '', 0, 0, 0),
('3513070808880004', 'Ninda', 'Blitar', '12-02-1996', 'P', 'A1', NULL, NULL, '', 0, 0, 0),
('3513082704670002', 'Iqbal', 'Blitar', '12-12-1996', 'L', 'A1', NULL, NULL, '', 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agama`
--
ALTER TABLE `agama`
  ADD PRIMARY KEY (`id_agama`);

--
-- Indexes for table `penduduk`
--
ALTER TABLE `penduduk`
  ADD PRIMARY KEY (`nik`),
  ADD KEY `penduduk` (`id_agama`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
