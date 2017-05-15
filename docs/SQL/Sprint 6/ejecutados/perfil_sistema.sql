-- Creacion de la tabla
CREATE SEQUENCE seguridad.perfil_sistema_id_seq INCREMENT 1 START 1
;

CREATE TABLE seguridad.perfil_sistema ( 
	id smallint DEFAULT NEXTVAL('seguridad.perfil_sistema_id_seq'::TEXT) NOT NULL,
	email_server_host varchar(99) NOT NULL,
	email_server_user varchar(99) NOT NULL,
	email_server_password varchar(99) NOT NULL,
	email_server_port integer NOT NULL,
	reportar_errores boolean NOT NULL,
	email_reportar_errores varchar(250),
	fecha_creacion timestamp NOT NULL,
	user_name varchar(50) NOT NULL
)
;

ALTER TABLE seguridad.perfil_sistema ADD CONSTRAINT PK_perfil_sistema 
	PRIMARY KEY (id)
;

-- Tabla de Auditoria
CREATE TABLE a_seguridad.a_perfil_sistema
(
  id smallint NOT NULL,
  email_server_host varchar(99) NOT NULL,
  email_server_user varchar(99) NOT NULL,
  email_server_password varchar(99) NOT NULL,
  email_server_port integer NOT NULL,
  reportar_errores boolean NOT NULL,
  email_reportar_errores varchar(250),
  fecha_creacion timestamp NOT NULL,
  user_name varchar(50) NOT NULL,
  fecha date NOT NULL DEFAULT ('now'::text)::date,
  hora time with time zone NOT NULL DEFAULT ('now'::text)::time with time zone,
  accion character varying(6) NOT NULL,
  id_a serial NOT NULL,
  CONSTRAINT a_perfil_sistema_pkey PRIMARY KEY (id_a)
)
;

-- Procedimiento Almacenado
CREATE OR REPLACE FUNCTION seguridad.fun_a_perfil_sistema() RETURNS TRIGGER AS $fun_a_perfil_sistema$
  DECLARE
  BEGIN
    IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE' ) THEN
	INSERT INTO a_seguridad.a_perfil_sistema
				(id, email_server_host, email_server_user, email_server_password, 
				email_server_port, reportar_errores, email_reportar_errores,
				fecha_creacion, user_name, accion)
			VALUES(NEW.id, NEW.email_server_host, NEW.email_server_user, NEW.email_server_password, 
				NEW.email_server_port, NEW.reportar_errores, NEW.email_reportar_errores,
				NEW.fecha_creacion, NEW.user_name, TG_OP);
	RETURN NULL;
    
    ELSEIF (TG_OP = 'DELETE') THEN
	INSERT INTO a_seguridad.a_perfil_sistema
				(id, email_server_host, email_server_user, email_server_password, 
				email_server_port, reportar_errores, email_reportar_errores, 
				fecha_creacion, user_name, accion)
			VALUES(OLD.id, OLD.email_server_host, OLD.email_server_user, OLD.email_server_password, 
				OLD.email_server_port, OLD.reportar_errores, OLD.email_reportar_errores, 
				OLD.fecha_creacion, OLD.user_name, TG_OP);
	RETURN OLD;
    END IF;
  END;
$fun_a_perfil_sistema$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trg_a_perfil_sistema_after 
    AFTER INSERT OR UPDATE
    ON seguridad.perfil_sistema FOR EACH ROW 
    EXECUTE PROCEDURE seguridad.fun_a_perfil_sistema();

CREATE TRIGGER trg_a_perfil_sistema_before
  BEFORE DELETE
  ON seguridad.perfil_sistema
  FOR EACH ROW
  EXECUTE PROCEDURE seguridad.fun_a_perfil_sistema();    

-- Datos de la tabla
INSERT INTO seguridad.perfil_sistema 
	VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', '2anl8qld6hd7k4rwihkt9jupl', '25', false, 'marisol.calderon@informatix.co', '2016-02-22', 'admin.prototipo');
	
-- Correccion en secuencia
ALTER SEQUENCE seguridad.perfil_sistema_id_seq RESTART WITH 2;
