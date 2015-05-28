CREATE TABLE carro (modelo int PRIMARY KEY, marca text, color text, transmision text);
CREATE TABLE empresa (id int PRIMARY KEY, nombre text, direccion text, pais text, departamento text);
CREATE TABLE aseguradora (id int PRIMARY KEY, nombre text, nit text, descripcion text, precio real);
CREATE TABLE pais (id int PRIMARY KEY, nombre text, continente text, codArea int);
CREATE TABLE cliente (nit int PRIMARY KEY, nombre text, apellido text, telefono text, dob date, nacionalidad text, sexo text, correo text, foto text, direccion text, estadoCivil text, idPais int REFERENCES pais (id), modeloCarro int REFERENCES carro (modelo), idEmpresa int REFERENCES empresa (id), idAseguradora int REFERENCES aseguradora (id));
--catalogos
--insert carro
INSERT INTO carro VALUES(12345,'mazda','verde','mecanico');
INSERT INTO carro VALUES(12645,'hyundai','amarillo','automatico');
INSERT INTO carro VALUES(12445,'mazda','verde','mecanico');
INSERT INTO carro VALUES(23443,'honda','amarillo','mecanico');
--insert empresa
INSERT INTO empresa VALUES(1,'Stark Industries','por la torre','USA','');
INSERT INTO empresa VALUES(2,'Oscorp','NY','USA','');
INSERT INTO empresa VALUES(3,'ACME','meek meek','USA','');
--insert pais
INSERT INTO pais VALUES(1,'Guatemala','America',502);
INSERT INTO pais VALUES(2,'Guam','Oceania',1);
INSERT INTO pais VALUES(3,'Holanda','Europa',31);
--insert aseguradora
INSERT INTO aseguradora VALUES(1,'Universales','111111','buenos seguros',20000);
INSERT INTO aseguradora VALUES(2,'El Roble','222222','no se seguros',20000);
--triggers
--revisa que la foto no sea nula o el que el campo este vacio y asigna una foto por default
CREATE FUNCTION foto_trigger_function()
RETURNS trigger AS '
BEGIN
  IF NEW.foto IS NULL OR NEW.foto = '''' THEN
    NEW.foto := ''default.png'';
  END IF;
  RETURN NEW;
END' LANGUAGE 'plpgsql'

CREATE TRIGGER foto_trigger
BEFORE INSERT ON cliente
FOR EACH ROW
EXECUTE PROCEDURE foto_trigger_function()