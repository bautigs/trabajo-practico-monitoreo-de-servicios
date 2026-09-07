CREATE SEQUENCE IF NOT EXISTS comunidad_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS empresa_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS entidad_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS establecimiento_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS incidente_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS miembro_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS organismo_control_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS persona_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS servicio_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS tipo_entidad_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS tipo_servicio_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS usuario_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE comunidad
(
    id     BIGINT NOT NULL,
    nombre VARCHAR(255),
    CONSTRAINT pk_comunidad PRIMARY KEY (id)
);

CREATE TABLE comunidad_servicios_de_interes
(
    comunidad_id            BIGINT NOT NULL,
    servicios_de_interes_id BIGINT NOT NULL
);

CREATE TABLE empresa
(
    id     BIGINT NOT NULL,
    nombre VARCHAR(255),
    CONSTRAINT pk_empresa PRIMARY KEY (id)
);

CREATE TABLE entidad
(
    id              BIGINT NOT NULL,
    nombre          VARCHAR(255),
    tipo_entidad_id BIGINT,
    empresa_id      BIGINT,
    CONSTRAINT pk_entidad PRIMARY KEY (id)
);

CREATE TABLE establecimiento
(
    id           BIGINT NOT NULL,
    nombre       VARCHAR(255),
    localidad_id BIGINT,
    provincia_id BIGINT,
    municipio_id BIGINT,
    entidad_id   BIGINT,
    CONSTRAINT pk_establecimiento PRIMARY KEY (id)
);

CREATE TABLE incidente
(
    id                     BIGINT NOT NULL,
    comunidad_id           BIGINT,
    estado                 VARCHAR(255),
    fecha_apertura         TIMESTAMP WITHOUT TIME ZONE,
    fecha_cierre           TIMESTAMP WITHOUT TIME ZONE,
    descripcion            TEXT,
    miembro_de_apertura_id BIGINT,
    miembro_de_cierre_id   BIGINT,
    descripcion_lugar      VARCHAR(255),
    servicio_id            BIGINT,
    CONSTRAINT pk_incidente PRIMARY KEY (id)
);

CREATE TABLE localidad
(
    id           BIGINT NOT NULL,
    nombre       VARCHAR(255),
    municipio_id BIGINT,
    CONSTRAINT pk_localidad PRIMARY KEY (id)
);

CREATE TABLE miembro
(
    id                      BIGINT NOT NULL,
    estrategia_notificacion VARCHAR(255),
    configuracion_recepcion VARCHAR(255),
    comunidad_id            BIGINT,
    rol                     VARCHAR(255),
    condicion_de_miembro    VARCHAR(255),
    persona_id              BIGINT,
    CONSTRAINT pk_miembro PRIMARY KEY (id)
);

CREATE TABLE municipio
(
    id           BIGINT NOT NULL,
    nombre       VARCHAR(255),
    provincia_id BIGINT,
    CONSTRAINT pk_municipio PRIMARY KEY (id)
);

CREATE TABLE organismo_control
(
    id               BIGINT NOT NULL,
    nombre           VARCHAR(255),
    tipo_servicio_id BIGINT,
    CONSTRAINT pk_organismocontrol PRIMARY KEY (id)
);

CREATE TABLE persona
(
    id                      BIGINT NOT NULL,
    usuario_id              BIGINT,
    nombre_apellido         VARCHAR(255),
    mail_de_contacto        VARCHAR(255),
    numero_de_contacto      VARCHAR(255),
    estrategia_notificacion VARCHAR(255),
    configuracion_recepcion VARCHAR(255),
    provincia_id            BIGINT,
    municipio_id            BIGINT,
    localidad_id            BIGINT,
    rol_persona             VARCHAR(255),
    CONSTRAINT pk_persona PRIMARY KEY (id)
);

CREATE TABLE persona_entidades_de_interes
(
    persona_id              BIGINT NOT NULL,
    entidades_de_interes_id BIGINT NOT NULL
);

CREATE TABLE persona_tipos_de_servicios_de_interes
(
    persona_id                       BIGINT NOT NULL,
    tipos_de_servicios_de_interes_id BIGINT NOT NULL
);

CREATE TABLE provincia
(
    id     BIGINT NOT NULL,
    nombre VARCHAR(255),
    CONSTRAINT pk_provincia PRIMARY KEY (id)
);

CREATE TABLE servicio
(
    id                 BIGINT NOT NULL,
    nombre             VARCHAR(255),
    tipo_servicio_id   BIGINT,
    esta_activo        BOOLEAN,
    establecimiento_id BIGINT,
    CONSTRAINT pk_servicio PRIMARY KEY (id)
);

CREATE TABLE tipo_entidad
(
    id     BIGINT NOT NULL,
    nombre VARCHAR(255),
    CONSTRAINT pk_tipoentidad PRIMARY KEY (id)
);

CREATE TABLE tipo_servicio
(
    id     BIGINT NOT NULL,
    nombre VARCHAR(255),
    CONSTRAINT pk_tiposervicio PRIMARY KEY (id)
);

CREATE TABLE usuario
(
    id             BIGINT NOT NULL,
    nombre_usuario VARCHAR(255),
    contrasenia    VARCHAR(255),
    fecha_de_alta  date,
    CONSTRAINT pk_usuario PRIMARY KEY (id)
);

ALTER TABLE persona
    ADD CONSTRAINT uc_persona_usuario UNIQUE (usuario_id);

