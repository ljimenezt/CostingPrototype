--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.7
-- Started on 2017-05-05 11:11:54

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 7 (class 2615 OID 32762127)
-- Name: a_seguridad; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA a_seguridad;


ALTER SCHEMA a_seguridad OWNER TO postgres;

--
-- TOC entry 8 (class 2615 OID 32762128)
-- Name: costs; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA costs;


ALTER SCHEMA costs OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 32762129)
-- Name: diesel; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA diesel;


ALTER SCHEMA diesel OWNER TO postgres;

--
-- TOC entry 10 (class 2615 OID 32762130)
-- Name: general; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA general;


ALTER SCHEMA general OWNER TO postgres;

--
-- TOC entry 11 (class 2615 OID 32762131)
-- Name: human_resources; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA human_resources;


ALTER SCHEMA human_resources OWNER TO postgres;

--
-- TOC entry 12 (class 2615 OID 32762132)
-- Name: kpi; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA kpi;


ALTER SCHEMA kpi OWNER TO postgres;

--
-- TOC entry 13 (class 2615 OID 32762133)
-- Name: life_cycle; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA life_cycle;


ALTER SCHEMA life_cycle OWNER TO postgres;

--
-- TOC entry 14 (class 2615 OID 32762134)
-- Name: machines; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA machines;


ALTER SCHEMA machines OWNER TO postgres;

--
-- TOC entry 15 (class 2615 OID 32762135)
-- Name: organizaciones; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA organizaciones;


ALTER SCHEMA organizaciones OWNER TO postgres;

--
-- TOC entry 16 (class 2615 OID 32762137)
-- Name: sales; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA sales;


ALTER SCHEMA sales OWNER TO postgres;

--
-- TOC entry 17 (class 2615 OID 32762138)
-- Name: seguridad; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA seguridad;


ALTER SCHEMA seguridad OWNER TO postgres;

--
-- TOC entry 18 (class 2615 OID 32762139)
-- Name: services; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA services;


ALTER SCHEMA services OWNER TO postgres;

--
-- TOC entry 19 (class 2615 OID 32762140)
-- Name: utz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA utz;


ALTER SCHEMA utz OWNER TO postgres;

--
-- TOC entry 20 (class 2615 OID 32762141)
-- Name: warehouse; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA warehouse;


ALTER SCHEMA warehouse OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3200 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 411 (class 1255 OID 32762142)
-- Name: fun_a_perfil_sistema(); Type: FUNCTION; Schema: seguridad; Owner: postgres
--

CREATE FUNCTION fun_a_perfil_sistema() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
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
$$;


ALTER FUNCTION seguridad.fun_a_perfil_sistema() OWNER TO postgres;

SET search_path = a_seguridad, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 187 (class 1259 OID 32762143)
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
-- TOC entry 188 (class 1259 OID 32762151)
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
-- TOC entry 3201 (class 0 OID 0)
-- Dependencies: 188
-- Name: a_perfil_sistema_id_a_seq; Type: SEQUENCE OWNED BY; Schema: a_seguridad; Owner: postgres
--

ALTER SEQUENCE a_perfil_sistema_id_a_seq OWNED BY a_perfil_sistema.id_a;


SET search_path = costs, pg_catalog;

--
-- TOC entry 189 (class 1259 OID 32762153)
-- Name: activities; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE activities (
    idactivity integer DEFAULT nextval(('costs.activities_id_seq'::text)::regclass) NOT NULL,
    id_crop integer NOT NULL,
    id_activity_name integer NOT NULL,
    sequence_number integer,
    initial_date_time_budget timestamp without time zone NOT NULL,
    final_date_time_budget timestamp without time zone NOT NULL,
    duration_budget real,
    cost_hr_budget real,
    cost_hr_actual real,
    cost_machines_eq_budget real,
    cost_machines_eq_actual real,
    cost_services_budget real,
    cost_services_actual real,
    cost_materials_budget real,
    cost_materials_actual real,
    general_cost_budget real,
    general_cost_actual real,
    dangerous boolean,
    hr_required boolean,
    materials_required boolean,
    service_required boolean,
    machine_required boolean,
    description text,
    initial_date_time_actual timestamp without time zone,
    final_date_time_actual timestamp without time zone,
    duration_actual real,
    id_cycle integer,
    routine boolean
);


ALTER TABLE activities OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 32762160)
-- Name: activities_and_hr; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE activities_and_hr (
    idactivity integer NOT NULL,
    idhr integer NOT NULL,
    initial_date_time_actual timestamp with time zone,
    initial_date_time_budget timestamp with time zone,
    final_date_time_actual timestamp with time zone,
    final_date_time_budget timestamp with time zone,
    total_cost_actual double precision,
    total_cost_budget double precision,
    duration_actual double precision,
    duration_budget double precision,
    total_hours double precision,
    normal_hours double precision,
    overtime_hours double precision,
    external_service boolean,
    task_work boolean,
    overtimepaymentid integer
);


ALTER TABLE activities_and_hr OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 32762163)
-- Name: activities_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE activities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE activities_id_seq OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 32762165)
-- Name: activity_machine; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE activity_machine (
    id_activity integer NOT NULL,
    id_machine integer NOT NULL,
    initial_date_time timestamp without time zone,
    final_date_time timestamp without time zone,
    duration_actual real,
    duration_budget real,
    depreciation_cost_budget real,
    depreciation_cost_actual real,
    maintenance_cost_budget real,
    maintenance_cost_actual real,
    consumables_cost_budget real,
    consumables_cost_actual real,
    insurance_cost_actual real,
    insurance_cost_budget real
);


ALTER TABLE activity_machine OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 32762168)
-- Name: activity_materials; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE activity_materials (
    idactivity integer NOT NULL,
    idmaterial integer NOT NULL,
    quantity_budget double precision NOT NULL,
    cost_budget double precision NOT NULL,
    quantity_actual double precision,
    cost_actual double precision,
    date_time timestamp without time zone NOT NULL
);


ALTER TABLE activity_materials OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 32762171)
-- Name: activity_team; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE activity_team (
    idteam smallint NOT NULL,
    idactivity integer NOT NULL
);


ALTER TABLE activity_team OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 32762174)
-- Name: allocation; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE allocation (
    idallocation integer DEFAULT nextval(('costs.allocation_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE allocation OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 32762178)
-- Name: allocation_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE allocation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE allocation_id_seq OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 32762180)
-- Name: consumable_use; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE consumable_use (
    id_activity integer NOT NULL,
    id_machine integer NOT NULL,
    initial_level real,
    final_level real,
    consumption_budget real,
    consumption_actual real,
    unit_cost_budget real,
    unit_cost_actual real,
    total_cost_budget real,
    total_cost_actual real,
    id_material integer NOT NULL,
    id_transaction integer NOT NULL
);


ALTER TABLE consumable_use OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 32762183)
-- Name: cost_types; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE cost_types (
    idcosttype integer DEFAULT nextval(('costs.cost_types_id_seq'::text)::regclass) NOT NULL,
    idallocation integer,
    name character varying(100) NOT NULL,
    hour boolean
);


ALTER TABLE cost_types OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 32762187)
-- Name: cost_types_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE cost_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cost_types_id_seq OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 32762189)
-- Name: cycle_activities_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE cycle_activities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cycle_activities_id_seq OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 32762191)
-- Name: cycle_standard_activities; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE cycle_standard_activities (
    idcycleactivity integer DEFAULT nextval(('costs.cycle_activities_id_seq'::text)::regclass) NOT NULL,
    id_crop_name integer NOT NULL,
    id_activity_name integer NOT NULL,
    sequence_number integer,
    duration real,
    cost_hr_ha real,
    cost_machines_eq_ha real,
    cost_services_ha real,
    cost_materials_ha real,
    general_cost_ha real,
    dangerous boolean,
    hr_required boolean,
    machine_required boolean,
    service_required boolean,
    materials_required boolean
);


ALTER TABLE cycle_standard_activities OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 32762195)
-- Name: general_cost_per_company; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE general_cost_per_company (
    idgeneralcostpercompany integer DEFAULT nextval(('costs.general_cost_per_company_id_seq'::text)::regclass) NOT NULL,
    idcosttype integer NOT NULL,
    value_budget real,
    value_actual real,
    registration_date timestamp without time zone,
    loaded_on_crop boolean
);


ALTER TABLE general_cost_per_company OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 32762199)
-- Name: general_cost_per_company_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE general_cost_per_company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE general_cost_per_company_id_seq OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 32762201)
-- Name: general_cost_per_crop; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE general_cost_per_crop (
    idgeneralcostpercrop integer DEFAULT nextval(('costs.general_cost_per_crop_id_seq'::text)::regclass) NOT NULL,
    idcosttype integer NOT NULL,
    idcrop integer NOT NULL,
    value_budget real,
    value_actual real
);


ALTER TABLE general_cost_per_crop OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 32762205)
-- Name: general_cost_per_crop_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE general_cost_per_crop_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE general_cost_per_crop_id_seq OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 32762207)
-- Name: general_cost_per_plot; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE general_cost_per_plot (
    idgeneralcostperfield integer DEFAULT nextval(('costs.general_cost_per_plot_id_seq'::text)::regclass) NOT NULL,
    idcosttype integer NOT NULL,
    idplot integer NOT NULL,
    value_budget real,
    value_actual real,
    registration_date timestamp without time zone,
    loaded_on_crop boolean
);


ALTER TABLE general_cost_per_plot OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 32762211)
-- Name: general_cost_per_plot_id_seq; Type: SEQUENCE; Schema: costs; Owner: postgres
--

CREATE SEQUENCE general_cost_per_plot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE general_cost_per_plot_id_seq OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 32762213)
-- Name: general_costs_company_allocated_crop; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE general_costs_company_allocated_crop (
    idgeneralcostpercompany integer NOT NULL,
    idcrop integer NOT NULL,
    value_budget real,
    value_actual real
);


ALTER TABLE general_costs_company_allocated_crop OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 32762216)
-- Name: general_costs_plot_allocated_crop; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE general_costs_plot_allocated_crop (
    idgeneralcostperplot integer NOT NULL,
    idcrop integer NOT NULL,
    value_budget real,
    value_actual real
);


ALTER TABLE general_costs_plot_allocated_crop OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 32762219)
-- Name: task_payment; Type: TABLE; Schema: costs; Owner: postgres; Tablespace: 
--

CREATE TABLE task_payment (
    idactivity integer NOT NULL,
    idhr integer NOT NULL,
    idpayment integer NOT NULL
);


ALTER TABLE task_payment OWNER TO postgres;

SET search_path = diesel, pg_catalog;

--
-- TOC entry 211 (class 1259 OID 32762222)
-- Name: consumable_resources; Type: TABLE; Schema: diesel; Owner: postgres; Tablespace: 
--

CREATE TABLE consumable_resources (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    unit_cost double precision NOT NULL,
    idmeasurementunits integer NOT NULL
);


ALTER TABLE consumable_resources OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 32762225)
-- Name: consumable_resources_id_seq; Type: SEQUENCE; Schema: diesel; Owner: postgres
--

CREATE SEQUENCE consumable_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE consumable_resources_id_seq OWNER TO postgres;

--
-- TOC entry 3202 (class 0 OID 0)
-- Dependencies: 212
-- Name: consumable_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: diesel; Owner: postgres
--

ALTER SEQUENCE consumable_resources_id_seq OWNED BY consumable_resources.id;


--
-- TOC entry 213 (class 1259 OID 32762227)
-- Name: engine_log; Type: TABLE; Schema: diesel; Owner: postgres; Tablespace: 
--

CREATE TABLE engine_log (
    id_engine_log integer NOT NULL,
    date timestamp without time zone NOT NULL,
    delivered_by integer,
    received_by integer,
    hour_on time without time zone NOT NULL,
    hour_off time without time zone NOT NULL,
    hourmeter_on double precision,
    hourmeter_off double precision,
    duration double precision,
    irrigation boolean NOT NULL,
    id_machine integer,
    id_activity integer,
    hydrometer double precision,
    hourmeter double precision
);


ALTER TABLE engine_log OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 32762230)
-- Name: engine_log_id_engine_log_seq; Type: SEQUENCE; Schema: diesel; Owner: postgres
--

CREATE SEQUENCE engine_log_id_engine_log_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE engine_log_id_engine_log_seq OWNER TO postgres;

--
-- TOC entry 3203 (class 0 OID 0)
-- Dependencies: 214
-- Name: engine_log_id_engine_log_seq; Type: SEQUENCE OWNED BY; Schema: diesel; Owner: postgres
--

ALTER SEQUENCE engine_log_id_engine_log_seq OWNED BY engine_log.id_engine_log;


--
-- TOC entry 215 (class 1259 OID 32762232)
-- Name: fuel_purchase; Type: TABLE; Schema: diesel; Owner: postgres; Tablespace: 
--

CREATE TABLE fuel_purchase (
    id_fuel_purchase integer NOT NULL,
    id_supplier integer NOT NULL,
    id_fuel_type integer NOT NULL,
    id_iva_rate integer,
    invoice_number character varying(50) NOT NULL,
    quantity double precision NOT NULL,
    unit_cost double precision NOT NULL,
    subtotal double precision NOT NULL,
    taxes double precision,
    total double precision NOT NULL,
    date_time timestamp without time zone NOT NULL,
    note text,
    invoice_document_link text
);


ALTER TABLE fuel_purchase OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 32762238)
-- Name: fuel_purchase_id_fuel_purchase_seq; Type: SEQUENCE; Schema: diesel; Owner: postgres
--

CREATE SEQUENCE fuel_purchase_id_fuel_purchase_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE fuel_purchase_id_fuel_purchase_seq OWNER TO postgres;

--
-- TOC entry 3204 (class 0 OID 0)
-- Dependencies: 216
-- Name: fuel_purchase_id_fuel_purchase_seq; Type: SEQUENCE OWNED BY; Schema: diesel; Owner: postgres
--

ALTER SEQUENCE fuel_purchase_id_fuel_purchase_seq OWNED BY fuel_purchase.id_fuel_purchase;


--
-- TOC entry 217 (class 1259 OID 32762240)
-- Name: fuel_usage_log; Type: TABLE; Schema: diesel; Owner: postgres; Tablespace: 
--

CREATE TABLE fuel_usage_log (
    id_fuel_usage integer NOT NULL,
    id_engine_log integer,
    id_fuel_purchase integer,
    id_transaction_type integer,
    consumption real,
    final_level real,
    deposited real,
    note text,
    date timestamp without time zone
);


ALTER TABLE fuel_usage_log OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 32762246)
-- Name: fuel_usage_log_id_fuel_usage_seq; Type: SEQUENCE; Schema: diesel; Owner: postgres
--

CREATE SEQUENCE fuel_usage_log_id_fuel_usage_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE fuel_usage_log_id_fuel_usage_seq OWNER TO postgres;

--
-- TOC entry 3205 (class 0 OID 0)
-- Dependencies: 218
-- Name: fuel_usage_log_id_fuel_usage_seq; Type: SEQUENCE OWNED BY; Schema: diesel; Owner: postgres
--

ALTER SEQUENCE fuel_usage_log_id_fuel_usage_seq OWNED BY fuel_usage_log.id_fuel_usage;


--
-- TOC entry 219 (class 1259 OID 32762248)
-- Name: irrigation_details; Type: TABLE; Schema: diesel; Owner: postgres; Tablespace: 
--

CREATE TABLE irrigation_details (
    id_irrigation_details integer NOT NULL,
    id_engine_log integer NOT NULL,
    id_machine integer NOT NULL,
    id_zone smallint NOT NULL,
    hidrometer_on double precision NOT NULL,
    hidrometer_off double precision NOT NULL,
    water_usage double precision NOT NULL,
    water_cost double precision,
    duration double precision,
    depreciation_cost double precision,
    maintenance_cost double precision,
    observations text
);


ALTER TABLE irrigation_details OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 32762254)
-- Name: irrigation_details_id_irrigation_details_seq; Type: SEQUENCE; Schema: diesel; Owner: postgres
--

CREATE SEQUENCE irrigation_details_id_irrigation_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE irrigation_details_id_irrigation_details_seq OWNER TO postgres;

--
-- TOC entry 3206 (class 0 OID 0)
-- Dependencies: 220
-- Name: irrigation_details_id_irrigation_details_seq; Type: SEQUENCE OWNED BY; Schema: diesel; Owner: postgres
--

ALTER SEQUENCE irrigation_details_id_irrigation_details_seq OWNED BY irrigation_details.id_irrigation_details;


--
-- TOC entry 221 (class 1259 OID 32762256)
-- Name: zone; Type: TABLE; Schema: diesel; Owner: postgres; Tablespace: 
--

CREATE TABLE zone (
    id integer NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE zone OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 32762259)
-- Name: zone_id_seq; Type: SEQUENCE; Schema: diesel; Owner: postgres
--

CREATE SEQUENCE zone_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE zone_id_seq OWNER TO postgres;

--
-- TOC entry 3207 (class 0 OID 0)
-- Dependencies: 222
-- Name: zone_id_seq; Type: SEQUENCE OWNED BY; Schema: diesel; Owner: postgres
--

ALTER SEQUENCE zone_id_seq OWNED BY zone.id;


SET search_path = general, pg_catalog;

--
-- TOC entry 223 (class 1259 OID 32762261)
-- Name: civil_status; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE civil_status (
    id integer DEFAULT nextval(('general.civil_status_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(200)
);


ALTER TABLE civil_status OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 32762265)
-- Name: civil_status_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE civil_status_id_seq
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE civil_status_id_seq OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 32762267)
-- Name: color; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE color (
    id integer NOT NULL,
    code character varying(7) NOT NULL,
    name character varying(50) NOT NULL,
    description text
);


ALTER TABLE color OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 32762273)
-- Name: color_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE color_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE color_id_seq OWNER TO postgres;

--
-- TOC entry 3208 (class 0 OID 0)
-- Dependencies: 226
-- Name: color_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE color_id_seq OWNED BY color.id;


--
-- TOC entry 227 (class 1259 OID 32762275)
-- Name: conversion_unidad; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE conversion_unidad (
    id_unidad_inicial integer NOT NULL,
    id_unidad_final integer NOT NULL,
    factor_conversion numeric(10,2) NOT NULL,
    user_name character varying(50) NOT NULL,
    fecha_creacion date NOT NULL
);


ALTER TABLE conversion_unidad OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 32762278)
-- Name: day; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE day (
    id integer NOT NULL,
    name character varying(10) NOT NULL
);


ALTER TABLE day OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 32762281)
-- Name: day_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE day_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE day_id_seq OWNER TO postgres;

--
-- TOC entry 3209 (class 0 OID 0)
-- Dependencies: 229
-- Name: day_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE day_id_seq OWNED BY day.id;


--
-- TOC entry 230 (class 1259 OID 32762283)
-- Name: departamento; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE departamento (
    id integer DEFAULT nextval(('general.departamento_id_seq'::text)::regclass) NOT NULL,
    id_pais smallint NOT NULL,
    nombre character varying(100) NOT NULL,
    codigo_postal integer,
    user_name character varying(50) NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia date
);


