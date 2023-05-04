--Drop de las tablas
DROP TABLE Instalaciones;
DROP TABLE Zonas;
DROP TABLE Concesiones;
DROP TABLE Faltas;
DROP TABLE Fianza;
DROP TABLE Espacios;
DROP TABLE Titular;
DROP TABLE Mercado;


--Creacion de los dominios
CREATE DOMAIN tipoDeZona AS VARCHAR(14)
	CHECK (VALUE IN ('zona comercial', 'puesto'));
	
CREATE DOMAIN tipoGestion AS VARCHAR(9)
	CHECK (VALUE IN ('directa', 'indirecta'));
	
CREATE DOMAIN tipoConcesion AS VARCHAR(12)
	CHECK (VALUE IN ('adjudicacion', 'renovacion', 'traspaso', 'especial'));
	
CREATE DOMAIN tipoPersona AS VARCHAR(8)
	CHECK (VALUE IN ('juridica', 'fisica'));
	
CREATE DOMAIN estadoFianza AS VARCHAR(9)
	CHECK (VALUE IN ('pagada', 'no pagada', 'devuelta'));
	
CREATE DOMAIN tipoProducto AS VARCHAR(12)
	CHECK (VALUE IN ('alimentacion', 'otros'));
	
CREATE DOMAIN gravedadFaltas AS VARCHAR(9)
	CHECK (VALUE IN ('leve', 'grave', 'muy grave'));
	
CREATE DOMAIN tipoAdjudicacion AS VARCHAR(8)
	CHECK (VALUE IN ('fijo', 'especial', 'temporal', 'ayuntamiento'));
	
CREATE DOMAIN tipoInstalacion AS VARCHAR(14)
	CHECK (VALUE IN ('complementaria', 'auxiliar'));
	
--Creacion de las tablas
CREATE TABLE Titular(
	dni CHAR(10),
	persona tipoPersona NOT NULL,
	nombre VARCHAR(30) NOT NULL,
	apellidos VARCHAR(200),
	PRIMARY KEY (dni),
	CONSTRAINT ayuntamiento_bilbao CHECK (
		(dni = '00000000A' AND persona = 'juridica' AND nombre = 'Ayuntamiento de Bilbao')
		OR (dni <> '00000000A')
	)
);

CREATE TABLE Mercado(
	nombre VARCHAR(100),
	gestion tipoGestion NOT NULL,
	horario_apertura TIME NOT NULL,
	horario_cierre TIME NOT NULL,
	PRIMARY KEY (nombre)
);

CREATE TABLE Espacios(
	id VARCHAR(10),
	nombre VARCHAR(100) NOT NULL,
	calle VARCHAR(100) NOT NULL,
	numero INTEGER,
	falta_leve BOOLEAN DEFAULT FALSE,
	falta_grave BOOLEAN DEFAULT FALSE,
	obras BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (id),
	FOREIGN KEY (nombre) REFERENCES Mercado
		ON DELETE CASCADE,
	CONSTRAINT numero_positivo_esp CHECK (numero > 0)
);

CREATE TABLE Fianza(
	id VARCHAR(10),
	dni CHAR(10) NOT NULL,
	fecha DATE NOT NULL,
	cantidad REAL NOT NULL,
	estado estadoFianza NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (dni) REFERENCES Titular
		ON DELETE CASCADE,
	CONSTRAINT fianza_positiva_fia CHECK (cantidad >= 0)
);

CREATE TABLE Faltas(
	id VARCHAR(10),
	dni CHAR(10) NOT NULL,
	idE VARCHAR(10) NOT NULL,
	motivo VARCHAR(1000),
	fecha DATE NOT NULL,
	gravedad gravedadFaltas NOT NULL,
	informe VARCHAR(1000),
	tiempo_sancion INTEGER,
	pago_sancion REAL,
	PRIMARY KEY (id),
	FOREIGN KEY (dni) REFERENCES Titular
		ON DELETE CASCADE,
	FOREIGN KEY (idE) REFERENCES Espacios (id),
	CONSTRAINT comprobar_gravedad_falta CHECK (
		(gravedad = 'leve' AND tiempo_sancion = NULL AND (pago_sancion > 0 AND pago_sancion <= 50000 OR pago_sancion = NULL))
		OR (gravedad = 'grave' AND (tiempo_sancion <= 60 AND tiempo_sancion >= 0 OR tiempo_sancion = NULL) AND (pago_sancion <= 150000 AND pago_sancion > 0 OR pago_sancion = NULL))
		OR (gravedad = 'muy grave' AND (tiempo_sancion <= 90 AND tiempo_sancion >= 0 OR tiempo_sancion = NULL) AND (pago_sancion <= 300000 AND pago_sancion > 0 OR pago_sancion = NULL))
	)
);

