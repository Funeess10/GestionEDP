package Gestor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import IO.IO;
import Objetos.Departamento;
import Objetos.Empleado;

public class DepartamentoDAO {
	private EmpleadoDAO ed;
	private String s;
	private static Connection con;
	private static EntityManager em = null;
	private static final String verde = "\u001B[32m";
	private static final String rojo = "\u001B[31m";
	public static final String reset = "\u001B[0m";
	public static final String amarillo = "\u001B[33m";

	public DepartamentoDAO() {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		em = Persistence.createEntityManagerFactory("departamentos").createEntityManager();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void beginTransaction() {
		em.getTransaction().begin();
	}

	public void beginComit() {
		em.getTransaction().commit();
	}

	/**
	 * Crea un departamento pidiendo datos por consola
	 */
	public void createDepartamento() {
		// Iniciar una transacción
		em.getTransaction().begin();

		try {
			Departamento dep = new Departamento();
			IO.println("¿Nombre del departamento a crear?");
			dep.setNombre(IO.readString());

			IO.println("¿Va a añadir a algún empleado? \nS->Sí \nN->No");
			switch (IO.readUpperChar()) {
			case 'S':
				IO.println("¿Id del empleado?");
				IO.print(amarillo);
				System.out.println("Listado Empleados");
				System.out.println();
				em.createQuery("FROM Empleado").getResultList().forEach(System.out::println);
				IO.print(reset);
				Empleado emp = em.find(Empleado.class, IO.readInt());
				if (emp != null) {
					IO.print("Se va a añadir el empleado");
					dep.addEmpleado(emp);
					IO.println(verde + "Empleado añadido" + reset);
				} else {
					IO.print(rojo + "El empleado buscado no existe" + reset);
					IO.println("");
				}
				break;

			case 'N':
				break;

			default:
				IO.println(rojo + "Opción incorrecta" + reset);
				break;
			}

			// Confirmar la transacción antes de persistir el departamento
			em.persist(dep);
			em.getTransaction().commit();
			IO.println(verde + "Departamento creado" + reset);
		} catch (Exception e) {
			// Manejar la excepción, por ejemplo, imprimir un mensaje o registrarla.
			e.printStackTrace();

			// Si ocurre un error, realizar un rollback de la transacción
			em.getTransaction().rollback();
			IO.println(rojo + "Error al crear el departamento" + reset);
		}
	}

	/**
	 * Borrar un departamento
	 * 
	 * @param d es el departamento que quieres borrar
	 */
	public void deleteDepartamento(Departamento d) {
		for (Empleado emp : d.getEmpleados()) {
			emp.setDepartamento(null);
		}
		em.remove(d);
	}

	/**
	 * Método que muestra todos los departamentos
	 */
	public void listDepartamentos() {
		em.getTransaction().begin();
		em.getTransaction().commit();
		System.out.println(amarillo + "Listado Departamentos");
		System.out.println();
		em.createQuery("FROM Departamento ORDER BY id").getResultList().forEach(System.out::println);
		IO.print(reset);
		System.out.println("\n" + "-----------------------------------------------------------------------------");
	}

	/**
	 * Modificar un departamento
	 * 
	 * @return si lo ha podido modificar o no
	 */
	public boolean updateDepartamento() {
		s = "departamento";
		DepartamentoDAO dd = new DepartamentoDAO();

		int id;
		IO.println("¿ID del Departamento a modificar?");
		id = IO.readInt();
		Departamento d = dd.getEntityManager().find(Departamento.class, id);

		if (d != null) {
			IO.println("Nombre [" + d.getNombre() + "] ?");
			d.setNombre(IO.readString());
			return true;
		}
		return false;
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

}
