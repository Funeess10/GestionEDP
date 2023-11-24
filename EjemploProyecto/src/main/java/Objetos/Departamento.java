
package Objetos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "departamento")
public class Departamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private int id;
	private String nombre;
	
	@OneToMany(mappedBy = "departamento")
	private Set<Empleado> empleados = new HashSet<>();
	
	public void addEmpleado(Empleado e) {
		e.setDepartamento(this);
		empleados.add(e);
	}
	
	public void removeEmpleado(Empleado e) {
		e.setDepartamento(null);
		empleados.remove(e);
	}
	
	public Set<Empleado> getEmpleados() {
		return empleados;
	}
	
	public void setEmpleados(Set<Empleado> empleados) {
		this.empleados = empleados;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Departamento() {
		
	}
	
	public Departamento (int id) {
		this.id = id;
	}
	
	public Departamento (int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

//	@Override
//	public String toString() 
//	{
//		List<String> emps = empleados.stream()
//			.map(e -> e.getNombre()).toList();
//		return String.format("Departamento [%-2d %-25s %s]", id, nombre, emps);
//	}
//	
	
	@Override
	public String toString() {
	    String empleadoList = "[";

	    boolean first = true;
	    for (Empleado empleado : empleados) {
	        if (!first) {
	            empleadoList += ", ";
	        }
	        empleadoList += empleado.getNombre();
	        first = false;
	    }

	    empleadoList += "]";

	    return String.format("Departamento [ID: %-2d, Nombre: %-25s, Empleados: %s]", id, nombre, empleadoList);
	}
	
	
}
