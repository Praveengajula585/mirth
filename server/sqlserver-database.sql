Use Master
Go

IF  EXISTS (SELECT NAME FROM SYSDATABASES WHERE NAME = N'MIRTHDB')
	DROP DATABASE MIRTHDB
	
CREATE DATABASE MIRTHDB

GO

USE MIRTHDB

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'SCHEMA_INFO') AND type = (N'U'))
	DROP TABLE SCHEMA_INFO;

CREATE TABLE SCHEMA_INFO
	(VERSION VARCHAR(40));
	
IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'EVENT') AND type = (N'U'))
    DROP TABLE EVENT

CREATE TABLE EVENT
	(ID INTEGER IDENTITY (1, 1) NOT NULL PRIMARY KEY,
	DATE_CREATED DATETIME DEFAULT GETDATE(),
	EVENT TEXT NOT NULL,
	EVENT_LEVEL VARCHAR(40) NOT NULL,
	DESCRIPTION TEXT,
	ATTRIBUTES TEXT)

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'MESSAGE') AND type = (N'U'))
    ALTER TABLE MESSAGE DROP CONSTRAINT CHANNEL_ID_FK
    
IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CHANNEL_STATISTICS') AND type = (N'U'))
    ALTER TABLE CHANNEL_STATISTICS DROP CONSTRAINT CHANNEL_STATS_ID_FK

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CHANNEL') AND type = (N'U'))
    DROP TABLE CHANNEL

CREATE TABLE CHANNEL
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	DESCRIPTION TEXT,
	IS_ENABLED SMALLINT,
	VERSION VARCHAR(40),
	REVISION INTEGER,
	LAST_MODIFIED DATETIME DEFAULT GETDATE(),
	SOURCE_CONNECTOR TEXT,
	DESTINATION_CONNECTORS TEXT,
	PROPERTIES TEXT,
	PREPROCESSING_SCRIPT TEXT,
	POSTPROCESSING_SCRIPT TEXT,
	DEPLOY_SCRIPT TEXT,
	SHUTDOWN_SCRIPT TEXT);
	
IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CHANNEL_STATISTICS') AND type = (N'U'))
    DROP TABLE CHANNEL_STATISTICS
	
CREATE TABLE CHANNEL_STATISTICS
	(SERVER_ID VARCHAR(255) NOT NULL,
	CHANNEL_ID VARCHAR(255) NOT NULL,
	RECEIVED INTEGER,
	FILTERED INTEGER,
	SENT INTEGER,
	ERROR INTEGER,
	QUEUED INTEGER,
	ALERTED INTEGER,
	PRIMARY KEY(SERVER_ID, CHANNEL_ID))
	
ALTER TABLE CHANNEL_STATISTICS
ADD CONSTRAINT CHANNEL_STATS_ID_FK
FOREIGN KEY (CHANNEL_ID)
REFERENCES CHANNEL (ID) ON DELETE CASCADE

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'ATTACHMENT') AND type = (N'U'))
    DROP TABLE ATTACHMENT

CREATE TABLE ATTACHMENT
    (ID VARCHAR(255) NOT NULL PRIMARY KEY,
     MESSAGE_ID VARCHAR(255) NOT NULL,
     ATTACHMENT_DATA IMAGE,
     ATTACHMENT_SIZE INTEGER,
     ATTACHMENT_TYPE VARCHAR(40));

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'MESSAGE') AND type = (N'U'))
    DROP TABLE MESSAGE

CREATE TABLE MESSAGE
	(SEQUENCE_ID INTEGER IDENTITY (1, 1) NOT NULL PRIMARY KEY,
	ID VARCHAR(255) NOT NULL,
	SERVER_ID VARCHAR(255) NOT NULL,
	CHANNEL_ID VARCHAR(255) NOT NULL,
	SOURCE VARCHAR(255),
	TYPE VARCHAR(255),
	DATE_CREATED DATETIME NOT NULL,
	VERSION VARCHAR(40),
	IS_ENCRYPTED SMALLINT NOT NULL,
	STATUS VARCHAR(40),
	RAW_DATA TEXT,
	RAW_DATA_PROTOCOL VARCHAR(40),
	TRANSFORMED_DATA TEXT,
	TRANSFORMED_DATA_PROTOCOL VARCHAR(40),
	ENCODED_DATA TEXT,
	ENCODED_DATA_PROTOCOL VARCHAR(40),
	CONNECTOR_MAP TEXT,
	CHANNEL_MAP TEXT,
	RESPONSE_MAP TEXT,
	CONNECTOR_NAME VARCHAR(255),
	ERRORS TEXT,
	CORRELATION_ID VARCHAR(255),
    ATTACHMENT SMALLINT,	
	UNIQUE (ID))
	
ALTER TABLE MESSAGE
ADD CONSTRAINT CHANNEL_ID_FK
FOREIGN KEY (CHANNEL_ID)
REFERENCES CHANNEL (ID) ON DELETE CASCADE

CREATE INDEX MESSAGE_INDEX1 ON MESSAGE(CHANNEL_ID, DATE_CREATED)

