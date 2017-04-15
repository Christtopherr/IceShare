package br.univel.jshare.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;
import br.univel.jshare.comum.IServer;
import br.univel.jshare.comum.TipoFiltro;
import br.univel.jshare.model.Model;
import br.univel.jshare.util.Util;

public class TelaPrincipal extends JFrame implements IServer, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -436957463310443862L;

	private JPanel contentPane;
	private JTable table;
	private JTextField txtPesquisa;
	private JButton btnConectar;
	private JTextField txtIpC;
	private IServer servidor;
	private JTextField txtNomeCliente;
	private JTextField txtPortaServer;
	private File fileDefault;
	private HashMap<Cliente, List<Arquivo>> clientmap;
	private List<Arquivo> listaArquivos;
	private JTextField txtIpS;
	private IServer conexaoServidor;
	private Registry regSv;
	private Registry regCliente;
	private JTextField txtPortaC;
	private long idCliente = 0;
	private Cliente clienteLocal;
	private long IdE = 0;
	private List<Cliente> clientes;
	private JButton btnDesconectar;
	private JButton btnDesconectServer;
	private JButton btnPesquisa;
	private JButton btnDown;
	private JLabel lblServer;
	private JLabel lblCliente;
	private JTextField txtValorFiltro;
	private JComboBox cmbTipoFiltro;
	private Thread atualizarDir;
	private static final String PATH_DOW_UP = "D:\\Share";
	private JScrollPane scrollPane_1;
	private JTextArea textAreaPainel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TelaPrincipal() {

		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(TelaPrincipal.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		setTitle("Ice Share");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 647, 495);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 63, 186, 89, 46, 92, 99, 0 };
		gbl_contentPane.rowHeights = new int[] { 20, 0, 0, 0, 0, 23, 22, 0, 20, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		lblServer = new JLabel("Server");
		lblServer.setDisplayedMnemonic(' ');
		lblServer.setIcon(
				new ImageIcon("D:\\Christopher\\Downloads\\picasion.com_53f52148dcc3dfecf12374a0e9c2b3d3.gif"));
		lblServer.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblServer = new GridBagConstraints();
		gbc_lblServer.insets = new Insets(0, 0, 5, 5);
		gbc_lblServer.gridx = 0;
		gbc_lblServer.gridy = 0;
		contentPane.add(lblServer, gbc_lblServer);

		JLabel lblips = new JLabel("Ip");
		GridBagConstraints gbc_lblips = new GridBagConstraints();
		gbc_lblips.insets = new Insets(0, 0, 5, 5);
		gbc_lblips.gridx = 0;
		gbc_lblips.gridy = 1;
		contentPane.add(lblips, gbc_lblips);

		txtIpS = new JTextField();
		txtIpS.setText(getIp());
		GridBagConstraints gbc_txtIpS = new GridBagConstraints();
		gbc_txtIpS.insets = new Insets(0, 0, 5, 5);
		gbc_txtIpS.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpS.gridx = 1;
		gbc_txtIpS.gridy = 1;
		contentPane.add(txtIpS, gbc_txtIpS);
		txtIpS.setColumns(10);

		JLabel lblPortaS = new JLabel("Porta");
		GridBagConstraints gbc_lblPortaS = new GridBagConstraints();
		gbc_lblPortaS.insets = new Insets(0, 0, 5, 5);
		gbc_lblPortaS.gridx = 2;
		gbc_lblPortaS.gridy = 1;
		contentPane.add(lblPortaS, gbc_lblPortaS);

		cmbTipoFiltro = new JComboBox(TipoFiltro.values());
		cmbTipoFiltro.setToolTipText("Filtro");
		GridBagConstraints gbc_cmbFiltros = new GridBagConstraints();
		gbc_cmbFiltros.insets = new Insets(0, 0, 5, 5);
		gbc_cmbFiltros.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbFiltros.gridx = 3;
		gbc_cmbFiltros.gridy = 5;
		contentPane.add(cmbTipoFiltro, gbc_cmbFiltros);

		txtPortaServer = new JTextField();
		txtPortaServer.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortaServer.setText("1818");
		GridBagConstraints gbc_txtPortaServer = new GridBagConstraints();
		gbc_txtPortaServer.anchor = GridBagConstraints.NORTH;
		gbc_txtPortaServer.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPortaServer.insets = new Insets(0, 0, 5, 5);
		gbc_txtPortaServer.gridx = 3;
		gbc_txtPortaServer.gridy = 1;
		contentPane.add(txtPortaServer, gbc_txtPortaServer);
		txtPortaServer.setColumns(10);

		JButton btnConectServer = new JButton("Subir Server");
		btnConectServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// RMI
				StartRMI();
				btnConectServer.setEnabled(false);
				txtPortaServer.setEditable(false);
				txtIpS.setEnabled(false);
				btnDesconectServer.setEnabled(true);

			}
		});

		GridBagConstraints gbc_btnConectServer = new GridBagConstraints();
		gbc_btnConectServer.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConectServer.insets = new Insets(0, 0, 5, 5);
		gbc_btnConectServer.gridx = 4;
		gbc_btnConectServer.gridy = 1;
		contentPane.add(btnConectServer, gbc_btnConectServer);

		btnConectServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {

					desconectar(getClienteLocal());

					btnConectar.setEnabled(true);

				} catch (RemoteException e) {
					e.printStackTrace();
				}

			}
		});

		btnDesconectServer = new JButton("Server Off");
		btnDesconectServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				desligarServidor();
				btnConectServer.setEnabled(true);
				btnDesconectServer.setEnabled(false);

			}
		});
		GridBagConstraints gbc_btnDesconectServer = new GridBagConstraints();
		gbc_btnDesconectServer.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDesconectServer.insets = new Insets(0, 0, 5, 0);
		gbc_btnDesconectServer.gridx = 5;
		gbc_btnDesconectServer.gridy = 1;
		contentPane.add(btnDesconectServer, gbc_btnDesconectServer);

		lblCliente = new JLabel("Cliente");
		lblCliente.setIcon(
				new ImageIcon("D:\\Christopher\\Downloads\\picasion.com_e2edcc6b6dd98ef8ceec12727ae72d2e.gif"));
		lblCliente.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_lblCliente = new GridBagConstraints();
		gbc_lblCliente.insets = new Insets(0, 0, 5, 5);
		gbc_lblCliente.gridx = 0;
		gbc_lblCliente.gridy = 2;
		contentPane.add(lblCliente, gbc_lblCliente);

		JLabel lblipC = new JLabel("Ip");
		GridBagConstraints gbc_lblipC = new GridBagConstraints();
		gbc_lblipC.insets = new Insets(0, 0, 5, 5);
		gbc_lblipC.gridx = 0;
		gbc_lblipC.gridy = 3;
		contentPane.add(lblipC, gbc_lblipC);

		txtIpC = new JTextField();
		txtIpC.setText(getIp());
		GridBagConstraints gbc_txtIpC = new GridBagConstraints();
		gbc_txtIpC.gridwidth = 2;
		gbc_txtIpC.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpC.insets = new Insets(0, 0, 5, 5);
		gbc_txtIpC.gridx = 1;
		gbc_txtIpC.gridy = 3;
		contentPane.add(txtIpC, gbc_txtIpC);
		txtIpC.setColumns(10);

		JLabel lblPortaC = new JLabel("Porta");
		GridBagConstraints gbc_lblPortaC = new GridBagConstraints();
		gbc_lblPortaC.insets = new Insets(0, 0, 5, 5);
		gbc_lblPortaC.gridx = 3;
		gbc_lblPortaC.gridy = 3;
		contentPane.add(lblPortaC, gbc_lblPortaC);

		txtPortaC = new JTextField();
		txtPortaC.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortaC.setText("1818");
		GridBagConstraints gbc_txtPortaC = new GridBagConstraints();
		gbc_txtPortaC.insets = new Insets(0, 0, 5, 5);
		gbc_txtPortaC.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPortaC.gridx = 4;
		gbc_txtPortaC.gridy = 3;
		contentPane.add(txtPortaC, gbc_txtPortaC);
		txtPortaC.setColumns(10);

		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				conectarServidor();
				btnConectar.setEnabled(false);

			}
		});
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConectar.insets = new Insets(0, 0, 5, 0);
		gbc_btnConectar.gridx = 5;
		gbc_btnConectar.gridy = 3;
		contentPane.add(btnConectar, gbc_btnConectar);

		JLabel lblNome = new JLabel("Nome");
		GridBagConstraints gbc_lblNome = new GridBagConstraints();
		gbc_lblNome.insets = new Insets(0, 0, 5, 5);
		gbc_lblNome.gridx = 0;
		gbc_lblNome.gridy = 4;
		contentPane.add(lblNome, gbc_lblNome);

		txtNomeCliente = new JTextField();
		txtNomeCliente.setHorizontalAlignment(SwingConstants.CENTER);
		txtNomeCliente.setText("");
		GridBagConstraints gbc_txtNomeCliente = new GridBagConstraints();
		gbc_txtNomeCliente.gridwidth = 2;
		gbc_txtNomeCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeCliente.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeCliente.gridx = 1;
		gbc_txtNomeCliente.gridy = 4;
		contentPane.add(txtNomeCliente, gbc_txtNomeCliente);
		txtNomeCliente.setColumns(10);

		JLabel lblPesquisa = new JLabel("Pesquisa");
		GridBagConstraints gbc_lblPesquisa = new GridBagConstraints();
		gbc_lblPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_lblPesquisa.gridx = 0;
		gbc_lblPesquisa.gridy = 5;
		contentPane.add(lblPesquisa, gbc_lblPesquisa);

		txtPesquisa = new JTextField();
		txtPesquisa.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_txtPesquisa = new GridBagConstraints();
		gbc_txtPesquisa.gridwidth = 2;
		gbc_txtPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_txtPesquisa.fill = GridBagConstraints.BOTH;
		gbc_txtPesquisa.gridx = 1;
		gbc_txtPesquisa.gridy = 5;
		contentPane.add(txtPesquisa, gbc_txtPesquisa);
		txtPesquisa.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent click) {

				if (click.getClickCount() == 2) {

					int linhaSelecionada = table.getSelectedRow();

					getArquivoCliente(linhaSelecionada);

				}

			}

		});

		btnDown = new JButton("Download");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				getArquivoCliente(table.getSelectedRow());

			}
		});

		btnPesquisa = new JButton("Pesquisar");
		btnPesquisa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String search = txtPesquisa.getText().trim();
				TipoFiltro filtro = TipoFiltro.valueOf(cmbTipoFiltro.getSelectedItem().toString());
				String vlrFiltro = txtValorFiltro.getText().trim();

				HashMap<Cliente, List<Arquivo>> resultSearch = new HashMap<>();

				try {
					resultSearch = (HashMap<Cliente, List<Arquivo>>) conexaoServidor.procurarArquivo(search, filtro,
							vlrFiltro);

					if (!resultSearch.isEmpty()) {

						btnDown.setEnabled(true);

						Model model = new Model(resultSearch);
						table.setModel(model);

					}

				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

			}
		});

		GridBagConstraints gbc_btnPesquisa = new GridBagConstraints();
		gbc_btnPesquisa.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPesquisa.insets = new Insets(0, 0, 5, 5);
		gbc_btnPesquisa.gridx = 4;
		gbc_btnPesquisa.gridy = 5;
		contentPane.add(btnPesquisa, gbc_btnPesquisa);

		GridBagConstraints gbc_btnDown = new GridBagConstraints();
		gbc_btnDown.gridwidth = 2;
		gbc_btnDown.insets = new Insets(0, 0, 5, 5);
		gbc_btnDown.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDown.gridx = 2;
		gbc_btnDown.gridy = 6;
		contentPane.add(btnDown, gbc_btnDown);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 6;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		contentPane.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		txtValorFiltro = new JTextField();
		scrollPane.setColumnHeaderView(txtValorFiltro);
		txtValorFiltro.setToolTipText("Filtro");
		txtValorFiltro.setText("Filtro");
		txtValorFiltro.setColumns(10);

		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 6;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 9;
		contentPane.add(scrollPane_1, gbc_scrollPane_1);

		textAreaPainel = new JTextArea();
		scrollPane_1.setViewportView(textAreaPainel);

		clientes = new ArrayList<Cliente>();

		fileDefault = new File(PATH_DOW_UP);
		fileDefault.mkdir();

		clientmap = new HashMap<>();

		defaultConfig();

	}

	protected void getArquivoCliente(int linhaSelecionada) {

		Cliente cliente = new Cliente();
		Arquivo arquivo = new Arquivo();

		// Cliente
		cliente.setNome(table.getValueAt(linhaSelecionada, 0).toString());
		cliente.setIp(table.getValueAt(linhaSelecionada, 1).toString());
		cliente.setPorta(Integer.parseInt(table.getValueAt(linhaSelecionada, 2).toString()));

		// Arquivo
		arquivo.setNome(table.getValueAt(linhaSelecionada, 3).toString());
		arquivo.setPath(table.getValueAt(linhaSelecionada, 4).toString());
		arquivo.setExtensao(table.getValueAt(linhaSelecionada, 5).toString());
		arquivo.setTamanho(new Long(table.getValueAt(linhaSelecionada, 6).toString()));
		arquivo.setMd5(table.getValueAt(linhaSelecionada, 7).toString());

		try {
			regCliente = LocateRegistry.getRegistry(cliente.getIp(), cliente.getPorta());

			conexaoServidor = (IServer) regCliente.lookup(IServer.NOME_SERVICO);
			conexaoServidor.registrarCliente(cliente);

			byte[] arqBytes = conexaoServidor.baixarArquivo(cliente, arquivo);

			String Md5Arqcop = new Util().getMD5(arquivo.getPath());

			if (arquivo.getMd5().equals(Md5Arqcop)) {

				arquivodown(new File("Download do " + arquivo.getNome()), arqBytes, arquivo);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void arquivodown(File file, byte[] arqBytes, Arquivo arq) {
		// TODO Auto-generated method stub

		try {
			Files.write(Paths.get(PATH_DOW_UP.concat("\\" + file.getName() + arq.getExtensao())), arqBytes,
					StandardOpenOption.CREATE);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void defaultConfig() {
		// TODO Auto-generated method stub

		btnPesquisa.setEnabled(false);
		btnDown.setEnabled(false);
		btnDesconectServer.setEnabled(false);

	}

	public Cliente createClienteLocal() {
		Cliente cliente = new Cliente();
		cliente.setId(new Long(idCliente++));
		cliente.setIp(txtIpS.getText().trim());
		cliente.setNome(txtNomeCliente.getText().trim());
		cliente.setPorta(Integer.parseInt(txtPortaServer.getText()));

		clienteLocal = cliente;

		return clienteLocal;
	}

	public Cliente getClienteLocal() {
		return clienteLocal;
	}

	// Desliga o Servidor
	protected void desligarServidor() {

		try {
			UnicastRemoteObject.unexportObject(servidor, true);
			servidor = null;
			

		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}

	}

	// Inserir ip aut
	private String getIp() {
		InetAddress IP = null;

		try {
			IP = InetAddress.getLocalHost();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return IP.getHostAddress().toString();

	}

	private Cliente getClient(java.util.Map.Entry<Cliente, List<Arquivo>> e) {

		Cliente client = new Cliente();
		client.setId(e.getKey().getId());
		client.setIp(e.getKey().getIp());
		client.setNome(e.getKey().getNome());
		client.setPorta(e.getKey().getPorta());

		return client;
	}

	protected List<Arquivo> getArquivosDisponiveis() {

		File dirStart = fileDefault;

		List<Arquivo> listArq = new ArrayList<>();

		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				Arquivo arq = new Arquivo();
				arq.setId(new Long(IdE++));
				arq.setNome(new Util().getNome(file.getName()));
				arq.setExtensao(new Util().getExtension(file.getName()));
				arq.setTamanho(file.length());
				arq.setDataHoraModificacao(new Date(file.lastModified()));
				arq.setPath(file.getPath());
				arq.setMd5(new Util().getMD5(arq.getPath()));
				arq.setTamanho(file.getTotalSpace());
				listArq.add(arq);
			}
		}
		return listArq;
	}

	private String getFileExtension(File file2) {
		String fileName = fileDefault.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";

	}

	// Conecta o Servidor
	protected void conectarServidor() {
		String meuNome = txtNomeCliente.getText().trim();
		if (meuNome.length() == 0) {
			JOptionPane.showMessageDialog(this, "Insira seu Nome");
			return;
		}

		String host = txtIpC.getText().trim();
		if (!host.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			JOptionPane.showMessageDialog(this, "Ip Inválido!");
			return;
		}

		String stPorta = txtPortaC.getText().trim();
		if (!stPorta.matches("[0-9]+") || stPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "Porta inválida!");
			return;
		}

		Cliente cliente = createClienteLocal();

		int intPorta = Integer.parseInt(stPorta);

		try {

			regCliente = LocateRegistry.getRegistry(host, intPorta);

			conexaoServidor = (IServer) regCliente.lookup(IServer.NOME_SERVICO);

			conexaoServidor.registrarCliente(cliente);

			imprimirLog("Cliente Conectado :" + cliente.getNome());

			conexaoServidor.publicarListaArquivos(cliente, getArquivosDisponiveis());

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {

					while (true) {

						try {
							Cliente client = getClienteLocal();
							List arqs = getArquivosDisponiveis();

							conexaoServidor.publicarListaArquivos(client, arqs);

							Thread.sleep(10000);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			});

			thread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		txtNomeCliente.setEditable(false);
		txtIpC.setEditable(false);
		txtPortaC.setEditable(false);
		btnConectar.setEnabled(false);
		btnPesquisa.setEnabled(true);

	}

	private void StartRMI() {

		String porta = txtPortaServer.getText().trim();

		if (!porta.matches("[0-9]+") || porta.length() > 5) {
			JOptionPane.showMessageDialog(this, "Porta inválida!");
			return;
		}

		int intPorta = Integer.parseInt(porta);
		if (intPorta < 80 || intPorta > 65535) {
			JOptionPane.showMessageDialog(this, " Porta inexistente");
			return;
		}

		try {

			if (servidor == null) {
				servidor = (IServer) UnicastRemoteObject.exportObject(TelaPrincipal.this, 0);
			}

			regSv = LocateRegistry.createRegistry(intPorta);

			regSv.rebind(IServer.NOME_SERVICO, servidor);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		imprimirLog("Server ON" );

	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub

		if (clientmap.containsKey(c)) {
			JOptionPane.showMessageDialog(TelaPrincipal.this, "Cliente Registrado no Server.");
		} else {

			clientmap.put(c, new ArrayList<>());

		}

	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		// TODO Auto-generated method stub

		for (java.util.Map.Entry<Cliente, List<Arquivo>> e : clientmap.entrySet()) {

			if (e.getKey().equals(c)) {

				e.getValue().clear();
				e.setValue(lista);

			}

		}

	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		// TODO Auto-generated method stub

		listaArquivos = new ArrayList<>();
		HashMap<Cliente, List<Arquivo>> resultpesq = new HashMap<>();

		Pattern pat = Pattern.compile(".*" + query + ".*");

		for (java.util.Map.Entry<Cliente, List<Arquivo>> e : clientmap.entrySet()) {

			Cliente client = getClient(e);

			for (Arquivo arq : e.getValue()) {

				switch (tipoFiltro) {
				case NOME:

					if (arq.getNome().contains(query)) {

						listaArquivos.add(arq);

					}

					break;

				case TAMANHO_MIN:

					try {

						if (arq.getTamanho() > Integer.parseInt(filtro)) {

							if (arq.getNome().contains(query)) {

								listaArquivos.add(arq);
							}

						}
					} catch (Exception e2) {
					}
					break;
				case TAMANHO_MAX:
					try {
						if (arq.getTamanho() < Integer.parseInt(query)) {

							if (arq.getNome().contains(query)) {
								listaArquivos.add(arq);
							}

						}
					} catch (Exception e3) {

					}
					break;
				case EXTENSAO:

					if (arq.getExtensao().contains(query)) {
						listaArquivos.add(arq);
					}
					break;

				default:
					JOptionPane.showMessageDialog(null, "Não foi possivel realizar a busca, tente novamente");
					break;
				}

			}

			resultpesq.put(client, listaArquivos);
		}

		return resultpesq;
	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arquivo) throws RemoteException {
		// TODO Auto-generated method stub
		byte[] shArq = null;

		Path path = Paths.get(arquivo.getPath());

		try {
			shArq = Files.readAllBytes(path);

		} catch (IOException e) {

			JOptionPane.showMessageDialog(TelaPrincipal.this, "Impossivel verificar arquivo", "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		return shArq;
	}

	@Override
	public void desconectar(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub

		if (servidor != null) {
			servidor.desconectar(c);
			servidor = null;
		}

	}

	public void imprimirLog(String texto) {
		textAreaPainel.append(texto + "\n");

	}
}
