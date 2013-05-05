
Warning: strftime(): It is not safe to rely on the system's timezone settings. You are *required* to use the date.timezone setting or the date_default_timezone_set() function. In case you used any of those methods and you are still getting this warning, you most likely misspelled the timezone identifier. We selected 'Asia/Chongqing' for 'CST/8.0/no DST' instead in /usr/share/phpmyadmin/export.php on line 257

Warning: strftime(): It is not safe to rely on the system's timezone settings. You are *required* to use the date.timezone setting or the date_default_timezone_set() function. In case you used any of those methods and you are still getting this warning, you most likely misspelled the timezone identifier. We selected 'Asia/Chongqing' for 'CST/8.0/no DST' instead in /usr/share/phpmyadmin/libraries/common.lib.php on line 1483

Warning: strftime(): It is not safe to rely on the system's timezone settings. You are *required* to use the date.timezone setting or the date_default_timezone_set() function. In case you used any of those methods and you are still getting this warning, you most likely misspelled the timezone identifier. We selected 'Asia/Chongqing' for 'CST/8.0/no DST' instead in /usr/share/phpmyadmin/libraries/common.lib.php on line 1484

Warning: strftime(): It is not safe to rely on the system's timezone settings. You are *required* to use the date.timezone setting or the date_default_timezone_set() function. In case you used any of those methods and you are still getting this warning, you most likely misspelled the timezone identifier. We selected 'Asia/Chongqing' for 'CST/8.0/no DST' instead in /usr/share/phpmyadmin/libraries/common.lib.php on line 1486
-- phpMyAdmin SQL Dump
-- version 2.11.11.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 06, 2013 at 12:19 AM
-- Server version: 5.1.69
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dctest`
--

-- --------------------------------------------------------

--
-- Table structure for table `friend_confirm_table`
--

CREATE TABLE IF NOT EXISTS `friend_confirm_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id1` varchar(32) NOT NULL,
  `id2` varchar(32) NOT NULL,
  `time` datetime NOT NULL,
  `confirm` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=43 ;

--
-- Dumping data for table `friend_confirm_table`
--

INSERT INTO `friend_confirm_table` (`id`, `id1`, `id2`, `time`, `confirm`) VALUES
(42, 'H', 'Crack     ', '2013-03-11 22:08:52', 1),
(41, 'crack', 'mark', '2013-03-11 22:08:23', 1),
(40, 'pccheng', 'mark', '2013-03-11 21:49:01', 1),
(39, 'aaaz', 'aaa', '2013-02-28 18:27:33', 0),
(38, 'aaaz', 'test1', '2013-02-28 18:27:33', 0);

-- --------------------------------------------------------

--
-- Table structure for table `friend_table`
--

CREATE TABLE IF NOT EXISTS `friend_table` (
  `id1` varchar(32) NOT NULL,
  `id2` varchar(32) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `friend_table`
--

INSERT INTO `friend_table` (`id1`, `id2`, `count`) VALUES
('Crack     ', 'H', 2),
('H', 'Crack     ', 0),
('mark', 'crack', 5),
('crack', 'mark', 1),
('H', 'H', 0),
('crack', 'crack', 4),
('mark', 'pccheng', 2),
('pccheng', 'mark', 0),
('pccheng', 'pccheng', 0),
('guitar', 'guitar', 0);

-- --------------------------------------------------------

--
-- Table structure for table `msg_table`
--

CREATE TABLE IF NOT EXISTS `msg_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(32) NOT NULL,
  `receiver` varchar(32) NOT NULL,
  `message` varchar(256) NOT NULL,
  `time` datetime NOT NULL,
  `timeout` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `del_s` tinyint(1) NOT NULL,
  `del_r` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=124 ;

--
-- Dumping data for table `msg_table`
--

