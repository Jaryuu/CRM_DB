package conexion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import modelos.Aseguradora;
import modelos.Carro;
import modelos.Empresa;
import modelos.Pais;
import controladores.ControladorCatalogo;
import controladores.ControladorCliente;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.nio.file.StandardCopyOption.*;

public class GUI extends JFrame {

	private JPanel contentPane, pnlFiltros;
	private JPanel pnlEditarUsuario, pnlCrearUsuario, pnlBotonesCliente;
	private JTable jtbUsuarios;
	private JScrollPane jspUsuarios;
	private AbstractAction delete, AAactualizar, AAmostrarTweets;	
	private String[] columnNames;
	private Object[][] data; 
	private ArrayList<String> tiposCols;
	private JButton btnCrearCliente, btnAgregarCampo;
	private ArrayList valCamposPopUp;
	private ArrayList<JLabel> lblCamposPopUp;
	private DefaultTableModel model;
	private int columnBorrar, columnActualizar, columnMostrarT;
	private JButton btnMostrarFiltros, btnOcultarFiltros, btnFiltrar, btnCatalogos;
	private ResultSet clientes;
	private JScrollPane jspFiltros;
	private JFileChooser chooser;
	private String nCarpetaImagenes;
	private ListSelectionListener fotoListener;
	private ArrayList<File> fFotos;
	private JComboBox jcbPais, jcbCarro, jcbEmpresa, jcbAseguradora;
	private int columnPais, columnCarro, columnEmpresa, columnAseguradora;
	private String[] lPais, lCarro, lEmpresa, lAseguradora;
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
		chooser = new JFileChooser();
		fFotos = new ArrayList<File>();
		// Para los combobox
		
		
		nCarpetaImagenes = "images";
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
		columnMostrarT = columnNames.length-3;
		data = new Object[][]{};		
		model = new DefaultTableModel(data, columnNames);		
		jtbUsuarios = new JTable( model )
        {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {                  	
                return getValueAt(0, column).getClass();
            }
            
            public boolean isCellEditable(int row, int col) {
                int modelCol = getColumnModel().getColumn(col).getModelIndex();
                return (modelCol != 0);
            }
        };    
        
        jtbUsuarios.setPreferredScrollableViewportSize(jtbUsuarios.getPreferredSize());
		jtbUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Para crear el listener de cambiar imagen		
	    fotoListener = new ListSelectionListener() {
		      public void valueChanged(ListSelectionEvent e) {
		    	  if (e.getValueIsAdjusting()){
		    		  String selectedData = null;

				        int[] selectedRow = jtbUsuarios.getSelectedRows();
				        int[] selectedColumns = jtbUsuarios.getSelectedColumns();
				        
				        int indexFoto = -1;
				        for (int j=0; j<columnNames.length; j++){
				        	if (columnNames[j].equals("foto")){
				        		indexFoto = j;
				        		break;
				        	}
				        }
				        boolean eligioFoto = false;			       
				        for (int j=0; j<selectedColumns.length; j++){
				        	if (selectedColumns[j] == indexFoto){
				        		eligioFoto = true;
				        	}
				        }
				        
				        int row = -1;
				        int column = -1;
				        if (selectedRow.length>0 && selectedColumns.length>0){
				        	row = selectedRow[0];
					        column = selectedColumns[0];
					        
				        }else{
				        	eligioFoto = false;
				        }				        				        
				        			        
				        if (eligioFoto){
				        	System.out.println("foto");
				        	File imgFile = mostrarChooser();
				        	ImageIcon icon = fileAImagen(imgFile);
				        	if (icon != null){
				        		ImageIcon newIcon = resizeImage(icon, 50, 50);
				        		fFotos.set(row, imgFile);
				        		model.setValueAt(newIcon, row, column);				        		
				        	}else{
//				        		File imgFile2 = new File(System.getProperty("user.dir")+"\\"+nCarpetaImagenes+"\\default.png");
//				        		//System.out.println(System.getProperty("user.dir")+"\\"+nCarpetaImagenes+"\\default.png");				        	
//				        		ImageIcon icon2 = fileAImagen(imgFile2);
//				        		ImageIcon newIcon2 = resizeImage(icon2, 50, 50);
//				        		model.setValueAt(newIcon2, row, column);
				        		System.out.println("Error al cambiar la imagen");
				        	}				        	
				        }
				        jtbUsuarios.clearSelection();
		    	  }
		      }

	    };	    
	    agregarFotoListener();
	    
