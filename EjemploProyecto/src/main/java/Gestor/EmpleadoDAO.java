
package Gestor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

import IO.IO;
import Objetos.Departamento;
import Objetos.Empleado;
import Objetos.Proyecto;

public class EmpleadoDAO {
	private static Connection con;
	private DepartamentoDAO dd;
	private String s;
	private static EntityManager em = null;
	private static final String verde = "\u001B[32m";
	private static final String rojo = "\u001B[31m";
	public static final String reset = "\u001B[0m";
	public static final String amarillo = "\u001B[33m";

	public EmpleadoDAO() {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		em = Persistence.createEntityManagerFactory("departamentos").createEntityManager();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void beginTransaction() {
		em.getTransaction().begin();
	}

	/**
	 * Añadir un empleado a un departamento
	 * 
	 * @param i id del empleado
	 * @param j id del departamento
	 */
	public void aniadirEmpleado(int i, int j) {
		Empleado emp = em.find(Empleado.class, i);
		Departamento dep = em.find(Departamento.class, j);
		dep.addEmpleado(emp);
	}

	/**
	 * Crear un empleado pidiendo los parámetros por teclado
	 */
	public void createEmpleado() {
		Empleado emp = new Empleado();
		IO.println("¿Nombre del empleado a añadir?");
		emp.setNombre(IO.readString());
		IO.println("¿Salario del empleado?");
		emp.setSalario(IO.readDouble());
		IO.println("¿Tiene departamento? \nS->Sí \nN->No");
		int j = 0;
		switch (IO.readUpperChar()) {
		case 'S':
			IO.println("¿ID del departamento?");
			IO.println(amarillo);
			IO.println("Listado de departamentos");
			em.createQuery("FROM Departamento ORDER BY id").getResultList().forEach(System.out::println);
			IO.println(reset);
			j = IO.readInt();
			Departamento dep = em.find(Departamento.class, j);
			if (dep != null) {
				emp.setDepartamento(dep);
				dep.addEmpleado(emp);
				IO.println(verde + "Empleado creado" + reset);
			}

			else {
				IO.println(rojo + "No se ha podido encontrar el departamento" + reset);
				emp.setDepartamento(null);
				IO.println(verde + "Empleado creado" + reset);
				IO.println("");
			}

			break;

		case 'N':
			emp.setDepartamento(null);
			break;

		default:
			IO.println(rojo + "Opción incorrecta" + reset);
			break;
		}
		em.persist(emp);
	}

	public void consultar(String s, int i) {
		em.clear();
		em.createQuery("FROM " + s + " WHERE id = " + i).getResultList().forEach(System.out::println);
	}

	/**
	 * Borra un empleado
	 * 
	 * @param e es el empleado que quieres borrar
	 */
	public void deleteEmpleado(Empleado e) {
		em.remove(e);

	}

	/**
	 * Lista todos los empleados
	 */
//	public void listEmpleado() {
//		em.getTransaction().begin();
//		em.getTransaction().commit();
//		System.out.println("\n" + "-----------------------------------------------------------------------------");
//		em.clear();
//		IO.print(amarillo);
//		System.out.println("Listado Empleados");
//		System.out.println();
//		em.createQuery("FROM Empleado").getResultList().forEach(System.out::println);
//		System.out.println(reset);
//	}

	public void listEmpleado() {
		em.getTransaction().begin();
		em.getTransaction().commit();
		System.out.println("\n" + "-----------------------------------------------------------------------------");
		em.clear();
		IO.print(amarillo);
		System.out.println("Listado Empleados con Proyectos Asignados");
		System.out.println();

		EntityGraph<Empleado> graph = em.createEntityGraph(Empleado.class);
		graph.addAttributeNodes("proyectos");

		List<Empleado> empleados = em.createQuery("FROM Empleado", Empleado.class)
				.setHint("javax.persistence.loadgraph", graph).getResultList();

		for (Empleado empleado : empleados) {
			System.out.println("Empleado [" + empleado.getId() + " " + empleado.getNombre() + " Departamento: "
					+ (empleado.getDepartamento() != null ? empleado.getDepartamento().getNombre() : "N/A") +

					" Proyectos: " + empleado.getProyectos() + "]");
		}

		System.out.println(reset);
	}

	/**
	 * Modifica un empleado
	 * 
	 * @return si ha conseguido modificarlo
	 */
	public boolean updateEmpleado() {
		s = "Empleado";
		boolean modifica = false;
		int id;
		EmpleadoDAO ed = new EmpleadoDAO();
		IO.println("ID del empleado a modificar?");
		id = IO.readInt();
		Empleado emp = ed.getEntityManager().find(Empleado.class, id);
		if (emp != null) {
			IO.println("Nombre [" + emp.getNombre() + "] ?");
			emp.setNombre(IO.readString());
			IO.println("Salario [" + emp.getSalario() + "] ?");
			emp.setSalario(IO.readDouble());

			Departamento departamentoActual = emp.getDepartamento();
			String nombreDepartamentoActual = (departamentoActual != null) ? departamentoActual.getNombre() : "N/A";
			IO.println("Departamento [" + nombreDepartamentoActual + "] ("
					+ ((departamentoActual != null) ? departamentoActual.getId() : "N/A") + ") ?");

			int nuevoDepartamentoId = IO.readInt();
			Departamento dep = ed.getEntityManager().find(Departamento.class, nuevoDepartamentoId);
			if (dep != null) {
				emp.setDepartamento(dep);
				return true;
			} else {
				IO.println("No se pudo encontrar el nuevo departamento. La modificación no se realizó.");
			}
		} else {
			IO.println("No se encontró un empleado con el ID proporcionado.");
		}
		return false;
	}

	/**
	 * Asignar un empleado a un proyecto
	 * 
	 * @param empleadoId del empleado que quieres asignar a un proyecto
	 * @param proyectoId del proyecto que quieres asignar a un empleado
	 */
	public void asignarEmpleadoAProyecto(int empleadoId, int proyectoId) {
		Empleado empleado = em.find(Empleado.class, empleadoId);
		Proyecto proyecto = em.find(Proyecto.class, proyectoId);

		if (empleado != null && proyecto != null) {
			proyecto.getEmpleados().add(empleado);
			empleado.getProyectos().add(proyecto);
			em.merge(proyecto);
			em.merge(empleado);
			IO.println(verde + "Empleado asignado al proyecto" + reset);
		} else {
			IO.println(rojo + "No se pudo encontrar el empleado o el proyecto" + reset);
		}
	}

	/**
	 * Borrar a un empleado de un proyecto
	 * 
	 * @param empleadoId del empleado que quieres asignar a un proyecto
	 * @param proyectoId del proyecto que quieres asignar a un empleado
	 */
	public void borrarEmpleadoDeProyecto(int empleadoId, int proyectoId) {
		Empleado empleado = em.find(Empleado.class, empleadoId);
		Proyecto proyecto = em.find(Proyecto.class, proyectoId);

		if (empleado != null && proyecto != null) {
			proyecto.getEmpleados().remove(empleado);
			empleado.getProyectos().remove(proyecto);
			em.merge(proyecto);
			em.merge(empleado);
			IO.println(verde + "Empleado eliminado del proyecto" + reset);
		} else {
			IO.println(rojo + "No se pudo encontrar el empleado o el proyecto" + reset);
		}
	}

	/**
	 * Crea un proyecto
	 */
	public void createProyecto() {
		Proyecto proyecto = new Proyecto();

		IO.println("¿Nombre del proyecto a crear?");
		proyecto.setNombre(IO.readString());

		em.persist(proyecto);

		IO.println(verde + "Proyecto creado" + reset);
	}

	/**
	 * Listar los empleados con sus proyectos asignados
	 */
	public void listarProyectosConEmpleados() {
		em.clear();
		List<Proyecto> proyectos = em.createQuery("FROM proyecto", Proyecto.class).getResultList();

		if (proyectos.isEmpty()) {
			IO.println("No hay proyectos registrados.");
		} else {
			IO.println(amarillo + "Listado de Proyectos con Empleados:" + reset);

			for (Proyecto proyecto : proyectos) {
				IO.println("Proyecto: " + proyecto.getNombre());
				IO.println("Empleados participantes:");

				for (Empleado empleado : proyecto.getEmpleados()) {
					IO.println("- " + empleado.getNombre());
				}

				IO.println("------------------------------");
			}
		}
	}

	public void borrarProyecto(int proyectoId) {
		Proyecto proyecto = em.find(Proyecto.class, proyectoId);

		if (proyecto != null) {
			em.remove(proyecto);
			IO.println(verde + "Proyecto eliminado" + reset);
		} else {
			IO.println(rojo + "No se pudo encontrar el proyecto" + reset);
		}
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			// Manejar la excepción, por ejemplo, imprimir un mensaje o registrarla.
			e.printStackTrace();
		} finally {
			con = null; // Establecer la conexión a null después de cerrarla
		}
	}

	/*
	 * public void init() { String sql = """ CREATE TABLE departamentos ( id INTEGER
	 * PRIMARY KEY AUTOINCREMENT, nombre TEXT );
	 * 
	 * CREATE TABLE empleados ( id INTEGER PRIMARY KEY AUTOINCREMENT, nif TEXT,
	 * nombre TEXT, departamento INTEGER NOT NULL, FOREIGN KEY (departamento)
	 * REFERENCES departamentos (id) ON UPDATE CASCADE ON DELETE CASCADE );
	 * 
	 * CREATE TABLE proyectos ( id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT );
	 * 
	 * CREATE TABLE proyecto_empleado ( proyecto_id INTEGER, empleado_id INTEGER,
	 * FOREIGN KEY (proyecto_id) REFERENCES proyectos (id) ON UPDATE CASCADE ON
	 * DELETE CASCADE, FOREIGN KEY (empleado_id) REFERENCES empleados (id) ON UPDATE
	 * CASCADE ON DELETE CASCADE ); """;
	 * 
	 * String sql2 = "PRAGMA foreign_keys = ON;"; try { Statement st =
	 * con.createStatement(); st.executeUpdate(sql2); st.executeUpdate(sql); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 */

}
