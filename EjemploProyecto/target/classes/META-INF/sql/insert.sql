INSERT INTO departamento (nombre) VALUES ('Finanzas'), ('Linux'), ('Produccion');
INSERT INTO empleado (nombre, salario, departamento) VALUES ('Ana Lopez', NULL, 1), ('Luis Maillo', '1234.56', 3), ('Milena Gracia', NULL, 3);
INSERT INTO proyecto(nombre) VALUES ('Proyecto1'),('Proyecto2'),('Proyecto3');
INSERT INTO proyecto_empleado (id_empleado,id_proyecto) VALUES (1,1);