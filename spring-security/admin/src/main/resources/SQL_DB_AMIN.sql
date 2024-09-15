ALTER DATABASE admin_service SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'admin_service')
BEGIN
    DROP DATABASE admin_service;
END
GO

CREATE DATABASE admin_service;
GO

USE admin_service;
GO

CREATE TABLE roles
(
    name        varchar(255) NOT NULL,
    description varchar(255),
    CONSTRAINT pk_roles PRIMARY KEY (name)
)
GO

CREATE TABLE roles_permission
(
    roles_name      varchar(255) NOT NULL,
    permission_name varchar(255) NOT NULL,
    CONSTRAINT pk_roles_permission PRIMARY KEY (roles_name, permission_name)
)
GO

ALTER TABLE roles_permission
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_name) REFERENCES permission (name)
GO

ALTER TABLE roles_permission
    ADD CONSTRAINT fk_rolper_on_roles FOREIGN KEY (roles_name) REFERENCES roles (name)
GO

CREATE TABLE users
(
    name          varchar(255) NOT NULL,
    username      varchar(20),
    fullname      varchar(50),
    hash_password varchar(255),
    email         varchar(255),
    created_date  datetime,
    img_url       varchar(255),
    is_deleted    bit,
    CONSTRAINT pk_users PRIMARY KEY (name)
)
GO

CREATE TABLE users_roles
(
    users_name varchar(255) NOT NULL,
    roles_name varchar(255) NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (users_name, roles_name)
)
GO

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_roles FOREIGN KEY (roles_name) REFERENCES roles (name)
GO

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_users FOREIGN KEY (users_name) REFERENCES users (name)
GO

CREATE TABLE invalidated_token
(
    id_token    varchar(255) NOT NULL,
    expiry_time datetime,
    CONSTRAINT pk_invalidated_token PRIMARY KEY (id_token)
)
GO