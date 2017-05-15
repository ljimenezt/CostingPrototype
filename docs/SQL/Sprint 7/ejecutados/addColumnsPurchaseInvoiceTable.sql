ALTER TABLE warehouse.purchase_invoices
ADD COLUMN subtotal double precision NOT NULL,
ADD COLUMN shipping double precision NOT NULL, 
ADD COLUMN packaging double precision NOT NULL, 
ADD COLUMN taxes double precision NOT NULL,
ADD COLUMN discount double precision NOT NULL,
ADD COLUMN note text,
ADD COLUMN invoice_document_link character varying(100);

ALTER TABLE warehouse.purchase_invoices
ALTER COLUMN total_value_actual SET NOT NULL,
ALTER COLUMN invoice_number SET NOT NULL,
ALTER COLUMN date_time SET NOT NULL;