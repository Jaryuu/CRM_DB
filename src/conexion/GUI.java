package conexion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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

import modelos.Aseguradora;
import modelos.Carro;
import modelos.Empresa;
import modelos.Pais;
import controladores.ControladorCatalogo;
import controladores.ControladorCliente;

import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private JPanel contentPane, pnlFiltros;
	private JPanel pnlEditarUsuario, pnlCrearUsuario, pnlBotonesCliente;
	private JTable jtbUsuarios;
	private JScrollPane jspUsuarios;
	private AbstractAction delete, AAactualizar;	
	private String[] columnNames;
	private Object[][] data; 
	private ArrayList<String> tiposCols;
	private JButton btnCrearCliente, btnAgregarCampo;
	private ArrayList valCamposPopUp;
	private ArrayList<JLabel> lblCamposPopUp;
	private DefaultTableModel model;
	private int columnBorrar, columnActualizar;
	private JButton btnMostrarFiltros, btnOcultarFiltros, btnFiltrar;
	private ResultSet clientes;
	private JScrollPane jspFiltros;
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
		
		// Panel para editar clientes -------------------------------------------------------------------------------------------------------------
		pnlEditarUsuario = new JPanel();
		tabbedPane.addTab("Editar Clientes", null, pnlEditarUsuario, null);
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
		jtbUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		jspUsuarios = new JScrollPane(jtbUsuarios, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//inicializando la tabla
		llenarTabla(ControladorCliente.getAllClientes());
		// Boton borrar
		delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        String nit = ""+table.getModel().getValueAt(modelRow, 0);
		        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
		        ControladorCliente.deleteCliente(nit);
		        
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
		        ArrayList<String[]> datosN=new ArrayList<String[]>();
		        for(int i=0;i<table.getModel().getColumnCount();i++){
		        	String[] fila= new String[2];
		        	fila[0]=table.getModel().getColumnName(i);
		        	fila[1]=String.valueOf(table.getModel().getValueAt(modelRow, i));
		        	datosN.add(fila);
		        }
		        //System.out.println(nit);
		        ControladorCliente.updateCliente(nit, datosN);
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
		
		// --------------------------------------------------------------------------------------------------------------
		
		// Panel para filtrar clientes que se muestran
		pnlFiltros = new JPanel();
		pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));		
		jspFiltros = new JScrollPane(pnlFiltros);
		pnlEditarUsuario.add(jspFiltros, BorderLayout.EAST);	
		
		btnMostrarFiltros = new JButton("Mostrar");
		btnMostrarFiltros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mostrarFiltros();
			}
		});
		btnOcultarFiltros = new JButton("Ocultar");
		btnOcultarFiltros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ocultarFiltros();		
			}
		});		
		btnFiltrar = new JButton("Filtrar");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Funcion para aplicar filtros
				ArrayList<String[]> filtros = new ArrayList<String[]>();
				for (int x=0; x<valCamposPopUp.size(); x++){
					Object actual = valCamposPopUp.get(x);
					String[] unFiltro = new String[2];
					String actualText="";
					if(actual instanceof JComboBox){
						JComboBox box=(JComboBox)actual;
						actualText=String.valueOf(box.getSelectedItem());
					}
					else{
						JTextField text=(JTextField)actual;
						actualText=text.getText();
					}
					if (! actualText.equals("")){
						unFiltro[0] = lblCamposPopUp.get(x).getText();
						unFiltro[1] = actualText;
						filtros.add(unFiltro);
					}										
				}
				// filtros es el arraylista a mandar
