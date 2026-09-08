ALTER TABLE persona DROP CONSTRAINT FK_PERSONA_ON_USUARIO;
ALTER TABLE persona DROP CONSTRAINT uc_persona_usuario;
ALTER TABLE persona DROP COLUMN usuario_id;

DROP TABLE usuario;
DROP SEQUENCE IF EXISTS usuario_seq;

ALTER TABLE persona ADD COLUMN keycloak_id VARCHAR(255);
ALTER TABLE persona ADD CONSTRAINT uc_persona_keycloak_id UNIQUE (keycloak_id);
ALTER TABLE persona ADD COLUMN fecha_de_alta date;
