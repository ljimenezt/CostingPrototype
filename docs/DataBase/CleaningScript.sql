------------------------- A SEGURIDAD ------------------------------
SET search_path = a_seguridad, pg_catalog;

------------------------- KPI ------------------------------
SET search_path = kpi, pg_catalog;

DELETE FROM bean_index;

ALTER SEQUENCE bean_index_id_seq restart with 1;

------------------------- UTZ ------------------------------
SET search_path = utz, pg_catalog;

DELETE FROM hr_certifications_and_roles;
DELETE FROM activities_and_certifications;
DELETE FROM certifications_and_roles;

ALTER SEQUENCE certifications_and_roles_id_seq restart with 1;

------------------------- SERVICES ------------------------------
SET search_path = services, pg_catalog;

DELETE FROM external_services_and_activities;
DELETE FROM service_type WHERE idservicetype >= 3;

ALTER SEQUENCE ext_services_and_activities_id_seq restart with 1;
ALTER SEQUENCE service_type_id_seq restart with 3;

------------------------- SALES ------------------------------
SET search_path = sales, pg_catalog;

DELETE FROM final_products;
DELETE FROM order_lines;
DELETE FROM order_header;
DELETE FROM customer;

ALTER SEQUENCE customer_id_seq restart with 1;
ALTER SEQUENCE final_products_id_seq restart with 1;
ALTER SEQUENCE order_header_id_seq restart with 1;

------------------------- ORGANIZACIONES ------------------------------
SET search_path = organizaciones, pg_catalog;

DELETE FROM empresa_persona;
DELETE FROM hacienda;
DELETE FROM empresa;
DELETE FROM organizacion;

ALTER SEQUENCE empresa_persona_id_seq restart with 1;
ALTER SEQUENCE finca_id_seq restart with 1;
ALTER SEQUENCE empresa_id_seq restart with 1;
ALTER SEQUENCE organizacion_id_seq restart with 1;

------------------------- RECURSOS HUMANOS ------------------------------
SET search_path = seguridad, pg_catalog;

DELETE FROM persona WHERE id >= 8;

ALTER SEQUENCE persona_id_seq restart with 8;


------------------------- COSTS ------------------------------
SET search_path = costs, pg_catalog;

DELETE FROM activities_and_hr;
DELETE FROM activity_machine;
DELETE FROM activity_materials;
DELETE FROM activity_team;
DELETE FROM consumable_use;
DELETE FROM cycle_standard_activities;
DELETE FROM task_payment;
DELETE FROM general_costs_company_allocated_crop;
DELETE FROM general_costs_plot_allocated_crop;
DELETE FROM general_cost_per_company;
DELETE FROM general_cost_per_crop;
DELETE FROM general_cost_per_plot;
DELETE FROM cost_types;
DELETE FROM allocation;

ALTER SEQUENCE cycle_activities_id_seq restart with 1;
ALTER SEQUENCE general_cost_per_company_id_seq restart with 1;
ALTER SEQUENCE general_cost_per_crop_id_seq restart with 1;
ALTER SEQUENCE general_cost_per_plot_id_seq restart with 1;
ALTER SEQUENCE cost_types_id_seq restart with 1;
ALTER SEQUENCE allocation_id_seq restart with 1;

------------------------- WAREHOUSE ------------------------------
SET search_path = warehouse, pg_catalog;

DELETE FROM transactions;
DELETE FROM deposits;
DELETE FROM invoice_items;
DELETE FROM purchase_invoices;
DELETE FROM transaction_type WHERE idtransactiontype >= 9;
DELETE FROM measurement_units WHERE idmeasurementunits >= 14;
DELETE FROM materials_types WHERE idmaterialtype >= 11;
DELETE FROM diseases WHERE iddisease >= 11;
DELETE FROM type_of_management WHERE idtypeofmanagement >= 9;

ALTER SEQUENCE transactions_id_seq restart with 1;
ALTER SEQUENCE deposits_id_seq restart with 1;
ALTER SEQUENCE invoice_items_id_invoice_item_seq restart with 1;
ALTER SEQUENCE purchase_invoices_id_seq restart with 1;
ALTER SEQUENCE transaction_types_id_seq restart with 9;
ALTER SEQUENCE measurement_units_id_seq restart with 14;
ALTER SEQUENCE materials_types_id_seq restart with 11;
ALTER SEQUENCE diseases_id_seq restart with 11;
ALTER SEQUENCE type_of_management_id_seq restart with 9;

