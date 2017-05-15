--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.7
-- Started on 2017-03-16 09:03:21

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 19 (class 2615 OID 7790061)
-- Name: a_seguridad; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA a_seguridad;


ALTER SCHEMA a_seguridad OWNER TO postgres;

SET search_path = a_seguridad, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 313 (class 1259 OID 7790062)
-- Name: a_perfil_sistema; Type: TABLE; Schema: a_seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE a_perfil_sistema (
    id smallint NOT NULL,
    email_server_host character varying(99) NOT NULL,
    email_server_user character varying(99) NOT NULL,
    email_server_password character varying(99) NOT NULL,
    email_server_port integer NOT NULL,
    reportar_errores boolean NOT NULL,
    email_reportar_errores character varying(250),
    fecha_creacion timestamp without time zone NOT NULL,
    user_name character varying(50) NOT NULL,
    fecha date DEFAULT ('now'::text)::date NOT NULL,
    hora time with time zone DEFAULT ('now'::text)::time with time zone NOT NULL,
    accion character varying(6) NOT NULL,
    id_a integer NOT NULL
);


ALTER TABLE a_perfil_sistema OWNER TO postgres;

--
-- TOC entry 314 (class 1259 OID 7790070)
-- Name: a_perfil_sistema_id_a_seq; Type: SEQUENCE; Schema: a_seguridad; Owner: postgres
--

CREATE SEQUENCE a_perfil_sistema_id_a_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE a_perfil_sistema_id_a_seq OWNER TO postgres;

--
-- TOC entry 2601 (class 0 OID 0)
-- Dependencies: 314
-- Name: a_perfil_sistema_id_a_seq; Type: SEQUENCE OWNED BY; Schema: a_seguridad; Owner: postgres
--

ALTER SEQUENCE a_perfil_sistema_id_a_seq OWNED BY a_perfil_sistema.id_a;


--
-- TOC entry 2483 (class 2604 OID 7790072)
-- Name: id_a; Type: DEFAULT; Schema: a_seguridad; Owner: postgres
--

ALTER TABLE ONLY a_perfil_sistema ALTER COLUMN id_a SET DEFAULT nextval('a_perfil_sistema_id_a_seq'::regclass);


--
-- TOC entry 2595 (class 0 OID 7790062)
-- Dependencies: 313
-- Data for Name: a_perfil_sistema; Type: TABLE DATA; Schema: a_seguridad; Owner: postgres
--

INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', '2anl8qld6hd7k4rwihkt9jupl', 25, false, 'marisol.calderon@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2016-03-11', '12:33:37.613+00', 'INSERT', 1);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', '2anl8qld6hd7k4rwihkt9jupl', 30, false, 'marisol.calderon@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2016-04-26', '09:37:27.117-05', 'UPDATE', 2);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', '2anl8qld6hd7k4rwihkt9jupl', 25, false, 'marisol.calderon@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2016-04-26', '09:39:02.872-05', 'UPDATE', 3);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', 'fdbcae5c5244e8a26ce64bb91f2a97b7e0ecf595', 25, false, 'marisol.calderon@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2016-05-16', '14:02:43.754-05', 'UPDATE', 4);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', 'fdbcae5c5244e8a26ce64bb91f2a97b7e0ecf595', 25, false, 'marisol.calderon@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2016-06-15', '15:18:55.915-05', 'UPDATE', 5);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', 'fdbcae5c5244e8a26ce64bb91f2a97b7e0ecf595', 25, false, 'claudia.rey@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2017-02-15', '15:01:06.597-05', 'UPDATE', 6);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', 'fdbcae5c5244e8a26ce64bb91f2a97b7e0ecf595', 25, false, 'patricia.patinio@informatix.co', '2016-02-22 00:00:00', 'admin.prototipo', '2017-02-28', '21:42:44.562-05', 'UPDATE', 7);
INSERT INTO a_perfil_sistema (id, email_server_host, email_server_user, email_server_password, email_server_port, reportar_errores, email_reportar_errores, fecha_creacion, user_name, fecha, hora, accion, id_a) VALUES (1, 'smtp.gmail.com', 'amigo.informatix@gmail.com', 'fdbcae5c5244e8a26ce64bb91f2a97b7e0ecf595', 25, false, 'amigo.informatix@gmail.com', '2016-02-22 00:00:00', 'admin.prototipo', '2017-03-09', '14:56:33.964-05', 'UPDATE', 8);


--
-- TOC entry 2602 (class 0 OID 0)
-- Dependencies: 314
-- Name: a_perfil_sistema_id_a_seq; Type: SEQUENCE SET; Schema: a_seguridad; Owner: postgres
--

SELECT pg_catalog.setval('a_perfil_sistema_id_a_seq', 8, true);


--
-- TOC entry 2485 (class 2606 OID 7790074)
-- Name: a_perfil_sistema_pkey; Type: CONSTRAINT; Schema: a_seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY a_perfil_sistema
    ADD CONSTRAINT a_perfil_sistema_pkey PRIMARY KEY (id_a);


-- Completed on 2017-03-16 09:03:21

--
-- PostgreSQL database dump complete
--

