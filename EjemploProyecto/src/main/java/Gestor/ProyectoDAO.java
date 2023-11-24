package Gestor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import IO.IO;
import Objetos.Empleado;
import Objetos.Proyecto;
import jakarta.persistence.Entity;

public class ProyectoDAO {
	private static Connection con;
	private DepartamentoDAO dd;
	private String s;
	private static EntityManager em = null;
	private static final String verde = "\u001B[32m";
	private static final String rojo = "\u001B[31m";
	public static final String reset = "\u001B[0m";
	public static final String amarillo = "\u001B[33m";

	public ProyectoDAO() {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		em = Persistence.createEntityManagerFactory("departamentos").createEntityManager();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void asignarProyectoAEmpleado(int empleadoId, int proyectoId) {
		try {
			em.getTransaction().begin();

			Empleado empleado = em.find(Empleado.class, empleadoId);
			Proyecto proyecto = em.find(Proyecto.class, proyectoId);

			if (proyecto != null && empleado != null) {
				proyecto.getEmpleados().add(empleado);
				empleado.getProyectos().add(proyecto);

				// Commit the transaction
				em.getTransaction().commit();

				IO.println(verde + "Proyecto asignado a empleado" + reset);
			} else {
				IO.println(rojo + "No se pudo encontrar el proyecto o el empleado" + reset);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			IO.println(rojo + "Error al asignar proyecto a empleado: " + e.getMessage() + reset);
		} finally {
			// Asegurar que la transacción se cierre adecuadamente
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}
	}

	public void eliminarEmpleadoDeProyecto(int empleadoId, int proyectoId) {
		try {
			em.getTransaction().begin();

			Empleado empleado = em.find(Empleado.class, empleadoId);
			Proyecto proyecto = em.find(Proyecto.class, proyectoId);

			if (proyecto != null && empleado != null) {
				// Remover al empleado del proyecto
				proyecto.getEmpleados().remove(empleado);
				// Remover el proyecto del empleado
				empleado.getProyectos().remove(proyecto);

				// Commit the transaction
				em.getTransaction().commit();

				IO.println(verde + "Empleado eliminado del proyecto" + reset);
			} else {
				IO.println(rojo + "No se pudo encontrar el proyecto o el empleado" + reset);
			}
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			IO.println(rojo + "Error al eliminar empleado del proyecto" + reset);
		}
	}

//	public void createProyecto() {
//		Proyecto proyecto = new Proyecto();
//
//		IO.println("¿Nombre del proyecto a crear?");
//		proyecto.setNombre(IO.readString());
//
//		em.persist(proyecto);
//
//		IO.println(verde + "Proyecto creado" + reset);
//	}

	public void consultar(String s, int i) {
		em.clear();
		em.createQuery("FROM " + s + " WHERE id = " + i).getResultList().forEach(System.out::println);
	}

	@SuppressWarnings("unchecked")
	public void listarProyectosConEmpleados() {
		try {
			em.clear();
			List<Proyecto> proyectos = em.createQuery("FROM Proyecto ORDER BY id", Proyecto.class).getResultList();

			if (proyectos.isEmpty()) {
				IO.println("No hay proyectos registrados.");
			} else {
				IO.println(amarillo + "Listado de Proyectos con Empleados:");

				for (Proyecto proyecto : proyectos) {
					IO.println("Id: " + proyecto.getId());
					IO.println("Proyecto: " + proyecto.getNombre());
					IO.println("Empleados participantes:");

					for (Empleado empleado : proyecto.getEmpleados()) {
						IO.println("- " + empleado.getNombre());
					}

					IO.println("------------------------------");
				}
				IO.println(reset);
			}
		} catch (Exception e) {
			IO.println("Error al listar proyectos con empleados: " + e.getMessage());
			// Puedes agregar más detalles o acciones según tus necesidades
		}
	}

	public void createProyecto() {
		try {
			// Iniciar una transacción
			em.getTransaction().begin();

			Proyecto proyecto = new Proyecto();

			IO.println("¿Nombre del proyecto a crear?");
			proyecto.setNombre(IO.readString());

			em.persist(proyecto);

			// Confirmar la transacción
			em.getTransaction().commit();

			IO.println(verde + "Proyecto creado" + reset);
		} catch (Exception e) {
			e.printStackTrace();

			// Si ocurre un error, realizar un rollback de la transacción
			em.getTransaction().rollback();
		}
	}

	/**
	 * Borra un proyecto
	 */
	public void borrarProyecto(int proyectoId) {
		try {
			// Iniciar una transacción
			em.getTransaction().begin();

			Proyecto proyecto = em.find(Proyecto.class, proyectoId);

			if (proyecto != null) {
				em.remove(proyecto);

				// Confirmar la transacción
				em.getTransaction().commit();

				IO.println(verde + "Proyecto eliminado" + reset);
			} else {
				IO.println(rojo + "No se pudo encontrar el proyecto" + reset);
			}
		} catch (Exception e) {
			// Manejar la excepción, por ejemplo, imprimir un mensaje o registrarla.
			e.printStackTrace();

			// Si ocurre un error, realizar un rollback de la transacción
			em.getTransaction().rollback();
			IO.println(rojo + "Error al eliminar el proyecto" + reset);
		}
	}

	/**
	 * Modifica un proyecto
	 */
	public void updateProyecto() {
		try {
			IO.println("Ingrese el ID del proyecto que desea modificar:");
			int proyectoId = IO.readInt();

			em.getTransaction().begin();

			Proyecto proyecto = em.find(Proyecto.class, proyectoId);

			if (proyecto != null) {
				IO.println("Nombre actual del proyecto: " + proyecto.getNombre());
				IO.println("¿Desea modificar el nombre del proyecto? (S/N)");
				String respuesta = IO.readString();
				if (respuesta.equalsIgnoreCase("S")) {
					IO.println("Ingrese el nuevo nombre del proyecto:");
					proyecto.setNombre(IO.readString());
				}

				// Commit the transaction
				em.getTransaction().commit();

				IO.println(verde + "Proyecto modificado" + reset);
			} else {
				IO.println(rojo + "No se pudo encontrar el proyecto con ID " + proyectoId + reset);
			}
		} catch (Exception e) {
			e.printStackTrace();
//	        em.getTransaction().rollback();
			IO.println(rojo + "Error al modificar el proyecto" + reset);
		}
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con = null;
		}
	}

}
