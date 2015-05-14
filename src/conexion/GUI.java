package conexion;

import java.awt.BorderLayout;
import java.awt.Desktop.Action;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JPanel pnlEditarUsuario;
	private JTable jtbUsuarios;
	private JScrollPane jspUsuarios;
	private AbstractAction delete;		
	

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 553, 478);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		pnlEditarUsuario = new JPanel();
		tabbedPane.addTab("Editar Usuario", null, pnlEditarUsuario, null);
		pnlEditarUsuario.setLayout(new BorderLayout(0, 0));
		
		// Datos de jtable
		int columnBorrar=3;
		int columnActualizar=2;
		String borrar = "Borrar";
		String actualizar = "Actualizar";
		String[] columnNames = {"Nombre", "Apellido", "", ""};
		Object[][] data ={				
				{"Diego", "Pato", actualizar, borrar},
				{"Julio", "Twitter", actualizar, borrar}
		};
		DefaultTableModel model = new DefaultTableModel(data, columnNames);		
		
		jtbUsuarios = new JTable(model);
		jspUsuarios = new JScrollPane(jtbUsuarios);

		// Boton borrar
		delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
		    }
		};		
		ButtonColumn buttonBorrar = new ButtonColumn(jtbUsuarios, delete, columnBorrar);
		jtbUsuarios.getColumnModel().getColumn(columnBorrar).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
		// Boton actualizar
		ButtonColumn buttonActualizar = new ButtonColumn(jtbUsuarios, delete, columnActualizar);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMinWidth(120);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMaxWidth(120);
		
		pnlEditarUsuario.add(jspUsuarios, BorderLayout.CENTER);		
	}

}