CREATE TABLE Concesiones(
	fecha_inicio DATE,
	dni CHAR(10),
	id VARCHAR(10),
	fecha_final DATE,
	condiciones VARCHAR(1000),
	precio REAL NOT NULL,
	tipo_concesion tipoConcesion NOT NULL,
	condicion_adjudicacion tipoAdjudicacion NOT NULL,
	PRIMARY KEY (fecha_inicio, dni, id),
	FOREIGN KEY (dni) REFERENCES Titular,
	FOREIGN KEY (id) REFERENCES Espacios
		ON DELETE CASCADE,
	CONSTRAINT comprobar_tipo_concesion CHECK (
		(tipo_concesion = 'traspaso' AND condicion_adjudicacion = 'fijo' AND fecha_final IS NOT NULL)
		OR (tipo_concesion <> 'traspaso' AND fecha_final IS NOT NULL)
		OR (tipo_concesion = 'especial' AND condicion_adjudicacion = 'ayuntamiento' AND dni = '00000000A' AND fecha_final IS NULL)
		OR (tipo_concesion <> 'especial' AND fecha_final IS NOT NULL)
	),
	CONSTRAINT orden_fechas CHECK (fecha_inicio<fecha_final),
	CONSTRAINT precio_positivo_con CHECK (precio >= 0)
);

CREATE TABLE Instalaciones(
	id VARCHAR(10),
	tipo tipoInstalacion NOT NULL,
	descripcion VARCHAR(1000) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES Espacios
		ON DELETE CASCADE
);

CREATE TABLE Zonas(
	id VARCHAR(10),
	espacio REAL NOT NULL,
	producto tipoProducto NOT NULL,
	tipo tipoDeZona NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES Espacios
		ON DELETE CASCADE,
	CONSTRAINT espacio_positivo_zon CHECK (espacio > 0)
);

--Insertar tuplas
insert into Mercado values ('Mercado de la Ribera', 'directa','8:00:00','20:00:00');
insert into Mercado values ('Mercado del Ensanche', 'indirecta','9:30:00','22:00:00');

insert into Titular values ('10314892M', 'fisica','Marta','Gomez Lopez');
insert into Titular values ('20235400M', 'fisica','Juan','Martinez Garcia');
insert into Titular values ('32370923B', 'fisica','Pablo','Pacheco Jimenez');
insert into Titular values ('45908140W', 'fisica','Irene','Tartilan Aranda');
insert into Titular values ('56573729F', 'fisica','Natalia','Sanchez Saez');
insert into Titular values ('665737297', 'juridica','Dulces y conservas Helios S.A.',NULL);
insert into Titular values ('00000000A', 'juridica','Ayuntamiento de Bilbao',NULL);

insert into Espacios values ('es01', 'Mercado de la Ribera','Rodriguez Arias', NULL,TRUE,FALSE);
insert into Espacios values ('es02', 'Mercado de la Ribera','Licenciado Poza', NULL,FALSE,FALSE);
insert into Espacios values ('es03', 'Mercado de la Ribera','Iparguirre', NULL,FALSE,TRUE);
insert into Espacios values ('es04', 'Mercado de la Ribera','Maria Diaz de Haro', NULL,TRUE,FALSE);
insert into Espacios values ('es05', 'Mercado del Ensanche','Gordoniz Kalea',12,FALSE,FALSE);
insert into Espacios values ('es06', 'Mercado del Ensanche','Egaña',89,FALSE,FALSE);
insert into Espacios values ('es07', 'Mercado del Ensanche','Fernandez del Campo Kalea',2,FALSE,TRUE);
insert into Espacios values ('es08', 'Mercado del Ensanche','Gordoniz Kalea',34,FALSE,TRUE);
insert into Espacios values ('es09', 'Mercado del Ensanche','Godorniz Kalea',36,FALSE,TRUE);
insert into Espacios values ('es10', 'Mercado de la Ribera','Iparguirre', NULL,FALSE,TRUE);
insert into Espacios values ('es11', 'Mercado del Ensanche','Fernandez del Campo Kalea', 4,TRUE,TRUE);
insert into Espacios values ('es12', 'Mercado del Ensanche','Egaña',87,FALSE,TRUE);
insert into Espacios values ('es13', 'Mercado de la Ribera','Marta Diaz de Haro', NULL,FALSE,FALSE);
insert into Espacios values ('es14', 'Mercado del Ensanche','Fernandez del Campo Kalea',6,FALSE,TRUE);

