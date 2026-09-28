-- ============================================================================
--  Seed de desarrollo — rama Spring Boot (Postgres + Flyway + Keycloak)
--  Base: Postgres `tp` en localhost:5433 (contenedor monitoreo-postgres)
--
--  Deja datos para recorrer todas las pantallas:
--    perfil · ubicación · comunidades · incidentes (abrir / cerrar / listar /
--    filtrar) · rankings (cantidad / impacto / tiempo) · panel admin
--
--  OJO: reinicia TODO el dominio (borra personas/comunidades/incidentes
--  cargados a mano). NO toca provincia/municipio/localidad: esas las carga
--  GeoRefImportRunner al arrancar la app y acá se usan sus ids reales.
--  Requiere haber levantado la app al menos una vez (Flyway + GeoRef).
--  Es re-ejecutable.
--
--  Uso (desde la raíz del proyecto):
--    ./db/seed-dev.sh
--  El script busca en Keycloak los ids de los usuarios `admin` y `basico` y se
--  los pasa a psql como :kc_admin / :kc_basico, para que al loguearse con ellos
--  la app encuentre la persona del seed (persona.keycloak_id = sub del JWT).
--
--  Login (usuarios del realm keycloak/import/monitoreo-realm.json):
--    admin / admin123    -> persona 50 (ADMIN)
--    basico / basico123  -> persona 51 (BASICO)
--  El resto de las personas (52, 53, 54) no tienen usuario en Keycloak: son
--  datos de dominio para comunidades, incidentes y rankings.
-- ============================================================================

BEGIN;

TRUNCATE
  incidente,
  miembro,
  comunidad_servicios_de_interes,
  comunidad,
  persona_entidades_de_interes,
  persona_tipos_de_servicios_de_interes,
  persona,
  servicio,
  establecimiento,
  entidad,
  tipo_entidad,
  organismo_control,
  tipo_servicio;

-- ----------------------------------------------------------------------------
-- Catálogos
-- ----------------------------------------------------------------------------
INSERT INTO tipo_servicio (id, nombre) VALUES
  (1, 'Subte'), (2, 'Tren'), (3, 'Ascensor'), (4, 'Escalera mecánica'),
  (5, 'Alumbrado público'), (6, 'Semáforo'), (7, 'Baño público'), (8, 'Agua potable');

INSERT INTO tipo_entidad (id, nombre) VALUES
  (1, 'Línea de transporte'), (2, 'Empresa de servicios'), (3, 'Edificio público');

INSERT INTO entidad (id, nombre, tipo_entidad_id) VALUES
  (1, 'Subte Línea A',                        1),
  (2, 'Subte Línea B',                        1),
  (3, 'Trenes Argentinos - Línea Mitre',      1),
  (4, 'Trenes Argentinos - Línea Sarmiento',  1),
  (5, 'AySA',                                 2),
  (6, 'Edenor',                               2),
  (7, 'Hospital Fernández',                   3);

-- ----------------------------------------------------------------------------
-- Ubicaciones usadas (ids de GeoRef, ya cargados por GeoRefImportRunner)
--   provincia  2 CABA          · municipio 22007 Comuna 1     · loc 200701004 Retiro
--                                                             · loc 200701005 San Nicolás
--                              · municipio 22035 Comuna 5     · loc 203501001 Almagro
--                                                             · loc 203501002 Boedo
--   provincia  6 Buenos Aires  · municipio 60441 La Plata     · loc 644103015 La Plata
--                              · municipio 60861 Vicente López· loc 686101006 Olivos
--   provincia 14 Córdoba       · municipio 140077 Córdoba     · loc 1401401003 Córdoba
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Establecimientos  (entidad + ubicación)
-- ----------------------------------------------------------------------------
INSERT INTO establecimiento (id, nombre, entidad_id, provincia_id, municipio_id, localidad_id) VALUES
  (10, 'Estación Perú (Línea A)',           1, 2, 22007, 200701005),
  (11, 'Estación Plaza de Mayo (Línea A)',  1, 2, 22007, 200701005),
  (12, 'Estación Medrano (Línea B)',        2, 2, 22035, 203501001),
  (13, 'Estación Retiro (Mitre)',           3, 2, 22007, 200701004),
  (14, 'Estación Once (Sarmiento)',         4, 2, 22035, 203501001),
  (15, 'Planta Potabilizadora La Plata',    5, 6, 60441, 644103015),
  (16, 'Subestación Vicente López',         6, 6, 60861, 686101006),
  (17, 'Hospital Fernández - Sede Central', 7, 2, 22035, 203501002);

