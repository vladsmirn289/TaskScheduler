ALTER TABLE client ADD COLUMN jwt_token character varying(500);
ALTER TABLE task ALTER COLUMN description TYPE character varying(1000);