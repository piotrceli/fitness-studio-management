CREATE DATABASE IF NOT EXISTS fitness_studio_management;
USE fitness_studio_management;

CREATE TABLE fitness_class (
  id bigint AUTO_INCREMENT,
  fitness_class_name varchar(50) NOT NULL,
  difficulty_level int NOT NULL,
  fitness_class_description text NOT NULL,

  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE trainer (
  id bigint AUTO_INCREMENT,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  email varchar(50) NOT NULL,
  trainer_description text NOT NULL,
  
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE fitness_class_trainer (
  fitness_class_id bigint NOT NULL,
  trainer_id bigint NOT NULL,
  
  PRIMARY KEY (fitness_class_id, trainer_id),
  CONSTRAINT FK_fitness_class_trainer_fitness_class FOREIGN KEY (fitness_class_id) REFERENCES fitness_class (id),
  CONSTRAINT FK_fitness_class_trainer_trainer FOREIGN KEY (trainer_id) REFERENCES trainer (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE gym_event (
  id bigint AUTO_INCREMENT,
  start_time datetime NOT NULL,
  end_time datetime NOT NULL,
  duration varchar(5) NOT NULL,
  participants_limit int NOT NULL,
  current_participants_number int NOT NULL,
  fitness_class_id bigint NOT NULL,
  
  PRIMARY KEY (id),
  CONSTRAINT FK_gym_event_fitness_class FOREIGN KEY (fitness_class_id) REFERENCES fitness_class (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE app_user (
  id bigint AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  user_password varchar(68) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  email varchar(50) NOT NULL,
  date_of_birth date NOT NULL,
  enabled tinyint NOT NULL,
  
  PRIMARY KEY (id),
  CONSTRAINT UQ_app_user_username UNIQUE (username),
  CONSTRAINT UQ_app_user_email UNIQUE (email)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE app_role (
  id bigint AUTO_INCREMENT,
  role_name varchar(50) NOT NULL,
  
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE user_role (
  app_user_id bigint NOT NULL,
  app_role_id bigint NOT NULL,
  
  PRIMARY KEY (app_user_id, app_role_id),
  CONSTRAINT FK_user_role_app_user FOREIGN KEY (app_user_id) REFERENCES app_user (id),
  CONSTRAINT FK_user_role_app_role FOREIGN KEY (app_role_id) REFERENCES app_role (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;

CREATE TABLE event_user (
  gym_event_id bigint NOT NULL,
  app_user_id bigint NOT NULL,
  
  PRIMARY KEY (gym_event_id, app_user_id),
  CONSTRAINT FK_event_user_gym_event FOREIGN KEY (gym_event_id) REFERENCES gym_event (id),
  CONSTRAINT FK_event_user_app_user FOREIGN KEY (app_user_id) REFERENCES app_user (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8MB4;