-- ----------------------------------------------------------------------------
-- Servicios  (establecimiento + tipo de servicio)
-- ----------------------------------------------------------------------------
INSERT INTO servicio (id, nombre, tipo_servicio_id, establecimiento_id, esta_activo) VALUES
  (20, 'Molinete Andén A',          1, 10, TRUE),
  (21, 'Ascensor a nivel calle',    3, 10, TRUE),
  (22, 'Escalera mecánica salida',  4, 11, TRUE),
  (23, 'Formación Línea A',         1, 11, TRUE),
  (24, 'Ascensor central',          3, 12, TRUE),
  (25, 'Andén FC Mitre',            2, 13, TRUE),
  (26, 'Escalera mecánica hall',    4, 13, TRUE),
  (27, 'Andén FC Sarmiento',        2, 14, TRUE),
  (28, 'Bombas de agua',            8, 15, TRUE),
  (29, 'Alumbrado perimetral',      5, 16, TRUE),
  (30, 'Ascensor pabellón B',       3, 17, TRUE),
  (31, 'Baño planta baja',          7, 17, TRUE);

-- ----------------------------------------------------------------------------
-- Personas
--   estrategia_notificacion: MAIL | WHATSAPP        (EstrategiaDeNotificacionConverter)
--   configuracion_recepcion: SINCRONICO | ASINCRONICO (ConfiguracionRecepcionConverter)
-- ----------------------------------------------------------------------------
INSERT INTO persona
  (id, keycloak_id, fecha_de_alta, nombre_apellido, mail_de_contacto, numero_de_contacto,
   rol_persona, estrategia_notificacion, configuracion_recepcion,
   provincia_id, municipio_id, localidad_id)
VALUES
  (50, NULLIF(:'kc_admin', ''),  '2026-01-10', 'Admina Sistema',   'admin@monitoreo.local',  '+54 11 4000-0000',
   'ADMIN',       'MAIL', 'SINCRONICO', 2, 22007, 200701004),
  (51, NULLIF(:'kc_basico', ''), '2026-01-12', 'Basilio Común',    'basico@monitoreo.local', '+54 11 4111-1111',
   'BASICO',      'MAIL', 'SINCRONICO', 2, 22035, 203501001),
  (52, NULL,                     '2026-01-12', 'Rosa Responsable', 'resp@monitoreo.local',   '+54 221 500-5000',
   'RESPONSABLE', 'MAIL', 'SINCRONICO', 6, 60441, 644103015),
  (53, NULL,                     '2026-02-01', 'Juan Pérez',       'juan@mail.com',          '+54 11 4222-2222',
   'BASICO',      'MAIL', 'SINCRONICO', 2, 22007, 200701005),
  (54, NULL,                     '2026-02-03', 'María Gómez',      'maria@mail.com',         '+54 11 4333-3333',
   'BASICO',      'MAIL', 'SINCRONICO', 6, 60861, 686101006);

INSERT INTO persona_tipos_de_servicios_de_interes (persona_id, tipos_de_servicios_de_interes_id) VALUES
  (50, 5), (50, 6),
  (51, 1), (51, 3), (51, 4),
  (53, 2), (53, 4),
  (54, 8);

INSERT INTO persona_entidades_de_interes (persona_id, entidades_de_interes_id) VALUES
  (50, 6),
  (51, 1), (51, 2),
  (53, 3),
  (54, 5);

-- ----------------------------------------------------------------------------
-- Comunidades  (+ tipos de servicio de interés)
-- ----------------------------------------------------------------------------
INSERT INTO comunidad (id, nombre) VALUES
  (60, 'Vecinos de Subte Línea A'),
  (61, 'Usuarios del FC Mitre'),
  (62, 'Comunidad AySA Zona Sur'),
  (63, 'Barrio de Vicente López');

INSERT INTO comunidad_servicios_de_interes (comunidad_id, servicios_de_interes_id) VALUES
  (60, 1), (60, 3), (60, 4),
  (61, 2), (61, 4),
  (62, 8),
  (63, 5), (63, 6);

-- ----------------------------------------------------------------------------
-- Miembros   (persona <-> comunidad)
--   rol:                  ADMINISTRADOR | USUARIOBASICO
--   condicion_de_miembro: AFECTADO | OBSERVADOR
-- ----------------------------------------------------------------------------
INSERT INTO miembro
  (id, persona_id, comunidad_id, rol, condicion_de_miembro,
   estrategia_notificacion, configuracion_recepcion)
VALUES
  (70, 51, 60, 'USUARIOBASICO', 'AFECTADO',   'MAIL', 'SINCRONICO'),
  (71, 51, 61, 'ADMINISTRADOR', 'AFECTADO',   'MAIL', 'SINCRONICO'),
  (72, 53, 60, 'USUARIOBASICO', 'OBSERVADOR', 'MAIL', 'SINCRONICO'),
  (73, 54, 62, 'ADMINISTRADOR', 'AFECTADO',   'MAIL', 'SINCRONICO'),
  (74, 50, 63, 'ADMINISTRADOR', 'AFECTADO',   'MAIL', 'SINCRONICO'),
  (75, 52, 61, 'USUARIOBASICO', 'OBSERVADOR', 'MAIL', 'SINCRONICO');