insert into Concesiones values ('11-11-2021','10314892M','es01','12-31-2022','condidiciones1','12334.95','traspaso','fijo');
insert into Concesiones values ('12-13-2020','10314892M','es11','12-02-2021','condidiciones2','645254.95','traspaso','fijo');
insert into Concesiones values ('12-03-2021','20235400M','es11','12-04-2022','condidiciones3','124.95','traspaso','fijo');
insert into Concesiones values ('12-04-2020','32370923B','es12','12-24-2021','condidiciones4','286734.95','traspaso','fijo');
insert into Concesiones values ('12-25-2021','32370923B','es12','12-25-2022','condidiciones5','9934.95','traspaso','fijo');
insert into Concesiones values ('12-02-2020','32370923B','es13','02-12-2021','condidiciones6','175734.95','adjudicacion','fijo');
insert into Concesiones values ('02-13-2021','45908140W','es13','03-13-2021','condidiciones7','875834.95','adjudicacion','temporal');
insert into Concesiones values ('12-10-2020','56573729F','es06','12-10-2023','condidiciones8','1834.95','adjudicacion','especial');
insert into Concesiones values ('12-10-2021','665737297','es04','12-31-2021','condidiciones9','884.95','renovacion','temporal');
insert into Concesiones values ('12-11-2021','665737297','es07','12-15-2021','condidiciones10','10034.95','renovacion','temporal');
insert into Concesiones values ('12-15-2021','665737297','es07','01-20-2022','condidiciones11','334.95','renovacion','temporal');

insert into Faltas values ('fa01','10314892M','es01','motivo1','01/10/2021','leve','informe1',NULL, NULL);
insert into Faltas values ('fa02','10314892M','es02','motivo2','02/10/2021','grave','informe2',NULL,'51000.50');
insert into Faltas values ('fa03','32370923B','es03','motivo3','03/10/2021','leve','informe3',NULL, '200');
insert into Faltas values ('fa04','32370923B','es03','motivo4','04/10/2021','leve','informe4',NULL, '4000');
insert into Faltas values ('fa05','45908140W','es05','motivo5','05/10/2021','leve','informe5',NULL, NULL);
insert into Faltas values ('fa06','56573729F','es06','motivo6','06/10/2021','grave','informe6',NULL,'55000.50');
insert into Faltas values ('fa07','665737297','es06','motivo7','07/10/2021','leve','informe7',NULL,'26.50');

insert into Instalaciones values ('es01','complementaria','descripcion1');
insert into Instalaciones values ('es08','complementaria','descripcion2');
insert into Instalaciones values ('es03','complementaria','descripcion3');
insert into Instalaciones values ('es04','auxiliar','descripcion4');
insert into Instalaciones values ('es05','complementaria','descripcion5');
insert into Instalaciones values ('es06','complementaria','descripcion6');
insert into Instalaciones values ('es09','auxiliar','descripcion7');

insert into Zonas values ('es10','155.89','alimentacion','zona comercial');
insert into Zonas values ('es02','355.89','alimentacion','puesto');
insert into Zonas values ('es11','94.89','alimentacion','zona comercial');
insert into Zonas values ('es12','285.89','alimentacion','zona comercial');
insert into Zonas values ('es13','545.89','alimentacion','zona comercial');
insert into Zonas values ('es14','645.89','otros','zona comercial');
insert into Zonas values ('es07','485.89','alimentacion','puesto');

insert into Fianza values ('fi01','10314892M','10-29-2021','1233.99','pagada');
insert into Fianza values ('fi02','10314892M','11-30-2021','533.54','no pagada');
insert into Fianza values ('fi03','20235400M','05-09-2021','1275.50','no pagada');
insert into Fianza values ('fi04','32370923B','10-14-2021','18855.99','pagada');
insert into Fianza values ('fi05','45908140W','11-23-2021','857853.99','devuelta');
insert into Fianza values ('fi06','56573729F','1-12-2021','48585.50','pagada');
insert into Fianza values ('fi07','665737297','09-28-2021','874573.50','no pagada');
insert into Fianza values ('fi08','665737297','10-21-2021','18583.394','pagada');

--Consultas implementadas
SELECT T.nombre, T.apellidos
FROM Titular T, Concesiones C, Espacios E
WHERE E.id = 'es03' AND E.id = C.id AND T.dni = C.dni;

SELECT T.nombre, T.dni, F.fecha
FROM Titular T, Fianza F
WHERE F.estado = 'no pagada' ;

SELECT count(*)
FROM Titular T, Concesiones C
WHERE T.nombre = 'Pablo' AND T.dni = '32370923B' AND T.dni = C.dni;

SELECT F.dni, count(F.gravedad = 'muy graves') as numfaltasgraves
FROM Faltas F
GROUP BY F.dni, F.gravedad
HAVING count(*) >= ALL
	(SELECT count(*) as numfaltasgraves
	FROM Faltas F
	GROUP BY F.dni);