------------------------- DIESEL ------------------------------
SET search_path = diesel, pg_catalog;

DELETE FROM consumable_resources;
DELETE FROM fuel_usage_log;
DELETE FROM irrigation_details;
DELETE FROM engine_log;
DELETE FROM fuel_purchase;
DELETE FROM zone WHERE id >= 3;

ALTER SEQUENCE consumable_resources_id_seq restart with 1;
ALTER SEQUENCE engine_log_id_engine_log_seq restart with 1;
ALTER SEQUENCE fuel_purchase_id_fuel_purchase_seq restart with 1;
ALTER SEQUENCE fuel_usage_log_id_fuel_usage_seq restart with 1;
ALTER SEQUENCE irrigation_details_id_irrigation_details_seq restart with 1;
ALTER SEQUENCE zone_id_seq restart with 3;


------------------------- LIFE CYCLE ------------------------------
SET search_path = life_cycle, pg_catalog;

DELETE FROM activity_plot;
DELETE FROM crops_plots;
DELETE FROM plot WHERE idplot >= 223;
DELETE FROM pluviometer;
DELETE FROM section;
DELETE FROM farm;
DELETE FROM details_harvest;
DELETE FROM crops WHERE idcrop >= 2;
DELETE FROM crop_names WHERE idcropname >= 2;
DELETE FROM activity_names WHERE idactivityname >= 12;

ALTER SEQUENCE plot_id_seq restart with 223;
ALTER SEQUENCE pluviometer_id_seq restart with 1;
ALTER SEQUENCE section_id_seq restart with 1;
ALTER SEQUENCE farm_id_seq restart with 1;
ALTER SEQUENCE details_harvest_id_seq restart with 1;
ALTER SEQUENCE crops_id_seq restart with 2;
ALTER SEQUENCE crop_names_id_seq restart with 2;
ALTER SEQUENCE activity_names_id_seq restart with 12;

------------------------- COSTS ------------------------------
SET search_path = costs, pg_catalog;

DELETE FROM activities;

ALTER SEQUENCE activities_id_seq restart with 1;

------------------------- LIFE CYCLE ------------------------------
SET search_path = life_cycle, pg_catalog;

DELETE FROM cycle;

ALTER SEQUENCE cycle_id_seq restart with 1;

------------------------- HUMAN RESOURCES ------------------------------
SET search_path = human_resources, pg_catalog;

DELETE FROM assist_control;
DELETE FROM day_type_food WHERE id >= 11;
DELETE FROM food_control;
DELETE FROM novelty;
DELETE FROM overtime_payment_rate WHERE overtimepaymentid >= 3;
DELETE FROM payments;
DELETE FROM team_members;
DELETE FROM team;
DELETE FROM novelty_type WHERE id >= 4;
DELETE FROM hr_types WHERE idhrtype >= 17;
DELETE FROM contract;
DELETE FROM contract_type WHERE id >= 8;
DELETE FROM payment_methods WHERE idpaymentmethod >= 4;

ALTER SEQUENCE assist_control_id_seq restart with 1;
ALTER SEQUENCE day_type_food_id_seq restart with 11;
ALTER SEQUENCE food_control_id_seq restart with 1;
ALTER SEQUENCE novelty_id_seq restart with 1;
ALTER SEQUENCE team_id_seq restart with 1;
ALTER SEQUENCE payments_id_seq restart with 1;
ALTER SEQUENCE overtime_payment_rate_id_seq restart with 3;
ALTER SEQUENCE novelty_type_id_seq restart with 4; 
ALTER SEQUENCE hr_types_id_seq restart with 17;
ALTER SEQUENCE contract_id_seq restart with 1;
ALTER SEQUENCE contract_type_id_seq restart with 8;
ALTER SEQUENCE payment_methods_id_seq restart with 4;

------------------------- MACHINES ------------------------------
SET search_path = machines, pg_catalog;

DELETE FROM insurance;
DELETE FROM machine_usage;
DELETE FROM maintenance_and_calibration;
DELETE FROM maintenance_lines;
DELETE FROM machines WHERE idmachine >= 10;
DELETE FROM machine_types WHERE idmachinetype >= 4;
DELETE FROM fuel_types WHERE idfueltype >= 13;