-- ----------------------------------------------------------------------------
-- Incidentes
--   estado: ABIERTO | CERRADO      (CERRADO => fecha_cierre y miembro_de_cierre)
-- ----------------------------------------------------------------------------
-- Abiertos (para /incidentes/cerrar)
INSERT INTO incidente
  (id, descripcion, descripcion_lugar, estado, fecha_apertura, fecha_cierre,
   comunidad_id, servicio_id, miembro_de_apertura_id, miembro_de_cierre_id)
VALUES
  (80, 'El molinete del andén hacia Plaza de Mayo está trabado', 'Estación Perú (Línea A)',
   'ABIERTO', NOW() - INTERVAL '2 days',  NULL, 60, 20, 70, NULL),
  (81, 'Filtración de agua en el techo del andén', 'Estación Retiro (Mitre)',
   'ABIERTO', NOW() - INTERVAL '1 day',   NULL, 61, 25, 71, NULL),
  (82, 'Baja presión de agua en toda la zona', 'Planta Potabilizadora La Plata',
   'ABIERTO', NOW() - INTERVAL '5 hours', NULL, 62, 28, 73, NULL),
  (83, 'Ascensor del pabellón B fuera de servicio', 'Hospital Fernández - Sede Central',
   'ABIERTO', NOW() - INTERVAL '3 days',  NULL, 63, 30, 74, NULL);

-- Cerrados (para listados, filtrado y rankings — duraciones variadas)
INSERT INTO incidente
  (id, descripcion, descripcion_lugar, estado, fecha_apertura, fecha_cierre,
   comunidad_id, servicio_id, miembro_de_apertura_id, miembro_de_cierre_id)
VALUES
  (84, 'Ascensor a nivel de calle detenido', 'Estación Perú (Línea A)',
   'CERRADO', NOW() - INTERVAL '20 days', NOW() - INTERVAL '19 days', 60, 21, 70, 70),
  (85, 'Escalera mecánica de salida frenada', 'Estación Plaza de Mayo (Línea A)',
   'CERRADO', NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days' + INTERVAL '20 hours', 60, 22, 72, 72),
  (86, 'Demoras por formación detenida entre estaciones', 'Estación Plaza de Mayo (Línea A)',
   'CERRADO', NOW() - INTERVAL '12 days', NOW() - INTERVAL '10 days', 60, 23, 70, 72),
  (87, 'Escalera mecánica del hall central sin funcionar', 'Estación Retiro (Mitre)',
   'CERRADO', NOW() - INTERVAL '18 days', NOW() - INTERVAL '17 days', 61, 26, 71, 75),
  (88, 'Servicio interrumpido por falla en señalización', 'Estación Once (Sarmiento)',
   'CERRADO', NOW() - INTERVAL '8 days',  NOW() - INTERVAL '3 days',  61, 27, 75, 71),
  (89, 'Luminaria perimetral apagada, zona oscura de noche', 'Subestación Vicente López',
   'CERRADO', NOW() - INTERVAL '25 days', NOW() - INTERVAL '24 days', 63, 29, 74, 74),
  (90, 'Baño de planta baja clausurado sin aviso', 'Hospital Fernández - Sede Central',
   'CERRADO', NOW() - INTERVAL '30 days', NOW() - INTERVAL '29 days', 63, 31, 74, 74),
  (91, 'Ascensor central con ruidos y cortes de servicio', 'Estación Medrano (Línea B)',
   'CERRADO', NOW() - INTERVAL '6 days',  NOW() - INTERVAL '5 days',  60, 24, 72, 70),
  (92, 'Molinete fuera de servicio en hora pico', 'Estación Perú (Línea A)',
   'CERRADO', NOW() - INTERVAL '40 days', NOW() - INTERVAL '38 days', 60, 20, 70, 70),
  (93, 'Filtraciones recurrentes en el andén', 'Estación Retiro (Mitre)',
   'CERRADO', NOW() - INTERVAL '35 days', NOW() - INTERVAL '33 days', 61, 25, 71, 71);

-- ----------------------------------------------------------------------------
-- Cada entidad JPA tiene su propia secuencia (<tabla>_seq, INCREMENT BY 50).
-- Las empujamos arriba de los ids fijos de este seed para que la app no
-- colisione al crear filas nuevas.
-- ----------------------------------------------------------------------------
SELECT setval(seq, GREATEST(nextval(seq), 10000))
FROM unnest(ARRAY[
  'comunidad_seq', 'empresa_seq', 'entidad_seq', 'establecimiento_seq', 'incidente_seq',
  'miembro_seq', 'organismo_control_seq', 'persona_seq', 'servicio_seq',
  'tipo_entidad_seq', 'tipo_servicio_seq'
]::regclass[]) AS seq;

COMMIT;
