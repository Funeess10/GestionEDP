package Objetos;

import java.util.List;

import javax.persistence.*;

@Entity
@Table (name="proyecto")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(unique=true)
    private String nombre;

//    @ManyToMany
//    @JoinTable(
//            name = "proyecto_empleado",
//            joinColumns = @JoinColumn(name = "proyecto_id"),
//            inverseJoinColumns = @JoinColumn(name = "empleado_id")
//        )
    @ManyToMany(mappedBy = "proyectos")
    private List<Empleado> empleados;

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

	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	@Override
	public String toString() {
		return "Proyecto [id=" + id + ", nombre=" + nombre + ", empleados=" + empleados + "]";
	}

    
}