INSERT INTO `msg_table` (`id`, `sender`, `receiver`, `message`, `time`, `timeout`, `status`, `del_s`, `del_r`) VALUES
(83, 'mark', 'test1', '5123928eaef09', '2013-02-19 23:56:14', 10, 0, 1, 0),
(81, 'test1', 'mark', '512220882474a', '2013-02-18 21:37:28', 10, 1, 0, 1),
(79, 'mark', 'aaa', '5121f4c20cae8', '2013-02-18 18:30:42', 10, 0, 1, 0),
(97, 'bbb', 'mark', '512ae2c2a6a45', '2013-02-25 13:04:18', 10, 1, 1, 1),
(76, 'test1', 'mark', '5121efa2ce168', '2013-02-18 18:08:50', 10, 1, 0, 1),
(75, 'test1', 'mark', '5121aa28788e4', '2013-02-18 13:12:24', 10, 1, 0, 1),
(109, 'ddd', 'ddd', '512e0ace5ecd4', '2013-02-27 22:31:58', 10, 1, 0, 0),
(96, 'mark', 'bbb', '512adc4db5efa', '2013-02-25 12:36:45', 10, 1, 1, 1),
(110, 'mark', 'pccheng', '513dd3ad26607', '2013-03-11 21:53:01', 5, 1, 0, 0),
(111, 'mark', 'crack', '513dd7892431b', '2013-03-11 22:09:29', 10, 1, 0, 1),
(112, 'mark', 'crack', '513dd7f11f59e', '2013-03-11 22:11:13', 5, 1, 0, 1),
(113, 'crack', 'H', '513dd892e85da', '2013-03-11 22:13:54', 10, 1, 1, 0),
(114, 'crack', 'mark', '513dda2d939b9', '2013-03-11 22:20:45', 5, 1, 1, 0),
(115, 'crack', 'crack', '513ddac2a6e24', '2013-03-11 22:23:14', 5, 1, 1, 1),
(116, 'crack', 'H', '513ddb1d52d04', '2013-03-11 22:24:45', 10, 1, 1, 0),
(117, 'crack', 'crack', '513ddb61e14db', '2013-03-11 22:25:53', 10, 1, 1, 1),
(118, 'crack', 'crack', '513e826611558', '2013-03-12 10:18:30', 5, 1, 0, 0),
(119, 'crack', 'crack', '513edd83374f0', '2013-03-12 16:47:15', 9, 1, 0, 0),
(120, 'mark', 'pccheng', '51559bd232fa0', '2013-03-29 22:49:06', 10, 0, 0, 0),
(121, 'mark', 'crack', '51559c0acb5d3', '2013-03-29 22:50:02', 5, 0, 0, 0),
(122, 'mark', 'crack', '5156bd7b5967e', '2013-03-30 19:24:59', 5, 1, 0, 0),
(123, 'mark', 'crack', '5156c0aee78a5', '2013-03-30 19:38:38', 5, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `session_table`
--

CREATE TABLE IF NOT EXISTS `session_table` (
  `user_id` varchar(32) NOT NULL,
  `key` varchar(64) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `session_table`
--

INSERT INTO `session_table` (`user_id`, `key`, `count`) VALUES
('crack', '5156c0a7c11b15156c0a7c11e8', 3),
('mark', '5156c0a310b705156c0a310ba7', 3),
('crack', '5156bd82a746c5156bd82a74a3', 3),
('mark', '5156bd596a14f5156bd596a187', 3),
('crack', '514fb6f59ccd8514fb6f59cd10', 3),
('crack', '5144633e2151e5144633e21555', 3),
('H', '51445e34eaffa51445e34eb032', 3),
('H', '514454c95c2d9514454c95c310', 3),
('guitar', '514454a989d95514454a989dcd', 3),
('mark', '51443a1da5de751443a1da5e1f', 3),
('mark', '514439ea48962514439ea4899a', 3),
('mark', '514439dfc0f64514439dfc0f9c', 3),
('mark', '514439af557ff514439af55836', 3),
('crack', '51443369211bc51443369211f4', 3),
('crack', '514431aa0d289514431aa0d2c0', 3),
('mark', '514076e33b5f3514076e33b62b', 3),
('mark', '514075e1d2c23514075e1d2c5a', 3),
('mark', '514075ce38fbc514075ce38ff3', 3),
('mark', '514073b98d2bd514073b98d2f4', 3),
('crack', '513edfb3b481b513edfb3b4852', 3),
('crack', '513edd630d086513edd630d0be', 3),
('crack', '513e8294a5b33513e8294a5b6a', 3),
('crack', '513e821962721513e821962759', 3),
('crack', '513e153b1f812513e153b1f849', 3),
('crack', '513e14f288ee7513e14f288f1e', 3),
('crack', '513e0f74509e5513e0f7450a1c', 3),
('crack', '513e0e727bcfe513e0e727bd35', 3),
('crack', '513e0e6258ca2513e0e6258cd9', 3),
('crack', '513e0df07d695513e0df07d6aa', 3),
('crack', '513e0dcbd1732513e0dcbd1769', 3),
('crack', '513e0bd107e1b513e0bd107e52', 3),
('crack', '513e069e4b334513e069e4b36b', 3),
('crack', '513e05aaea029513e05aaea061', 3),
('H', '513ddba0d2f72513ddba0d2fa9', 3),
('crack', '513ddac169017513ddac16904e', 3),
('mark', '513dda6a9ff70513dda6a9ffa7', 3),
('crack', '513dd866dc285513dd866dc2bc', 3),
('H', '513dd84d0fcbc513dd84d0fcf3', 3),
('aaaz', '512df889df2ec512df889df323', 3),
('aaaz', '512df86789af9512df86789b31', 3),
('aaaz', '512df84464356512df8446438d', 3),
('maaz', '512dca2d60f1f512dca2d60f56', 3),
('maaz', '512dca11617e2512dca1161819', 3),
('maa', '512dc9be46798512dc9be467cb', 3),
('aaaz', '512dc1d91bcbc512dc1d91bcf3', 3),
('aaaz', '512dc1c6814f9512dc1c681531', 3),
('zzz', '512dc13abbcb3512dc13abbcea', 3),
('aaaz', '512dbf6e68970512dbf6e689a7', 3),
('aaaz', '512dbdc97c2e0512dbdc97c318', 3),
('aaaz', '512dbcf5aa823512dbcf5aa85a', 3),
('aaaz', '512dbc61b2e63512dbc61b2e9a', 3),
('mark', '512dbc54ea474512dbc54ea4ac', 3),
('aaaz', '512db98ba5596512db98ba55cd', 3),
('aaaz', '512db933233b7512db933233ef', 3),
('aaaz', '512db8e081951512db8e081989', 3),
('aaaz', '512db8d015f5c512db8d015f94', 3),
('aaaz', '512db86cb38ee512db86cb3925', 3),
('aaaz', '512db7f9ac4dc512db7f9ac4e8', 3),
('aaaz', '512db76ea67e3512db76ea681a', 3),
('mark', '512db73fcc586512db73fcc5bd', 3),
('aaaz', '512db72034b7c512db72034bb3', 3),
('aaaz', '512db6cca3cf1512db6cca3d29', 3),
('aaaz', '512db662c3005512db662c3030', 3),
('aaaz', '512db64c8da20512db64c8da57', 3),
('aaaz', '512db62c96f54512db62c96f8b', 3),
('aaaz', '512db5d9856c6512db5d9856fd', 3),
('aazz', '512db5c2bb33b512db5c2bb372', 3),
('aazz', '512db58c92a57512db58c92a8e', 3),
('H', '513dd7f512ae1513dd7f512b18', 3),
('mark', '513dd7566c1fb513dd7566c232', 3),
('H', '513dd72f64add513dd72f64b14', 3),
('crack', '513dd70eb514d513dd70eb5184', 3),
('crack', '513dd69cc63c2513dd69cc63f9', 3),
('mark', '513dd629bf1b0513dd629bf1e6', 3),
('H', '513dd5edf1ca3513dd5edf1cd9', 3),
('pccheng', '513dd5c607ca6513dd5c607cde', 3),
('crack', '513dd4b1a07bb513dd4b1a07f2', 3),
('crack', '513dd46375c98513dd46375cd0', 3),
('crack', '513dd43df2e68513dd43df2e9f', 3),
('mark', '513dd2e4a5dbf513dd2e4a5df6', 3),
('pccheng', '513dd29f84bdb513dd29f84c12', 3),
('mark', '513dce666cfcc513dce666d003', 3),
('mark', '513dce437af21513dce437af58', 3),
('mark', '513dce10678b3513dce10678ea', 3),
('mark', '513dcb6027408513dcb6027440', 3),
('mark', '513dcb46e5244513dcb46e527b', 3),
('mark', '513dcb1b555c5513dcb1b555fc', 3),
('mark', '513dc9fcc3a21513dc9fcc3a58', 3),
('mark', '513dc9b68191a513dc9b681952', 3),
('mark', '513dc9839bfe6513dc9839c01d', 3),
('mark', '513dc953e1435513dc953e146d', 3),
('mark', '513b3c74a356f513b3c74a35a6', 3),
('ddd', '5138882fbd31b5138882fbd352', 3),
('bbb', '5134d2f0229ff5134d2f022a36', 3),
('mark', '5134d2e747d975134d2e747dce', 3),
('bbb', '5134d2ced6d6a5134d2ced6da1', 3),
('mark', '5134d26a983d75134d26a9840e', 3),
('mark', '5134d16d550425134d16d55079', 3),
('mark', '5134d092c9bd65134d092c9c0d', 3),
('mark', '5134d0463c1d25134d0463c209', 3),
('mark', '5134cfd7509115134cfd750948', 3),
('mark', '5134cf87c0b3e5134cf87c0b75', 3),
('mark', '5134cf140deef5134cf140df26', 3),
('mark', '5134ce7c8b3335134ce7c8b36a', 3),
('mark', '5134cd65b4a455134cd65b4a7b', 3),
('mark', '5134cd244291d5134cd2442953', 3),
('mark', '5134ca827d2425134ca827d279', 3),
('mark', '5134c9681edd65134c9681ee0d', 3),
('mark', '5134c7b5db2dd5134c7b5db2e7', 3),
('mark', '5134c3a60ed275134c3a60ed61', 3),
('mark', '5134a378944305134a37894467', 3),
('mark', '5134a2f7d1a4b5134a2f7d1a82', 3),
('mark', '5134a2e6cd2735134a2e6cd2aa', 3),
('mark', '51349f6e5dd6051349f6e5dd97', 3),
('mark', '51349f27d7a8a51349f27d7ac1', 3),
('mark', '51349e2bc586451349e2bc589b', 3),
('mark', '51349d0c260f451349d0c2612c', 3),
('mark', '51349cb4707f151349cb470828', 3),
('mark', '513498468afba513498468aff1', 3),
('mark', '513497a9f1e95513497a9f1ecb', 3),
('mark', '513495f8ca88b513495f8ca8c2', 3),
('mark', '5134949c400195134949c40050', 3),
('mark', '513000e7a3d8b513000e7a3dc2', 3),
('mark', '512f4c94baa8e512f4c94baac5', 3),
('mark', '512f4c430f0a8512f4c430f0df', 3),
('mark', '512f269d3e796512f269d3e7ce', 3),
('aaaz', '512f22ffb7ee0512f22ffb7f18', 3),
('ddd', '512e07539b9d6512e07539ba0e', 3),
('ddd', '512e054de150b512e054de1543', 3),
('ddd', '512e05053484e512e050534884', 3),
('ddd', '512e03a9b065a512e03a9b0691', 3),
('ddd', '512e035320874512e0353208ab', 3),
('ddd', '512e03191e09f512e03191e0d6', 3),
('mark', '512e00365970b512e003659742', 3),
('mark', '512dfec4366a2512dfec4366d9', 3),
('mark', '512dfd9420546512dfd942057d', 3),
('mark', '512dfbbc5c739512dfbbc5c752', 3),
('mark', '512dfb8214e41512dfb8214e79', 3),
('aaaz', '512dfaeb9c120512dfaeb9c157', 3),
('aaaz', '512dfa12ac270512dfa12ac2a7', 3),
('aaaz', '512df946cb88a512df946cb8c1', 3);

-- --------------------------------------------------------

--
-- Table structure for table `user_table`
--

CREATE TABLE IF NOT EXISTS `user_table` (
  `id` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `fbid` varchar(32) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_table`
--

INSERT INTO `user_table` (`id`, `email`, `fbid`, `password`) VALUES
('test1', 'r01943082@gmail.com', '3109917', '900150983cd24fb0d6963f7d28e17f72'),
('mark', 'mark23456@hotmail.com', '', '900150983cd24fb0d6963f7d28e17f72'),
('aaa', 'aaa@a.a', '774285175', '900150983cd24fb0d6963f7d28e17f72'),
('bbb', 'bbb@b.b', '', '900150983cd24fb0d6963f7d28e17f72'),
('pccheng', '', '100001345142475', ''),
('aaaz', '', '1233266413', ''),
('ddd', '', '100003254036255', ''),
('maa', 'mark23456@hotmail.co', '', '900150983cd24fb0d6963f7d28e17f72'),
('maaz', 'mark23456@a.b', '', '900150983cd24fb0d6963f7d28e17f72'),
('crack', 'crack1108@hotmail.com', '', '900150983cd24fb0d6963f7d28e17f72'),
('H', 'Allen78104@yahoo.com.tw', '', 'ff7982d1a0e94eb7cf13b842cc1b5333'),
('guitar', 'just_guitar@hotmail.com', '', 'ff7982d1a0e94eb7cf13b842cc1b5333');
