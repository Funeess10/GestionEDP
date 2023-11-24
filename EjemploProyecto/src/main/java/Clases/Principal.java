
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import Objetos.Departamento;
import Objetos.Empleado;
import Objetos.Proyecto;

public class Principal 
{
	
	static EntityManager em = null;

	public static void main(String[] args) 
	{
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		em = Persistence.createEntityManagerFactory("departamentos").createEntityManager();	
		showResult();
		em.getTransaction().begin();
		addDepartamento();
		addEmpleadoToDepartamento();
		updateDepartamento();
		borrarEmpleado();
		em.getTransaction().commit();		
		showResult();
	}
	
	public static void showResult() 
	{
		em.clear();
		em.createQuery("FROM Departamento").getResultList().forEach(System.out::println);
		em.createQuery("FROM Empleado").getResultList().forEach(System.out::println);
		em.createQuery("FROM Proyecto").getResultList().forEach(System.out::println);	
		System.out.println("-".repeat(80));
	}

	public static void borrarDepartamento() 
	{
		Departamento dep = em.find(Departamento.class, 3);
		for (Empleado emp : dep.getEmpleados()) 
		{
			emp.setDepartamento(null);
		}
		em.remove(dep);
	}

	public static void borrarEmpleado() 
	{
		Empleado emp = em.find(Empleado.class, 2);
		em.remove(emp);
	}

	public static void updateDepartamento() 
	{
		Departamento d = em.find(Departamento.class, 3);
		d.setNombre("vcxvcxvcxvcxvcx");
	}

	public static void addEmpleadoToDepartamento() 
	{
		Empleado e = em.find(Empleado.class, 1);
		Departamento d = em.find(Departamento.class, 2);
		d.addEmpleado(e);
	}

	public static void addDepartamento() 
	{
		Departamento d = new Departamento();
		d.setNombre("Innovación");
		
		em.persist(d);
	}
	
	public static void addEmpleado() 
	{
		Empleado e = new Empleado();
		e.setNombre("Amadeo");
		e.setSalario(null);
		e.setDepartamento(null);
		em.persist(e);
	}
	
	public static void addProyecto() {
	    Proyecto proyecto = new Proyecto();
	    proyecto.setNombre("Nuevo Proyecto");

	    // Añade más configuraciones si es necesario

	    em.getTransaction().begin();
	    em.persist(proyecto);
	    em.getTransaction().commit();
	}

	public static void updateProyecto(int proyectoId, String nuevoNombre) {
	    em.getTransaction().begin();
	    Proyecto proyecto = em.find(Proyecto.class, proyectoId);
	    if (proyecto != null) {
	        proyecto.setNombre(nuevoNombre);
	        // Puedes realizar más actualizaciones si es necesario
	    }
	    em.getTransaction().commit();
	}

	public static void borrarProyecto(int proyectoId) {
	    em.getTransaction().begin();
	    Proyecto proyecto = em.find(Proyecto.class, proyectoId);
	    if (proyecto != null) {
	        em.remove(proyecto);
	    }
	    em.getTransaction().commit();
	}

	public static void asignarProyectoAEmpleado(int empleadoId, int proyectoId) {
	    em.getTransaction().begin();
	    Empleado empleado = em.find(Empleado.class, empleadoId);
	    Proyecto proyecto = em.find(Proyecto.class, proyectoId);
	    if (empleado != null && proyecto != null) {
	        empleado.addProyecto(proyecto);
	    }
	    em.getTransaction().commit();
	}


}