//				for (int x=0; x<filtros.size(); x++){
//					System.out.println(filtros.get(x)[0]+ " : "+filtros.get(x)[1]);
//				}
				llenarTabla(ControladorCliente.getFilteredClientes(filtros));
			}
		});	
		ocultarFiltros();	
		
	}
	
	private void mostrarFiltros(){
		pnlFiltros.removeAll();
		pnlFiltros.setPreferredSize(new Dimension(300, pnlFiltros.getHeight()));
		pnlFiltros.setLayout(new GridLayout(0,2));
		valCamposPopUp = new ArrayList();
		lblCamposPopUp = new ArrayList<JLabel>();
		for (int x=0; x<columnNames.length; x++){			
			String campo = columnNames[x];			
			if (! campo.equals("")){
				lblCamposPopUp.add(new JLabel(columnNames[x]+":"));
				if(campo.equals("idPais".toLowerCase())){
					ArrayList<Pais> paises= ControladorCatalogo.findAllPaises();
					String[] tipos = new String[paises.size()+1];
					tipos[0]="";
					for(int i=0;i<paises.size();i++){
						tipos[i+1]=paises.get(i).getId()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("modeloCarro".toLowerCase())){
					ArrayList<Carro> carros= ControladorCatalogo.findAllCarros();
					String[] tipos = new String[carros.size()+1];
					tipos[0]="";
					for(int i=0;i<carros.size();i++){
						tipos[i+1]=carros.get(i).getModelo()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("idEmpresa".toLowerCase())){
					ArrayList<Empresa> empresas= ControladorCatalogo.findAllEmpresas();
					String[] tipos = new String[empresas.size()+1];
					tipos[0]="";
					for(int i=0;i<empresas.size();i++){
						tipos[i+1]=empresas.get(i).getId()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("idAseguradora".toLowerCase())){
					ArrayList<Aseguradora> aseguradoras= ControladorCatalogo.findAllAseguradoras();
					String[] tipos = new String[aseguradoras.size()+1];
					tipos[0]="";
					for(int i=0;i<aseguradoras.size();i++){
						tipos[i+1]=aseguradoras.get(i).getId()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else{
					valCamposPopUp.add(new JTextField());
				}
				pnlFiltros.add(lblCamposPopUp.get(x));
				pnlFiltros.add((Component) valCamposPopUp.get(x));
			}
		}				
		pnlFiltros.add(btnOcultarFiltros);
		pnlFiltros.add(btnFiltrar);
	}
	
	private void ocultarFiltros(){
		pnlFiltros.removeAll();	
		pnlFiltros.setLayout(new FlowLayout());
		pnlFiltros.setPreferredSize(new Dimension(100, pnlFiltros.getHeight()));
		pnlFiltros.add(btnMostrarFiltros);
		pnlEditarUsuario.repaint();
		pnlEditarUsuario.revalidate();
	}
	
	private void crearPopUpCrearCliente(){
		// Pop-up del form para crear un cliente
		JPanel pnlPopUp = new JPanel(new GridLayout(0, 1));
		valCamposPopUp = new ArrayList<JTextField>();
		lblCamposPopUp = new ArrayList<JLabel>();
		for (int x=0; x<columnNames.length; x++){			
			String campo = columnNames[x];			
			if (! campo.equals("")){
				lblCamposPopUp.add(new JLabel(columnNames[x]+":"));
				if(campo.equals("idPais".toLowerCase())){
					ArrayList<Pais> paises= ControladorCatalogo.findAllPaises();
					String[] tipos = new String[paises.size()];
					for(int i=0;i<paises.size();i++){
						tipos[i]=paises.get(i).getId()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("modeloCarro".toLowerCase())){
					ArrayList<Carro> carros= ControladorCatalogo.findAllCarros();
					String[] tipos = new String[carros.size()];
					for(int i=0;i<carros.size();i++){
						tipos[i]=carros.get(i).getModelo()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("idEmpresa".toLowerCase())){
					ArrayList<Empresa> empresas= ControladorCatalogo.findAllEmpresas();
					String[] tipos = new String[empresas.size()];
					for(int i=0;i<empresas.size();i++){
						tipos[i]=empresas.get(i).getId()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("idAseguradora".toLowerCase())){
					ArrayList<Aseguradora> aseguradoras= ControladorCatalogo.findAllAseguradoras();
					String[] tipos = new String[aseguradoras.size()];
					for(int i=0;i<aseguradoras.size();i++){
						tipos[i]=aseguradoras.get(i).getId()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else{
					valCamposPopUp.add(new JTextField());
				}
				pnlPopUp.add(lblCamposPopUp.get(x));
				pnlPopUp.add((Component) valCamposPopUp.get(x));
			}			
		}
		int result = JOptionPane.showConfirmDialog(null, pnlPopUp, "Crear Cliente",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			ArrayList<String> datos = new ArrayList<String>();
            for (int x=0; x<valCamposPopUp.size(); x++){
            	// System.out.println(jtfCamposPopUp.get(x).getText());
            	Object actual = valCamposPopUp.get(x);
            	String add="";
            	if(actual instanceof JComboBox){
            		JComboBox box=(JComboBox)actual;
            		add=String.valueOf(box.getSelectedItem());
            	}
            	else{
            		JTextField text=(JTextField)actual;
            		add=text.getText();
            	}
            	datos.add(add);            	          	            
            }
         // Datos es el arraylist para mandar  
            ControladorCliente.insertCliente(datos);
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
		
		JScrollPane jspPopUpAgrCol = new JScrollPane(pnlPopUpAgrCol);
		
		int result = JOptionPane.showConfirmDialog(null, jspPopUpAgrCol, "Agregar Campo",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
            //model.addColumn(jtfColNombre.getText(),columnNames.length-3);
			ControladorCliente.addCampo(jtfColNombre.getText(), jcbTipos.getSelectedItem().toString());
            //agregarColumna(jtfColNombre.getText());
			llenarTabla(ControladorCliente.getAllClientes());
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
	private void llenarTabla(ResultSet clientes){
		this.clientes=clientes;
		columnNames = new String[]{};
		columnBorrar=columnNames.length-1;
		columnActualizar=columnNames.length-2;
		data = new Object[][]{};
		model = new DefaultTableModel(data, columnNames);
		jtbUsuarios.setModel(model);
		//crear campos
		ResultSetMetaData metadata=null;
		try {
			metadata=clientes.getMetaData();
			int tamanoMD =metadata.getColumnCount(); 
			int tamano = tamanoMD+2;
			columnNames=new String[tamano];
			for(int i=0;i<tamano;i++){
				if (i>=tamanoMD){
					model.addColumn("");
					columnNames[i] = "";
				}else{
					model.addColumn(metadata.getColumnName(i+1));
					columnNames[i]=metadata.getColumnName(i+1);
				}				
			}
			columnActualizar = tamano-2;
			columnBorrar = tamano-1;
			while(clientes.next()){				
				Object[] fila = new Object[columnNames.length];
				for(int i=0;i<tamanoMD;i++){
					fila[i]=clientes.getObject(columnNames[i]);
				}
				fila[columnActualizar] = "Actualizar";
				fila[columnBorrar] = "Borrar";				
				model.addRow(fila);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		columnBorrar=columnNames.length-1;
		columnActualizar=columnNames.length-2;
	}
}