		jspUsuarios = new JScrollPane(jtbUsuarios, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//inicializando la tabla
		try {
			llenarTabla(ControladorCliente.getAllClientes());
		} catch (Exception e1) {
			e1.printStackTrace();
			//agregar pop up
			JOptionPane.showMessageDialog(null,e1.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE); 
		}
		// Boton borrar
		delete = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {		    	
		    	Object[] options = {"Sí",
	                    "No"};
		    	int dialogResult = JOptionPane.showOptionDialog(null,
		    		    "Seguro que desea borrar el cliente?",
		    		    "Cuidado!",
		    		    JOptionPane.YES_NO_OPTION,
		    		    JOptionPane.WARNING_MESSAGE,
		    		    null,
		    		    options,
		    		    options[0]);
		    	if(dialogResult == JOptionPane.YES_OPTION){
		    		JTable table = (JTable)e.getSource();
			        int modelRow = Integer.valueOf( e.getActionCommand() );
			        String nit = ""+table.getModel().getValueAt(modelRow, 0);
			        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
			        try {
						ControladorCliente.deleteCliente(nit);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						//agregar pop up
						JOptionPane.showMessageDialog(null,e1.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
					}
		    	}		    	
		        
		        
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
		        for(int i=0;i<table.getModel().getColumnCount()-3;i++){		        
		        	String[] fila= new String[2];
		        	fila[0]=table.getModel().getColumnName(i);		        	       			           
		        	if (fila[0].equals("foto")){		        		
		        		fila[1]=fFotos.get(modelRow).getName();
		        		// Se tiene que copiar la imagen
		        		boolean copia = copiarArchivo(fFotos.get(modelRow).getAbsolutePath(), System.getProperty("user.dir")+"\\"+nCarpetaImagenes+"\\"+fila[1]);
		        		if (!copia){
		        			System.out.println("Hubo un error al actualizar la foto");
		        		}
		        	}else{
		        		fila[1]=String.valueOf(table.getModel().getValueAt(modelRow, i));
		        	}	
//		        	if (i<tiposCols.size()){
//		        		String tipo = tiposCols.get(i); 		        	
//			        	if (tipo.equals("varchar") || tipo.equals("date")){
//	        				fila[1]="'"+fila[1]+"'";
//	        			}else{
//	        				
//	        			}
//		        	}		        	
		        	datosN.add(fila);
		        }
		        //System.out.println(nit);
		        try {
					ControladorCliente.updateCliente(nit, datosN);
				} catch (Exception e1) {
					e1.printStackTrace();
					//agregar pop up
					JOptionPane.showMessageDialog(null,e1.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
				}
		    }
		};
		
		// Funcion de mostrar tweets
		AAmostrarTweets = new AbstractAction()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        JTable table = (JTable)e.getSource();
		        int modelRow = Integer.valueOf( e.getActionCommand() );
		        String nit = ""+table.getModel().getValueAt(modelRow, 0);
		        ArrayList tweets=null;
				try {
					tweets = ControladorCliente.getTweets(Integer.parseInt(nit));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
					//agregar pop up
					JOptionPane.showMessageDialog(null,e1.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
				}
		        crearPopUpTweets(nit, tweets);
		    }
		};
				
		ButtonColumn buttonBorrar = new ButtonColumn(jtbUsuarios, delete, columnBorrar);
		jtbUsuarios.getColumnModel().getColumn(columnBorrar).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
		// Boton actualizar
		ButtonColumn buttonActualizar = new ButtonColumn(jtbUsuarios, AAactualizar, columnActualizar);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMinWidth(120);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMaxWidth(120);
		
		// Boton de los tweets
		ButtonColumn buttonTweets = new ButtonColumn(jtbUsuarios, AAmostrarTweets, columnMostrarT);
		jtbUsuarios.getColumnModel().getColumn(columnMostrarT).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
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
				try {
					crearPopUpCrearCliente();
				} catch (Exception e) {
					e.printStackTrace();
					//agregar pop up
					JOptionPane.showMessageDialog(null,e.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
				}
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
		pnlFiltros.setBorder(BorderFactory.createTitledBorder("Más..."));		
		jspFiltros = new JScrollPane(pnlFiltros);
		pnlEditarUsuario.add(jspFiltros, BorderLayout.EAST);	
		
		btnMostrarFiltros = new JButton("Filtros");
		btnMostrarFiltros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					mostrarFiltros();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,e.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnCatalogos = new JButton("Catálogos");
		btnCatalogos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				crearPopUpCatalogos();		
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
				try {
					llenarTabla(ControladorCliente.getFilteredClientes(filtros));
				} catch (Exception e) {
					e.printStackTrace();
					//agregar pop up
					JOptionPane.showMessageDialog(null,e.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});	
		ocultarFiltros();	
		
	}
	
	private void mostrarFiltros() throws Exception{
		pnlFiltros.removeAll();
		pnlFiltros.setPreferredSize(new Dimension(300, pnlFiltros.getHeight()));
		pnlFiltros.setLayout(new GridLayout(0,2));
		valCamposPopUp = new ArrayList();
		lblCamposPopUp = new ArrayList<JLabel>();
		for (int x=0; x<columnNames.length; x++){			
			String campo = columnNames[x];			
			if (! campo.equals("")){
				lblCamposPopUp.add(new JLabel(columnNames[x]+""));
				if(campo.equals("idPais".toLowerCase())){
					ArrayList<Pais> paises= ControladorCatalogo.findAllPaises();
					String[] tipos = new String[paises.size()+1];
					tipos[0]="";
					for(int i=0;i<paises.size();i++){
						tipos[i+1]=paises.get(i).getNombre()+"";
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
						tipos[i+1]=empresas.get(i).getNombre()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("idAseguradora".toLowerCase())){
					ArrayList<Aseguradora> aseguradoras= ControladorCatalogo.findAllAseguradoras();
					String[] tipos = new String[aseguradoras.size()+1];
					tipos[0]="";
					for(int i=0;i<aseguradoras.size();i++){
						tipos[i+1]=aseguradoras.get(i).getNombre()+"";
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
		pnlFiltros.add(btnCatalogos);
		pnlFiltros.add(btnMostrarFiltros);
		pnlEditarUsuario.repaint();
		pnlEditarUsuario.revalidate();
	}
	
	private void crearPopUpCatalogos(){	
		JPanel pnlCatalogos = agregarCatalogo(new JPanel(),"");
		pnlCatalogos.setPreferredSize(new Dimension(300, 300));
//		JOptionPane pane = new JOptionPane(null, JOptionPane.INFORMATION_MESSAGE);
//		pane.showMessageDialog(null, pnlCatalogos);
		JOptionPane.showMessageDialog(null, pnlCatalogos, "Catálogos",
				JOptionPane.CLOSED_OPTION);
	}
	
	private JPanel agregarCatalogo(JPanel panel1, String catalogo){
		String defaultFont = new JLabel().getFont().getName();
		Font fontCatalogo = new Font(defaultFont, Font.BOLD, 14);
		Font fontCol = new Font(defaultFont, Font.BOLD, 12);
		Font fontDato = new Font(defaultFont, Font.PLAIN, 12);
		panel1.removeAll();
		JPanel[] lPanel = {panel1};
		JPanel panel = lPanel[0];
		panel.setLayout(new GridBagLayout());
		String[] listaCatalogos = {"", "Pais", "Carro", "Empresa", "Aseguradora"};
		JComboBox jcbCatalogos = new JComboBox(listaCatalogos);
		jcbCatalogos.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	lPanel[0]= agregarCatalogo(panel1,""+jcbCatalogos.getSelectedItem());	
		    	lPanel[0].repaint();
		    	lPanel[0].revalidate();
		    	lPanel[0].getParent().repaint();
		    	lPanel[0].getParent().revalidate();		    	
		    	
//		    	JOptionPane.showMessageDialog(null, lPanel[0], "Catálogos",
//						JOptionPane.INFORMATION_MESSAGE);
		    	
		    }
		});
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 2;			
		c.gridx= 0;
		c.gridy = 0;
		int offset = 1;
		panel.add(jcbCatalogos, c);		
		try{
			switch (catalogo){
				case "Pais":
					String[] paisesCols = {"id", "nombre", "continente", "codarea"};
					ArrayList<Pais> paises;
					// Para paises	
					paises = ControladorCatalogo.findAllPaises();
					JLabel paisesTitulo = new JLabel("Paises");															
					paisesTitulo.setFont(fontCatalogo);
					c.gridwidth = 2;			
					c.gridx= 0;
					c.gridy = 0;
					panel.add(paisesTitulo, c);
					c.fill = GridBagConstraints.HORIZONTAL;
					for(int i=0;i<paises.size();i++){
						c.gridwidth = 1;
						for (int x = 0; x<paisesCols.length; x++){
							c.gridy = offset;
							c.gridx = 0;
							String pCol = paisesCols[x];
							JLabel paisesTemp = new JLabel(pCol+": ");	
							paisesTemp.setFont(fontCol);
							panel.add(paisesTemp, c);				
							c.gridx = 1;
							JLabel paisesDato = new JLabel(paises.get(i).getAtr(pCol));
							paisesDato.setFont(fontDato);
							panel.add(paisesDato, c);
							offset++;
						}	
						JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
						c.gridwidth = 2;
						c.gridy = offset;
						c.gridx = 0;
						offset++;
						panel.add(sep, c);
					}
					break;
				case "Empresa":
					// Para empresas	
					String[] empresasCols = {"id", "nombre", "dirección", "pais", "departamento"};
					ArrayList<Empresa> empresas;
					empresas = ControladorCatalogo.findAllEmpresas();
					JLabel empresasTitulo = new JLabel("Paises");			
					empresasTitulo.setFont(fontCatalogo);
					c.gridwidth = 2;			
					c.gridx= 0;
					c.gridy = 0;
					panel.add(empresasTitulo, c);
					c.fill = GridBagConstraints.HORIZONTAL;
					for(int i=0;i<empresas.size();i++){
						c.gridwidth = 1;
						for (int x = 0; x<empresasCols.length; x++){
							c.gridy = offset;
							c.gridx = 0;
							String pCol = empresasCols[x];
							JLabel empresasTemp = new JLabel(pCol+": ");	
							empresasTemp.setFont(fontCol);
							panel.add(empresasTemp, c);				
							c.gridx = 1;
							JLabel empresasDato = new JLabel(empresas.get(i).getAtr(pCol));
							empresasDato.setFont(fontDato);
							panel.add(empresasDato, c);
							offset++;
						}	
						JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
						c.gridwidth = 2;
						c.gridy = offset;
						c.gridx = 0;
						offset++;
						panel.add(sep, c);
					}
					break;
				case "Carro":
					String[] carrosCols = {"modelo", "marca", "color", "transmisión"};
					ArrayList<Carro> carros;
					// Para carros	
					carros = ControladorCatalogo.findAllCarros();
					JLabel carrosTitulo = new JLabel("Paises");			
					carrosTitulo.setFont(fontCatalogo);
					c.gridwidth = 2;			
					c.gridx= 0;
					c.gridy = 0;
					panel.add(carrosTitulo, c);
					c.fill = GridBagConstraints.HORIZONTAL;
					offset = 1;				
					for(int i=0;i<carros.size();i++){
						c.gridwidth = 1;
						for (int x = 0; x<carrosCols.length; x++){
							c.gridy = offset;
							c.gridx = 0;
							String pCol = carrosCols[x];
							JLabel carrosTemp = new JLabel(pCol+": ");	
							carrosTemp.setFont(fontCol);
							panel.add(carrosTemp, c);				
							c.gridx = 1;
							JLabel carrosDato = new JLabel(carros.get(i).getAtr(pCol));
							carrosDato.setFont(fontDato);
							panel.add(carrosDato, c);
							offset++;
						}	
						JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
						c.gridwidth = 2;
						c.gridy = offset;
						c.gridx = 0;
						offset++;
						panel.add(sep, c);
					}
					break;
				case "Aseguradora":
					String[] aseguradorasCols = {"id", "nombre", "dirección", "precio"};
					ArrayList<Aseguradora> aseguradoras;
					// Para aseguradoras	
					aseguradoras = ControladorCatalogo.findAllAseguradoras();
					JLabel aseguradorasTitulo = new JLabel("Paises");			
					aseguradorasTitulo.setFont(fontCatalogo);
					c.gridwidth = 2;			
					c.gridx= 0;
					c.gridy = 0;
					panel.add(aseguradorasTitulo, c);
					c.fill = GridBagConstraints.HORIZONTAL;
					offset = 1;				
					for(int i=0;i<aseguradoras.size();i++){
						c.gridwidth = 1;
						for (int x = 0; x<aseguradorasCols.length; x++){
							c.gridy = offset;
							c.gridx = 0;
							String pCol = aseguradorasCols[x];
							JLabel aseguradorasTemp = new JLabel(pCol+": ");	
							aseguradorasTemp.setFont(fontCol);
							panel.add(aseguradorasTemp, c);				
							c.gridx = 1;
							JLabel aseguradorasDato = new JLabel(aseguradoras.get(i).getAtr(pCol));
							aseguradorasDato.setFont(fontDato);
							panel.add(aseguradorasDato, c);
							offset++;
						}	
						JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
						c.gridwidth = 2;
						c.gridy = offset;
						c.gridx = 0;
						offset++;
						panel.add(sep, c);
					}
					break;
					
			}
		} catch (Exception e){}
		return panel;
	}
		
	
	private void crearPopUpCrearCliente() throws Exception{
		// Pop-up del form para crear un cliente	
		final JTextField jtfFoto = new JTextField();
		final String[] pathNombre = new String[1];
		JPanel pnlPadre = new JPanel();
		JPanel pnlPopUp = new JPanel(new GridLayout(0, 1));	
		JLabel lblImagen = new JLabel();
		lblImagen.setPreferredSize(new Dimension(50, 50));
		JButton cargar = new JButton("Cargar");		
		valCamposPopUp = new ArrayList<JTextField>();
		lblCamposPopUp = new ArrayList<JLabel>();		
		for (int x=0; x<columnNames.length; x++){
			boolean agrFoto = false;
			String campo = columnNames[x];			
			if (! campo.equals("")){
				lblCamposPopUp.add(new JLabel(columnNames[x]+":"));
				if(campo.equals("idPais".toLowerCase())){
					ArrayList<Pais> paises= ControladorCatalogo.findAllPaises();
					String[] tipos = new String[paises.size()];
					for(int i=0;i<paises.size();i++){
						tipos[i]=paises.get(i).getNombre()+"";
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
						tipos[i]=empresas.get(i).getNombre()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if(campo.equals("idAseguradora".toLowerCase())){
					ArrayList<Aseguradora> aseguradoras= ControladorCatalogo.findAllAseguradoras();
					String[] tipos = new String[aseguradoras.size()];
					for(int i=0;i<aseguradoras.size();i++){
						tipos[i]=aseguradoras.get(i).getNombre()+"";
					}
					valCamposPopUp.add(new JComboBox(tipos));
				}
				else if (campo.equals("foto")){		
					jtfFoto.setEditable(false);
					valCamposPopUp.add(jtfFoto);
					// Funcion para cargar imagen
					ActionListener cargarImagen = new ActionListener()
			        {
			            public void actionPerformed(ActionEvent e)
			            {
			            	File fileImg = mostrarChooser();
			            	ImageIcon icon = fileAImagen(fileImg);
			            	if (icon != null){
			            		ImageIcon newIcon = resizeImage(icon, 50, 50);
			            		lblImagen.setIcon(newIcon);
			            		jtfFoto.setText(fileImg.getName());
			                    pathNombre[0] = fileImg.getAbsolutePath();
			            	}else{
			            		System.out.println("Error al cargar la imagen");
			            		jtfFoto.setText("");
			            		lblImagen.setIcon(null);
			            	}			            	
			            }
			        };
					cargar.addActionListener(cargarImagen);
					agrFoto = true;
				}
				else{
					valCamposPopUp.add(new JTextField());
				}				
				pnlPopUp.add(lblCamposPopUp.get(x));							
				if (agrFoto){													
					pnlPopUp.add((Component) valCamposPopUp.get(x));
					pnlPopUp.add(cargar);
				}else{
					pnlPopUp.add((Component) valCamposPopUp.get(x));
				}
			}			
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		JScrollPane jspPopUp = new JScrollPane(pnlPopUp);
		jspPopUp.setPreferredSize(new Dimension(0, (int) height-150));
		//pnlPadre.add(jspPopUp);
		int result = JOptionPane.showConfirmDialog(null, jspPopUp, "Crear Cliente",
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
            		if (x<tiposCols.size()){
            			if (tiposCols.get(x).equals("varchar") || tiposCols.get(x).equals("date")){
            				//add="'"+text.getText()+"'";
            				add = text.getText();
            			}else{
            				add = text.getText();
            			}
            		}else{
            			add = text.getText();
            		}
            		if (columnNames[x].equals("foto")){

            			String path = pathNombre[0];
            			if (path != null && !path.equals("")){
            				boolean copia = copiarArchivo(path, System.getProperty("user.dir")+"\\"+nCarpetaImagenes+"\\"+text.getText());
            				System.out.println(copia);
            				if (!copia){
            					System.out.println("--Error al copiar la imagen");
            					add = "";
            				}            				
            			}
            			
            		}
            	}
            	datos.add(add);            	          	            
            }
//          for (int x=0;x<datos.size();x++){
//        	  System.out.println("-  "+datos.get(x));
//          }
            
         // Datos es el arraylist para mandar  
        ControladorCliente.insertCliente(datos);
        // Le agregamos lo campos para los botones
        datos.add("Tweets");
        datos.add("Actualizar");
        datos.add("Borrar");
        
      //inicializando la tabla
        llenarTabla(ControladorCliente.getAllClientes());	
        //model.addRow(datos.toArray());
        
        ButtonColumn buttonBorrar = new ButtonColumn(jtbUsuarios, delete, columnBorrar);
		jtbUsuarios.getColumnModel().getColumn(columnBorrar).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
		// Boton actualizar
		ButtonColumn buttonActualizar = new ButtonColumn(jtbUsuarios, AAactualizar, columnActualizar);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMinWidth(120);
		jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMaxWidth(120);
		
		// Boton de los tweets
		ButtonColumn buttonTweets = new ButtonColumn(jtbUsuarios, AAmostrarTweets, columnMostrarT);
		jtbUsuarios.getColumnModel().getColumn(columnMostrarT).setMaxWidth(100);
		jtbUsuarios.setRowHeight(30);
		
		pnlEditarUsuario.repaint();
		pnlEditarUsuario.revalidate();		
		
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
			try {
				ControladorCliente.addCampo(jtfColNombre.getText(), jcbTipos.getSelectedItem().toString());
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,e.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
			}			
            //agregarColumna(jtfColNombre.getText());
			try {
				llenarTabla(ControladorCliente.getAllClientes());
				ButtonColumn buttonBorrar = new ButtonColumn(jtbUsuarios, delete, columnBorrar);
				jtbUsuarios.getColumnModel().getColumn(columnBorrar).setMaxWidth(100);
				jtbUsuarios.setRowHeight(30);
				
				// Boton actualizar
				ButtonColumn buttonActualizar = new ButtonColumn(jtbUsuarios, AAactualizar, columnActualizar);
				jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMinWidth(120);
				jtbUsuarios.getColumnModel().getColumn(columnActualizar).setMaxWidth(120);
				
				// Boton de los tweets
				ButtonColumn buttonTweets = new ButtonColumn(jtbUsuarios, AAmostrarTweets, columnMostrarT);
				jtbUsuarios.getColumnModel().getColumn(columnMostrarT).setMaxWidth(100);
				jtbUsuarios.setRowHeight(30);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,e.getMessage() ,"alert", JOptionPane.ERROR_MESSAGE);
			}
        } else {
            System.out.println("Cancelled");
        }
	}
	
	private void crearPopUpTweets(String nit, ArrayList<String> tweets){
		// Pop-up del form para crear un cliente	
		//JPanel pnlPopUp = new JPanel(new GridLayout(0, 1));
		JPanel pnlPopUp = new JPanel(new BorderLayout());
		JLabel lblNit = new JLabel(nit);
		pnlPopUp.add(lblNit, BorderLayout.NORTH);
		String tweetsStr = "";
		for (int x=0; x<tweets.size(); x++){
			tweetsStr += ""+x+". "+tweets.get(x)+"\n---\n";
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		JTextArea jtaTweet = new JTextArea();
		jtaTweet.setText(tweetsStr);	
		JScrollPane jsp = new JScrollPane(jtaTweet);
		//jsp.setPreferredSize(new Dimension(500, (int) height-200));
		jsp.setMaximumSize(new Dimension((int) width-100, 0)); 
		pnlPopUp.add(jsp, BorderLayout.CENTER);		
		JScrollPane jspPopUp = new JScrollPane(pnlPopUp);		
		JOptionPane.showMessageDialog(null, jspPopUp, "Tweets",
				JOptionPane.INFORMATION_MESSAGE);		
	}
	
	private void llenarTabla(ResultSet clientes) throws Exception{
		this.clientes=clientes;
		tiposCols.clear();
		columnNames = new String[]{};
		columnBorrar=columnNames.length-1;
		columnActualizar=columnNames.length-2;
		columnMostrarT = columnNames.length-3;
		data = new Object[][]{};
		model = new DefaultTableModel(data, columnNames);
		// ComboBoxes para catalogos
		jcbPais = new JComboBox();
		jcbCarro = new JComboBox();
		jcbEmpresa = new JComboBox();
		jcbAseguradora = new JComboBox();
		
		jtbUsuarios.setModel(model);		
		//crear campos
		ResultSetMetaData metadata=null;
		try {
			metadata=clientes.getMetaData();			
			int tamanoMD =metadata.getColumnCount(); 
			int tamano = tamanoMD+3;			
			columnNames=new String[tamano];
			for(int i=0;i<tamano;i++){
				if (i>=tamanoMD){
					model.addColumn("");
					columnNames[i] = "";
				}else{
					model.addColumn(metadata.getColumnName(i+1));
					//System.out.println(typeNumToString(metadata.getColumnType(i+1)));
					tiposCols.add(typeNumToString(metadata.getColumnType(i+1)));					
					columnNames[i]=metadata.getColumnName(i+1);
					//System.out.println(columnNames[i]);
					if (columnNames[i].equals("idpais")){
						columnPais = i;
					}else if (columnNames[i].equals("modelocarro")){
						columnCarro = i;
					}else if (columnNames[i].equals("idempresa")){
						columnEmpresa = i;
					}else if (columnNames[i].equals("idaseguradora")){
						columnAseguradora = i;
					}
				}				
			}
			// Para los combobox
			// Pais
			ArrayList<Pais> paises= ControladorCatalogo.findAllPaises();
			lPais = new String[paises.size()];
			for(int i=0;i<paises.size();i++){
				lPais[i]=paises.get(i).getNombre()+"";
			}
			jcbPais = new JComboBox(lPais);
			TableColumn paisColumn = jtbUsuarios.getColumnModel().getColumn(columnPais);
			paisColumn.setCellEditor(new DefaultCellEditor(jcbPais));
			DefaultTableCellRenderer rendererPais =
		                new DefaultTableCellRenderer();
			paisColumn.setCellRenderer(rendererPais);
			// Carro TODO
			ArrayList<Carro> carros= ControladorCatalogo.findAllCarros();
			lCarro = new String[carros.size()];
			for(int i=0;i<carros.size();i++){
				lCarro[i]=carros.get(i).getModelo()+"";
			}
			jcbCarro = new JComboBox(lCarro);
			TableColumn carroColumn = jtbUsuarios.getColumnModel().getColumn(columnCarro);
			carroColumn.setCellEditor(new DefaultCellEditor(jcbCarro));
			DefaultTableCellRenderer rendererCarro =
		                new DefaultTableCellRenderer();
			carroColumn.setCellRenderer(rendererCarro);
			// Empresa
			ArrayList<Empresa> empresaes= ControladorCatalogo.findAllEmpresas();
			lEmpresa = new String[empresaes.size()];
			for(int i=0;i<empresaes.size();i++){
				lEmpresa[i]=empresaes.get(i).getNombre()+"";
			}
			jcbEmpresa = new JComboBox(lEmpresa);
			TableColumn empresaColumn = jtbUsuarios.getColumnModel().getColumn(columnEmpresa);
			empresaColumn.setCellEditor(new DefaultCellEditor(jcbEmpresa));
			DefaultTableCellRenderer rendererEmpresa =
		                new DefaultTableCellRenderer();
			empresaColumn.setCellRenderer(rendererEmpresa);
			// Aseguradora
			ArrayList<Aseguradora> aseguradoraes= ControladorCatalogo.findAllAseguradoras();
			lAseguradora = new String[aseguradoraes.size()];
			for(int i=0;i<aseguradoraes.size();i++){
				lAseguradora[i]=aseguradoraes.get(i).getNombre()+"";
			}
			jcbAseguradora = new JComboBox(lAseguradora);
			TableColumn aseguradoraColumn = jtbUsuarios.getColumnModel().getColumn(columnAseguradora);
			aseguradoraColumn.setCellEditor(new DefaultCellEditor(jcbAseguradora));
			DefaultTableCellRenderer rendererAseguradora =
		                new DefaultTableCellRenderer();
			aseguradoraColumn.setCellRenderer(rendererAseguradora);
			
			columnMostrarT = tamano-3;
			columnActualizar = tamano-2;
			columnBorrar = tamano-1;	
			int numCol = 1;
			fFotos.clear();
			while(clientes.next()){				
				Object[] fila = new Object[columnNames.length];				
				for(int i=0;i<tamanoMD;i++){
					String tipoCol = tiposCols.get(i);
					String nombreCol = columnNames[i];
					if (nombreCol.equals("foto")){
						numCol = i;						
						try{
							//System.out.println("-  "+clientes.getObject(nombreCol).toString());
							String nombreF = clientes.getObject(nombreCol).toString();
							String pathFoto = nCarpetaImagenes+"\\"+nombreF;
							fFotos.add(new File(pathFoto));
							ImageIcon icon = new ImageIcon(pathFoto);							
							ImageIcon newIcon = resizeImage(icon, 50, 50);
							fila[i] = newIcon;							
							//fila[i]=clientes.getObject(nombreCol).toString();
						}catch(Exception e){
							System.out.println("Error al abrir la imagen");
						}						
					}else if (nombreCol.equals("idpais")){
						String agregar = "" +clientes.getObject(columnNames[i]);
						for (Pais p : paises){
							if(agregar.equals(p.getId()+"")){
								fila[i]=p.getNombre();
							}
						}
						
						
					}else if (nombreCol.equals("idempresa")){
						String agregar = "" +clientes.getObject(columnNames[i]);
						for (Empresa e : empresaes){
							if(agregar.equals(e.getId()+"")){
								fila[i]=e.getNombre();
							}
						}
						
					}else if (nombreCol.equals("idaseguradora")){
						String agregar = "" +clientes.getObject(columnNames[i]);
						for (Aseguradora a : aseguradoraes){
							if(agregar.equals(a.getId()+"")){
								fila[i]=a.getNombre();
							}
						}

					}else{
						String agregar = "" +clientes.getObject(columnNames[i]);
						if (agregar.equals("null")){
							agregar = "";
						}
						fila[i]=agregar;												
					}
					
				}
//				System.out.println("| "+columnMostrarT+" | "+columnActualizar+" | "+columnBorrar);
				fila[columnMostrarT] = "Tweets";
				fila[columnActualizar] = "Actualizar";
				fila[columnBorrar] = "Borrar";					
//				for (int x=0; x<fila.length; x++){
//					System.out.println(fila[x]);
//				}
				model.addRow(fila);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}				
		columnBorrar=columnNames.length-1;
		columnActualizar=columnNames.length-2;
		columnMostrarT = columnNames.length-3;
	}
	
	private String typeNumToString(int num){
		String devolver = null;
		switch (num){
			case 0:
				devolver = "null";
				break;
			case 1:
				devolver = "char";
			case 4:
				devolver = "integer";
				break;
			case 7:
				devolver = "real";
				break;
			case 12:
				devolver = "varchar";
				break;
			case 91:
				devolver = "date";
				break;						
		}
		return devolver;
	}		
	
	private void agregarFotoListener(){
		jtbUsuarios.setCellSelectionEnabled(true);
	    ListSelectionModel cellSelectionModel = jtbUsuarios.getSelectionModel();
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //cellSelectionModel.removeListSelectionListener(null);
	    cellSelectionModel.addListSelectionListener(fotoListener);
	}
	
	private File mostrarChooser(){
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
		chooser = new JFileChooser();
		chooser.removeChoosableFileFilter(
				chooser.getFileFilter() );
		chooser.setFileFilter(filter);		
		chooser.showOpenDialog(null);		
        File fileImg = chooser.getSelectedFile();
        return fileImg;
    	
	}
	
	private ImageIcon fileAImagen(File fileImg){
		try {
			if (fileImg!=null){
				Image img=ImageIO.read(fileImg);
	            ImageIcon icon=new ImageIcon(img); // ADDED
	            return icon;
			}           
        }
        catch(IOException e1) {}
		return null;
	}
	
	private ImageIcon resizeImage(ImageIcon icon, int width, int height){
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}
	
	private boolean copiarArchivo(String fileOrigen, String fileDestino){
			File imagen = new File(fileOrigen);
			File destino = new File(fileDestino);
			try{
				Files.copy(imagen.toPath(), destino.toPath(), REPLACE_EXISTING);
				return true;
			}catch(Exception e){
				return false;
			}			
	}
	
	// Para el combo box
	class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
		  public MyComboBoxRenderer(String[] items) {
		    super(items);
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    if (isSelected) {
		      //setForeground(table.getSelectionForeground());
		      super.setBackground(table.getSelectionBackground());
		    } else {
		      setForeground(table.getForeground());
		      setBackground(table.getBackground());
		    }
		    setSelectedItem(value);
		    return this;
		  }
		}

		class MyComboBoxEditor extends DefaultCellEditor {
		  public MyComboBoxEditor(String[] items) {
		    super(new JComboBox(items));
		  }
		}
}
