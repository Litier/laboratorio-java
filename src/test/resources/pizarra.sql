select *
from categoría as c, cat_jerarq as p
where c.id = p.cat_padre 

select c.id as id_padre, c.nombre as nombre_padre, p.cat_hija as id_hija, c2.nombre as nombre_hija
from categoría as c, cat_jerarq as p, categoría as c2
where c.id = p.cat_padre and p.cat_hija = c2.id

select c.id as id_hija, c.nombre as nombre_hija, p.cat_padre as id_padre, c2.nombre as nombre_padre
from categoría as c, cat_jerarq as p, categoría as c2
where c.id = p.cat_hija and p.cat_padre = c2.id
order by c.id

-- (select c.id as elem_padre, 
--     null as elem_hija, 
--     c.nombre as nombre_padre, 
--     null as nombre_hija, 
--     p.cat_hija as id_hija, 
--     null as id_padre, 
--     c2.nombre as nombre_hija,
--     null as nombre_padre
-- from categoría as c, cat_jerarq as p, categoría as c2
-- where c.id = p.cat_padre and p.cat_hija = c2.id
-- order by elem_padre)
-- union all
-- (select null as elem_padre, 
--     c.id as elem_hija, 
--     null as nombre_padre, 
--     c.nombre as nombre_hija, 
--     null as id_hija, 
--     p.cat_padre as id_padre, 
--     null as nombre_hija,
--     c2.nombre as nombre_padre
-- from categoría as c, cat_jerarq as p, categoría as c2
-- where c.id = p.cat_hija and p.cat_padre = c2.id
-- order by elem_hija)


SELECT c.id AS id, 
    c.nombre AS nombre, 
    p.cat_hija AS id_hija, 
    c2.nombre AS nombre_hija,
    p2.cat_padre AS id_padre, 
    c3.nombre AS nombre_padre
FROM categoría AS c 
LEFT JOIN cat_jerarq AS p ON c.id = p.cat_padre
LEFT JOIN categoría AS c2 ON p.cat_hija = c2.id
LEFT JOIN cat_jerarq AS p2 ON c.id = p2.cat_hija
LEFT JOIN categoría AS c3 ON p2.cat_padre = c3.id

----------

SELECT m1.id AS id,
    m1.nombre AS nombre,
    m2.id AS id_hijo,
    m2.nombre AS nombre_hijo
FROM menú m1
JOIN menú m2 ON m1.id = m2.ID_PADRE
ORDER BY m1.id

