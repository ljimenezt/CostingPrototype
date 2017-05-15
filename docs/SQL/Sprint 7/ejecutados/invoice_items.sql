
CREATE TABLE warehouse.invoice_items
(
	id_invoice_item serial NOT NULL,
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
  	note text,
	CONSTRAINT PK_invoice_items PRIMARY KEY (id_invoice_item),
	CONSTRAINT FK_invoice_items_materials FOREIGN KEY (id_material) 
		REFERENCES warehouse.materials (idmaterial) 
		ON DELETE No Action ON UPDATE No Action,
	CONSTRAINT FK_invoice_items_purchase_invoices FOREIGN KEY (id_purchase_invoice) 
		REFERENCES warehouse.purchase_invoices (idpurchaseinvoice) 
		ON DELETE No Action ON UPDATE No Action,
	CONSTRAINT fk_iva_rate FOREIGN KEY	(id_iva_rate) 
		REFERENCES general.iva_rate (id_iva) 
		ON UPDATE NO ACTION ON DELETE NO ACTION
);