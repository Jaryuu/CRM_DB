CREATE TABLE carro (modelo int PRIMARY KEY, marca text, color text, transmision text);

CREATE TABLE empresa (id int PRIMARY KEY, nombre text, direccion text, pais text, departamento text);

CREATE TABLE aseguradora (id int PRIMARY KEY, nombre text, nit text, descripcion text, precio real);

CREATE TABLE pais (id int PRIMARY KEY, nombre text, continente text, codArea int);

CREATE TABLE cliente (nit int PRIMARY KEY, nombre text, apellido text, telefono text, dob date, nacionalidad text, sexo text, correo text, foto text, direccion text, estadoCivil text, idPais int REFERENCES pais (id), modeloCarro int REFERENCES carro (modelo), idEmpresa int REFERENCES empresa (id), idAseguradora int REFERENCES aseguradora (id));