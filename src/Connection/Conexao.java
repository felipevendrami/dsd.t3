package Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import Model.Maquina;
import Model.Rede;

public class Conexao{

	private int porta = 1001;
	
	public Conexao() {}
	
	public void aguardarConexoes(Maquina maquina) throws IOException {
		new Thread(() -> {
			try {
				System.out.println("Abrindo servidor (Máquina Líder) ...");
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
				System.out.println("Erro durante a execução do servidor: \n" + e.getMessage());
			}
		}).start();
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
			System.out.println("Erro ao realizar requisição: \n" + e.getMessage());
		} finally {
			conexao.close();
		}
	}
	
	public String aguardaMensgemEleicao(Maquina maquina){
		// Criamos um socket datagram para ouvir a mensagem de eleição
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(1002);
			// Criamos um pacote de dados para recebimento
			DatagramPacket pacoteEntrada = new DatagramPacket(new byte[1024], 1024);
			// Aguardamos a mensagem de eleição
			datagramSocket.receive(pacoteEntrada);
			// Transformamos essa mensagem em String e retornamos
			String mensagemRecebida = new String(pacoteEntrada.getData());
			return mensagemRecebida;
		} catch (Exception e) {
			System.out.println("Erro no recebimento da mensagem de eleição: \n" + e.getMessage());
		} finally {
			// Fechamos a conexão
			datagramSocket.close();
		}
		return null;
	}
	
	public void enviaMensagemEleicao(String mensagemEleicao, Maquina maquina) {
		// Criamos um socket datagram para emviar a mensagem de eleição
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket();
			// Recuperamos o ip da máquina que irá receber a mensagem (sucessora)
			InetAddress ipMaquinaSucessora = InetAddress.getByName(Rede.getMaquinaSucessora(maquina).getIpMaquina());
			// Criamos um pacote para envio
			DatagramPacket datagramPacket = 
					new DatagramPacket(mensagemEleicao.getBytes(),
							mensagemEleicao.getBytes().length,
							ipMaquinaSucessora, 1002);
			// Fazemos o envio
			datagramSocket.send(datagramPacket);
		} catch (Exception e) {
			System.out.println("Erro no envio da mensagem de eleição: \n" + e.getMessage());
		} finally {
			// Fechamos a conexão
			datagramSocket.close();
		}
	}
	
}