package conexion;

import java.awt.BorderLayout;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import controladores.ControladorCliente;

import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JPanel pnlEditarUsuario, pnlCrearUsuario, pnlBotonesCliente;
	private JTable jtbUsuarios;
	private JScrollPane jspUsuarios;
	private AbstractAction delete, AAactualizar;	
	private String[] columnNames;
	private Object[][] data; 
	private ArrayList<String> tiposCols;
	private JButton btnCrearCliente, btnAgregarCampo;
	private ArrayList<JTextField> jtfCamposPopUp;
	private ArrayList<JLabel> lblCamposPopUp;
	private DefaultTableModel model;
	private int columnBorrar, columnActualizar;
	private ResultSet clientes;
	// Para Pop-Up de crear columna
	private JLabel lblColTipo, lblColNombre;
	private JComboBox jcbTipos;
	private JTextField jtfColNombre;
	

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 553, 478);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		pnlEditarUsuario = new JPanel();
		tabbedPane.addTab("Editar Usuario", null, pnlEditarUsuario, null);
		pnlEditarUsuario.setLayout(new BorderLayout(0, 0));
		
		// Datos de jtable		
		String borrar = "Borrar";
		String actualizar = "Actualizar";
		tiposCols = new ArrayList<String>();
		columnNames = new String[]{};
		columnBorrar=columnNames.length-1;
		columnActualizar=columnNames.length-2;
		data = new Object[][]{};
		model = new DefaultTableModel(data, columnNames);		
		
		jtbUsuarios = new JTable(model);
		
		jspUsuarios = new JScrollPane(jtbUsuarios);
		//inicializando la tabla
		clientes=ControladorCliente.getAllClientes();
		//crear campos
		ResultSetMetaData metadata=null;
		try {
			metadata=clientes.getMetaData();
			columnNames=new String[metadata.getColumnCount()];
			for(int i=0;i<metadata.getColumnCount();i++){
				model.addColumn(metadata.getColumnName(i+1));
				columnNames[i]=metadata.getColumnName(i+1);
			}
			while(clientes.next()){
				Object[] fila = new Object[columnNames.length];
				for(int i=0;i<columnNames.length;i++){
					fila[i]=clientes.getObject(columnNames[i]);
				}
				model.addRow(fila);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		columnBorrar=columnNames.length-1;
		columnActualizar=columnNames.length-2;
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
		
		// Funcion actualizar
		AAactualizar = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        String nit = ""+table.getModel().getValueAt(modelRow, 0);
		        //System.out.println(nit);
		    }
		};
		
		ButtonColumn buttonBorrar = new ButtonColumn(jtbUsuarios, delete, columnBorrar);
		jtbUsuarios.getColumnModel().getColumn(columnBorrar).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
		// Boton actualizar
		ButtonColumn buttonActualizar = new ButtonColumn(jtbUsuarios, AAactualizar, columnActualizar);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMinWidth(120);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMaxWidth(120);
		
		pnlEditarUsuario.add(jspUsuarios, BorderLayout.CENTER);		
		
		// Paneles para los creak client 
		pnlCrearUsuario = new JPanel();
		pnlCrearUsuario.setLayout(new BorderLayout(0,0));
		pnlBotonesCliente = new JPanel();
		pnlBotonesCliente.setLayout(new GridLayout(0,2));
		
		// Boton de crear Cliente
		btnCrearCliente = new JButton("Crear Cliente");	
		btnCrearCliente.setPreferredSize(new Dimension(100, 40));;
		btnCrearCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				crearPopUpCrearCliente();
			}
		});
		
		// Boton de agregar Campo
		btnAgregarCampo = new JButton("Agregar Campo");		
		btnAgregarCampo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				crearPopUpAgrCol();
			}
		});
		
		pnlBotonesCliente.add(btnAgregarCampo);
		pnlBotonesCliente.add(btnCrearCliente);
		pnlCrearUsuario.add(pnlBotonesCliente, BorderLayout.EAST);	
		pnlEditarUsuario.add(pnlCrearUsuario, BorderLayout.SOUTH);
		
		
	}
	
	private void crearPopUpCrearCliente(){
		// Pop-up del form para crear un cliente
		JPanel pnlPopUp = new JPanel(new GridLayout(0, 1));
		jtfCamposPopUp = new ArrayList<JTextField>();
		lblCamposPopUp = new ArrayList<JLabel>();
		for (int x=0; x<columnNames.length; x++){
			String campo = columnNames[x];
			if (! campo.equals("")){
				lblCamposPopUp.add(new JLabel(columnNames[x]+":"));
				jtfCamposPopUp.add(new JTextField());
				pnlPopUp.add(lblCamposPopUp.get(x));
				pnlPopUp.add(jtfCamposPopUp.get(x));
			}			
		}
		int result = JOptionPane.showConfirmDialog(null, pnlPopUp, "Crear Cliente",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			ArrayList<String> datos = new ArrayList<String>();
            for (int x=0; x<jtfCamposPopUp.size(); x++){
            	// System.out.println(jtfCamposPopUp.get(x).getText());
            	datos.add(jtfCamposPopUp.get(x).getText());            	          	            
            }
         // Datos es el arraylist para mandar  
                    
        // Le agregamos lo campos para los botones
        datos.add("Actualizar");
        datos.add("Borrar");
        model.addRow(datos.toArray());
        
        ButtonColumn buttonBorrar = new ButtonColumn(jtbUsuarios, delete, columnBorrar);
		jtbUsuarios.getColumnModel().getColumn(columnBorrar).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
		// Boton actualizar
		ButtonColumn buttonActualizar = new ButtonColumn(jtbUsuarios, AAactualizar, columnActualizar);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMinWidth(120);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMaxWidth(120);
        
        } else {
            System.out.println("Cancelled");
        }
	}
	
	private void crearPopUpAgrCol(){
		// Pop-up del form para crear un cliente
		JPanel pnlPopUpAgrCol = new JPanel(new GridLayout(0, 1));
		lblColTipo = new JLabel("Tipo Campo");
		String[] tipos = {"int", "real", "text", "date"};
		jcbTipos = new JComboBox(tipos);
		lblColNombre = new JLabel("Nombre de Nuevo Campo:");
		jtfColNombre = new JTextField();
		
		// Agregamos al panel
		pnlPopUpAgrCol.add(lblColTipo);
		pnlPopUpAgrCol.add(jcbTipos);
		pnlPopUpAgrCol.add(lblColNombre);
		pnlPopUpAgrCol.add(jtfColNombre);
		
		int result = JOptionPane.showConfirmDialog(null, pnlPopUpAgrCol, "Agregar Campo",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
            //model.addColumn(jtfColNombre.getText());
			//ControladorCliente.addCampo(jtfColNombre.getText(), jcbTipos.getSelectedItem().toString());
            agregarColumna(jtfColNombre.getText());
        } else {
            System.out.println("Cancelled");
        }
	}
	
	// Este metodo agrega una columna antes de los botones
	private void agregarColumna(String nombre){		
		String [] temp = new String[columnNames.length+1];
		for (int x=0; x<columnNames.length-2;x++){
			temp[x] = columnNames[x];
		}
		temp[temp.length-3] = nombre;
		temp[temp.length-2] = "";
		temp[temp.length-1] = "";		
		columnNames = temp;		
	}
	private void llenarTabla(){
		
	}
}
