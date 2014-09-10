-- Este documento es parte del proyecto 'Juntando Líneas'
-- 
-- Copyright (C) 2012 Antonio Sánchez - Todos los derechos reservados
-- 
-- 'Juntando Líneas' es software libre: Ud. puede redistribuirlo y/o modificarlo 
-- bajo los términos de la Licencia Pública General de GNU tal como está publicada 
-- por la Fundación para el Software Libre, ya sea la versión 3 de la Licencia, o 
-- (a su elección) cualquier versión posterior.
-- 
-- 'Juntando Líneas' se distribuye con la esperanza de que sea útil, pero 
-- SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita MERCANTIL o de 
-- APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte los detalles de la 
-- Licencia Pública General GNU para obtener una información más detallada. 
-- 
-- Debería haber recibido una copia de la Licencia Pública General GNU junto a 
-- 'Juntando Líneas'. En caso contrario, consulte <http://www.gnu.org/licenses/>.

drop all objects;

CREATE TABLE autor
   (
      id SMALLINT NOT NULL IDENTITY (1000),
      nombre VARCHAR(127) NOT NULL,
      usuario VARCHAR(31) NOT NULL,
      contraseña VARCHAR(31) NOT NULL,
      correo VARCHAR(63),
      bio LONGVARCHAR
   );


CREATE TABLE bitácora
   (
      id SMALLINT NOT NULL IDENTITY (1000),
      aut_id SMALLINT NOT NULL REFERENCES autor,
      título VARCHAR(200) NOT NULL DEFAULT 'MI BITÁCORA'
   );

CREATE TABLE artículo
   (
      id SMALLINT NOT NULL IDENTITY (1000),
      bit_id SMALLINT NOT NULL REFERENCES bitácora,
      aut_id SMALLINT NOT NULL REFERENCES autor,
      asunto VARCHAR(200) NOT NULL DEFAULT 'MI ARTÍCULO',
      cuerpo VARCHAR(2000) NOT NULL DEFAULT '',
      publicado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
   );

CREATE TABLE comentario
   (
      id SMALLINT NOT NULL IDENTITY (1000),
      art_id SMALLINT NOT NULL REFERENCES artículo,      
      contenido VARCHAR(500) NOT NULL DEFAULT '',
      publicado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
   );
   
CREATE TABLE etiqueta (
    id SMALLINT NOT NULL IDENTITY (1000),
    nombre VARCHAR(255) NOT NULL
);
   
CREATE TABLE eti_art (
    art_id SMALLINT NOT NULL REFERENCES artículo,
    eti_id SMALLINT NOT NULL REFERENCES etiqueta,
    PRIMARY KEY (art_id, eti_id)
);

---- CIRCULARIDAD

CREATE TABLE menú (
    id SMALLINT NOT NULL IDENTITY (1000),
    id_padre SMALLINT REFERENCES menú,
    nombre VARCHAR(63) NOT NULL
);

CREATE TABLE categoría (
    id SMALLINT NOT NULL IDENTITY (1000),
    nombre VARCHAR(63) NOT NULL
);

CREATE TABLE cat_jerarq (
    cat_padre SMALLINT NOT NULL REFERENCES categoría,
    cat_hija SMALLINT NOT NULL REFERENCES categoría,
    PRIMARY KEY (cat_hija, cat_padre),
    CHECK (cat_hija <> cat_padre)
);

CREATE TABLE cat_art (
    art_id SMALLINT NOT NULL REFERENCES artículo,
    cat_id SMALLINT NOT NULL REFERENCES categoría,
    PRIMARY KEY (art_id, cat_id)
);

