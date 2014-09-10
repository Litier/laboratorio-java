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

INSERT INTO autor VALUES 
    (1, 'Felipe', 'felipe', 'ñeña', 'felipe@servidor.com', null), 
    (2, 'Antonio', 'antonio', 'ñeña', 'antonio@servidor.com', null),
    (3, 'Carmen', 'carmen', 'ñeña', 'carmen@servidor.com', null),
    (4, 'Antonio', 'antonio2', 'ñeña', 'antonio2@servidor.com', null), -- Usuario con el mismo nombre que otro
    (5, 'Pedro', 'pedro', 'ñeña', 'pedro@servidor.com', null),
    (6, 'Juán', 'juán', 'ñeña', 'juán@servidor.com', null); 

INSERT INTO bitácora VALUES 
    (1, 1, 'Bitácora de Felipe'),
    (2, 2, 'La casa de Antonio'),
    (3, 2, 'La esperanza'),
    (4, 3, 'Monte Carmelo'),
    (5, 2, 'Juntando Líneas'),
    (6, 5, 'Javeando'),
    (7, 5, 'Bateando con pajaritos');

INSERT INTO artículo VALUES 
    (1,1,1,'Empezamos','Aquí empezamos la andadura de esta bitácora en la que trataré de bla... bla... bla...', '2010-12-12'),
    (2,1,1,'Continuamos','El otro día iba pasenado por la calle cuando de repente bla... bla... bla...', '2010-12-12 13:01:07'),
    (3,2,2,'Hola','Este es mi diario personal que quiero compartir con todo el mundo para que sepa bla... bla... bla...', '2011-11-30 22:16:27'),
    (4,4,3,'Monte Carmelo','Desde los antiguos ermitaños que se establecieron en el Monte Carmelo, Los Carmelitas han sido conocidos bla... bla... bla...', '2012-01-15 12:31:48'),
    (5,3,2,'Esperanza','Esperanza es lo que hoy necesitamos ante la evidente descomposición de este mundo que bla... bla... bla...', '2012-02-04'),
    (6,3,2,'Presentamos a Carmen','En esta casa no solo servidor va a escribir. Presentamos a Carmen, autora de ''Monte Carmelo'' que nos va a contar su singular bla... bla... bla...', '2012-02-05 17:20:19'),
    (7,3,3,'Bien hallada','Gracias por la presentación de la entrada anterior pero yo no merezco tanto reconocimiento bla... bla... bla...', DEFAULT);

INSERT INTO etiqueta VALUES 
    (1, 'Religión'),
    (2, 'Tecnología'),
    (3, 'Java'),
    (4, 'MyBatis'),
    (5, 'Apologética'),
    (6, 'Historia');

---------

INSERT INTO menú VALUES 
    (1, null, 'raiz'), 
    (2, 1, 'a'), (3, 1, 'b'), 
    (4, 2, 'aa'), (5, 2, 'ab'), (6, 3, 'ba'), (7, 3, 'bb'); 


INSERT INTO categoría VALUES 
    (1, 'Consultoría'), (2, 'Ingeniería'), (3, 'Persistencia'), (4, 'Lenguajes'), (5, 'Bases de datos'), (6, 'ERP'),
    (7, 'Swing'), (8, 'JavaFX'),
    (9, 'JPA'), (10, 'MyBatis'), (11, 'Hibernate'), (12, 'H2'), (13, 'Derby'), (14, 'HSQLDBL'), (15, 'PostgreSQL'),
    (16, 'Java'), (17, 'Groovy'), (18, 'Jython'), (19, 'Scala'),
    (20, 'Adempiere'), (21, 'OpenErp'), (22, 'OpenBravo'), (23, 'GnuCash'),
    (24, 'Contabilidad'), 
    (25, 'MiERP');

INSERT INTO cat_jerarq VALUES     
    (1, 6), (1, 24), -- rama de consultoría
    (2, 3), (2, 4), (2, 5), -- rama de ingeniería
    (3, 9), (3, 10), (3, 11), -- rama de persistencia
    (4, 16), (4, 17), (4, 18), (4, 19), -- rama de lenguajes
    (6, 21), (6, 22), (6, 23), -- rama de erps
    (5, 12), (5, 13), (5, 14), (5, 15), -- rama de bbdds
    (1, 20), (6, 20), (16, 20), (24, 20), -- padres de adempiere
    (1, 25), (6, 25), (7, 25), (12, 25), (16, 25);  -- padres de miERP

