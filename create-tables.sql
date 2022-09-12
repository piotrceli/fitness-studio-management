CREATE DATABASE IF NOT EXISTS `fitness_studio_management`;
USE `fitness_studio_management`;

CREATE TABLE `fitness_class` (
  `id` bigint AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `difficulty_level` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `trainer` (
  `id` bigint AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `fitness_class_trainer` (
  `fitness_class_id` bigint NOT NULL,
  `trainer_id` bigint NOT NULL,
  
  PRIMARY KEY (`fitness_class_id`, `trainer_id`),
  CONSTRAINT `FK_FITNESS_CLASS_0` FOREIGN KEY (`fitness_class_id`) REFERENCES `fitness_class` (`id`),
  CONSTRAINT `FK_TRAINER_0` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `gym_event` (
  `id` bigint AUTO_INCREMENT,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `duration` varchar(10) NOT NULL,
  `participants_limit` int NOT NULL,
  `current_participants_number` int NOT NULL,
  `fitness_class_id` bigint NOT NULL,
  
  CONSTRAINT `FK_FITNESS_CLASS_1` FOREIGN KEY (`fitness_class_id`) REFERENCES `fitness_class` (`id`),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `app_user` (
  `id` bigint AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(68) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `date_of_birth` date NOT NULL,
  `enabled` tinyint NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `role` (
  `id` bigint AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `user_role` (
  `app_user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  
  PRIMARY KEY (`app_user_id`,`role_id`),
  CONSTRAINT `FK_APP_USER_0` FOREIGN KEY (`app_user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK_ROLE_0` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `event_user` (
  `gym_event_id` bigint NOT NULL,
  `app_user_id` bigint NOT NULL,
  
  PRIMARY KEY (`gym_event_id`, `app_user_id`),
  CONSTRAINT `FK_APP_USER_1` FOREIGN KEY (`app_user_id`) REFERENCES `app_user` (`id`),
  CONSTRAINT `FK_GYM_EVENT_0` FOREIGN KEY (`gym_event_id`) REFERENCES `gym_event` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;