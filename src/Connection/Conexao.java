package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import Model.Maquina;

public class Conexao{

	private int porta = 1001;
	
	public Conexao() {}
	
	public void aguardaConexao(Maquina maquina) throws IOException {
		Socket conexao = null;
		try {
			// Inicia o servidor e aguarda requisição enquanto a maquina estiver ativa
			ServerSocket server = new ServerSocket(this.porta);
			while (maquina.isAtivo()) {
				System.out.println("Aguardando requisições ...");
				conexao = server.accept();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
				PrintWriter printWriter = new PrintWriter(conexao.getOutputStream(), true);
				
				// Recupera o Ip da máquina que fez a requisição
				InetAddress ip = conexao.getInetAddress();
				
				// Imprime a requisição recebida
				String mensagemRequisicao = bufferedReader.readLine();
				System.out.println("Requisição recebida de: " + ip.getHostAddress());
				
				// Retorna mensagem para a máquina
				printWriter.println("OK !");
				conexao.close();
			}
		} catch (IOException e) {
			System.out.println("Erro:" + e.getMessage());
		} finally {
			conexao.close();
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