CREATE INDEX MESSAGE_INDEX2 ON MESSAGE(CHANNEL_ID, DATE_CREATED, CONNECTOR_NAME)

CREATE INDEX MESSAGE_INDEX3 ON MESSAGE(CHANNEL_ID, DATE_CREATED, RAW_DATA_PROTOCOL)

CREATE INDEX MESSAGE_INDEX4 ON MESSAGE(CHANNEL_ID, DATE_CREATED, SOURCE)

CREATE INDEX MESSAGE_INDEX5 ON MESSAGE(CHANNEL_ID, DATE_CREATED, STATUS)

CREATE INDEX MESSAGE_INDEX6 ON MESSAGE(CHANNEL_ID, DATE_CREATED, TYPE)
	
IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'SCRIPT') AND type = (N'U'))
    DROP TABLE SCRIPT

CREATE TABLE SCRIPT
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	SCRIPT TEXT)

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'TEMPLATE') AND type = (N'U'))
    DROP TABLE TEMPLATE

CREATE TABLE TEMPLATE
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	TEMPLATE TEXT)
	
IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'PERSON') AND type = (N'U'))
    DROP TABLE PERSON

CREATE TABLE PERSON
	(ID INTEGER IDENTITY (1, 1) NOT NULL PRIMARY KEY,
	USERNAME VARCHAR(40) NOT NULL,
	PASSWORD VARCHAR(40) NOT NULL,
	SALT VARCHAR(40) NOT NULL,
	FIRSTNAME VARCHAR(40),
	LASTNAME VARCHAR(40),
	ORGANIZATION VARCHAR(255),
	EMAIL VARCHAR(255),
	PHONENUMBER VARCHAR(40),
	DESCRIPTION VARCHAR(255),
	LAST_LOGIN DATETIME DEFAULT GETDATE(),
	LOGGED_IN SMALLINT NOT NULL)

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CHANNEL_ALERT') AND type = (N'U'))
    ALTER TABLE CHANNEL_ALERT DROP CONSTRAINT ALERT_ID_CA_FK

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'ALERT_EMAIL') AND type = (N'U'))
    ALTER TABLE ALERT_EMAIL DROP CONSTRAINT ALERT_ID_AE_FK

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'ALERT') AND type = (N'U'))
    DROP TABLE ALERT

CREATE TABLE ALERT
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	IS_ENABLED SMALLINT NOT NULL,
	EXPRESSION TEXT,
	TEMPLATE TEXT)
	
IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CODE_TEMPLATE') AND type = (N'U'))
    DROP TABLE CODE_TEMPLATE;

CREATE TABLE CODE_TEMPLATE
	(ID VARCHAR(255) NOT NULL PRIMARY KEY,
	NAME VARCHAR(40) NOT NULL,
	CODE_SCOPE VARCHAR(40) NOT NULL,
	CODE_TYPE VARCHAR(40) NOT NULL,
	TOOLTIP VARCHAR(255) NOT NULL,
	CODE TEXT);	

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CHANNEL_ALERT') AND type = (N'U'))
    DROP TABLE CHANNEL_ALERT
	
CREATE TABLE CHANNEL_ALERT
	(CHANNEL_ID VARCHAR(255) NOT NULL,
	ALERT_ID VARCHAR(255) NOT NULL)
	
ALTER TABLE CHANNEL_ALERT
ADD CONSTRAINT ALERT_ID_CA_FK
FOREIGN KEY (ALERT_ID)
REFERENCES ALERT (ID) ON DELETE CASCADE

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'ALERT_EMAIL') AND type = (N'U'))
    DROP TABLE ALERT_EMAIL

CREATE TABLE ALERT_EMAIL
	(ALERT_ID VARCHAR(255) NOT NULL,
	EMAIL VARCHAR(255) NOT NULL)
	
ALTER TABLE ALERT_EMAIL
ADD CONSTRAINT ALERT_ID_AE_FK
FOREIGN KEY (ALERT_ID)
REFERENCES ALERT(ID) ON DELETE CASCADE

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'CONFIGURATION') AND type = (N'U'))
    DROP TABLE CONFIGURATION

CREATE TABLE CONFIGURATION
	(ID INTEGER IDENTITY (1, 1) NOT NULL PRIMARY KEY,
	DATE_CREATED DATETIME DEFAULT GETDATE(),
	DATA TEXT NOT NULL)

IF EXISTS(SELECT 1 FROM sysobjects WHERE ID = OBJECT_ID(N'ENCRYPTION_KEY') AND type = (N'U'))
    DROP TABLE ENCRYPTION_KEY

CREATE TABLE ENCRYPTION_KEY
	(DATA TEXT NOT NULL)

INSERT INTO PERSON (USERNAME, PASSWORD, SALT, LOGGED_IN) VALUES('admin', 'NdgB6ojoGb/uFa5amMEyBNG16mE=', 'Np+FZYzu4M0=', 0)
INSERT INTO SCHEMA_INFO (VERSION) VALUES ('4')