ALTER TABLE departamento OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 32762287)
-- Name: departamento_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE departamento_id_seq
    START WITH 1002
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE departamento_id_seq OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 32762289)
-- Name: estado; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE estado (
    id integer DEFAULT nextval(('general.estado_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion character varying(200),
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE estado OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 32762293)
-- Name: estado_civil_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE estado_civil_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE estado_civil_id_seq OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 32762295)
-- Name: estado_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE estado_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE estado_id_seq OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 32762297)
-- Name: holiday; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE holiday (
    id integer NOT NULL,
    date timestamp without time zone NOT NULL,
    description text NOT NULL
);


ALTER TABLE holiday OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 32762303)
-- Name: holiday_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE holiday_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE holiday_id_seq OWNER TO postgres;

--
-- TOC entry 3210 (class 0 OID 0)
-- Dependencies: 236
-- Name: holiday_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE holiday_id_seq OWNED BY holiday.id;


--
-- TOC entry 237 (class 1259 OID 32762305)
-- Name: iva_rate; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE iva_rate (
    id_iva integer NOT NULL,
    name character varying(100) NOT NULL,
    rate double precision NOT NULL
);


ALTER TABLE iva_rate OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 32762308)
-- Name: iva_rate_id_iva_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE iva_rate_id_iva_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE iva_rate_id_iva_seq OWNER TO postgres;

--
-- TOC entry 3211 (class 0 OID 0)
-- Dependencies: 238
-- Name: iva_rate_id_iva_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE iva_rate_id_iva_seq OWNED BY iva_rate.id_iva;


--
-- TOC entry 239 (class 1259 OID 32762310)
-- Name: medio_pago_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE medio_pago_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medio_pago_id_seq OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 32762312)
-- Name: moneda; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE moneda (
    id smallint DEFAULT nextval(('general.moneda_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(255) NOT NULL,
    codigo character varying(10) NOT NULL,
    simbolo character varying(5),
    descripcion character varying(255),
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE moneda OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 32762319)
-- Name: moneda_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE moneda_id_seq
    START WITH 13
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE moneda_id_seq OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 32762321)
-- Name: municipio; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE municipio (
    id integer DEFAULT nextval(('general.municipio_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(100) NOT NULL,
    codigo_postal integer,
    id_departamento integer NOT NULL,
    user_name character varying(50) NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone
);


ALTER TABLE municipio OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 32762325)
-- Name: municipio_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE municipio_id_seq
    START WITH 100002
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE municipio_id_seq OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 32762327)
-- Name: pais; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE pais (
    id smallint DEFAULT nextval(('general.pais_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(100) NOT NULL,
    codigo_postal integer,
    user_name character varying(50) NOT NULL,
    fecha_creacion date NOT NULL,
    fecha_fin_vigencia date,
    abreviatura_pais character varying(2),
    id_moneda smallint NOT NULL
);


ALTER TABLE pais OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 32762331)
-- Name: pais_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE pais_id_seq
    START WITH 100002
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pais_id_seq OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 32762333)
-- Name: system_profile; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE system_profile (
    id integer NOT NULL,
    break_start time without time zone NOT NULL,
    break_end time without time zone NOT NULL,
    break_duration double precision NOT NULL,
    activity_default_start time without time zone NOT NULL,
    activity_default_end time without time zone NOT NULL,
    activity_default_duration double precision NOT NULL
);


ALTER TABLE system_profile OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 32762336)
-- Name: system_profile_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE system_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE system_profile_id_seq OWNER TO postgres;

--
-- TOC entry 3212 (class 0 OID 0)
-- Dependencies: 247
-- Name: system_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE system_profile_id_seq OWNED BY system_profile.id;


--
-- TOC entry 248 (class 1259 OID 32762338)
-- Name: tipo_documento; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_documento (
    id smallint DEFAULT nextval(('general.tipo_documento_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(50) NOT NULL,
    descripcion character varying(100) NOT NULL,
    abreviatura character varying(10),
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE tipo_documento OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 32762342)
-- Name: tipo_documento_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE tipo_documento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tipo_documento_id_seq OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 32762344)
-- Name: tipo_unidad; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_unidad (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL,
    descripcion text,
    fecha_fin_vigencia date,
    fecha_inicio_vigencia date,
    user_name character varying(50) NOT NULL,
    fecha_registro date NOT NULL
);


ALTER TABLE tipo_unidad OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 32762350)
-- Name: tipo_unidad_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE tipo_unidad_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tipo_unidad_id_seq OWNER TO postgres;

--
-- TOC entry 3213 (class 0 OID 0)
-- Dependencies: 251
-- Name: tipo_unidad_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE tipo_unidad_id_seq OWNED BY tipo_unidad.id;


--
-- TOC entry 252 (class 1259 OID 32762352)
-- Name: type_food; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE type_food (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    description text,
    abbreviation character varying(3) NOT NULL
);


ALTER TABLE type_food OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 32762358)
-- Name: type_food_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE type_food_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE type_food_id_seq OWNER TO postgres;

--
-- TOC entry 3214 (class 0 OID 0)
-- Dependencies: 253
-- Name: type_food_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE type_food_id_seq OWNED BY type_food.id;


--
-- TOC entry 254 (class 1259 OID 32762360)
-- Name: type_unit; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE type_unit (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    description text
);


ALTER TABLE type_unit OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 32762366)
-- Name: type_unit_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE type_unit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE type_unit_id_seq OWNER TO postgres;

--
-- TOC entry 3215 (class 0 OID 0)
-- Dependencies: 255
-- Name: type_unit_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE type_unit_id_seq OWNED BY type_unit.id;


--
-- TOC entry 256 (class 1259 OID 32762368)
-- Name: unidad_medida; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE unidad_medida (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL,
    abreviatura character varying(10) NOT NULL,
    id_tipo_unidad integer NOT NULL,
    fecha_registro date NOT NULL,
    fecha_inicio_vigencia date,
    user_name character varying(50) NOT NULL,
    fecha_fin_vigencia date
);


ALTER TABLE unidad_medida OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 32762371)
-- Name: unidad_medida_id_seq; Type: SEQUENCE; Schema: general; Owner: postgres
--

CREATE SEQUENCE unidad_medida_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unidad_medida_id_seq OWNER TO postgres;

--
-- TOC entry 3216 (class 0 OID 0)
-- Dependencies: 257
-- Name: unidad_medida_id_seq; Type: SEQUENCE OWNED BY; Schema: general; Owner: postgres
--

ALTER SEQUENCE unidad_medida_id_seq OWNED BY unidad_medida.id;


--
-- TOC entry 258 (class 1259 OID 32762373)
-- Name: unit_conversions; Type: TABLE; Schema: general; Owner: postgres; Tablespace: 
--

CREATE TABLE unit_conversions (
    id_original_unit integer NOT NULL,
    id_final_unit integer NOT NULL,
    conversion_factor double precision NOT NULL
);


ALTER TABLE unit_conversions OWNER TO postgres;

SET search_path = human_resources, pg_catalog;

--
-- TOC entry 259 (class 1259 OID 32762376)
-- Name: assist_control; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE assist_control (
    id integer NOT NULL,
    idhr integer NOT NULL,
    date timestamp without time zone NOT NULL,
    absent boolean NOT NULL,
    observations text
);


ALTER TABLE assist_control OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 32762382)
-- Name: assist_control_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE assist_control_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE assist_control_id_seq OWNER TO postgres;

--
-- TOC entry 3217 (class 0 OID 0)
-- Dependencies: 260
-- Name: assist_control_id_seq; Type: SEQUENCE OWNED BY; Schema: human_resources; Owner: postgres
--

ALTER SEQUENCE assist_control_id_seq OWNED BY assist_control.id;


--
-- TOC entry 261 (class 1259 OID 32762384)
-- Name: contract; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE contract (
    id integer DEFAULT nextval(('human_resources.contract_id_seq'::text)::regclass) NOT NULL,
    id_hr integer NOT NULL,
    observations character varying(300),
    is_test_period boolean,
    contract_end_date timestamp without time zone,
    created timestamp without time zone NOT NULL,
    user_name character varying(50) NOT NULL
);


ALTER TABLE contract OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 32762388)
-- Name: contract_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE contract_id_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE contract_id_seq OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 32762390)
-- Name: contract_type; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE contract_type (
    id smallint DEFAULT nextval(('human_resources.contract_type_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(50) NOT NULL,
    descripcion character varying(255)
);


ALTER TABLE contract_type OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 32762394)
-- Name: contract_type_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE contract_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE contract_type_id_seq OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 32762396)
-- Name: day_type_food; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE day_type_food (
    id integer NOT NULL,
    id_type_food integer NOT NULL,
    id_day integer,
    after_holiday boolean
);


ALTER TABLE day_type_food OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 32762399)
-- Name: day_type_food_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE day_type_food_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE day_type_food_id_seq OWNER TO postgres;

--
-- TOC entry 3218 (class 0 OID 0)
-- Dependencies: 266
-- Name: day_type_food_id_seq; Type: SEQUENCE OWNED BY; Schema: human_resources; Owner: postgres
--

ALTER SEQUENCE day_type_food_id_seq OWNED BY day_type_food.id;


--
-- TOC entry 267 (class 1259 OID 32762401)
-- Name: food_control; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE food_control (
    id integer NOT NULL,
    id_hr integer,
    id_type_food integer NOT NULL,
    quantity integer NOT NULL,
    date timestamp without time zone NOT NULL,
    other text
);


ALTER TABLE food_control OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 32762407)
-- Name: food_control_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE food_control_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE food_control_id_seq OWNER TO postgres;

--
-- TOC entry 3219 (class 0 OID 0)
-- Dependencies: 268
-- Name: food_control_id_seq; Type: SEQUENCE OWNED BY; Schema: human_resources; Owner: postgres
--

ALTER SEQUENCE food_control_id_seq OWNED BY food_control.id;


--
-- TOC entry 269 (class 1259 OID 32762409)
-- Name: hr; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE hr (
    idhr integer DEFAULT nextval(('human_resources.hr_id_seq'::text)::regclass) NOT NULL,
    id_hr_type integer NOT NULL,
    id_payment_method integer NOT NULL,
    name character varying(100) NOT NULL,
    annual_wage real,
    hours_per_day real,
    total_number_days integer,
    hour_cost real,
    birth_date timestamp without time zone NOT NULL,
    maternity_breast_feeding boolean,
    central_facilities_access_start_date timestamp without time zone,
    central_facilities_access_end_date timestamp without time zone,
    labor_rights boolean,
    family_name character varying(100) NOT NULL,
    telefono character varying(150),
    genero character varying(1),
    direccion character varying(200),
    movil character varying(10),
    fax character varying(30),
    correo character varying(150),
    foto character varying(300),
    id_civil_status integer,
    id_pais_nac integer NOT NULL,
    id_departamento_nac integer,
    id_municipio_nac integer,
    id_pais_res integer NOT NULL,
    id_departamento_res integer,
    id_municipio_res integer,
    user_name character varying(50),
    hour_cost_overtime real
);


ALTER TABLE hr OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 32762416)
-- Name: hr_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE hr_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hr_id_seq OWNER TO postgres;

--
-- TOC entry 271 (class 1259 OID 32762418)
-- Name: hr_types; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE hr_types (
    idhrtype integer DEFAULT nextval(('human_resources.hr_types_id_seq'::text)::regclass) NOT NULL,
    name character varying(70) NOT NULL,
    description text
);


ALTER TABLE hr_types OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 32762425)
-- Name: hr_types_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE hr_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hr_types_id_seq OWNER TO postgres;

--
-- TOC entry 273 (class 1259 OID 32762427)
-- Name: novelty; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE novelty (
    id integer NOT NULL,
    idhr integer NOT NULL,
    idnoveltytype integer NOT NULL,
    initial_date_time timestamp without time zone NOT NULL,
    final_date_time timestamp without time zone NOT NULL,
    observations text
);


ALTER TABLE novelty OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 32762433)
-- Name: novelty_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE novelty_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE novelty_id_seq OWNER TO postgres;

--
-- TOC entry 3220 (class 0 OID 0)
-- Dependencies: 274
-- Name: novelty_id_seq; Type: SEQUENCE OWNED BY; Schema: human_resources; Owner: postgres
--

ALTER SEQUENCE novelty_id_seq OWNED BY novelty.id;


--
-- TOC entry 275 (class 1259 OID 32762435)
-- Name: novelty_type; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE novelty_type (
    id integer NOT NULL,
    name character varying(250) NOT NULL,
    description text,
    id_color integer NOT NULL
);


ALTER TABLE novelty_type OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 32762441)
-- Name: novelty_type_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE novelty_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE novelty_type_id_seq OWNER TO postgres;

--
-- TOC entry 3221 (class 0 OID 0)
-- Dependencies: 276
-- Name: novelty_type_id_seq; Type: SEQUENCE OWNED BY; Schema: human_resources; Owner: postgres
--

ALTER SEQUENCE novelty_type_id_seq OWNED BY novelty_type.id;


--
-- TOC entry 277 (class 1259 OID 32762443)
-- Name: overtime_payment_rate; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE overtime_payment_rate (
    overtimepaymentid integer DEFAULT nextval(('human_resources.overtime_payment_rate_id_seq'::text)::regclass) NOT NULL,
    overtime_rate_type character varying(100) NOT NULL,
    overtime_rate_ratio real,
    description character varying(250),
    default_rate boolean
);


ALTER TABLE overtime_payment_rate OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 32762447)
-- Name: overtime_payment_rate_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE overtime_payment_rate_id_seq
    START WITH 14
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE overtime_payment_rate_id_seq OWNER TO postgres;

--
-- TOC entry 279 (class 1259 OID 32762449)
-- Name: payment_methods; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE payment_methods (
    idpaymentmethod integer DEFAULT nextval(('human_resources.payment_methods_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(250)
);


ALTER TABLE payment_methods OWNER TO postgres;

--
-- TOC entry 280 (class 1259 OID 32762453)
-- Name: payment_methods_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE payment_methods_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE payment_methods_id_seq OWNER TO postgres;

--
-- TOC entry 281 (class 1259 OID 32762455)
-- Name: payments; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE payments (
    idpayment integer DEFAULT nextval(('human_resources.payments_id_seq'::text)::regclass) NOT NULL,
    idcontract integer NOT NULL,
    idhr integer NOT NULL,
    emission_date timestamp with time zone,
    initial_work_day timestamp with time zone,
    final_work_day timestamp with time zone,
    total_amount double precision,
    net_amount double precision,
    gross_amount double precision,
    task_work boolean
);


ALTER TABLE payments OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 32762459)
-- Name: payments_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE payments_id_seq OWNER TO postgres;

--
-- TOC entry 283 (class 1259 OID 32762461)
-- Name: team; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE team (
    idteam smallint DEFAULT nextval(('human_resources.team_id_seq'::text)::regclass) NOT NULL,
    name character varying(200) NOT NULL,
    size smallint NOT NULL,
    note text
);


ALTER TABLE team OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 32762468)
-- Name: team_id_seq; Type: SEQUENCE; Schema: human_resources; Owner: postgres
--

CREATE SEQUENCE team_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE team_id_seq OWNER TO postgres;

--
-- TOC entry 285 (class 1259 OID 32762470)
-- Name: team_members; Type: TABLE; Schema: human_resources; Owner: postgres; Tablespace: 
--

CREATE TABLE team_members (
    idteam smallint NOT NULL,
    idhr integer NOT NULL,
    lead boolean NOT NULL,
    statistician boolean NOT NULL
);


ALTER TABLE team_members OWNER TO postgres;

SET search_path = kpi, pg_catalog;

--
-- TOC entry 286 (class 1259 OID 32762473)
-- Name: bean_index; Type: TABLE; Schema: kpi; Owner: postgres; Tablespace: 
--

CREATE TABLE bean_index (
    idbeanindex integer DEFAULT nextval(('kpi.bean_index_id_seq'::text)::regclass) NOT NULL,
    sample_weight real NOT NULL,
    id_section integer NOT NULL,
    id_crop integer NOT NULL,
    cycle_number integer NOT NULL,
    date_bean_index timestamp without time zone NOT NULL
);


ALTER TABLE bean_index OWNER TO postgres;

--
-- TOC entry 287 (class 1259 OID 32762477)
-- Name: bean_index_id_seq; Type: SEQUENCE; Schema: kpi; Owner: postgres
--

CREATE SEQUENCE bean_index_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bean_index_id_seq OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 32762479)
-- Name: harvest_type_id_seq; Type: SEQUENCE; Schema: kpi; Owner: postgres
--

CREATE SEQUENCE harvest_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE harvest_type_id_seq OWNER TO postgres;

SET search_path = life_cycle, pg_catalog;

--
-- TOC entry 289 (class 1259 OID 32762481)
-- Name: activity_names; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE activity_names (
    idactivityname integer DEFAULT nextval(('life_cycle.activity_names_id_seq'::text)::regclass) NOT NULL,
    activity_name character varying(100) NOT NULL,
    description text,
    cycle boolean,
    harvest boolean,
    replanted boolean
);


ALTER TABLE activity_names OWNER TO postgres;

--
-- TOC entry 290 (class 1259 OID 32762488)
-- Name: activity_names_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE activity_names_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE activity_names_id_seq OWNER TO postgres;

--
-- TOC entry 291 (class 1259 OID 32762490)
-- Name: activity_plot; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE activity_plot (
    idactivity integer NOT NULL,
    idplot integer NOT NULL,
    tachos_collected integer,
    replated_plants integer
);


ALTER TABLE activity_plot OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 32762493)
-- Name: crop_names; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE crop_names (
    idcropname integer DEFAULT nextval(('life_cycle.crop_names_id_seq'::text)::regclass) NOT NULL,
    crop_name character varying(100) NOT NULL,
    description text
);


ALTER TABLE crop_names OWNER TO postgres;

--
-- TOC entry 293 (class 1259 OID 32762500)
-- Name: crop_names_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE crop_names_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE crop_names_id_seq OWNER TO postgres;

--
-- TOC entry 294 (class 1259 OID 32762502)
-- Name: crops; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE crops (
    idcrop integer DEFAULT nextval(('life_cycle.crops_id_seq'::text)::regclass) NOT NULL,
    id_crop_name integer NOT NULL,
    description character varying(200),
    initial_date timestamp without time zone,
    final_date timestamp without time zone,
    registration_year timestamp without time zone
);


ALTER TABLE crops OWNER TO postgres;

--
-- TOC entry 295 (class 1259 OID 32762506)
-- Name: crops_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE crops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE crops_id_seq OWNER TO postgres;

--
-- TOC entry 296 (class 1259 OID 32762508)
-- Name: crops_plots; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE crops_plots (
    id_crop integer NOT NULL,
    id_plot integer NOT NULL
);


ALTER TABLE crops_plots OWNER TO postgres;

--
-- TOC entry 297 (class 1259 OID 32762511)
-- Name: cycle; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE cycle (
    idcycle integer DEFAULT nextval(('life_cycle.cycle_id_seq'::text)::regclass) NOT NULL,
    id_crop integer NOT NULL,
    id_activity_name integer NOT NULL,
    cycle_number integer NOT NULL,
    initial_date_time timestamp without time zone NOT NULL,
    final_date_time timestamp without time zone NOT NULL,
    cost_hr_budget real,
    cost_hr_actual real,
    cost_machines_eq_budget real,
    cost_machines_eq_actual real,
    cost_services_budget real,
    cost_services_actual real,
    cost_materials_budget real,
    cost_materials_actual real,
    hr_required boolean,
    materials_required boolean,
    service_required boolean,
    machine_required boolean,
    dangerous boolean
);


