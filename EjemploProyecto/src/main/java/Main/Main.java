
package Main;

import Gestor.DepartamentoDAO;
import Gestor.EmpleadoDAO;
import Gestor.ProyectoDAO;
import IO.IO;
import Menu.Menu;

public class Main 
{
	private static final String rojo="\u001B[31m";
	public static final String reset = "\u001B[0m";
	
	public static void main(String[] args) 
	{
		
		EmpleadoDAO ed = new EmpleadoDAO();
		DepartamentoDAO dd=new DepartamentoDAO();
		ProyectoDAO pd=new ProyectoDAO();
		new Menu(ed,dd,pd);
		IO.println(rojo+"\nMenu y Gestor Cerrados"+reset);
		ed.close();
		dd.close();
		pd.close();
	}
}