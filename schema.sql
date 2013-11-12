delimiter $$

CREATE DATABASE `courseware` /*!40100 DEFAULT CHARACTER SET utf8 */$$

delimiter $$

CREATE TABLE `assignment` (
  `course_id` int(11) NOT NULL COMMENT 'Course to which this assignment belong to',
  `assignment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `external_tool_id` int(11) NOT NULL COMMENT 'External Tool that launches this assignment',
  `assignment_name` varchar(255) NOT NULL COMMENT 'Name given by our Assignments Management Application',
  `canvas_assignment_id` varchar(255) NOT NULL COMMENT 'Unique Canvas Assignment ID maps to custom_canvas_assignment_id parameter of POST request',
  `canvas_assignment_name` varchar(255) NOT NULL COMMENT 'Canvas Assignment Title maps to custom_canvas_assignment_title  parameter of POST request',
  `canvas_lti_assignment_id` varchar(255) NOT NULL COMMENT 'Unique canvas LTI assignment ID maps to resource_link_id',
  `canvas_external_tool_name` varchar(255) NOT NULL COMMENT ' External tool name Maps to resource_link_title parameter of POST request',
  `canvas_user_id` varchar(255) NOT NULL COMMENT 'Canvas User ID maps to custom_canvas_user_id parameter of POST request',
  `canvas_lti_user_id` varchar(255) NOT NULL COMMENT 'Unique Canvas LTI User ID maps to user_id parameter of POST request',
  `canvas_user_login_id` varchar(255) NOT NULL COMMENT 'Canvas User Login ID maps to custom_canvas_user_login_id parameter of POST request',
  `canvas_user_role` varchar(255) NOT NULL COMMENT 'Canvas User Role (Instructor/Learner) maps to roles parameter of POST request',
  `ext_ims_lis_basic_outcome_url` varchar(255) NOT NULL COMMENT 'URL for posting back grade maps to ext_ims_lis_basic_outcome_url parameter of POST request',
  `launch_presentation_return_url` varchar(255) NOT NULL COMMENT 'URL to return back after assignment completion maps to launch_presentation_return_url parameter of POST request',
  `lis_outcome_service_url` varchar(255) NOT NULL COMMENT 'URL for posting back grade maps to lis_outcome_service_url parameter of POST request',
  `canvas_instance_guid` varchar(255) NOT NULL COMMENT 'Unique Canvas Instance ID maps to tool_consumer_instance_guid parameter of POST request',
  `canvas_instance_name` varchar(255) NOT NULL COMMENT 'Canvas Instance Name maps to tool_consumer_instance_name parameter of POST request',
  PRIMARY KEY (`assignment_id`),
  UNIQUE KEY `COURSE_ASSIGNMENT` (`course_id`,`canvas_lti_assignment_id`),
  KEY `FK_COURSE_ID` (`course_id`),
  KEY `FK_EXTERNAL_TOOL_ID` (`external_tool_id`),
  CONSTRAINT `FK_COURSE_ID` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='Holds Assignment Details'$$

delimiter $$

CREATE TABLE `course` (
  `course_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `canvas_course_id` varchar(255) NOT NULL COMMENT 'Unique Canvas Course ID maps to custom_canvas_course_id parameter of POST request',
  `canvas_lti_course_id` varchar(255) NOT NULL COMMENT 'Unique Canvas LTI Course ID maps to context_id parameter of POST request',
  `canvas_lti_course_code` varchar(255) NOT NULL COMMENT 'Canvas Course Code maps to context_label parameter of POST request',
  `canvas_lti_course_name` varchar(255) NOT NULL COMMENT 'Canvas Course Title maps to context_title parameter of POST request',
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `LTI_COURSE_ID` (`canvas_lti_course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='Holds Course Details'$$

delimiter $$

CREATE TABLE `external_tool` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique Key',
  `name` varchar(255) NOT NULL COMMENT 'Name of the external tool',
  `consumer_key` varchar(255) NOT NULL COMMENT 'Consumer Key for the external tool',
  `shared_secret` varchar(255) NOT NULL COMMENT 'Shared Secret for the external tool',
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME_UNIQUE` (`name`),
  UNIQUE KEY `CONSUMER_KEY_UNIQUE` (`consumer_key`),
  UNIQUE KEY `SHARED_SECRET_UNIQUE` (`shared_secret`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Holds External Tool Configuration'$$

delimiter $$

CREATE TABLE `jar` (
  `jar_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID',
  `assignment_id` int(11) NOT NULL COMMENT 'Assignment to which this jar belong to',
  `jar_name` varchar(255) NOT NULL COMMENT 'Jar File Name',
  `jar_main_class` varchar(255) DEFAULT NULL COMMENT 'Fully Qualified Class Name that serve as entry point which our applet will call to render the assignment. When this is null this jar is just a dependency.',
  `jar_file` longblob COMMENT 'Actual Jar file itself',
  PRIMARY KEY (`jar_id`),
  UNIQUE KEY `JARNAME_ASSIGNMENT` (`jar_name`,`assignment_id`),
  KEY `FK_ASSIGNMENT_ID` (`assignment_id`),
  CONSTRAINT `FK_ASSIGNMENT_ID` FOREIGN KEY (`assignment_id`) REFERENCES `assignment` (`assignment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Holds Jars for a given Assignment'$$

