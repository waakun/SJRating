SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;


CREATE TABLE `competitions` (
  `id` int(11) NOT NULL,
  `race_id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `place` varchar(32) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `competition_results` (
  `id` int(11) NOT NULL,
  `competition_id` int(11) NOT NULL,
  `skijumper_id` int(11) NOT NULL,
  `place` int(11) NOT NULL,
  `rating_before` double NOT NULL DEFAULT '0',
  `rating_after` double NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `skijumpers` (
  `id` int(11) NOT NULL,
  `jumper_id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `country` varchar(6) NOT NULL,
  `rating` double NOT NULL DEFAULT '1000'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `competitions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `race_id` (`race_id`);

ALTER TABLE `competition_results`
  ADD PRIMARY KEY (`id`),
  ADD KEY `skijumper_id` (`skijumper_id`),
  ADD KEY `competition_results_ibfk_1` (`competition_id`);

ALTER TABLE `skijumpers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `jumper_id` (`jumper_id`);


ALTER TABLE `competitions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=726;
ALTER TABLE `competition_results`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36673;
ALTER TABLE `skijumpers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=939;

ALTER TABLE `competition_results`
  ADD CONSTRAINT `competition_results_ibfk_1` FOREIGN KEY (`competition_id`) REFERENCES `competitions` (`race_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `competition_results_ibfk_2` FOREIGN KEY (`skijumper_id`) REFERENCES `skijumpers` (`jumper_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
