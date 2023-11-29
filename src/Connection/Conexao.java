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
				System.out.println("Abrindo servidor como maquina coordenadora §§...");
				ServerSocket server = new ServerSocket(this.porta);
				server.setReuseAddress(true);
				Socket conexao = null;
				
				// Aguarda requisicao enquanto a maquina estiver ativa
				while (maquina.isAtivo()) {
					try {
						System.out.println("Aguardando requisicoes ...");
						conexao = server.accept();
						BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
						PrintWriter printWriter = new PrintWriter(conexao.getOutputStream(), true);
						
						// Recupera o Ip da maquina que fez a requisicao
						InetAddress ip = conexao.getInetAddress();
						
						String mensagemRequisicao = bufferedReader.readLine();
						if(mensagemRequisicao != null) { 
							// Loga a requisicao recebida
							System.out.println("Requisicao recebida de: " + ip.getHostAddress());
							// Retorna mensagem para a maquina
							System.out.println("Enviando resposta para: " + ip.getHostAddress());
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
				System.out.println("Erro durante a execucao do servidor: \n" + e.getMessage());
			}
		}).start();
	}
	
	public void fazerRequisicao(Maquina maquina, Maquina maquinaCoordenador) throws IOException {
		Socket conexao = null;
		try {
			// Inicia o socket para realizar a requisicao
			conexao = new Socket(maquinaCoordenador.getIpMaquina(), this.porta);
			PrintWriter printWriter = new PrintWriter(conexao.getOutputStream(), true);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			
			// Manda requisicao com mensagem
			System.out.println("Enviando requisicao ao coordenador: " + maquinaCoordenador.getIpMaquina());
			printWriter.println("Req");
			
			// Recebe o retorno do coordenador
			String mensagemRetorno = bufferedReader.readLine();
			System.out.println("Resposta do coordenador recebida: " + mensagemRetorno);
		} catch (IOException e) {
			System.out.println("Erro ao realizar requisicao: \n" + e.getMessage());
		} finally {
			conexao.close();
		}
	}
	
	public String aguardaMensgemEleicao(Maquina maquina){
		// Criamos um socket datagram para ouvir a mensagem de eleicao
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(1002);
			// Criamos um pacote de dados para recebimento
			DatagramPacket pacoteEntrada = new DatagramPacket(new byte[1024], 1024);
			
			// Aguardamos a mensagem de eleicao
			datagramSocket.receive(pacoteEntrada);
			InetAddress ipMaquinaRemetente = pacoteEntrada.getAddress();
			
			// Transformamos essa mensagem em String e retornamos
			String mensagemRecebida = new String(pacoteEntrada.getData());
			System.out.println("Mensagem de eleicao recebida de [" + ipMaquinaRemetente + "]: " + mensagemRecebida);
			return mensagemRecebida;
		} catch (Exception e) {
			System.out.println("Erro no recebimento da mensagem de eleicao: \n" + e.getMessage());
		} finally {
			// Fechamos a conexao
			if(datagramSocket != null) {
				datagramSocket.close();
			}
		}
		return null;
	}
	
	public void enviaMensagemEleicao(String mensagemEleicao, Maquina maquinaSucessora) {
		// Criamos um socket datagram para emviar a mensagem de eleicao
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket();
			// Recuperamos o ip da maquina que ira receber a mensagem (sucessora)
			InetAddress ipMaquinaSucessora = InetAddress.getByName(maquinaSucessora.getIpMaquina());
			// Criamos um pacote para envio
			DatagramPacket datagramPacket = 
					new DatagramPacket(mensagemEleicao.getBytes(),
							mensagemEleicao.getBytes().length,
							ipMaquinaSucessora, 1002);
			
			// Fazemos o envio
			System.out.println("Enviando mensagem de eleicao para [" + ipMaquinaSucessora + "]: " + mensagemEleicao);
			datagramSocket.send(datagramPacket);
		} catch (Exception e) {
			System.out.println("Erro no envio da mensagem de eleicao: \n" + e.getMessage());
		} finally {
			// Fechamos a conexao
			if(datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}
	
}