ALTER TABLE cycle OWNER TO postgres;

--
-- TOC entry 298 (class 1259 OID 32762515)
-- Name: cycle_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE cycle_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cycle_id_seq OWNER TO postgres;

--
-- TOC entry 299 (class 1259 OID 32762517)
-- Name: details_harvest; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE details_harvest (
    id integer NOT NULL,
    idactivity integer NOT NULL,
    previous_sacks double precision,
    sacks_day double precision,
    current_sacks double precision,
    dispatch double precision,
    poundage double precision,
    total_sacks double precision
);


ALTER TABLE details_harvest OWNER TO postgres;

--
-- TOC entry 300 (class 1259 OID 32762520)
-- Name: details_harvest_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE details_harvest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE details_harvest_id_seq OWNER TO postgres;

--
-- TOC entry 3222 (class 0 OID 0)
-- Dependencies: 300
-- Name: details_harvest_id_seq; Type: SEQUENCE OWNED BY; Schema: life_cycle; Owner: postgres
--

ALTER SEQUENCE details_harvest_id_seq OWNED BY details_harvest.id;


--
-- TOC entry 301 (class 1259 OID 32762522)
-- Name: farm; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE farm (
    idfarm integer DEFAULT nextval(('life_cycle.farm_id_seq'::text)::regclass) NOT NULL,
    name character varying(70) NOT NULL,
    location_link_to_map real,
    other_field_address character varying(100),
    nit character varying(25),
    nombre_comercial character varying(100),
    logo character varying(200),
    direccion character varying(50),
    telefono character varying(150),
    correo character varying(50),
    fax character varying(20),
    movil character varying(20),
    id_pais integer,
    id_departamento integer,
    id_municipio integer
);


ALTER TABLE farm OWNER TO postgres;

--
-- TOC entry 302 (class 1259 OID 32762529)
-- Name: farm_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE farm_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE farm_id_seq OWNER TO postgres;

--
-- TOC entry 303 (class 1259 OID 32762531)
-- Name: plot; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE plot (
    idplot integer DEFAULT nextval(('life_cycle.plot_id_seq'::text)::regclass) NOT NULL,
    name character varying(70) NOT NULL,
    location_link_to_map real,
    size real,
    description text,
    number_of_trees integer,
    idsection integer,
    idcropname integer NOT NULL,
    position_row smallint,
    map_row smallint
);


ALTER TABLE plot OWNER TO postgres;

--
-- TOC entry 304 (class 1259 OID 32762538)
-- Name: plot_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE plot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE plot_id_seq OWNER TO postgres;

--
-- TOC entry 305 (class 1259 OID 32762540)
-- Name: pluviometer; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE pluviometer (
    id integer NOT NULL,
    date_record timestamp without time zone NOT NULL,
    reading integer NOT NULL
);


ALTER TABLE pluviometer OWNER TO postgres;

--
-- TOC entry 306 (class 1259 OID 32762543)
-- Name: pluviometer_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE pluviometer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pluviometer_id_seq OWNER TO postgres;

--
-- TOC entry 3223 (class 0 OID 0)
-- Dependencies: 306
-- Name: pluviometer_id_seq; Type: SEQUENCE OWNED BY; Schema: life_cycle; Owner: postgres
--

ALTER SEQUENCE pluviometer_id_seq OWNED BY pluviometer.id;


--
-- TOC entry 307 (class 1259 OID 32762545)
-- Name: section; Type: TABLE; Schema: life_cycle; Owner: postgres; Tablespace: 
--

CREATE TABLE section (
    idsection integer DEFAULT nextval(('life_cycle.section_id_seq'::text)::regclass) NOT NULL,
    name character varying(70) NOT NULL,
    description character varying(250),
    idcropname integer NOT NULL
);


ALTER TABLE section OWNER TO postgres;

--
-- TOC entry 308 (class 1259 OID 32762549)
-- Name: section_id_seq; Type: SEQUENCE; Schema: life_cycle; Owner: postgres
--

CREATE SEQUENCE section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE section_id_seq OWNER TO postgres;

SET search_path = machines, pg_catalog;

--
-- TOC entry 309 (class 1259 OID 32762551)
-- Name: consumable_types_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE consumable_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE consumable_types_id_seq OWNER TO postgres;

--
-- TOC entry 310 (class 1259 OID 32762553)
-- Name: fuel_types; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE fuel_types (
    idfueltype integer DEFAULT nextval(('machines.fuel_types_id_seq'::text)::regclass) NOT NULL,
    name character varying(70) NOT NULL,
    description text
);


ALTER TABLE fuel_types OWNER TO postgres;

--
-- TOC entry 311 (class 1259 OID 32762560)
-- Name: fuel_types_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE fuel_types_id_seq
    START WITH 6
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE fuel_types_id_seq OWNER TO postgres;

--
-- TOC entry 312 (class 1259 OID 32762562)
-- Name: insurance; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE insurance (
    idinsurance integer DEFAULT nextval(('machines.insurance_id_seq'::text)::regclass) NOT NULL,
    id_machine integer NOT NULL,
    date_time timestamp without time zone,
    total_cost_budget real,
    total_cost_actual real,
    description character varying(250) NOT NULL
);


ALTER TABLE insurance OWNER TO postgres;

--
-- TOC entry 313 (class 1259 OID 32762566)
-- Name: insurance_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE insurance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE insurance_id_seq OWNER TO postgres;

