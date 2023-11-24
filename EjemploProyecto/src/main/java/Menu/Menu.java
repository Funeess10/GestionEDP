
package Menu;

import IO.IO;
import Objetos.Departamento;
import Objetos.Empleado;
import Objetos.Proyecto;
import Gestor.EmpleadoDAO;
import Gestor.ProyectoDAO;
import Gestor.DepartamentoDAO;

public class Menu {

	private EmpleadoDAO ed;
	private DepartamentoDAO dd;
	private ProyectoDAO pd;
	private String s;
	private boolean condition = true;
	private static final String verde = "\u001B[32m";
	private static final String rojo = "\u001B[31m";
	public static final String reset = "\u001B[0m";

	public Menu(EmpleadoDAO ed, DepartamentoDAO dd, ProyectoDAO pd) {
		this.ed = ed;
		this.dd = dd;
		this.pd = pd;
		while (menu())
			;
	}

	private boolean alta() {
		IO.println("¿Desea añadir un empleado o un departamento o proyecto? E o D o P");
		switch (IO.readUpperChar()) {
		case 'E':
			ed.createEmpleado();
			return true;

		case 'D':
			dd.createDepartamento();
			return true;

		case 'P':
			pd.createProyecto();
			return true;

		default:
			IO.println("Opción incorrecta");
			break;
		}
		return false;

	}

	private void consulta() {
		IO.println("Desea consultar un empleado o un departamento o un proyecto? E o D o P");
		switch (IO.readUpperChar()) {
		case 'E':
			s = "Empleado";
			IO.println("¿ID del empleado?");
			int i = IO.readInt();
			Empleado e = ed.getEntityManager().find(Empleado.class, i);
			if (e != null) {
				System.out.println(verde);
				ed.consultar(s, i);
				System.out.println(reset);
			} else {
				IO.println(rojo + "No existe el empleado con ese id" + reset);
				IO.println("");
			}
			break;

		case 'D':
			s = "Departamento";
			IO.println("¿ID del departamento?");
			i = IO.readInt();
			Departamento d = ed.getEntityManager().find(Departamento.class, i);
			if (d != null) {
				System.out.println(verde);
				ed.consultar(s, i);
				System.out.println(reset);
			} else {
				IO.println(rojo + "No existe el departamento con ese id" + reset);
			}
			break;

		case 'P':
			s = "Proyecto";
			IO.println("¿ID del proyecto?");
			i = IO.readInt();
			Proyecto p = ed.getEntityManager().find(Proyecto.class, i);
			if (p != null) {
				System.out.println(verde);
				ed.consultar(s, i);
				System.out.println(reset);
			} else {
				IO.println(rojo + "No existe el proyecto con ese id" + reset);
			}
			break;

		default:
			IO.println(rojo + "Opcion incorrecta" + reset);
			break;
		}

	}

	private boolean baja() {
		IO.println("¿Desea dar de baja a un empleado o un departamento o un proyecto? E o D o P");
		switch (IO.readUpperChar()) {
		case 'E':
			s = "empleado";
			IO.println("¿ID del Empleado?");
			int id = IO.readInt();
			Empleado e = ed.getEntityManager().find(Empleado.class, id);
			if (e != null) {
				ed.deleteEmpleado(e);
				return true;
			} else {
				IO.println(rojo + "No existe el empleado con ese id" + reset);
				return false;
			}

		case 'D':
			s = "departamento";
			IO.println("¿ID del Departamento?");
			int id_dep = IO.readInt();
			Departamento d = dd.getEntityManager().find(Departamento.class, id_dep);
			if (d != null) {
				dd.deleteDepartamento(d);
				return true;
			} else {
				IO.println(rojo + "No existe el departamento con ese id" + reset);
				return false;
			}

		case 'P':
			s = "proyecto";
			IO.println("ID del Proyecto?");
			int id_pro = IO.readInt();
			Proyecto p = pd.getEntityManager().find(Proyecto.class, id_pro);
			if (p != null) {
				pd.borrarProyecto(id_pro);
				return true;
			} else {
				IO.println(rojo + "No existe el proyecto con ese id" + reset);
				return false;
			}

		default:
			IO.println(rojo + "Opcion incorrecta" + reset);
			break;
		}
		return false;

	}

	private boolean modificar() {
		IO.println("Desea modificar a un empleado o un departamento o proyecto? E o D o P");
		switch (IO.readUpperChar()) {
		case 'E':
			ed.updateEmpleado();
			return true;

		case 'D':
			dd.updateDepartamento();
			return true;

		case 'P':
			pd.updateProyecto();
			return true;
		default:
			IO.println(rojo + "Opción incorrecta" + reset);
			break;
		}
		return false;

	}

	public void listar() {
		IO.println("Desea listar a los empleados o los departamento o los proyectos? E, D o P");

		switch (IO.readUpperChar()) {
		case 'E':
			ed.listEmpleado();
			break;

		case 'D':
			dd.listDepartamentos();
			break;

		case 'P':
			pd.listarProyectosConEmpleados();
			break;
		}
	}

	/**
	 * Dar de alta a un proyecto
	 */
	public void altaProyecto() {
		IO.println("Id del empleado a añadir");
		int idE = IO.readInt();
		IO.println("Id del proyecto a asignar");
		int idP = IO.readInt();
		pd.asignarProyectoAEmpleado(idE, idP);
	}

	/**
	 * Dar de baja a un proyecto
	 */
	public void bajaProyecto() {
		IO.println("Id del empleado a quitar");
		int idE = IO.readInt();
		IO.println("Id del proyecto al que esta asignado");
		int idP = IO.readInt();
		pd.eliminarEmpleadoDeProyecto(idE, idP);
	}

	public boolean menu() {
		IO.print("Gestor de empleados y departamentos ");
		menuPrincipal();

		switch (IO.readInt()) {
		case 1:
			if (alta()) {
				IO.println(verde + "Se ha dado de alta correctamente" + reset);
			} else {
				IO.println(rojo + "No ha sido posible dar de alta" + reset);
			}
			IO.print("\n");
			break;

		case 2:
			if (baja()) {
				IO.println(verde + "Se ha dado de baja" + reset);
			} else {
				IO.println(rojo + "No se ha podido dar de baja" + reset);
			}
			IO.print("\n");
			break;

		case 3:
			if (modificar()) {
				IO.println(verde + "Se ha modificado correctamente " + reset);
				IO.println("");
			} else {
				IO.println(rojo + "No se ha podido modificar" + reset);
			}
			IO.print("\n");
			break;

		case 4:
			consulta();
			IO.print("\n");
			break;

		case 5:
			listar();
			IO.print("\n");
			break;

		case 6:
			altaProyecto();
			IO.print("\n");
			break;

		case 7:
			bajaProyecto();
			IO.print("\n");
			break;

		case 8:
			IO.print(rojo + "Fin del programa" + reset);
			return false;

		default:
			IO.print(rojo + "Opcion no correcta" + reset);
			IO.println("");
		}

		return true;
	}

	private void menuPrincipal() {
		IO.println("\n1->Alta");
		IO.println("2->Baja");
		IO.println("3->Modificar");
		IO.println("4->Consulta");
		IO.println("5->Listado");
		IO.println("6->Asignar proyecto");
		IO.println("7->Baja proyecto");
		IO.println("8->Salir");
	}

}