ALTER SEQUENCE insurance_id_seq restart with 1;
ALTER SEQUENCE maintenance_lines_id_seq restart with 1;
ALTER SEQUENCE maintenance_and_calibration_id_seq restart with 1;
ALTER SEQUENCE machine_types_id_seq restart with 4;
ALTER SEQUENCE fuel_types_id_seq restart with 13;
ALTER SEQUENCE machines_id_seq restart with 10;

------------------------- GENERAL ------------------------------
SET search_path = general, pg_catalog;

DELETE FROM civil_status WHERE id >= 5;
DELETE FROM color WHERE id >= 4;
DELETE FROM conversion_unidad;
DELETE FROM day WHERE id >= 8;
DELETE FROM departamento WHERE id >= 53;
DELETE FROM estado;
DELETE FROM holiday WHERE id >= 15;
DELETE FROM iva_rate WHERE id_iva >= 4;
DELETE FROM moneda WHERE id >= 4;
DELETE FROM municipio WHERE id >= 221;
DELETE FROM pais WHERE id >= 245;
DELETE FROM system_profile WHERE id >= 2;
DELETE FROM tipo_documento WHERE id >= 3;
DELETE FROM tipo_unidad;
DELETE FROM type_food WHERE id >= 4;
DELETE FROM type_unit;
DELETE FROM unit_conversions;

ALTER SEQUENCE civil_status_id_seq restart with 5;
ALTER SEQUENCE color_id_seq restart with 4;
ALTER SEQUENCE day_id_seq restart with  8;
ALTER SEQUENCE departamento_id_seq restart with 53;
ALTER SEQUENCE estado_id_seq restart with 1;
ALTER SEQUENCE holiday_id_seq restart with  15;
ALTER SEQUENCE iva_rate_id_iva_seq restart with  4;
ALTER SEQUENCE moneda_id_seq restart with 4;
ALTER SEQUENCE municipio_id_seq restart with  221;
ALTER SEQUENCE pais_id_seq restart with 245;
ALTER SEQUENCE system_profile_id_seq restart with 2;
ALTER SEQUENCE tipo_documento_id_seq restart with 3;
ALTER SEQUENCE tipo_unidad_id_seq restart with  1;
ALTER SEQUENCE type_food_id_seq restart with  4;
ALTER SEQUENCE unidad_medida_id_seq restart with  1;

------------------------- WAREHOUSE ------------------------------
SET search_path = warehouse, pg_catalog;

DELETE FROM materials WHERE idmaterial >= 373;
DELETE FROM suppliers WHERE idsupplier >= 38;

ALTER SEQUENCE  materials_id_seq restart with 373;
ALTER SEQUENCE suppliers_id_seq restart with 38;


------------------------- HUMAN RESOURCE ------------------------------

SET search_path = human_resources, pg_catalog;

DELETE FROM hr WHERE  idhr >= 20;

ALTER SEQUENCE hr_id_seq restart with 20;

------------------------- SEGURIDAD ------------------------------
SET search_path = seguridad, pg_catalog;

DELETE FROM metodo_menu WHERE  id >= 142;
DELETE FROM rol_menu WHERE id_rol>4 or id_menu>229;
DELETE FROM rol_metodo WHERE id_rol>4 or id_metodo>76;
DELETE FROM rol_usuario WHERE id_rol>4 or id_usuario>7;
DELETE FROM usuario WHERE id >= 8;
DELETE FROM rol WHERE id >= 5;
DELETE FROM registro_sesion;
DELETE FROM permiso_persona_empresa;
DELETE FROM perfil_sistema WHERE  id >= 2;
DELETE FROM menu WHERE  id >= 229;
DELETE FROM metodo WHERE  id >= 76;
DELETE FROM icono WHERE  id >= 80;

ALTER SEQUENCE metodo_menu_id_seq restart with 142;
ALTER SEQUENCE usuario_id_seq restart with 8;
ALTER SEQUENCE rol_id_seq restart with 5;
ALTER SEQUENCE registro_sesion_id_seq restart with 1;
ALTER SEQUENCE permiso_persona_empresa_id_seq restart with 1;
ALTER SEQUENCE perfil_sistema_id_seq restart with 2;
ALTER SEQUENCE menu_id_seq restart with 229;
ALTER SEQUENCE metodo_id_seq restart with 76;
ALTER SEQUENCE icono_id_seq restart with 80;


