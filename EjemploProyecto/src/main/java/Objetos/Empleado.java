


package Objetos;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private Double salario;

    @ManyToOne()
    @JoinColumn(name = "departamento")
    private Departamento departamento;
    
    @ManyToMany
    @JoinTable(name = "proyecto_empleado",
            joinColumns = @JoinColumn(name = "id_empleado"),
            inverseJoinColumns = @JoinColumn(name = "id_proyecto"))
    private List<Proyecto> proyectos;

//	@ManyToMany(mappedBy = "empleados")
//	private List<Proyecto> proyectos;

	public Empleado() {

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

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Empleado(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Empleado(int id, String nombre, double salario, Departamento departamento) {
		this.id = id;
		this.nombre = nombre;
		this.salario = salario;
		this.departamento = departamento;
	}

	public List<Proyecto> getProyectos() {
		return proyectos;
	}

	public void setProyectos(List<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public void addProyecto(Proyecto proyecto) {
		if (proyectos == null) {
			proyectos = new ArrayList<>();
		}
		proyectos.add(proyecto);
		proyecto.getEmpleados().add(this);
	}

	@Override
	public String toString() {
		String dep = departamento == null ? "Sin Departamento" : departamento.getNombre();
		return String.format("Empleado [%-2d %-25s %s", id, nombre, dep);
	}

}
