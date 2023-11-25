package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Model.Maquina;

public class Conexao{

	private int porta = 1001;
	
	public Conexao() {}
	
	public void aguardarConexoes(Maquina maquina) throws IOException {
		try {
			ServerSocket server = new ServerSocket(this.porta);
			server.setReuseAddress(true);
			Socket conexao = null;
			
			// Aguarda requisição enquanto a maquina estiver ativa
			while (maquina.isAtivo()) {
				try {
					System.out.println("Aguardando requisições ...");
					conexao = server.accept();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
					PrintWriter printWriter = new PrintWriter(conexao.getOutputStream(), true);
					
					// Recupera o Ip da máquina que fez a requisição
					InetAddress ip = conexao.getInetAddress();
					
					String mensagemRequisicao = bufferedReader.readLine();
					if(mensagemRequisicao != null) { 
						// Loga a requisição recebida
						System.out.println("Requisição recebida de: " + ip.getHostAddress());
						// Retorna mensagem para a máquina
						printWriter.println("OK !");
					}
				} catch (IOException e) {
					throw new IOException(e.getMessage());
				}  finally {
					// Ao final sempre fechamos a conexao
					conexao.close();
				}
			}
		} catch (Exception e) {
			System.out.println("Erro duranta a execução do ServerSocket: \n" + e.getMessage());
		}
	}
	
	public void fazerRequisicao(Maquina maquina, Maquina maquinaLider) throws IOException {
		Socket conexao = null;
		try {
			// Inicia o socket para realizar a requisição
			conexao = new Socket(maquinaLider.getIpMaquina(), this.porta);
			PrintWriter printWriter = new PrintWriter(conexao.getOutputStream(), true);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			
			// Manda requisição com mensagem
			printWriter.println("Req");
			System.out.println("Requisição feita ao coordenador: " + maquinaLider.getIpMaquina());
			
			// Recebe o retorno do coordenador
			String mensagemRetorno = bufferedReader.readLine();
			System.out.println("Coordenador: " + mensagemRetorno);
		} catch (IOException e) {
			System.out.println("Erro:" + e.getMessage());
		} finally {
			conexao.close();
		}
	}
	
}