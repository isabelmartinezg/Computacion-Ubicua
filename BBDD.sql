create table datos (
id				int(11) NOT NULL AUTO_INCREMENT ,
temperatura		float NOT NULL,
humedad			float NOT NULL,
presion			float NOT NULL,
velocidad		float NOT NULL,
calidadAire		float NOT NULL,
fecha 			timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
primary key (id)
);

create user 'root' identified by '123456789%';
grant all on *.* to 'root' with grant option;