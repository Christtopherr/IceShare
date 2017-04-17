package br.univel.jshare.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import br.univel.jshare.comum.Arquivo;
import br.univel.jshare.comum.Cliente;

public class Model extends AbstractTableModel implements TableModel {

	private Object[][] matriz;
	private int linhas;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public Model(Map<Cliente, List<Arquivo>> mapa) {

		linhas = 0;
		for (Entry<Cliente, List<Arquivo>> e : mapa.entrySet()) {
			linhas += e.getValue().size();
		}

		matriz = new Object[linhas][9];

		int linha = 0;

		for (Entry<Cliente, List<Arquivo>> e : mapa.entrySet()) {
			for (Arquivo arq : e.getValue()) {
				matriz[linha][0] = e.getKey().getNome();
				matriz[linha][1] = e.getKey().getIp();
				matriz[linha][2] = e.getKey().getPorta();
				matriz[linha][3] = arq.getNome();
				matriz[linha][4] = arq.getPath();
				matriz[linha][5] = arq.getExtensao();
				matriz[linha][6] = arq.getTamanho();
				matriz[linha][7] = arq.getMd5();
				matriz[linha][8] = arq.getDataHoraModificacao();
				linha++;
			}
		}
	}

	@Override
	public int getColumnCount() {
		return 9;
	}

	@Override
	public int getRowCount() {
		return linhas;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return matriz[rowIndex][columnIndex];
	}

	@Override
	public String getColumnName(int col) {

		switch (col) {
		case 0:
			return "Cliente";
		case 1:
			return "IP";
		case 2:
			return "Porta";

		case 3:
			return "Nome Arquivo";
		case 4:
			return "Path";
		case 5:
			return "Extensão";
		case 6:
			return "Tamanho";
		case 7: 
			return "Md5";
		case 8: 
			return "Data/Hora";

		default:
			return null;
		}

	}
	
	public Map<Cliente, Arquivo> pegarInformacoesArquivo(int row) throws ParseException {
		Map<Cliente, Arquivo> informacoesArquivo = new HashMap<>();
		
		// Monta o cliente com as informações para conexão
		Cliente cliente = new Cliente();
		cliente.setNome(String.valueOf(matriz[row][0]));
		cliente.setIp(String.valueOf(matriz[row][1]));
		cliente.setPorta(Integer.parseInt(String.valueOf(matriz[row][2])));		
		
		// Monta o arquivo passando a posição da linha e o indice de cada atributo correspondente
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(String.valueOf(matriz[row][3]));
		arquivo.setExtensao(String.valueOf(matriz[row][4]));
		arquivo.setTamanho(Long.valueOf(String.valueOf(matriz[row][5])));
		arquivo.setPath(String.valueOf(matriz[row][6]));
		arquivo.setMd5(String.valueOf(matriz[row][7]));
		arquivo.setDataHoraModificacao(sdf.parse(String.valueOf(matriz[row][8])));
		
		informacoesArquivo.put(cliente, arquivo);
		
		return informacoesArquivo;
	}
}