--
-- TOC entry 314 (class 1259 OID 32762568)
-- Name: machine_types; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE machine_types (
    idmachinetype integer DEFAULT nextval(('machines.machine_types_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description text
);


ALTER TABLE machine_types OWNER TO postgres;

--
-- TOC entry 315 (class 1259 OID 32762575)
-- Name: machine_types_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE machine_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE machine_types_id_seq OWNER TO postgres;

--
-- TOC entry 316 (class 1259 OID 32762577)
-- Name: machine_usage; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE machine_usage (
    idmachine integer NOT NULL,
    year integer NOT NULL,
    usage integer,
    hourly_maintenance double precision,
    hourly_insurance double precision,
    hourly_depreciation double precision
);


ALTER TABLE machine_usage OWNER TO postgres;

--
-- TOC entry 317 (class 1259 OID 32762580)
-- Name: machines; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE machines (
    idmachine integer DEFAULT nextval(('machines.machines_id_seq'::text)::regclass) NOT NULL,
    id_machine_type integer NOT NULL,
    fuel boolean,
    name character varying(70) NOT NULL,
    purchase_date timestamp without time zone,
    investment real,
    life_years real,
    residual_value real,
    depreciation real,
    horsepower real,
    kilowatts real,
    last_maintenance timestamp without time zone,
    serial_number character varying(20),
    id_fuel_type integer,
    fuel_consumption real,
    hydrometer real,
    hourmeter real
);


ALTER TABLE machines OWNER TO postgres;

--
-- TOC entry 318 (class 1259 OID 32762584)
-- Name: machines_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE machines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE machines_id_seq OWNER TO postgres;

--
-- TOC entry 319 (class 1259 OID 32762586)
-- Name: maintenance_and_calibration; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE maintenance_and_calibration (
    idmachine integer NOT NULL,
    idmaintenance integer DEFAULT nextval(('machines.maintenance_and_calibration_id_seq'::text)::regclass) NOT NULL,
    date_time timestamp with time zone,
    total_cost_budget double precision,
    total_cost_actual double precision,
    machine_equipement_in_good_condition boolean,
    description character varying(30)
);


ALTER TABLE maintenance_and_calibration OWNER TO postgres;

--
-- TOC entry 320 (class 1259 OID 32762590)
-- Name: maintenance_and_calibration_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE maintenance_and_calibration_id_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE maintenance_and_calibration_id_seq OWNER TO postgres;

--
-- TOC entry 321 (class 1259 OID 32762592)
-- Name: maintenance_lines; Type: TABLE; Schema: machines; Owner: postgres; Tablespace: 
--

CREATE TABLE maintenance_lines (
    idmaintenance integer NOT NULL,
    idmaintenanceline integer DEFAULT nextval(('machines.maintenance_lines_id_seq'::text)::regclass) NOT NULL,
    description text,
    cost_budget double precision,
    cost_actual double precision
);


ALTER TABLE maintenance_lines OWNER TO postgres;

--
-- TOC entry 322 (class 1259 OID 32762599)
-- Name: maintenance_lines_id_seq; Type: SEQUENCE; Schema: machines; Owner: postgres
--

CREATE SEQUENCE maintenance_lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE maintenance_lines_id_seq OWNER TO postgres;

SET search_path = organizaciones, pg_catalog;

--
-- TOC entry 323 (class 1259 OID 32762601)
-- Name: departamento_empresa_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE departamento_empresa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE departamento_empresa_id_seq OWNER TO postgres;

--
-- TOC entry 324 (class 1259 OID 32762603)
-- Name: empresa; Type: TABLE; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE TABLE empresa (
    id integer DEFAULT nextval(('organizaciones.empresa_id_seq'::text)::regclass) NOT NULL,
    id_organizacion integer,
    nit character varying(25) NOT NULL,
    nombre character varying(100) NOT NULL,
    logo character varying(200),
    direccion character varying(50) NOT NULL,
    telefono character varying(150) NOT NULL,
    correo character varying(50),
    fax character varying(20),
    movil character varying(20),
    id_pais smallint NOT NULL,
    id_departamento integer NOT NULL,
    id_municipio integer NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE empresa OWNER TO postgres;

--
-- TOC entry 325 (class 1259 OID 32762610)
-- Name: empresa_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE empresa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE empresa_id_seq OWNER TO postgres;

--
-- TOC entry 326 (class 1259 OID 32762612)
-- Name: empresa_persona; Type: TABLE; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE TABLE empresa_persona (
    id integer DEFAULT nextval(('organizaciones.empresa_persona_id_seq'::text)::regclass) NOT NULL,
    id_persona integer NOT NULL,
    id_empresa integer NOT NULL,
    id_farm integer,
    id_tipo_cargo integer NOT NULL,
    contacto boolean NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_inicio_vigencia timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE empresa_persona OWNER TO postgres;

--
-- TOC entry 327 (class 1259 OID 32762616)
-- Name: empresa_persona_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE empresa_persona_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE empresa_persona_id_seq OWNER TO postgres;

--
-- TOC entry 328 (class 1259 OID 32762618)
-- Name: finca_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE finca_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE finca_id_seq OWNER TO postgres;

--
-- TOC entry 329 (class 1259 OID 32762620)
-- Name: hacienda; Type: TABLE; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE TABLE hacienda (
    id integer DEFAULT nextval(('organizaciones.finca_id_seq'::text)::regclass) NOT NULL,
    id_empresa integer NOT NULL,
    codigo_registro character varying(50),
    nombre character varying(70) NOT NULL,
    ubicacion character varying(200) NOT NULL,
    id_contacto integer NOT NULL,
    foto character varying(200),
    id_pais smallint NOT NULL,
    id_departamento integer,
    id_municipio integer,
    area integer,
    id_unidad_medida integer,
    vereda character varying(100),
    longitud real,
    latitud real,
    altitud real,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE hacienda OWNER TO postgres;

--
-- TOC entry 330 (class 1259 OID 32762627)
-- Name: lote_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE lote_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lote_id_seq OWNER TO postgres;

--
-- TOC entry 331 (class 1259 OID 32762629)
-- Name: organizacion; Type: TABLE; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE TABLE organizacion (
    id integer DEFAULT nextval(('organizaciones.organizacion_id_seq'::text)::regclass) NOT NULL,
    id_tipo_documento smallint NOT NULL,
    nit character varying(100) NOT NULL,
    razon_social character varying(150) NOT NULL,
    direccion character varying(200),
    telefono character varying(100),
    logo character varying(200),
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE organizacion OWNER TO postgres;

--
-- TOC entry 332 (class 1259 OID 32762636)
-- Name: organizacion_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE organizacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE organizacion_id_seq OWNER TO postgres;

--
-- TOC entry 333 (class 1259 OID 32762638)
-- Name: sucursal_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE sucursal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sucursal_id_seq OWNER TO postgres;

--
-- TOC entry 398 (class 1259 OID 32764009)
-- Name: tipo_cargo; Type: TABLE; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE TABLE tipo_cargo (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL,
    funciones text,
    fecha_creacion date NOT NULL,
    user_name character varying(50) NOT NULL,
    fecha_fin_vigencia date
);


ALTER TABLE tipo_cargo OWNER TO postgres;

--
-- TOC entry 397 (class 1259 OID 32764007)
-- Name: tipo_cargo_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE tipo_cargo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tipo_cargo_id_seq OWNER TO postgres;

--
-- TOC entry 3224 (class 0 OID 0)
-- Dependencies: 397
-- Name: tipo_cargo_id_seq; Type: SEQUENCE OWNED BY; Schema: organizaciones; Owner: postgres
--

ALTER SEQUENCE tipo_cargo_id_seq OWNED BY tipo_cargo.id;


--
-- TOC entry 334 (class 1259 OID 32762640)
-- Name: zona_id_seq; Type: SEQUENCE; Schema: organizaciones; Owner: postgres
--

CREATE SEQUENCE zona_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE zona_id_seq OWNER TO postgres;

SET search_path = sales, pg_catalog;

--
-- TOC entry 335 (class 1259 OID 32762669)
-- Name: customer; Type: TABLE; Schema: sales; Owner: postgres; Tablespace: 
--

CREATE TABLE customer (
    idcustomer integer DEFAULT nextval(('sales.customer_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(100),
    city character varying(100),
    postal_code character varying(30),
    phone character varying(30),
    phone_2 character varying(30),
    fax character varying(30)
);


ALTER TABLE customer OWNER TO postgres;

--
-- TOC entry 336 (class 1259 OID 32762673)
-- Name: customer_id_seq; Type: SEQUENCE; Schema: sales; Owner: postgres
--

CREATE SEQUENCE customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE customer_id_seq OWNER TO postgres;

--
-- TOC entry 337 (class 1259 OID 32762675)
-- Name: final_products; Type: TABLE; Schema: sales; Owner: postgres; Tablespace: 
--

CREATE TABLE final_products (
    idfinalproduct integer DEFAULT nextval(('sales.final_products_id_seq'::text)::regclass) NOT NULL,
    id_material integer NOT NULL,
    id_crop integer,
    id_farm integer,
    date_time timestamp without time zone,
    quantity_actual real,
    quantity_budget real,
    unit_price_actual real,
    unit_price_budget real,
    total_price_actual real,
    total_price_budget real,
    real_quantity real,
    expire_date timestamp without time zone,
    quantity_level real,
    withdrawed_actual boolean,
    withdrawed_budget boolean,
    humidity_level real
);


ALTER TABLE final_products OWNER TO postgres;

--
-- TOC entry 338 (class 1259 OID 32762679)
-- Name: final_products_id_seq; Type: SEQUENCE; Schema: sales; Owner: postgres
--

CREATE SEQUENCE final_products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE final_products_id_seq OWNER TO postgres;

--
-- TOC entry 339 (class 1259 OID 32762681)
-- Name: order_header; Type: TABLE; Schema: sales; Owner: postgres; Tablespace: 
--

CREATE TABLE order_header (
    idorder integer DEFAULT nextval(('sales.order_header_id_seq'::text)::regclass) NOT NULL,
    id_customer integer NOT NULL,
    date_time timestamp without time zone,
    total_value real,
    delivered boolean
);


ALTER TABLE order_header OWNER TO postgres;

--
-- TOC entry 340 (class 1259 OID 32762685)
-- Name: order_header_id_seq; Type: SEQUENCE; Schema: sales; Owner: postgres
--

CREATE SEQUENCE order_header_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE order_header_id_seq OWNER TO postgres;

--
-- TOC entry 341 (class 1259 OID 32762687)
-- Name: order_lines; Type: TABLE; Schema: sales; Owner: postgres; Tablespace: 
--

CREATE TABLE order_lines (
    id_final_product integer NOT NULL,
    id_order integer NOT NULL,
    quantity real,
    unit_price real,
    total_price real
);


ALTER TABLE order_lines OWNER TO postgres;

SET search_path = seguridad, pg_catalog;

--
-- TOC entry 342 (class 1259 OID 32762690)
-- Name: icono; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE icono (
    id integer DEFAULT nextval(('seguridad.icono_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(200) NOT NULL,
    fecha_registro timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE icono OWNER TO postgres;

--
-- TOC entry 343 (class 1259 OID 32762694)
-- Name: icono_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE icono_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE icono_id_seq OWNER TO postgres;

--
-- TOC entry 344 (class 1259 OID 32762696)
-- Name: ip_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE ip_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE ip_id_seq OWNER TO postgres;

--
-- TOC entry 345 (class 1259 OID 32762698)
-- Name: menu_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE menu_id_seq OWNER TO postgres;

--
-- TOC entry 346 (class 1259 OID 32762700)
-- Name: menu; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE menu (
    id integer DEFAULT nextval('menu_id_seq'::regclass) NOT NULL,
    id_menu_padre integer,
    nombre character varying(100) NOT NULL,
    descripcion character varying(300),
    url character varying(200),
    fecha_inicio_vigencia date NOT NULL,
    fecha_fin_vigencia date,
    fecha_creacion date NOT NULL,
    orden integer NOT NULL,
    user_name character varying(50) NOT NULL,
    id_icono integer,
    visible boolean NOT NULL,
    numero_columnas_izquierda integer,
    numero_columnas_derecha integer,
    posicion_horizontal boolean,
    mostrar_ultimo_nivel boolean,
    posicion_izquierda boolean
);


ALTER TABLE menu OWNER TO postgres;

--
-- TOC entry 347 (class 1259 OID 32762707)
-- Name: metodo; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE metodo (
    id integer DEFAULT nextval(('seguridad.metodo_id_seq'::text)::regclass) NOT NULL,
    nombre_metodo character varying(100) NOT NULL,
    descripcion character varying(255) NOT NULL,
    user_name character varying(50) NOT NULL,
    nombre character varying(50) NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone
);


ALTER TABLE metodo OWNER TO postgres;

--
-- TOC entry 348 (class 1259 OID 32762711)
-- Name: metodo_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE metodo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE metodo_id_seq OWNER TO postgres;

--
-- TOC entry 349 (class 1259 OID 32762713)
-- Name: metodo_menu; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE metodo_menu (
    id integer NOT NULL,
    id_metodo integer NOT NULL,
    id_menu integer NOT NULL,
    user_name character varying(50) NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL
);


ALTER TABLE metodo_menu OWNER TO postgres;

--
-- TOC entry 350 (class 1259 OID 32762716)
-- Name: metodo_menu_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE metodo_menu_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE metodo_menu_id_seq OWNER TO postgres;

--
-- TOC entry 3225 (class 0 OID 0)
-- Dependencies: 350
-- Name: metodo_menu_id_seq; Type: SEQUENCE OWNED BY; Schema: seguridad; Owner: postgres
--

ALTER SEQUENCE metodo_menu_id_seq OWNED BY metodo_menu.id;


--
-- TOC entry 351 (class 1259 OID 32762718)
-- Name: modulo_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE modulo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE modulo_id_seq OWNER TO postgres;

--
-- TOC entry 352 (class 1259 OID 32762720)
-- Name: perfil_sistema; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE perfil_sistema (
    id smallint DEFAULT nextval(('seguridad.perfil_sistema_id_seq'::text)::regclass) NOT NULL,
    email_server_host character varying(99) NOT NULL,
    email_server_user character varying(99) NOT NULL,
    email_server_password character varying(99) NOT NULL,
    email_server_port integer NOT NULL,
    reportar_errores boolean NOT NULL,
    email_reportar_errores character varying(250),
    fecha_creacion timestamp without time zone NOT NULL,
    user_name character varying(50) NOT NULL
);


ALTER TABLE perfil_sistema OWNER TO postgres;

--
-- TOC entry 353 (class 1259 OID 32762727)
-- Name: perfil_sistema_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE perfil_sistema_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE perfil_sistema_id_seq OWNER TO postgres;

--
-- TOC entry 354 (class 1259 OID 32762729)
-- Name: permiso_persona_empresa; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE permiso_persona_empresa (
    id integer DEFAULT nextval(('seguridad.permiso_persona_empresa_id_seq'::text)::regclass) NOT NULL,
    id_empresa integer NOT NULL,
    id_persona integer NOT NULL,
    id_sucursal integer,
    id_farm integer NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_inicio_vigencia timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL,
    predeterminada boolean
);


ALTER TABLE permiso_persona_empresa OWNER TO postgres;

--
-- TOC entry 355 (class 1259 OID 32762733)
-- Name: permiso_persona_empresa_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE permiso_persona_empresa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE permiso_persona_empresa_id_seq OWNER TO postgres;

--
-- TOC entry 395 (class 1259 OID 32763939)
-- Name: persona; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE persona (
    id integer DEFAULT nextval(('seguridad.persona_id_seq'::text)::regclass) NOT NULL,
    id_tipo_documento smallint NOT NULL,
    documento character varying(20) NOT NULL,
    nombres character varying(100) NOT NULL,
    apellidos character varying(100),
    genero character varying(1) NOT NULL,
    foto character varying(300),
    fecha_nacimiento timestamp without time zone,
    id_municipio_nac integer NOT NULL,
    id_departamento_nac integer NOT NULL,
    id_pais_nac smallint NOT NULL,
    id_civil_status integer,
    nro_hijos integer,
    telefono character varying(150) NOT NULL,
    direccion character varying(200),
    correo character varying(150),
    fax character varying(30),
    movil character varying(10),
    id_municipio_res integer NOT NULL,
    id_departamento_res integer NOT NULL,
    id_pais_res smallint NOT NULL,
    id_usuario integer,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    user_name character varying(50) NOT NULL
);


ALTER TABLE persona OWNER TO postgres;

--
-- TOC entry 396 (class 1259 OID 32764005)
-- Name: persona_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE persona_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE persona_id_seq OWNER TO postgres;

--
-- TOC entry 356 (class 1259 OID 32762735)
-- Name: registro_sesion_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE registro_sesion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE registro_sesion_id_seq OWNER TO postgres;

--
-- TOC entry 357 (class 1259 OID 32762737)
-- Name: registro_sesion; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE registro_sesion (
    id bigint DEFAULT nextval('registro_sesion_id_seq'::regclass) NOT NULL,
    id_persona integer NOT NULL,
    fecha_inicio date,
    hora_inicio time without time zone,
    fecha_fin date,
    hora_fin time without time zone,
    user_name character varying(50)
);


ALTER TABLE registro_sesion OWNER TO postgres;

--
-- TOC entry 358 (class 1259 OID 32762741)
-- Name: rol_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE rol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rol_id_seq OWNER TO postgres;

--
-- TOC entry 359 (class 1259 OID 32762743)
-- Name: rol; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE rol (
    id smallint DEFAULT nextval('rol_id_seq'::regclass) NOT NULL,
    nombre character varying(50) NOT NULL,
    descripcion character varying(150),
    fecha_inicio_vigencia date NOT NULL,
    fecha_fin_vigencia date,
    fecha_creacion date NOT NULL,
    user_name character varying(50) NOT NULL
);


ALTER TABLE rol OWNER TO postgres;

--
-- TOC entry 360 (class 1259 OID 32762747)
-- Name: rol_menu; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE rol_menu (
    id_rol integer NOT NULL,
    id_menu integer NOT NULL,
    user_name character varying(50),
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone
);


ALTER TABLE rol_menu OWNER TO postgres;

--
-- TOC entry 361 (class 1259 OID 32762750)
-- Name: rol_metodo; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE rol_metodo (
    id_rol smallint NOT NULL,
    id_metodo integer NOT NULL,
    permiso character varying(1) NOT NULL,
    user_name character varying(50) NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    fecha_fin_vigencia timestamp without time zone,
    CONSTRAINT rol_metodo_permiso_check CHECK ((((((((permiso)::bpchar = 'A'::bpchar) OR ((permiso)::bpchar = 'S'::bpchar)) OR ((permiso)::bpchar = 'U'::bpchar)) OR ((permiso)::bpchar = 'I'::bpchar)) OR ((permiso)::bpchar = 'D'::bpchar)) OR ((permiso)::bpchar = 'L'::bpchar)))
);


ALTER TABLE rol_metodo OWNER TO postgres;

--
-- TOC entry 362 (class 1259 OID 32762754)
-- Name: rol_usuario; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE rol_usuario (
    id_rol smallint NOT NULL,
    id_usuario integer NOT NULL,
    fecha_inicio_vigencia date NOT NULL,
    fecha_fin_vigencia date,
    fecha_creacion date NOT NULL,
    user_name character varying(50) NOT NULL
);


ALTER TABLE rol_usuario OWNER TO postgres;

--
-- TOC entry 363 (class 1259 OID 32762757)
-- Name: usuario; Type: TABLE; Schema: seguridad; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id integer DEFAULT nextval(('seguridad.usuario_id_seq'::text)::regclass) NOT NULL,
    nombre character varying(100) NOT NULL,
    apellido character varying(100) NOT NULL,
    nombre_usuario character varying(50) NOT NULL,
    correo_electronico character varying(150),
    fecha_inicio_vigencia date NOT NULL,
    fecha_fin_vigencia date,
    fecha_creacion timestamp without time zone NOT NULL,
    password character varying(100) NOT NULL,
    user_name character varying(50) NOT NULL
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 364 (class 1259 OID 32762764)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: seguridad; Owner: postgres
--

CREATE SEQUENCE usuario_id_seq
    START WITH 51
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE usuario_id_seq OWNER TO postgres;

SET search_path = services, pg_catalog;

--
-- TOC entry 365 (class 1259 OID 32762766)
-- Name: ext_services_and_activities_id_seq; Type: SEQUENCE; Schema: services; Owner: postgres
--

CREATE SEQUENCE ext_services_and_activities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ext_services_and_activities_id_seq OWNER TO postgres;

--
-- TOC entry 366 (class 1259 OID 32762768)
-- Name: external_services_and_activities; Type: TABLE; Schema: services; Owner: postgres; Tablespace: 
--

CREATE TABLE external_services_and_activities (
    idservice integer DEFAULT nextval(('services.ext_services_and_activities_id_seq'::text)::regclass) NOT NULL,
    idactivity integer NOT NULL,
    idservicetype integer NOT NULL,
    idsupplier integer NOT NULL,
    description character varying(200),
    initial_date_time timestamp without time zone,
    final_date_time timestamp without time zone,
    duration_actual real,
    duration_budget real,
    task_work boolean,
    hour_cost_budget real,
    hour_cost_actual real,
    total_cost_budget real,
    total_cost_actual real
);


ALTER TABLE external_services_and_activities OWNER TO postgres;

--
-- TOC entry 367 (class 1259 OID 32762772)
-- Name: service_type; Type: TABLE; Schema: services; Owner: postgres; Tablespace: 
--

CREATE TABLE service_type (
    idservicetype integer DEFAULT nextval(('services.service_type_id_seq'::text)::regclass) NOT NULL,
    description character varying(200)
);


ALTER TABLE service_type OWNER TO postgres;

--
-- TOC entry 368 (class 1259 OID 32762776)
-- Name: service_type_id_seq; Type: SEQUENCE; Schema: services; Owner: postgres
--

CREATE SEQUENCE service_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE service_type_id_seq OWNER TO postgres;

SET search_path = utz, pg_catalog;

--
-- TOC entry 369 (class 1259 OID 32762778)
-- Name: activities_and_certifications; Type: TABLE; Schema: utz; Owner: postgres; Tablespace: 
--

CREATE TABLE activities_and_certifications (
    idactivity integer NOT NULL,
    idcertificationsandroles integer NOT NULL
);


ALTER TABLE activities_and_certifications OWNER TO postgres;

--
-- TOC entry 370 (class 1259 OID 32762781)
-- Name: certifications_and_roles; Type: TABLE; Schema: utz; Owner: postgres; Tablespace: 
--

CREATE TABLE certifications_and_roles (
    idcertificactionsandroles integer DEFAULT nextval(('utz.certifications_and_roles_id_seq'::text)::regclass) NOT NULL,
    name text,
    certifications boolean,
    role boolean,
    dangerous boolean,
    description text
);


ALTER TABLE certifications_and_roles OWNER TO postgres;

--
-- TOC entry 371 (class 1259 OID 32762788)
-- Name: certifications_and_roles_id_seq; Type: SEQUENCE; Schema: utz; Owner: postgres
--

CREATE SEQUENCE certifications_and_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE certifications_and_roles_id_seq OWNER TO postgres;

--
-- TOC entry 372 (class 1259 OID 32762790)
-- Name: hr_certifications_and_roles; Type: TABLE; Schema: utz; Owner: postgres; Tablespace: 
--

CREATE TABLE hr_certifications_and_roles (
    idhr integer NOT NULL,
    idcertificationsandroles integer NOT NULL,
    release_date timestamp with time zone,
    end_date timestamp with time zone,
    link_to_certificate text
);


ALTER TABLE hr_certifications_and_roles OWNER TO postgres;

SET search_path = warehouse, pg_catalog;

--
-- TOC entry 373 (class 1259 OID 32762796)
-- Name: deposits; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE deposits (
    iddeposit integer DEFAULT nextval(('warehouse.deposits_id_seq'::text)::regclass) NOT NULL,
    date_time timestamp without time zone,
    id_material integer NOT NULL,
    initial_quantity double precision,
    actual_quantity double precision,
    expire_date timestamp without time zone,
    location text,
    id_purchase_invoice integer NOT NULL,
    id_farm integer,
    quality_certificate_location_link text,
    total_cost double precision NOT NULL,
    id_measurement_unit integer NOT NULL,
    unit_cost double precision NOT NULL
);


ALTER TABLE deposits OWNER TO postgres;

--
-- TOC entry 374 (class 1259 OID 32762803)
-- Name: deposits_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE deposits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE deposits_id_seq OWNER TO postgres;

--
-- TOC entry 375 (class 1259 OID 32762805)
-- Name: diseases; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE diseases (
    iddisease integer DEFAULT nextval(('warehouse.diseases_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description text
);


ALTER TABLE diseases OWNER TO postgres;

--
-- TOC entry 376 (class 1259 OID 32762812)
-- Name: diseases_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE diseases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE diseases_id_seq OWNER TO postgres;

--
-- TOC entry 377 (class 1259 OID 32762814)
-- Name: invoice_items; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE invoice_items (
    id_invoice_item integer NOT NULL,
    id_purchase_invoice integer NOT NULL,
    id_material integer NOT NULL,
    id_iva_rate integer,
    quantity text NOT NULL,
    subtotal double precision NOT NULL,
    shipping double precision NOT NULL,
    packaging double precision NOT NULL,
    handling double precision NOT NULL,
    taxes double precision NOT NULL,
    discount double precision NOT NULL,
    unit_cost double precision NOT NULL,
    total double precision NOT NULL,
    note text
);


ALTER TABLE invoice_items OWNER TO postgres;

--
-- TOC entry 378 (class 1259 OID 32762820)
-- Name: invoice_items_id_invoice_item_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE invoice_items_id_invoice_item_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE invoice_items_id_invoice_item_seq OWNER TO postgres;

--
-- TOC entry 3226 (class 0 OID 0)
-- Dependencies: 378
-- Name: invoice_items_id_invoice_item_seq; Type: SEQUENCE OWNED BY; Schema: warehouse; Owner: postgres
--

ALTER SEQUENCE invoice_items_id_invoice_item_seq OWNED BY invoice_items.id_invoice_item;


--
-- TOC entry 379 (class 1259 OID 32762822)
-- Name: materials; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE materials (
    idmaterial integer DEFAULT nextval(('warehouse.materials_id_seq'::text)::regclass) NOT NULL,
    id_material_type integer NOT NULL,
    id_measurement_units integer NOT NULL,
    id_type_of_management integer NOT NULL,
    id_responsible integer NOT NULL,
    name character varying(100) NOT NULL,
    dangerous boolean,
    quantity_hectar_plot double precision,
    waiting_time_harvest integer,
    waiting_time_people integer,
    eu_approved boolean,
    usa_approved boolean,
    japan_approved boolean,
    classified_1a boolean,
    classified_1b boolean,
    pops boolean,
    included_unep boolean,
    included_pan boolean,
    product_trade_brand_name text,
    application_method text,
    max_residue_levels text,
    first_aid_info text,
    obsolete boolean,
    fao boolean,
    organic boolean,
    active_ingredients character varying(100),
    mineral_chemical_content character varying(100),
    presentation smallint DEFAULT 0 NOT NULL
);


ALTER TABLE materials OWNER TO postgres;

--
-- TOC entry 380 (class 1259 OID 32762830)
-- Name: materials_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE materials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE materials_id_seq OWNER TO postgres;

--
-- TOC entry 381 (class 1259 OID 32762832)
-- Name: materials_types; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE materials_types (
    idmaterialtype integer DEFAULT nextval(('warehouse.materials_types_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description text
);


ALTER TABLE materials_types OWNER TO postgres;

--
-- TOC entry 382 (class 1259 OID 32762839)
-- Name: materials_types_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE materials_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE materials_types_id_seq OWNER TO postgres;

--
-- TOC entry 383 (class 1259 OID 32762841)
-- Name: measurement_units; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE measurement_units (
    idmeasurementunits integer DEFAULT nextval(('warehouse.measurement_units_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description text
);


ALTER TABLE measurement_units OWNER TO postgres;

--
-- TOC entry 384 (class 1259 OID 32762848)
-- Name: measurement_units_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE measurement_units_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE measurement_units_id_seq OWNER TO postgres;

--
-- TOC entry 385 (class 1259 OID 32762850)
-- Name: purchase_invoices; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE purchase_invoices (
    idpurchaseinvoice integer DEFAULT nextval(('warehouse.purchase_invoices_id_seq'::text)::regclass) NOT NULL,
    date_time timestamp without time zone NOT NULL,
    id_supplier integer NOT NULL,
    total_value_actual double precision NOT NULL,
    invoice_number character varying(50) NOT NULL,
    subtotal double precision DEFAULT 0 NOT NULL,
    shipping double precision DEFAULT 0 NOT NULL,
    packaging double precision DEFAULT 0 NOT NULL,
    taxes double precision DEFAULT 0 NOT NULL,
    discount double precision DEFAULT 0 NOT NULL,
    note text,
    invoice_document_link character varying(100),
    reconcile boolean DEFAULT false
);


ALTER TABLE purchase_invoices OWNER TO postgres;

--
-- TOC entry 386 (class 1259 OID 32762863)
-- Name: purchase_invoices_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE purchase_invoices_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE purchase_invoices_id_seq OWNER TO postgres;

--
-- TOC entry 387 (class 1259 OID 32762865)
-- Name: suppliers; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE suppliers (
    idsupplier integer DEFAULT nextval(('warehouse.suppliers_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(100),
    city character varying(100),
    postal_code character varying(30),
    phone character varying(30),
    phone_2 character varying(30),
    fax character varying(30)
);


ALTER TABLE suppliers OWNER TO postgres;

--
-- TOC entry 388 (class 1259 OID 32762869)
-- Name: suppliers_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE suppliers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE suppliers_id_seq OWNER TO postgres;

--
-- TOC entry 389 (class 1259 OID 32762871)
-- Name: transaction_type; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE transaction_type (
    idtransactiontype integer DEFAULT nextval(('warehouse.transaction_types_id_seq'::text)::regclass) NOT NULL,
    transactiontype text NOT NULL
);


ALTER TABLE transaction_type OWNER TO postgres;

--
-- TOC entry 390 (class 1259 OID 32762878)
-- Name: transaction_types_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE transaction_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transaction_types_id_seq OWNER TO postgres;

--
-- TOC entry 391 (class 1259 OID 32762880)
-- Name: transactions; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE transactions (
    idtransaction integer DEFAULT nextval(('warehouse.transactions_id_seq'::text)::regclass) NOT NULL,
    date_time timestamp without time zone NOT NULL,
    quantity double precision NOT NULL,
    id_transaction_type integer NOT NULL,
    id_hr integer,
    id_activity integer,
    justification text,
    id_deposit integer NOT NULL,
    user_name character varying(50)
);


ALTER TABLE transactions OWNER TO postgres;

--
-- TOC entry 392 (class 1259 OID 32762887)
-- Name: transactions_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE transactions_id_seq OWNER TO postgres;

--
-- TOC entry 393 (class 1259 OID 32762889)
-- Name: type_of_management; Type: TABLE; Schema: warehouse; Owner: postgres; Tablespace: 
--

CREATE TABLE type_of_management (
    idtypeofmanagement integer DEFAULT nextval(('warehouse.type_of_management_id_seq'::text)::regclass) NOT NULL,
    name character varying(100) NOT NULL,
    description text
);


ALTER TABLE type_of_management OWNER TO postgres;

--
-- TOC entry 394 (class 1259 OID 32762896)
-- Name: type_of_management_id_seq; Type: SEQUENCE; Schema: warehouse; Owner: postgres
--

CREATE SEQUENCE type_of_management_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE type_of_management_id_seq OWNER TO postgres;

SET search_path = a_seguridad, pg_catalog;

--
-- TOC entry 2583 (class 2604 OID 32762898)
-- Name: id_a; Type: DEFAULT; Schema: a_seguridad; Owner: postgres
--

ALTER TABLE ONLY a_perfil_sistema ALTER COLUMN id_a SET DEFAULT nextval('a_perfil_sistema_id_a_seq'::regclass);


SET search_path = diesel, pg_catalog;

--
-- TOC entry 2591 (class 2604 OID 32762899)
-- Name: id; Type: DEFAULT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY consumable_resources ALTER COLUMN id SET DEFAULT nextval('consumable_resources_id_seq'::regclass);


--
-- TOC entry 2592 (class 2604 OID 32762900)
-- Name: id_engine_log; Type: DEFAULT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY engine_log ALTER COLUMN id_engine_log SET DEFAULT nextval('engine_log_id_engine_log_seq'::regclass);


--
-- TOC entry 2593 (class 2604 OID 32762901)
-- Name: id_fuel_purchase; Type: DEFAULT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_purchase ALTER COLUMN id_fuel_purchase SET DEFAULT nextval('fuel_purchase_id_fuel_purchase_seq'::regclass);


--
-- TOC entry 2594 (class 2604 OID 32762902)
-- Name: id_fuel_usage; Type: DEFAULT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_usage_log ALTER COLUMN id_fuel_usage SET DEFAULT nextval('fuel_usage_log_id_fuel_usage_seq'::regclass);


--
-- TOC entry 2595 (class 2604 OID 32762903)
-- Name: id_irrigation_details; Type: DEFAULT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY irrigation_details ALTER COLUMN id_irrigation_details SET DEFAULT nextval('irrigation_details_id_irrigation_details_seq'::regclass);


--
-- TOC entry 2596 (class 2604 OID 32762904)
-- Name: id; Type: DEFAULT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY zone ALTER COLUMN id SET DEFAULT nextval('zone_id_seq'::regclass);


SET search_path = general, pg_catalog;

--
-- TOC entry 2598 (class 2604 OID 32762905)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY color ALTER COLUMN id SET DEFAULT nextval('color_id_seq'::regclass);


--
-- TOC entry 2599 (class 2604 OID 32762906)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY day ALTER COLUMN id SET DEFAULT nextval('day_id_seq'::regclass);


--
-- TOC entry 2602 (class 2604 OID 32762907)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY holiday ALTER COLUMN id SET DEFAULT nextval('holiday_id_seq'::regclass);


--
-- TOC entry 2603 (class 2604 OID 32762908)
-- Name: id_iva; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY iva_rate ALTER COLUMN id_iva SET DEFAULT nextval('iva_rate_id_iva_seq'::regclass);


--
-- TOC entry 2607 (class 2604 OID 32762909)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY system_profile ALTER COLUMN id SET DEFAULT nextval('system_profile_id_seq'::regclass);


--
-- TOC entry 2609 (class 2604 OID 32762910)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY tipo_unidad ALTER COLUMN id SET DEFAULT nextval('tipo_unidad_id_seq'::regclass);


--
-- TOC entry 2610 (class 2604 OID 32762911)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY type_food ALTER COLUMN id SET DEFAULT nextval('type_food_id_seq'::regclass);


--
-- TOC entry 2611 (class 2604 OID 32762912)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY type_unit ALTER COLUMN id SET DEFAULT nextval('type_unit_id_seq'::regclass);


--
-- TOC entry 2612 (class 2604 OID 32762913)
-- Name: id; Type: DEFAULT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY unidad_medida ALTER COLUMN id SET DEFAULT nextval('unidad_medida_id_seq'::regclass);


SET search_path = human_resources, pg_catalog;

--
-- TOC entry 2613 (class 2604 OID 32762914)
-- Name: id; Type: DEFAULT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY assist_control ALTER COLUMN id SET DEFAULT nextval('assist_control_id_seq'::regclass);


--
-- TOC entry 2616 (class 2604 OID 32762915)
-- Name: id; Type: DEFAULT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY day_type_food ALTER COLUMN id SET DEFAULT nextval('day_type_food_id_seq'::regclass);


--
-- TOC entry 2617 (class 2604 OID 32762916)
-- Name: id; Type: DEFAULT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY food_control ALTER COLUMN id SET DEFAULT nextval('food_control_id_seq'::regclass);


--
-- TOC entry 2620 (class 2604 OID 32762917)
-- Name: id; Type: DEFAULT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY novelty ALTER COLUMN id SET DEFAULT nextval('novelty_id_seq'::regclass);


--
-- TOC entry 2621 (class 2604 OID 32762918)
-- Name: id; Type: DEFAULT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY novelty_type ALTER COLUMN id SET DEFAULT nextval('novelty_type_id_seq'::regclass);


SET search_path = life_cycle, pg_catalog;

--
-- TOC entry 2631 (class 2604 OID 32762919)
-- Name: id; Type: DEFAULT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY details_harvest ALTER COLUMN id SET DEFAULT nextval('details_harvest_id_seq'::regclass);


--
-- TOC entry 2634 (class 2604 OID 32762920)
-- Name: id; Type: DEFAULT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY pluviometer ALTER COLUMN id SET DEFAULT nextval('pluviometer_id_seq'::regclass);


SET search_path = organizaciones, pg_catalog;

--
-- TOC entry 2681 (class 2604 OID 32764012)
-- Name: id; Type: DEFAULT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY tipo_cargo ALTER COLUMN id SET DEFAULT nextval('tipo_cargo_id_seq'::regclass);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 2652 (class 2604 OID 32762922)
-- Name: id; Type: DEFAULT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY metodo_menu ALTER COLUMN id SET DEFAULT nextval('metodo_menu_id_seq'::regclass);


SET search_path = warehouse, pg_catalog;

--
-- TOC entry 2664 (class 2604 OID 32762923)
-- Name: id_invoice_item; Type: DEFAULT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY invoice_items ALTER COLUMN id_invoice_item SET DEFAULT nextval('invoice_items_id_invoice_item_seq'::regclass);


SET search_path = a_seguridad, pg_catalog;

--
-- TOC entry 2683 (class 2606 OID 32762925)
-- Name: a_perfil_sistema_pkey; Type: CONSTRAINT; Schema: a_seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY a_perfil_sistema
    ADD CONSTRAINT a_perfil_sistema_pkey PRIMARY KEY (id_a);


SET search_path = costs, pg_catalog;

--
-- TOC entry 2687 (class 2606 OID 32762927)
-- Name: pk_activities_and_hr; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activities_and_hr
    ADD CONSTRAINT pk_activities_and_hr PRIMARY KEY (idactivity, idhr);


--
-- TOC entry 2685 (class 2606 OID 32762929)
-- Name: pk_activity; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activities
    ADD CONSTRAINT pk_activity PRIMARY KEY (idactivity);


--
-- TOC entry 2689 (class 2606 OID 32762931)
-- Name: pk_activity_machine; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activity_machine
    ADD CONSTRAINT pk_activity_machine PRIMARY KEY (id_activity, id_machine);


--
-- TOC entry 2691 (class 2606 OID 32762933)
-- Name: pk_activity_materials; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activity_materials
    ADD CONSTRAINT pk_activity_materials PRIMARY KEY (idactivity, idmaterial);


--
-- TOC entry 2695 (class 2606 OID 32762935)
-- Name: pk_allocation; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY allocation
    ADD CONSTRAINT pk_allocation PRIMARY KEY (idallocation);


--
-- TOC entry 2697 (class 2606 OID 32762937)
-- Name: pk_consumable_use; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY consumable_use
    ADD CONSTRAINT pk_consumable_use PRIMARY KEY (id_activity, id_machine, id_material);


--
-- TOC entry 2699 (class 2606 OID 32762939)
-- Name: pk_cost_type; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cost_types
    ADD CONSTRAINT pk_cost_type PRIMARY KEY (idcosttype);


--
-- TOC entry 2701 (class 2606 OID 32762941)
-- Name: pk_cycle_activity; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cycle_standard_activities
    ADD CONSTRAINT pk_cycle_activity PRIMARY KEY (idcycleactivity);


--
-- TOC entry 2703 (class 2606 OID 32762943)
-- Name: pk_general_cost_company; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY general_cost_per_company
    ADD CONSTRAINT pk_general_cost_company PRIMARY KEY (idgeneralcostpercompany);


--
-- TOC entry 2709 (class 2606 OID 32762945)
-- Name: pk_general_cost_company_allocate_crop; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY general_costs_company_allocated_crop
    ADD CONSTRAINT pk_general_cost_company_allocate_crop PRIMARY KEY (idgeneralcostpercompany, idcrop);


--
-- TOC entry 2705 (class 2606 OID 32762947)
-- Name: pk_general_cost_crop; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY general_cost_per_crop
    ADD CONSTRAINT pk_general_cost_crop PRIMARY KEY (idgeneralcostpercrop);


--
-- TOC entry 2707 (class 2606 OID 32762949)
-- Name: pk_general_cost_plot; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY general_cost_per_plot
    ADD CONSTRAINT pk_general_cost_plot PRIMARY KEY (idgeneralcostperfield);


--
-- TOC entry 2711 (class 2606 OID 32762951)
-- Name: pk_general_cost_plot_allocate_crop; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY general_costs_plot_allocated_crop
    ADD CONSTRAINT pk_general_cost_plot_allocate_crop PRIMARY KEY (idgeneralcostperplot, idcrop);


--
-- TOC entry 2713 (class 2606 OID 32762953)
-- Name: pk_task_payment; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY task_payment
    ADD CONSTRAINT pk_task_payment PRIMARY KEY (idactivity, idhr, idpayment);


--
-- TOC entry 2693 (class 2606 OID 32762955)
-- Name: pk_team_activity; Type: CONSTRAINT; Schema: costs; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activity_team
    ADD CONSTRAINT pk_team_activity PRIMARY KEY (idteam, idactivity);


SET search_path = diesel, pg_catalog;

--
-- TOC entry 2715 (class 2606 OID 32762957)
-- Name: pk_consumable_resources; Type: CONSTRAINT; Schema: diesel; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY consumable_resources
    ADD CONSTRAINT pk_consumable_resources PRIMARY KEY (id);


--
-- TOC entry 2717 (class 2606 OID 32762959)
-- Name: pk_engine_log; Type: CONSTRAINT; Schema: diesel; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY engine_log
    ADD CONSTRAINT pk_engine_log PRIMARY KEY (id_engine_log);


--
-- TOC entry 2719 (class 2606 OID 32762961)
-- Name: pk_fuel_purchase; Type: CONSTRAINT; Schema: diesel; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY fuel_purchase
    ADD CONSTRAINT pk_fuel_purchase PRIMARY KEY (id_fuel_purchase);


--
-- TOC entry 2721 (class 2606 OID 32762963)
-- Name: pk_fuel_usage_log; Type: CONSTRAINT; Schema: diesel; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY fuel_usage_log
    ADD CONSTRAINT pk_fuel_usage_log PRIMARY KEY (id_fuel_usage);


--
-- TOC entry 2723 (class 2606 OID 32762965)
-- Name: pk_irrigation_details; Type: CONSTRAINT; Schema: diesel; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY irrigation_details
    ADD CONSTRAINT pk_irrigation_details PRIMARY KEY (id_irrigation_details);


--
-- TOC entry 2725 (class 2606 OID 32762967)
-- Name: pk_type_motors; Type: CONSTRAINT; Schema: diesel; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY zone
    ADD CONSTRAINT pk_type_motors PRIMARY KEY (id);


SET search_path = general, pg_catalog;

--
-- TOC entry 2727 (class 2606 OID 32762969)
-- Name: pk_civil_status; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY civil_status
    ADD CONSTRAINT pk_civil_status PRIMARY KEY (id);


--
-- TOC entry 2729 (class 2606 OID 32762971)
-- Name: pk_color; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY color
    ADD CONSTRAINT pk_color PRIMARY KEY (id);


--
-- TOC entry 2735 (class 2606 OID 32762973)
-- Name: pk_conversion_unidad; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY conversion_unidad
    ADD CONSTRAINT pk_conversion_unidad PRIMARY KEY (id_unidad_inicial, id_unidad_final);


--
-- TOC entry 2737 (class 2606 OID 32762975)
-- Name: pk_day; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY day
    ADD CONSTRAINT pk_day PRIMARY KEY (id);


--
-- TOC entry 2741 (class 2606 OID 32762977)
-- Name: pk_departamento; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY departamento
    ADD CONSTRAINT pk_departamento PRIMARY KEY (id);


--
-- TOC entry 2745 (class 2606 OID 32762979)
-- Name: pk_holiday; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY holiday
    ADD CONSTRAINT pk_holiday PRIMARY KEY (id);


--
-- TOC entry 2751 (class 2606 OID 32762981)
-- Name: pk_moneda; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY moneda
    ADD CONSTRAINT pk_moneda PRIMARY KEY (id);


--
-- TOC entry 2753 (class 2606 OID 32762983)
-- Name: pk_municipio; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY municipio
    ADD CONSTRAINT pk_municipio PRIMARY KEY (id);


--
-- TOC entry 2755 (class 2606 OID 32762985)
-- Name: pk_pais; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pais
    ADD CONSTRAINT pk_pais PRIMARY KEY (id);


--
-- TOC entry 2743 (class 2606 OID 32762987)
-- Name: pk_proceso; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY estado
    ADD CONSTRAINT pk_proceso PRIMARY KEY (id);


--
-- TOC entry 2757 (class 2606 OID 32762989)
-- Name: pk_system_profile; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY system_profile
    ADD CONSTRAINT pk_system_profile PRIMARY KEY (id);


--
-- TOC entry 2749 (class 2606 OID 32762991)
-- Name: pk_tax_rate; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY iva_rate
    ADD CONSTRAINT pk_tax_rate PRIMARY KEY (id_iva);


--
-- TOC entry 2759 (class 2606 OID 32762993)
-- Name: pk_tipo_documento; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_documento
    ADD CONSTRAINT pk_tipo_documento PRIMARY KEY (id);


--
-- TOC entry 2761 (class 2606 OID 32762995)
-- Name: pk_tipo_unidad; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_unidad
    ADD CONSTRAINT pk_tipo_unidad PRIMARY KEY (id);


--
-- TOC entry 2763 (class 2606 OID 32762997)
-- Name: pk_type_food; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY type_food
    ADD CONSTRAINT pk_type_food PRIMARY KEY (id);


--
-- TOC entry 2767 (class 2606 OID 32762999)
-- Name: pk_type_unit; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY type_unit
    ADD CONSTRAINT pk_type_unit PRIMARY KEY (id);


--
-- TOC entry 2769 (class 2606 OID 32763001)
-- Name: pk_unidad_medida; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY unidad_medida
    ADD CONSTRAINT pk_unidad_medida PRIMARY KEY (id);


--
-- TOC entry 2773 (class 2606 OID 32763003)
-- Name: pk_unit_conversions; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY unit_conversions
    ADD CONSTRAINT pk_unit_conversions PRIMARY KEY (id_original_unit, id_final_unit);


--
-- TOC entry 2765 (class 2606 OID 32763005)
-- Name: uq_abbreviation; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY type_food
    ADD CONSTRAINT uq_abbreviation UNIQUE (abbreviation);


--
-- TOC entry 2731 (class 2606 OID 32763007)
-- Name: uq_color_code; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY color
    ADD CONSTRAINT uq_color_code UNIQUE (code);


--
-- TOC entry 2733 (class 2606 OID 32763009)
-- Name: uq_color_name; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY color
    ADD CONSTRAINT uq_color_name UNIQUE (name);


--
-- TOC entry 2747 (class 2606 OID 32763011)
-- Name: uq_date; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY holiday
    ADD CONSTRAINT uq_date UNIQUE (date);


--
-- TOC entry 2739 (class 2606 OID 32763013)
-- Name: uq_day_name; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY day
    ADD CONSTRAINT uq_day_name UNIQUE (name);


--
-- TOC entry 2771 (class 2606 OID 32763015)
-- Name: uq_unidad_medida_nombre; Type: CONSTRAINT; Schema: general; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY unidad_medida
    ADD CONSTRAINT uq_unidad_medida_nombre UNIQUE (nombre);


SET search_path = human_resources, pg_catalog;

--
-- TOC entry 2775 (class 2606 OID 32763017)
-- Name: pk_assist_control; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assist_control
    ADD CONSTRAINT pk_assist_control PRIMARY KEY (id);


--
-- TOC entry 2777 (class 2606 OID 32763019)
-- Name: pk_contract; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY contract
    ADD CONSTRAINT pk_contract PRIMARY KEY (id);


--
-- TOC entry 2779 (class 2606 OID 32763021)
-- Name: pk_contract_type; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY contract_type
    ADD CONSTRAINT pk_contract_type PRIMARY KEY (id);


--
-- TOC entry 2781 (class 2606 OID 32763023)
-- Name: pk_day_type_food; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY day_type_food
    ADD CONSTRAINT pk_day_type_food PRIMARY KEY (id);


--
-- TOC entry 2783 (class 2606 OID 32763025)
-- Name: pk_food_control; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY food_control
    ADD CONSTRAINT pk_food_control PRIMARY KEY (id);


--
-- TOC entry 2785 (class 2606 OID 32763027)
-- Name: pk_hr; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT pk_hr PRIMARY KEY (idhr);


--
-- TOC entry 2787 (class 2606 OID 32763029)
-- Name: pk_hr_type; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hr_types
    ADD CONSTRAINT pk_hr_type PRIMARY KEY (idhrtype);


--
-- TOC entry 2789 (class 2606 OID 32763031)
-- Name: pk_novelty; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY novelty
    ADD CONSTRAINT pk_novelty PRIMARY KEY (id);


--
-- TOC entry 2791 (class 2606 OID 32763033)
-- Name: pk_novelty_type; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY novelty_type
    ADD CONSTRAINT pk_novelty_type PRIMARY KEY (id);


--
-- TOC entry 2797 (class 2606 OID 32763035)
-- Name: pk_overtime_payment_rate; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY overtime_payment_rate
    ADD CONSTRAINT pk_overtime_payment_rate PRIMARY KEY (overtimepaymentid);


--
-- TOC entry 2799 (class 2606 OID 32763037)
-- Name: pk_payment_method; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY payment_methods
    ADD CONSTRAINT pk_payment_method PRIMARY KEY (idpaymentmethod);


--
-- TOC entry 2801 (class 2606 OID 32763039)
-- Name: pk_payments; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY payments
    ADD CONSTRAINT pk_payments PRIMARY KEY (idpayment);


--
-- TOC entry 2803 (class 2606 OID 32763041)
-- Name: pk_team; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY team
    ADD CONSTRAINT pk_team PRIMARY KEY (idteam);


--
-- TOC entry 2805 (class 2606 OID 32763043)
-- Name: pk_team_members; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY team_members
    ADD CONSTRAINT pk_team_members PRIMARY KEY (idteam, idhr);


--
-- TOC entry 2793 (class 2606 OID 32763045)
-- Name: uq_novelty_type_color; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY novelty_type
    ADD CONSTRAINT uq_novelty_type_color UNIQUE (id_color);


--
-- TOC entry 2795 (class 2606 OID 32763047)
-- Name: uq_novelty_type_name; Type: CONSTRAINT; Schema: human_resources; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY novelty_type
    ADD CONSTRAINT uq_novelty_type_name UNIQUE (name);


SET search_path = kpi, pg_catalog;

--
-- TOC entry 2807 (class 2606 OID 32763049)
-- Name: pk_bean_index; Type: CONSTRAINT; Schema: kpi; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY bean_index
    ADD CONSTRAINT pk_bean_index PRIMARY KEY (idbeanindex);


SET search_path = life_cycle, pg_catalog;

--
-- TOC entry 2809 (class 2606 OID 32763051)
-- Name: pk_activity_name; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activity_names
    ADD CONSTRAINT pk_activity_name PRIMARY KEY (idactivityname);


--
-- TOC entry 2811 (class 2606 OID 32763053)
-- Name: pk_activity_plot; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activity_plot
    ADD CONSTRAINT pk_activity_plot PRIMARY KEY (idactivity, idplot);


--
-- TOC entry 2815 (class 2606 OID 32763055)
-- Name: pk_crop; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY crops
    ADD CONSTRAINT pk_crop PRIMARY KEY (idcrop);


--
-- TOC entry 2813 (class 2606 OID 32763057)
-- Name: pk_crop_name; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY crop_names
    ADD CONSTRAINT pk_crop_name PRIMARY KEY (idcropname);


--
-- TOC entry 2817 (class 2606 OID 32763059)
-- Name: pk_crops_plots; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY crops_plots
    ADD CONSTRAINT pk_crops_plots PRIMARY KEY (id_crop, id_plot);


--
-- TOC entry 2819 (class 2606 OID 32763061)
-- Name: pk_cycle; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cycle
    ADD CONSTRAINT pk_cycle PRIMARY KEY (idcycle);


--
-- TOC entry 2821 (class 2606 OID 32763063)
-- Name: pk_details_harvest; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY details_harvest
    ADD CONSTRAINT pk_details_harvest PRIMARY KEY (id);


--
-- TOC entry 2823 (class 2606 OID 32763065)
-- Name: pk_farm; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY farm
    ADD CONSTRAINT pk_farm PRIMARY KEY (idfarm);


--
-- TOC entry 2825 (class 2606 OID 32763067)
-- Name: pk_plot; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY plot
    ADD CONSTRAINT pk_plot PRIMARY KEY (idplot);


--
-- TOC entry 2827 (class 2606 OID 32763069)
-- Name: pk_pluviometer; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pluviometer
    ADD CONSTRAINT pk_pluviometer PRIMARY KEY (id);


--
-- TOC entry 2829 (class 2606 OID 32763071)
-- Name: pk_section; Type: CONSTRAINT; Schema: life_cycle; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY section
    ADD CONSTRAINT pk_section PRIMARY KEY (idsection);


SET search_path = machines, pg_catalog;

--
-- TOC entry 2831 (class 2606 OID 32763073)
-- Name: pk_fuel_type; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY fuel_types
    ADD CONSTRAINT pk_fuel_type PRIMARY KEY (idfueltype);


--
-- TOC entry 2833 (class 2606 OID 32763075)
-- Name: pk_insurance; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY insurance
    ADD CONSTRAINT pk_insurance PRIMARY KEY (idinsurance);


--
-- TOC entry 2835 (class 2606 OID 32763077)
-- Name: pk_machine_type; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY machine_types
    ADD CONSTRAINT pk_machine_type PRIMARY KEY (idmachinetype);


--
-- TOC entry 2837 (class 2606 OID 32763079)
-- Name: pk_machine_usage; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY machine_usage
    ADD CONSTRAINT pk_machine_usage PRIMARY KEY (idmachine, year);


--
-- TOC entry 2839 (class 2606 OID 32763081)
-- Name: pk_machines; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY machines
    ADD CONSTRAINT pk_machines PRIMARY KEY (idmachine);


--
-- TOC entry 2843 (class 2606 OID 32763083)
-- Name: pk_maintenance_and_calibration; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY maintenance_and_calibration
    ADD CONSTRAINT pk_maintenance_and_calibration PRIMARY KEY (idmaintenance);


--
-- TOC entry 2845 (class 2606 OID 32763085)
-- Name: pk_maintenance_lines; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY maintenance_lines
    ADD CONSTRAINT pk_maintenance_lines PRIMARY KEY (idmaintenanceline);


--
-- TOC entry 2841 (class 2606 OID 32763087)
-- Name: unique_serial_machine; Type: CONSTRAINT; Schema: machines; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY machines
    ADD CONSTRAINT unique_serial_machine UNIQUE (serial_number);


SET search_path = organizaciones, pg_catalog;

--
-- TOC entry 2847 (class 2606 OID 32763089)
-- Name: pk_empresa; Type: CONSTRAINT; Schema: organizaciones; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT pk_empresa PRIMARY KEY (id);


--
-- TOC entry 2850 (class 2606 OID 32763091)
-- Name: pk_empresa_persona; Type: CONSTRAINT; Schema: organizaciones; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY empresa_persona
    ADD CONSTRAINT pk_empresa_persona PRIMARY KEY (id);


--
-- TOC entry 2853 (class 2606 OID 32763093)
-- Name: pk_finca; Type: CONSTRAINT; Schema: organizaciones; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hacienda
    ADD CONSTRAINT pk_finca PRIMARY KEY (id);


--
-- TOC entry 2855 (class 2606 OID 32763095)
-- Name: pk_organizacion; Type: CONSTRAINT; Schema: organizaciones; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organizacion
    ADD CONSTRAINT pk_organizacion PRIMARY KEY (id);


--
-- TOC entry 2929 (class 2606 OID 32764017)
-- Name: pk_tipo_cargo; Type: CONSTRAINT; Schema: organizaciones; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tipo_cargo
    ADD CONSTRAINT pk_tipo_cargo PRIMARY KEY (id);


--
-- TOC entry 2857 (class 2606 OID 32763097)
-- Name: uq_organizacion_id_tipo_documento_nit; Type: CONSTRAINT; Schema: organizaciones; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY organizacion
    ADD CONSTRAINT uq_organizacion_id_tipo_documento_nit UNIQUE (id_tipo_documento, nit);


SET search_path = sales, pg_catalog;

--
-- TOC entry 2859 (class 2606 OID 32763105)
-- Name: pk_customer; Type: CONSTRAINT; Schema: sales; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY customer
    ADD CONSTRAINT pk_customer PRIMARY KEY (idcustomer);


--
-- TOC entry 2861 (class 2606 OID 32763107)
-- Name: pk_final_products; Type: CONSTRAINT; Schema: sales; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY final_products
    ADD CONSTRAINT pk_final_products PRIMARY KEY (idfinalproduct);


--
-- TOC entry 2863 (class 2606 OID 32763109)
-- Name: pk_order_header; Type: CONSTRAINT; Schema: sales; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY order_header
    ADD CONSTRAINT pk_order_header PRIMARY KEY (idorder);


--
-- TOC entry 2865 (class 2606 OID 32763111)
-- Name: pk_order_line; Type: CONSTRAINT; Schema: sales; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY order_lines
    ADD CONSTRAINT pk_order_line PRIMARY KEY (id_final_product, id_order);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 2867 (class 2606 OID 32763113)
-- Name: pk_icono; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY icono
    ADD CONSTRAINT pk_icono PRIMARY KEY (id);


--
-- TOC entry 2869 (class 2606 OID 32763115)
-- Name: pk_menu; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT pk_menu PRIMARY KEY (id);


--
-- TOC entry 2871 (class 2606 OID 32763117)
-- Name: pk_metodo; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metodo
    ADD CONSTRAINT pk_metodo PRIMARY KEY (id);


--
-- TOC entry 2873 (class 2606 OID 32763119)
-- Name: pk_metodo_menu; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metodo_menu
    ADD CONSTRAINT pk_metodo_menu PRIMARY KEY (id);


--
-- TOC entry 2875 (class 2606 OID 32763121)
-- Name: pk_perfil_sistema; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY perfil_sistema
    ADD CONSTRAINT pk_perfil_sistema PRIMARY KEY (id);


--
-- TOC entry 2877 (class 2606 OID 32763123)
-- Name: pk_permiso_persona_empresa; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY permiso_persona_empresa
    ADD CONSTRAINT pk_permiso_persona_empresa PRIMARY KEY (id);


--
-- TOC entry 2925 (class 2606 OID 32763947)
-- Name: pk_persona; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT pk_persona PRIMARY KEY (id);


--
-- TOC entry 2879 (class 2606 OID 32763125)
-- Name: pk_registro_sesion; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY registro_sesion
    ADD CONSTRAINT pk_registro_sesion PRIMARY KEY (id);


--
-- TOC entry 2881 (class 2606 OID 32763127)
-- Name: pk_rol; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol
    ADD CONSTRAINT pk_rol PRIMARY KEY (id);


--
-- TOC entry 2885 (class 2606 OID 32763129)
-- Name: pk_rol_metodo; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol_metodo
    ADD CONSTRAINT pk_rol_metodo PRIMARY KEY (id_rol, id_metodo, permiso);


--
-- TOC entry 2887 (class 2606 OID 32763131)
-- Name: pk_rol_usuario; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol_usuario
    ADD CONSTRAINT pk_rol_usuario PRIMARY KEY (id_rol, id_usuario);


--
-- TOC entry 2889 (class 2606 OID 32763133)
-- Name: pk_usuario_2; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT pk_usuario_2 PRIMARY KEY (id);


--
-- TOC entry 2883 (class 2606 OID 32763135)
-- Name: rol_menu_pkey; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rol_menu
    ADD CONSTRAINT rol_menu_pkey PRIMARY KEY (id_rol, id_menu);


--
-- TOC entry 2927 (class 2606 OID 32763949)
-- Name: uq_persona_documento; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT uq_persona_documento UNIQUE (documento, id_tipo_documento);


--
-- TOC entry 2891 (class 2606 OID 32763137)
-- Name: uq_usuario_nombre_usuario_2; Type: CONSTRAINT; Schema: seguridad; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT uq_usuario_nombre_usuario_2 UNIQUE (nombre_usuario);


SET search_path = services, pg_catalog;

--
-- TOC entry 2893 (class 2606 OID 32763139)
-- Name: pk_service; Type: CONSTRAINT; Schema: services; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY external_services_and_activities
    ADD CONSTRAINT pk_service PRIMARY KEY (idservice);


--
-- TOC entry 2895 (class 2606 OID 32763141)
-- Name: pk_service_type; Type: CONSTRAINT; Schema: services; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY service_type
    ADD CONSTRAINT pk_service_type PRIMARY KEY (idservicetype);


SET search_path = utz, pg_catalog;

--
-- TOC entry 2897 (class 2606 OID 32763143)
-- Name: pk_activities_and_certifications; Type: CONSTRAINT; Schema: utz; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY activities_and_certifications
    ADD CONSTRAINT pk_activities_and_certifications PRIMARY KEY (idactivity, idcertificationsandroles);


--
-- TOC entry 2899 (class 2606 OID 32763145)
-- Name: pk_certificacions_and_roles; Type: CONSTRAINT; Schema: utz; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY certifications_and_roles
    ADD CONSTRAINT pk_certificacions_and_roles PRIMARY KEY (idcertificactionsandroles);


--
-- TOC entry 2901 (class 2606 OID 32763147)
-- Name: pk_hr_certifications_and_roles; Type: CONSTRAINT; Schema: utz; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hr_certifications_and_roles
    ADD CONSTRAINT pk_hr_certifications_and_roles PRIMARY KEY (idhr, idcertificationsandroles);


SET search_path = warehouse, pg_catalog;

--
-- TOC entry 2903 (class 2606 OID 32763149)
-- Name: pk_deposits; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY deposits
    ADD CONSTRAINT pk_deposits PRIMARY KEY (iddeposit);


--
-- TOC entry 2905 (class 2606 OID 32763151)
-- Name: pk_diseases_types; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY diseases
    ADD CONSTRAINT pk_diseases_types PRIMARY KEY (iddisease);


--
-- TOC entry 2907 (class 2606 OID 32763153)
-- Name: pk_invoice_items; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY invoice_items
    ADD CONSTRAINT pk_invoice_items PRIMARY KEY (id_invoice_item);


--
-- TOC entry 2909 (class 2606 OID 32763155)
-- Name: pk_materials; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY materials
    ADD CONSTRAINT pk_materials PRIMARY KEY (idmaterial);


--
-- TOC entry 2911 (class 2606 OID 32763157)
-- Name: pk_materials_types; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY materials_types
    ADD CONSTRAINT pk_materials_types PRIMARY KEY (idmaterialtype);


--
-- TOC entry 2913 (class 2606 OID 32763159)
-- Name: pk_measurement_units; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY measurement_units
    ADD CONSTRAINT pk_measurement_units PRIMARY KEY (idmeasurementunits);


--
-- TOC entry 2915 (class 2606 OID 32763161)
-- Name: pk_purchase_invoices; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY purchase_invoices
    ADD CONSTRAINT pk_purchase_invoices PRIMARY KEY (idpurchaseinvoice);


--
-- TOC entry 2917 (class 2606 OID 32763163)
-- Name: pk_suppliers; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY suppliers
    ADD CONSTRAINT pk_suppliers PRIMARY KEY (idsupplier);


--
-- TOC entry 2919 (class 2606 OID 32763165)
-- Name: pk_transaction_types; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY transaction_type
    ADD CONSTRAINT pk_transaction_types PRIMARY KEY (idtransactiontype);


--
-- TOC entry 2921 (class 2606 OID 32763167)
-- Name: pk_transactions; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY transactions
    ADD CONSTRAINT pk_transactions PRIMARY KEY (idtransaction);


--
-- TOC entry 2923 (class 2606 OID 32763169)
-- Name: pk_type_of_management; Type: CONSTRAINT; Schema: warehouse; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY type_of_management
    ADD CONSTRAINT pk_type_of_management PRIMARY KEY (idtypeofmanagement);


SET search_path = organizaciones, pg_catalog;

--
-- TOC entry 2848 (class 1259 OID 32763170)
-- Name: fki_empresa_persona_finca; Type: INDEX; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_empresa_persona_finca ON empresa_persona USING btree (id_farm);


--
-- TOC entry 2851 (class 1259 OID 32763171)
-- Name: fki_finca_unidad_medida; Type: INDEX; Schema: organizaciones; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_finca_unidad_medida ON hacienda USING btree (id_unidad_medida);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 3082 (class 2620 OID 32763172)
-- Name: trg_a_perfil_sistema_after; Type: TRIGGER; Schema: seguridad; Owner: postgres
--

CREATE TRIGGER trg_a_perfil_sistema_after AFTER INSERT OR UPDATE ON perfil_sistema FOR EACH ROW EXECUTE PROCEDURE fun_a_perfil_sistema();


--
-- TOC entry 3083 (class 2620 OID 32763173)
-- Name: trg_a_perfil_sistema_before; Type: TRIGGER; Schema: seguridad; Owner: postgres
--

CREATE TRIGGER trg_a_perfil_sistema_before BEFORE DELETE ON perfil_sistema FOR EACH ROW EXECUTE PROCEDURE fun_a_perfil_sistema();


SET search_path = costs, pg_catalog;

--
-- TOC entry 2933 (class 2606 OID 32763174)
-- Name: fk_activities_and_hr_activities; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activities_and_hr
    ADD CONSTRAINT fk_activities_and_hr_activities FOREIGN KEY (idactivity) REFERENCES activities(idactivity) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2934 (class 2606 OID 32763179)
-- Name: fk_activities_hr_hr; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activities_and_hr
    ADD CONSTRAINT fk_activities_hr_hr FOREIGN KEY (idhr) REFERENCES human_resources.hr(idhr) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2935 (class 2606 OID 32763184)
-- Name: fk_activities_hr_overtime_rate; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activities_and_hr
    ADD CONSTRAINT fk_activities_hr_overtime_rate FOREIGN KEY (overtimepaymentid) REFERENCES human_resources.overtime_payment_rate(overtimepaymentid);


--
-- TOC entry 2930 (class 2606 OID 32763189)
-- Name: fk_activity_activity_name; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activities
    ADD CONSTRAINT fk_activity_activity_name FOREIGN KEY (id_activity_name) REFERENCES life_cycle.activity_names(idactivityname);


--
-- TOC entry 2945 (class 2606 OID 32763194)
-- Name: fk_activity_activity_name; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY cycle_standard_activities
    ADD CONSTRAINT fk_activity_activity_name FOREIGN KEY (id_activity_name) REFERENCES life_cycle.activity_names(idactivityname);


--
-- TOC entry 2931 (class 2606 OID 32763199)
-- Name: fk_activity_crop; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activities
    ADD CONSTRAINT fk_activity_crop FOREIGN KEY (id_crop) REFERENCES life_cycle.crops(idcrop);


--
-- TOC entry 2932 (class 2606 OID 32763204)
-- Name: fk_activity_cycle; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activities
    ADD CONSTRAINT fk_activity_cycle FOREIGN KEY (id_cycle) REFERENCES life_cycle.cycle(idcycle);


--
-- TOC entry 2936 (class 2606 OID 32763209)
-- Name: fk_activity_machine_activity; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activity_machine
    ADD CONSTRAINT fk_activity_machine_activity FOREIGN KEY (id_activity) REFERENCES activities(idactivity);


--
-- TOC entry 2937 (class 2606 OID 32763214)
-- Name: fk_activity_machine_machine; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activity_machine
    ADD CONSTRAINT fk_activity_machine_machine FOREIGN KEY (id_machine) REFERENCES machines.machines(idmachine);


--
-- TOC entry 2938 (class 2606 OID 32763219)
-- Name: fk_activity_materials_activity; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activity_materials
    ADD CONSTRAINT fk_activity_materials_activity FOREIGN KEY (idactivity) REFERENCES activities(idactivity);


--
-- TOC entry 2939 (class 2606 OID 32763224)
-- Name: fk_activity_materials_materials; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activity_materials
    ADD CONSTRAINT fk_activity_materials_materials FOREIGN KEY (idmaterial) REFERENCES warehouse.materials(idmaterial);


--
-- TOC entry 2940 (class 2606 OID 32763229)
-- Name: fk_activty; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activity_team
    ADD CONSTRAINT fk_activty FOREIGN KEY (idactivity) REFERENCES activities(idactivity);


--
-- TOC entry 2954 (class 2606 OID 32763234)
-- Name: fk_allocated_crop_crop; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_costs_plot_allocated_crop
    ADD CONSTRAINT fk_allocated_crop_crop FOREIGN KEY (idcrop) REFERENCES life_cycle.crops(idcrop);


--
-- TOC entry 2955 (class 2606 OID 32763239)
-- Name: fk_allocated_crop_general_cost_plot; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_costs_plot_allocated_crop
    ADD CONSTRAINT fk_allocated_crop_general_cost_plot FOREIGN KEY (idgeneralcostperplot) REFERENCES general_cost_per_plot(idgeneralcostperfield);


--
-- TOC entry 2952 (class 2606 OID 32763244)
-- Name: fk_company_allocated_crop_crop; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_costs_company_allocated_crop
    ADD CONSTRAINT fk_company_allocated_crop_crop FOREIGN KEY (idcrop) REFERENCES life_cycle.crops(idcrop);


--
-- TOC entry 2953 (class 2606 OID 32763249)
-- Name: fk_company_allocated_crop_general_cost_company; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_costs_company_allocated_crop
    ADD CONSTRAINT fk_company_allocated_crop_general_cost_company FOREIGN KEY (idgeneralcostpercompany) REFERENCES general_cost_per_company(idgeneralcostpercompany);


--
-- TOC entry 2942 (class 2606 OID 32763254)
-- Name: fk_consumable_use_activity; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY consumable_use
    ADD CONSTRAINT fk_consumable_use_activity FOREIGN KEY (id_activity) REFERENCES activities(idactivity);


--
-- TOC entry 2943 (class 2606 OID 32763259)
-- Name: fk_consumable_use_machine; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY consumable_use
    ADD CONSTRAINT fk_consumable_use_machine FOREIGN KEY (id_machine) REFERENCES machines.machines(idmachine);


--
-- TOC entry 2944 (class 2606 OID 32763264)
-- Name: fk_cost_type_allocation; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY cost_types
    ADD CONSTRAINT fk_cost_type_allocation FOREIGN KEY (idallocation) REFERENCES allocation(idallocation);


--
-- TOC entry 2946 (class 2606 OID 32763269)
-- Name: fk_cycle_activity_crop_name; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY cycle_standard_activities
    ADD CONSTRAINT fk_cycle_activity_crop_name FOREIGN KEY (id_crop_name) REFERENCES life_cycle.crop_names(idcropname);


--
-- TOC entry 2948 (class 2606 OID 32763274)
-- Name: fk_general_cost_crop_cost_type; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_cost_per_crop
    ADD CONSTRAINT fk_general_cost_crop_cost_type FOREIGN KEY (idcosttype) REFERENCES cost_types(idcosttype);


--
-- TOC entry 2947 (class 2606 OID 32763279)
-- Name: fk_general_cost_crop_cost_type; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_cost_per_company
    ADD CONSTRAINT fk_general_cost_crop_cost_type FOREIGN KEY (idcosttype) REFERENCES cost_types(idcosttype);


--
-- TOC entry 2949 (class 2606 OID 32763284)
-- Name: fk_general_cost_crop_crop; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_cost_per_crop
    ADD CONSTRAINT fk_general_cost_crop_crop FOREIGN KEY (idcrop) REFERENCES life_cycle.crops(idcrop);


--
-- TOC entry 2950 (class 2606 OID 32763289)
-- Name: fk_general_cost_plot_cost_type; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_cost_per_plot
    ADD CONSTRAINT fk_general_cost_plot_cost_type FOREIGN KEY (idcosttype) REFERENCES cost_types(idcosttype);


--
-- TOC entry 2951 (class 2606 OID 32763294)
-- Name: fk_general_cost_plot_plot; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY general_cost_per_plot
    ADD CONSTRAINT fk_general_cost_plot_plot FOREIGN KEY (idplot) REFERENCES life_cycle.plot(idplot);


--
-- TOC entry 2956 (class 2606 OID 32763299)
-- Name: fk_task_payment_activities; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY task_payment
    ADD CONSTRAINT fk_task_payment_activities FOREIGN KEY (idactivity) REFERENCES activities(idactivity) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2957 (class 2606 OID 32763304)
-- Name: fk_task_payment_hr; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY task_payment
    ADD CONSTRAINT fk_task_payment_hr FOREIGN KEY (idhr) REFERENCES human_resources.hr(idhr) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2958 (class 2606 OID 32763309)
-- Name: fk_task_payment_payment; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY task_payment
    ADD CONSTRAINT fk_task_payment_payment FOREIGN KEY (idpayment) REFERENCES human_resources.payments(idpayment);


--
-- TOC entry 2941 (class 2606 OID 32763314)
-- Name: fk_team; Type: FK CONSTRAINT; Schema: costs; Owner: postgres
--

ALTER TABLE ONLY activity_team
    ADD CONSTRAINT fk_team FOREIGN KEY (idteam) REFERENCES human_resources.team(idteam);


SET search_path = diesel, pg_catalog;

--
-- TOC entry 2959 (class 2606 OID 32763319)
-- Name: fk_consumable_resources_measurement; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY consumable_resources
    ADD CONSTRAINT fk_consumable_resources_measurement FOREIGN KEY (idmeasurementunits) REFERENCES warehouse.measurement_units(idmeasurementunits);


--
-- TOC entry 2966 (class 2606 OID 32763324)
-- Name: fk_engine_log; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_usage_log
    ADD CONSTRAINT fk_engine_log FOREIGN KEY (id_engine_log) REFERENCES engine_log(id_engine_log);


--
-- TOC entry 2960 (class 2606 OID 32763329)
-- Name: fk_engine_log_activity_machines; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY engine_log
    ADD CONSTRAINT fk_engine_log_activity_machines FOREIGN KEY (id_activity, id_machine) REFERENCES costs.activity_machine(id_activity, id_machine);


--
-- TOC entry 2961 (class 2606 OID 32763334)
-- Name: fk_engine_log_delivered; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY engine_log
    ADD CONSTRAINT fk_engine_log_delivered FOREIGN KEY (delivered_by) REFERENCES human_resources.hr(idhr);


--
-- TOC entry 2962 (class 2606 OID 32763339)
-- Name: fk_engine_log_received; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY engine_log
    ADD CONSTRAINT fk_engine_log_received FOREIGN KEY (received_by) REFERENCES human_resources.hr(idhr);


--
-- TOC entry 2967 (class 2606 OID 32763344)
-- Name: fk_fuel_purchase; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_usage_log
    ADD CONSTRAINT fk_fuel_purchase FOREIGN KEY (id_fuel_purchase) REFERENCES fuel_purchase(id_fuel_purchase);


--
-- TOC entry 2963 (class 2606 OID 32763349)
-- Name: fk_fuel_purchase_fuel_types; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_purchase
    ADD CONSTRAINT fk_fuel_purchase_fuel_types FOREIGN KEY (id_fuel_type) REFERENCES machines.fuel_types(idfueltype);


--
-- TOC entry 2964 (class 2606 OID 32763354)
-- Name: fk_fuel_purchase_iva_rate; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_purchase
    ADD CONSTRAINT fk_fuel_purchase_iva_rate FOREIGN KEY (id_iva_rate) REFERENCES general.iva_rate(id_iva);


--
-- TOC entry 2965 (class 2606 OID 32763359)
-- Name: fk_fuel_purchase_suppliers; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_purchase
    ADD CONSTRAINT fk_fuel_purchase_suppliers FOREIGN KEY (id_supplier) REFERENCES warehouse.suppliers(idsupplier);


--
-- TOC entry 2969 (class 2606 OID 32763364)
-- Name: fk_irrigation_details_engine_log; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY irrigation_details
    ADD CONSTRAINT fk_irrigation_details_engine_log FOREIGN KEY (id_engine_log) REFERENCES engine_log(id_engine_log);


--
-- TOC entry 2970 (class 2606 OID 32763369)
-- Name: fk_irrigation_details_machines; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY irrigation_details
    ADD CONSTRAINT fk_irrigation_details_machines FOREIGN KEY (id_machine) REFERENCES machines.machines(idmachine);


--
-- TOC entry 2971 (class 2606 OID 32763374)
-- Name: fk_irrigation_details_zone; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY irrigation_details
    ADD CONSTRAINT fk_irrigation_details_zone FOREIGN KEY (id_zone) REFERENCES zone(id);


--
-- TOC entry 2968 (class 2606 OID 32763379)
-- Name: fk_transaction_type; Type: FK CONSTRAINT; Schema: diesel; Owner: postgres
--

ALTER TABLE ONLY fuel_usage_log
    ADD CONSTRAINT fk_transaction_type FOREIGN KEY (id_transaction_type) REFERENCES warehouse.transaction_type(idtransactiontype);


SET search_path = general, pg_catalog;

--
-- TOC entry 2972 (class 2606 OID 32763384)
-- Name: fk_conversion_unidad_unidad_medida_f; Type: FK CONSTRAINT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY conversion_unidad
    ADD CONSTRAINT fk_conversion_unidad_unidad_medida_f FOREIGN KEY (id_unidad_final) REFERENCES unidad_medida(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2973 (class 2606 OID 32763389)
-- Name: fk_conversion_unidad_unidad_medida_i; Type: FK CONSTRAINT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY conversion_unidad
    ADD CONSTRAINT fk_conversion_unidad_unidad_medida_i FOREIGN KEY (id_unidad_inicial) REFERENCES unidad_medida(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2975 (class 2606 OID 32763394)
-- Name: fk_id_departamento; Type: FK CONSTRAINT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY municipio
    ADD CONSTRAINT fk_id_departamento FOREIGN KEY (id_departamento) REFERENCES departamento(id);


--
-- TOC entry 2974 (class 2606 OID 32763399)
-- Name: fk_id_pais_departamento; Type: FK CONSTRAINT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY departamento
    ADD CONSTRAINT fk_id_pais_departamento FOREIGN KEY (id_pais) REFERENCES pais(id);


--
-- TOC entry 2976 (class 2606 OID 32763404)
-- Name: fk_pais_moneda; Type: FK CONSTRAINT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY pais
    ADD CONSTRAINT fk_pais_moneda FOREIGN KEY (id_moneda) REFERENCES moneda(id);


--
-- TOC entry 2977 (class 2606 OID 32763409)
-- Name: fk_unidad_medida_tipo_unidad; Type: FK CONSTRAINT; Schema: general; Owner: postgres
--

ALTER TABLE ONLY unidad_medida
    ADD CONSTRAINT fk_unidad_medida_tipo_unidad FOREIGN KEY (id_tipo_unidad) REFERENCES tipo_unidad(id) ON UPDATE CASCADE ON DELETE CASCADE;


SET search_path = human_resources, pg_catalog;

--
-- TOC entry 2978 (class 2606 OID 32763414)
-- Name: fk_assist_control_hr; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY assist_control
    ADD CONSTRAINT fk_assist_control_hr FOREIGN KEY (idhr) REFERENCES hr(idhr);


--
-- TOC entry 2979 (class 2606 OID 32763419)
-- Name: fk_contracto_hr; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY contract
    ADD CONSTRAINT fk_contracto_hr FOREIGN KEY (id_hr) REFERENCES hr(idhr);


--
-- TOC entry 2980 (class 2606 OID 32763424)
-- Name: fk_day_type_food_day; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY day_type_food
    ADD CONSTRAINT fk_day_type_food_day FOREIGN KEY (id_day) REFERENCES general.day(id);


--
-- TOC entry 2981 (class 2606 OID 32763429)
-- Name: fk_day_type_food_type_food; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY day_type_food
    ADD CONSTRAINT fk_day_type_food_type_food FOREIGN KEY (id_type_food) REFERENCES general.type_food(id);


--
-- TOC entry 2982 (class 2606 OID 32763434)
-- Name: fk_food_control_hr; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY food_control
    ADD CONSTRAINT fk_food_control_hr FOREIGN KEY (id_hr) REFERENCES hr(idhr);


--
-- TOC entry 2983 (class 2606 OID 32763439)
-- Name: fk_food_control_type_food; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY food_control
    ADD CONSTRAINT fk_food_control_type_food FOREIGN KEY (id_type_food) REFERENCES general.type_food(id);


--
-- TOC entry 2984 (class 2606 OID 32763444)
-- Name: fk_hr_civil_status; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_civil_status FOREIGN KEY (id_civil_status) REFERENCES general.civil_status(id);


--
-- TOC entry 2985 (class 2606 OID 32763449)
-- Name: fk_hr_dep_nac; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_dep_nac FOREIGN KEY (id_departamento_nac) REFERENCES general.departamento(id);


--
-- TOC entry 2986 (class 2606 OID 32763454)
-- Name: fk_hr_dep_res; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_dep_res FOREIGN KEY (id_departamento_res) REFERENCES general.departamento(id);


--
-- TOC entry 2987 (class 2606 OID 32763459)
-- Name: fk_hr_hr_type; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_hr_type FOREIGN KEY (id_hr_type) REFERENCES hr_types(idhrtype);


--
-- TOC entry 2988 (class 2606 OID 32763464)
-- Name: fk_hr_mun_nac; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_mun_nac FOREIGN KEY (id_municipio_nac) REFERENCES general.municipio(id);


--
-- TOC entry 2989 (class 2606 OID 32763469)
-- Name: fk_hr_mun_res; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_mun_res FOREIGN KEY (id_municipio_res) REFERENCES general.municipio(id);


--
-- TOC entry 2990 (class 2606 OID 32763474)
-- Name: fk_hr_pais_nac; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_pais_nac FOREIGN KEY (id_pais_nac) REFERENCES general.pais(id);


--
-- TOC entry 2991 (class 2606 OID 32763479)
-- Name: fk_hr_pais_res; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_pais_res FOREIGN KEY (id_pais_res) REFERENCES general.pais(id);


--
-- TOC entry 2992 (class 2606 OID 32763484)
-- Name: fk_hr_payment_method; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY hr
    ADD CONSTRAINT fk_hr_payment_method FOREIGN KEY (id_payment_method) REFERENCES payment_methods(idpaymentmethod);


--
-- TOC entry 2993 (class 2606 OID 32763489)
-- Name: fk_novelty_hr; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY novelty
    ADD CONSTRAINT fk_novelty_hr FOREIGN KEY (idhr) REFERENCES hr(idhr);


--
-- TOC entry 2994 (class 2606 OID 32763494)
-- Name: fk_novelty_noveltytype; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY novelty
    ADD CONSTRAINT fk_novelty_noveltytype FOREIGN KEY (idnoveltytype) REFERENCES novelty_type(id);


--
-- TOC entry 2995 (class 2606 OID 32763499)
-- Name: fk_novelty_type_color; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY novelty_type
    ADD CONSTRAINT fk_novelty_type_color FOREIGN KEY (id_color) REFERENCES general.color(id);


--
-- TOC entry 2996 (class 2606 OID 32763504)
-- Name: fk_payments_contract; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY payments
    ADD CONSTRAINT fk_payments_contract FOREIGN KEY (idcontract) REFERENCES contract(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2997 (class 2606 OID 32763509)
-- Name: fk_payments_hr; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY payments
    ADD CONSTRAINT fk_payments_hr FOREIGN KEY (idhr) REFERENCES hr(idhr) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2998 (class 2606 OID 32763514)
-- Name: fk_team_members_hr; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY team_members
    ADD CONSTRAINT fk_team_members_hr FOREIGN KEY (idhr) REFERENCES hr(idhr);


--
-- TOC entry 2999 (class 2606 OID 32763519)
-- Name: fk_team_members_team; Type: FK CONSTRAINT; Schema: human_resources; Owner: postgres
--

ALTER TABLE ONLY team_members
    ADD CONSTRAINT fk_team_members_team FOREIGN KEY (idteam) REFERENCES team(idteam);


SET search_path = kpi, pg_catalog;

--
-- TOC entry 3000 (class 2606 OID 32763524)
-- Name: fk_bean_index_crop; Type: FK CONSTRAINT; Schema: kpi; Owner: postgres
--

ALTER TABLE ONLY bean_index
    ADD CONSTRAINT fk_bean_index_crop FOREIGN KEY (id_crop) REFERENCES life_cycle.crops(idcrop);


--
-- TOC entry 3001 (class 2606 OID 32763529)
-- Name: fk_bean_index_section; Type: FK CONSTRAINT; Schema: kpi; Owner: postgres
--

ALTER TABLE ONLY bean_index
    ADD CONSTRAINT fk_bean_index_section FOREIGN KEY (id_section) REFERENCES life_cycle.section(idsection);


SET search_path = life_cycle, pg_catalog;

--
-- TOC entry 3002 (class 2606 OID 32763534)
-- Name: fk_activity_plot_activities; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY activity_plot
    ADD CONSTRAINT fk_activity_plot_activities FOREIGN KEY (idactivity) REFERENCES costs.activities(idactivity);


--
-- TOC entry 3003 (class 2606 OID 32763539)
-- Name: fk_activity_plot_plot; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY activity_plot
    ADD CONSTRAINT fk_activity_plot_plot FOREIGN KEY (idplot) REFERENCES plot(idplot);


--
-- TOC entry 3004 (class 2606 OID 32763544)
-- Name: fk_crop_crop_names; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY crops
    ADD CONSTRAINT fk_crop_crop_names FOREIGN KEY (id_crop_name) REFERENCES crop_names(idcropname) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3005 (class 2606 OID 32763549)
-- Name: fk_crops_plots_crop; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY crops_plots
    ADD CONSTRAINT fk_crops_plots_crop FOREIGN KEY (id_crop) REFERENCES crops(idcrop);


--
-- TOC entry 3006 (class 2606 OID 32763554)
-- Name: fk_crops_plots_plot; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY crops_plots
    ADD CONSTRAINT fk_crops_plots_plot FOREIGN KEY (id_plot) REFERENCES plot(idplot);


--
-- TOC entry 3007 (class 2606 OID 32763559)
-- Name: fk_cycle_activity_name; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY cycle
    ADD CONSTRAINT fk_cycle_activity_name FOREIGN KEY (id_activity_name) REFERENCES activity_names(idactivityname);


--
-- TOC entry 3008 (class 2606 OID 32763564)
-- Name: fk_cycle_crop; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY cycle
    ADD CONSTRAINT fk_cycle_crop FOREIGN KEY (id_crop) REFERENCES crops(idcrop);


--
-- TOC entry 3009 (class 2606 OID 32763569)
-- Name: fk_details_harvest_activities; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY details_harvest
    ADD CONSTRAINT fk_details_harvest_activities FOREIGN KEY (idactivity) REFERENCES costs.activities(idactivity);


--
-- TOC entry 3010 (class 2606 OID 32763574)
-- Name: fk_empresa_departamento; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY farm
    ADD CONSTRAINT fk_empresa_departamento FOREIGN KEY (id_departamento) REFERENCES general.departamento(id);


--
-- TOC entry 3011 (class 2606 OID 32763579)
-- Name: fk_empresa_municipio; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY farm
    ADD CONSTRAINT fk_empresa_municipio FOREIGN KEY (id_municipio) REFERENCES general.municipio(id);


--
-- TOC entry 3012 (class 2606 OID 32763584)
-- Name: fk_farm_pais; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY farm
    ADD CONSTRAINT fk_farm_pais FOREIGN KEY (id_pais) REFERENCES general.pais(id);


--
-- TOC entry 3013 (class 2606 OID 32763589)
-- Name: fk_plot_crop_names; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY plot
    ADD CONSTRAINT fk_plot_crop_names FOREIGN KEY (idcropname) REFERENCES crop_names(idcropname);


--
-- TOC entry 3014 (class 2606 OID 32763594)
-- Name: fk_plot_section; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY plot
    ADD CONSTRAINT fk_plot_section FOREIGN KEY (idsection) REFERENCES section(idsection);


--
-- TOC entry 3015 (class 2606 OID 32763599)
-- Name: fk_section_crop_names; Type: FK CONSTRAINT; Schema: life_cycle; Owner: postgres
--

ALTER TABLE ONLY section
    ADD CONSTRAINT fk_section_crop_names FOREIGN KEY (idcropname) REFERENCES crop_names(idcropname);


SET search_path = machines, pg_catalog;

--
-- TOC entry 3016 (class 2606 OID 32763604)
-- Name: fk_insurance_machine; Type: FK CONSTRAINT; Schema: machines; Owner: postgres
--

ALTER TABLE ONLY insurance
    ADD CONSTRAINT fk_insurance_machine FOREIGN KEY (id_machine) REFERENCES machines(idmachine);


--
-- TOC entry 3017 (class 2606 OID 32763609)
-- Name: fk_machine_usage_machines; Type: FK CONSTRAINT; Schema: machines; Owner: postgres
--

ALTER TABLE ONLY machine_usage
    ADD CONSTRAINT fk_machine_usage_machines FOREIGN KEY (idmachine) REFERENCES machines(idmachine);


--
-- TOC entry 3018 (class 2606 OID 32763614)
-- Name: fk_machines_fuel_types; Type: FK CONSTRAINT; Schema: machines; Owner: postgres
--

ALTER TABLE ONLY machines
    ADD CONSTRAINT fk_machines_fuel_types FOREIGN KEY (id_fuel_type) REFERENCES fuel_types(idfueltype);


--
-- TOC entry 3019 (class 2606 OID 32763619)
-- Name: fk_machines_machine_types; Type: FK CONSTRAINT; Schema: machines; Owner: postgres
--

ALTER TABLE ONLY machines
    ADD CONSTRAINT fk_machines_machine_types FOREIGN KEY (id_machine_type) REFERENCES machine_types(idmachinetype);


--
-- TOC entry 3020 (class 2606 OID 32763624)
-- Name: fk_maintenance_and_calibration_machine; Type: FK CONSTRAINT; Schema: machines; Owner: postgres
--

ALTER TABLE ONLY maintenance_and_calibration
    ADD CONSTRAINT fk_maintenance_and_calibration_machine FOREIGN KEY (idmachine) REFERENCES machines(idmachine) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3021 (class 2606 OID 32763629)
-- Name: fk_maintenance_lines_maintenance; Type: FK CONSTRAINT; Schema: machines; Owner: postgres
--

ALTER TABLE ONLY maintenance_lines
    ADD CONSTRAINT fk_maintenance_lines_maintenance FOREIGN KEY (idmaintenance) REFERENCES maintenance_and_calibration(idmaintenance);


SET search_path = organizaciones, pg_catalog;

--
-- TOC entry 3022 (class 2606 OID 32763634)
-- Name: fk_empresa_departamento; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT fk_empresa_departamento FOREIGN KEY (id_departamento) REFERENCES general.departamento(id);


--
-- TOC entry 3023 (class 2606 OID 32763639)
-- Name: fk_empresa_municipio; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT fk_empresa_municipio FOREIGN KEY (id_municipio) REFERENCES general.municipio(id);


--
-- TOC entry 3024 (class 2606 OID 32763644)
-- Name: fk_empresa_organizacion; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT fk_empresa_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3025 (class 2606 OID 32763649)
-- Name: fk_empresa_pais; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa
    ADD CONSTRAINT fk_empresa_pais FOREIGN KEY (id_pais) REFERENCES general.pais(id);


--
-- TOC entry 3026 (class 2606 OID 32763654)
-- Name: fk_empresa_persona_empresa; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa_persona
    ADD CONSTRAINT fk_empresa_persona_empresa FOREIGN KEY (id_empresa) REFERENCES empresa(id);


--
-- TOC entry 3027 (class 2606 OID 32763659)
-- Name: fk_empresa_persona_farm; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa_persona
    ADD CONSTRAINT fk_empresa_persona_farm FOREIGN KEY (id_farm) REFERENCES life_cycle.farm(idfarm);


--
-- TOC entry 3028 (class 2606 OID 32763995)
-- Name: fk_empresa_persona_persona; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa_persona
    ADD CONSTRAINT fk_empresa_persona_persona FOREIGN KEY (id_persona) REFERENCES seguridad.persona(id);


--
-- TOC entry 3029 (class 2606 OID 32764018)
-- Name: fk_empresa_persona_tipo_cargo; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY empresa_persona
    ADD CONSTRAINT fk_empresa_persona_tipo_cargo FOREIGN KEY (id_tipo_cargo) REFERENCES tipo_cargo(id);


--
-- TOC entry 3030 (class 2606 OID 32763674)
-- Name: fk_finca_departamento; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY hacienda
    ADD CONSTRAINT fk_finca_departamento FOREIGN KEY (id_departamento) REFERENCES general.departamento(id);


--
-- TOC entry 3031 (class 2606 OID 32763679)
-- Name: fk_finca_municipio; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY hacienda
    ADD CONSTRAINT fk_finca_municipio FOREIGN KEY (id_municipio) REFERENCES general.municipio(id);


--
-- TOC entry 3032 (class 2606 OID 32763684)
-- Name: fk_finca_pais; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY hacienda
    ADD CONSTRAINT fk_finca_pais FOREIGN KEY (id_pais) REFERENCES general.pais(id);


--
-- TOC entry 3034 (class 2606 OID 32764000)
-- Name: fk_finca_persona; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY hacienda
    ADD CONSTRAINT fk_finca_persona FOREIGN KEY (id_contacto) REFERENCES seguridad.persona(id);


--
-- TOC entry 3033 (class 2606 OID 32763694)
-- Name: fk_id_empresa_finca; Type: FK CONSTRAINT; Schema: organizaciones; Owner: postgres
--

ALTER TABLE ONLY hacienda
    ADD CONSTRAINT fk_id_empresa_finca FOREIGN KEY (id_empresa) REFERENCES empresa(id);


SET search_path = sales, pg_catalog;

--
-- TOC entry 3035 (class 2606 OID 32763739)
-- Name: fk_final_products_crops; Type: FK CONSTRAINT; Schema: sales; Owner: postgres
--

ALTER TABLE ONLY final_products
    ADD CONSTRAINT fk_final_products_crops FOREIGN KEY (id_crop) REFERENCES life_cycle.crops(idcrop);


--
-- TOC entry 3036 (class 2606 OID 32763744)
-- Name: fk_final_products_farms; Type: FK CONSTRAINT; Schema: sales; Owner: postgres
--

ALTER TABLE ONLY final_products
    ADD CONSTRAINT fk_final_products_farms FOREIGN KEY (id_farm) REFERENCES life_cycle.farm(idfarm);


--
-- TOC entry 3037 (class 2606 OID 32763749)
-- Name: fk_order_header_customer; Type: FK CONSTRAINT; Schema: sales; Owner: postgres
--

ALTER TABLE ONLY order_header
    ADD CONSTRAINT fk_order_header_customer FOREIGN KEY (id_customer) REFERENCES customer(idcustomer);


--
-- TOC entry 3038 (class 2606 OID 32763754)
-- Name: fk_order_line_final_product; Type: FK CONSTRAINT; Schema: sales; Owner: postgres
--

ALTER TABLE ONLY order_lines
    ADD CONSTRAINT fk_order_line_final_product FOREIGN KEY (id_final_product) REFERENCES final_products(idfinalproduct);


--
-- TOC entry 3039 (class 2606 OID 32763759)
-- Name: fk_order_line_header; Type: FK CONSTRAINT; Schema: sales; Owner: postgres
--

ALTER TABLE ONLY order_lines
    ADD CONSTRAINT fk_order_line_header FOREIGN KEY (id_order) REFERENCES order_header(idorder);


SET search_path = seguridad, pg_catalog;

--
-- TOC entry 3040 (class 2606 OID 32763764)
-- Name: fk_id_menu_padre; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT fk_id_menu_padre FOREIGN KEY (id_menu_padre) REFERENCES menu(id);


--
-- TOC entry 3048 (class 2606 OID 32763769)
-- Name: fk_id_metodo; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY rol_metodo
    ADD CONSTRAINT fk_id_metodo FOREIGN KEY (id_metodo) REFERENCES metodo(id);


--
-- TOC entry 3049 (class 2606 OID 32763774)
-- Name: fk_id_rol_metodo; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY rol_metodo
    ADD CONSTRAINT fk_id_rol_metodo FOREIGN KEY (id_rol) REFERENCES rol(id);


--
-- TOC entry 3041 (class 2606 OID 32763779)
-- Name: fk_menu_icono; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY menu
    ADD CONSTRAINT fk_menu_icono FOREIGN KEY (id_icono) REFERENCES icono(id);


--
-- TOC entry 3042 (class 2606 OID 32763784)
-- Name: fk_metodo_menu_menu; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY metodo_menu
    ADD CONSTRAINT fk_metodo_menu_menu FOREIGN KEY (id_menu) REFERENCES menu(id);


--
-- TOC entry 3043 (class 2606 OID 32763789)
-- Name: fk_permiso_persona_empresa_empresa; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY permiso_persona_empresa
    ADD CONSTRAINT fk_permiso_persona_empresa_empresa FOREIGN KEY (id_empresa) REFERENCES organizaciones.empresa(id);


--
-- TOC entry 3044 (class 2606 OID 32763794)
-- Name: fk_permiso_persona_empresa_farm; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY permiso_persona_empresa
    ADD CONSTRAINT fk_permiso_persona_empresa_farm FOREIGN KEY (id_farm) REFERENCES life_cycle.farm(idfarm);


--
-- TOC entry 3045 (class 2606 OID 32763990)
-- Name: fk_permiso_persona_empresa_persona; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY permiso_persona_empresa
    ADD CONSTRAINT fk_permiso_persona_empresa_persona FOREIGN KEY (id_persona) REFERENCES persona(id);


--
-- TOC entry 3081 (class 2606 OID 32763950)
-- Name: fk_persona_civil_status; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_civil_status FOREIGN KEY (id_civil_status) REFERENCES general.civil_status(id);


--
-- TOC entry 3080 (class 2606 OID 32763955)
-- Name: fk_persona_departamento_nacimiento; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_departamento_nacimiento FOREIGN KEY (id_departamento_nac) REFERENCES general.departamento(id);


--
-- TOC entry 3079 (class 2606 OID 32763960)
-- Name: fk_persona_departamento_residencia; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_departamento_residencia FOREIGN KEY (id_departamento_res) REFERENCES general.departamento(id);


--
-- TOC entry 3078 (class 2606 OID 32763965)
-- Name: fk_persona_municipio_nacimiento; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_municipio_nacimiento FOREIGN KEY (id_municipio_nac) REFERENCES general.municipio(id);


--
-- TOC entry 3077 (class 2606 OID 32763970)
-- Name: fk_persona_municipio_residencia; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_municipio_residencia FOREIGN KEY (id_municipio_res) REFERENCES general.municipio(id);


--
-- TOC entry 3076 (class 2606 OID 32763975)
-- Name: fk_persona_pais_nacimiento; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_pais_nacimiento FOREIGN KEY (id_pais_nac) REFERENCES general.pais(id);


--
-- TOC entry 3075 (class 2606 OID 32763980)
-- Name: fk_persona_pais_residencia; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_pais_residencia FOREIGN KEY (id_pais_res) REFERENCES general.pais(id);


--
-- TOC entry 3074 (class 2606 OID 32763985)
-- Name: fk_persona_tipo_documento; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT fk_persona_tipo_documento FOREIGN KEY (id_tipo_documento) REFERENCES general.tipo_documento(id);


--
-- TOC entry 3050 (class 2606 OID 32763804)
-- Name: fk_rol; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY rol_usuario
    ADD CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES rol(id);


--
-- TOC entry 3046 (class 2606 OID 32763809)
-- Name: fk_rol_menu_menu; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY rol_menu
    ADD CONSTRAINT fk_rol_menu_menu FOREIGN KEY (id_menu) REFERENCES menu(id);


--
-- TOC entry 3047 (class 2606 OID 32763814)
-- Name: fk_rol_menu_rol; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY rol_menu
    ADD CONSTRAINT fk_rol_menu_rol FOREIGN KEY (id_rol) REFERENCES rol(id);


--
-- TOC entry 3051 (class 2606 OID 32763819)
-- Name: fk_usuario; Type: FK CONSTRAINT; Schema: seguridad; Owner: postgres
--

ALTER TABLE ONLY rol_usuario
    ADD CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id);


SET search_path = services, pg_catalog;

--
-- TOC entry 3052 (class 2606 OID 32763824)
-- Name: fk_service_activity; Type: FK CONSTRAINT; Schema: services; Owner: postgres
--

ALTER TABLE ONLY external_services_and_activities
    ADD CONSTRAINT fk_service_activity FOREIGN KEY (idactivity) REFERENCES costs.activities(idactivity);


--
-- TOC entry 3053 (class 2606 OID 32763829)
-- Name: fk_service_service_type; Type: FK CONSTRAINT; Schema: services; Owner: postgres
--

ALTER TABLE ONLY external_services_and_activities
    ADD CONSTRAINT fk_service_service_type FOREIGN KEY (idservicetype) REFERENCES service_type(idservicetype);


SET search_path = utz, pg_catalog;

--
-- TOC entry 3054 (class 2606 OID 32763834)
-- Name: fk_activities_and_certifications_activities; Type: FK CONSTRAINT; Schema: utz; Owner: postgres
--

ALTER TABLE ONLY activities_and_certifications
    ADD CONSTRAINT fk_activities_and_certifications_activities FOREIGN KEY (idactivity) REFERENCES costs.activities(idactivity) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3055 (class 2606 OID 32763839)
-- Name: fk_activities_and_certifications_certifications_and_roles; Type: FK CONSTRAINT; Schema: utz; Owner: postgres
--

ALTER TABLE ONLY activities_and_certifications
    ADD CONSTRAINT fk_activities_and_certifications_certifications_and_roles FOREIGN KEY (idcertificationsandroles) REFERENCES certifications_and_roles(idcertificactionsandroles) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3056 (class 2606 OID 32763844)
-- Name: fk_activities_and_certifications_certifications_and_roles; Type: FK CONSTRAINT; Schema: utz; Owner: postgres
--

ALTER TABLE ONLY hr_certifications_and_roles
    ADD CONSTRAINT fk_activities_and_certifications_certifications_and_roles FOREIGN KEY (idcertificationsandroles) REFERENCES certifications_and_roles(idcertificactionsandroles) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 3057 (class 2606 OID 32763849)
-- Name: fk_hr_certifications_and_roles_hr; Type: FK CONSTRAINT; Schema: utz; Owner: postgres
--

ALTER TABLE ONLY hr_certifications_and_roles
    ADD CONSTRAINT fk_hr_certifications_and_roles_hr FOREIGN KEY (idhr) REFERENCES human_resources.hr(idhr) ON UPDATE CASCADE ON DELETE CASCADE;


SET search_path = warehouse, pg_catalog;

--
-- TOC entry 3058 (class 2606 OID 32763854)
-- Name: fk_deposits_farm; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY deposits
    ADD CONSTRAINT fk_deposits_farm FOREIGN KEY (id_farm) REFERENCES life_cycle.farm(idfarm);


--
-- TOC entry 3059 (class 2606 OID 32763859)
-- Name: fk_deposits_material; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY deposits
    ADD CONSTRAINT fk_deposits_material FOREIGN KEY (id_material) REFERENCES materials(idmaterial);


--
-- TOC entry 3060 (class 2606 OID 32763864)
-- Name: fk_deposits_measurement_unit; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY deposits
    ADD CONSTRAINT fk_deposits_measurement_unit FOREIGN KEY (id_measurement_unit) REFERENCES measurement_units(idmeasurementunits);


--
-- TOC entry 3061 (class 2606 OID 32763869)
-- Name: fk_deposits_purchase_invoice; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY deposits
    ADD CONSTRAINT fk_deposits_purchase_invoice FOREIGN KEY (id_purchase_invoice) REFERENCES purchase_invoices(idpurchaseinvoice);


--
-- TOC entry 3062 (class 2606 OID 32763874)
-- Name: fk_invoice_items_materials; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY invoice_items
    ADD CONSTRAINT fk_invoice_items_materials FOREIGN KEY (id_material) REFERENCES materials(idmaterial);


--
-- TOC entry 3063 (class 2606 OID 32763879)
-- Name: fk_invoice_items_purchase_invoices; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY invoice_items
    ADD CONSTRAINT fk_invoice_items_purchase_invoices FOREIGN KEY (id_purchase_invoice) REFERENCES purchase_invoices(idpurchaseinvoice);


--
-- TOC entry 3064 (class 2606 OID 32763884)
-- Name: fk_iva_rate; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY invoice_items
    ADD CONSTRAINT fk_iva_rate FOREIGN KEY (id_iva_rate) REFERENCES general.iva_rate(id_iva);


--
-- TOC entry 3065 (class 2606 OID 32763889)
-- Name: fk_materials_hr_responsible; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY materials
    ADD CONSTRAINT fk_materials_hr_responsible FOREIGN KEY (id_responsible) REFERENCES human_resources.hr(idhr);


--
-- TOC entry 3066 (class 2606 OID 32763894)
-- Name: fk_materials_material_type; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY materials
    ADD CONSTRAINT fk_materials_material_type FOREIGN KEY (id_material_type) REFERENCES materials_types(idmaterialtype);


--
-- TOC entry 3067 (class 2606 OID 32763899)
-- Name: fk_materials_measurement_units; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY materials
    ADD CONSTRAINT fk_materials_measurement_units FOREIGN KEY (id_measurement_units) REFERENCES measurement_units(idmeasurementunits);


--
-- TOC entry 3068 (class 2606 OID 32763904)
-- Name: fk_materials_type_of_management; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY materials
    ADD CONSTRAINT fk_materials_type_of_management FOREIGN KEY (id_type_of_management) REFERENCES type_of_management(idtypeofmanagement);


--
-- TOC entry 3069 (class 2606 OID 32763909)
-- Name: fk_purchase_invoices_supplier; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY purchase_invoices
    ADD CONSTRAINT fk_purchase_invoices_supplier FOREIGN KEY (id_supplier) REFERENCES suppliers(idsupplier);


--
-- TOC entry 3070 (class 2606 OID 32763914)
-- Name: fk_transactions_activities; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY transactions
    ADD CONSTRAINT fk_transactions_activities FOREIGN KEY (id_activity) REFERENCES costs.activities(idactivity) MATCH FULL;


--
-- TOC entry 3071 (class 2606 OID 32763919)
-- Name: fk_transactions_deposits; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY transactions
    ADD CONSTRAINT fk_transactions_deposits FOREIGN KEY (id_deposit) REFERENCES deposits(iddeposit);


--
-- TOC entry 3072 (class 2606 OID 32763924)
-- Name: fk_transactions_transaction_type; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY transactions
    ADD CONSTRAINT fk_transactions_transaction_type FOREIGN KEY (id_transaction_type) REFERENCES transaction_type(idtransactiontype) MATCH FULL;


--
-- TOC entry 3073 (class 2606 OID 32763929)
-- Name: fk_withdrawals_hr; Type: FK CONSTRAINT; Schema: warehouse; Owner: postgres
--

ALTER TABLE ONLY transactions
    ADD CONSTRAINT fk_withdrawals_hr FOREIGN KEY (id_hr) REFERENCES human_resources.hr(idhr);


--
-- TOC entry 3199 (class 0 OID 0)
-- Dependencies: 21
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-05-05 11:11:57

--
-- PostgreSQL database dump complete
--