ALTER TABLE entidad
    ADD CONSTRAINT FK_ENTIDAD_ON_EMPRESA FOREIGN KEY (empresa_id) REFERENCES empresa (id);

ALTER TABLE entidad
    ADD CONSTRAINT FK_ENTIDAD_ON_TIPOENTIDAD FOREIGN KEY (tipo_entidad_id) REFERENCES tipo_entidad (id);

ALTER TABLE establecimiento
    ADD CONSTRAINT FK_ESTABLECIMIENTO_ON_ENTIDAD FOREIGN KEY (entidad_id) REFERENCES entidad (id);

ALTER TABLE establecimiento
    ADD CONSTRAINT FK_ESTABLECIMIENTO_ON_LOCALIDAD FOREIGN KEY (localidad_id) REFERENCES localidad (id);

ALTER TABLE establecimiento
    ADD CONSTRAINT FK_ESTABLECIMIENTO_ON_MUNICIPIO FOREIGN KEY (municipio_id) REFERENCES municipio (id);

ALTER TABLE establecimiento
    ADD CONSTRAINT FK_ESTABLECIMIENTO_ON_PROVINCIA FOREIGN KEY (provincia_id) REFERENCES provincia (id);

ALTER TABLE incidente
    ADD CONSTRAINT FK_INCIDENTE_ON_COMUNIDAD FOREIGN KEY (comunidad_id) REFERENCES comunidad (id);

ALTER TABLE incidente
    ADD CONSTRAINT FK_INCIDENTE_ON_MIEMBRODEAPERTURA FOREIGN KEY (miembro_de_apertura_id) REFERENCES miembro (id);

ALTER TABLE incidente
    ADD CONSTRAINT FK_INCIDENTE_ON_MIEMBRODECIERRE FOREIGN KEY (miembro_de_cierre_id) REFERENCES miembro (id);

ALTER TABLE incidente
    ADD CONSTRAINT FK_INCIDENTE_ON_SERVICIO FOREIGN KEY (servicio_id) REFERENCES servicio (id);

ALTER TABLE localidad
    ADD CONSTRAINT FK_LOCALIDAD_ON_MUNICIPIO FOREIGN KEY (municipio_id) REFERENCES municipio (id);

ALTER TABLE miembro
    ADD CONSTRAINT FK_MIEMBRO_ON_COMUNIDAD FOREIGN KEY (comunidad_id) REFERENCES comunidad (id);

ALTER TABLE miembro
    ADD CONSTRAINT FK_MIEMBRO_ON_PERSONA FOREIGN KEY (persona_id) REFERENCES persona (id);

ALTER TABLE municipio
    ADD CONSTRAINT FK_MUNICIPIO_ON_PROVINCIA FOREIGN KEY (provincia_id) REFERENCES provincia (id);

ALTER TABLE organismo_control
    ADD CONSTRAINT FK_ORGANISMOCONTROL_ON_TIPOSERVICIO FOREIGN KEY (tipo_servicio_id) REFERENCES tipo_servicio (id);

ALTER TABLE persona
    ADD CONSTRAINT FK_PERSONA_ON_LOCALIDAD FOREIGN KEY (localidad_id) REFERENCES localidad (id);

ALTER TABLE persona
    ADD CONSTRAINT FK_PERSONA_ON_MUNICIPIO FOREIGN KEY (municipio_id) REFERENCES municipio (id);

ALTER TABLE persona
    ADD CONSTRAINT FK_PERSONA_ON_PROVINCIA FOREIGN KEY (provincia_id) REFERENCES provincia (id);

ALTER TABLE persona
    ADD CONSTRAINT FK_PERSONA_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (id);

ALTER TABLE servicio
    ADD CONSTRAINT FK_SERVICIO_ON_ESTABLECIMIENTO FOREIGN KEY (establecimiento_id) REFERENCES establecimiento (id);

ALTER TABLE servicio
    ADD CONSTRAINT FK_SERVICIO_ON_TIPOSERVICIO FOREIGN KEY (tipo_servicio_id) REFERENCES tipo_servicio (id);

ALTER TABLE comunidad_servicios_de_interes
    ADD CONSTRAINT fk_comserdeint_on_comunidad FOREIGN KEY (comunidad_id) REFERENCES comunidad (id);

ALTER TABLE comunidad_servicios_de_interes
    ADD CONSTRAINT fk_comserdeint_on_tipo_servicio FOREIGN KEY (servicios_de_interes_id) REFERENCES tipo_servicio (id);

ALTER TABLE persona_entidades_de_interes
    ADD CONSTRAINT fk_perentdeint_on_entidad FOREIGN KEY (entidades_de_interes_id) REFERENCES entidad (id);

ALTER TABLE persona_entidades_de_interes
    ADD CONSTRAINT fk_perentdeint_on_persona FOREIGN KEY (persona_id) REFERENCES persona (id);

ALTER TABLE persona_tipos_de_servicios_de_interes
    ADD CONSTRAINT fk_pertipdeserdeint_on_persona FOREIGN KEY (persona_id) REFERENCES persona (id);

ALTER TABLE persona_tipos_de_servicios_de_interes
    ADD CONSTRAINT fk_pertipdeserdeint_on_tipo_servicio FOREIGN KEY (tipos_de_servicios_de_interes_id) REFERENCES tipo_servicio (id);