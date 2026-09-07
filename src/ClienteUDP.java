import java.net.*;

public class ClienteUDP {
  public static void main(String[] args) {
    String host = "192.168.10.10"; // IP da VM1
    int porta = 5001;

    try (DatagramSocket udpSocket = new DatagramSocket()) {
      byte[] mensagem = "solicitar_placar".getBytes();
      InetAddress enderecoServidor = InetAddress.getByName(host);
      
      DatagramPacket pacoteEnvio = new DatagramPacket(mensagem, mensagem.length, enderecoServidor, porta);
      System.out.println("Solicitando placar via UDP (sem conexao previa)...");
      udpSocket.send(pacoteEnvio);

      byte[] buffer = new byte[256];
      DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
      udpSocket.receive(pacoteRecebido);

      String resposta = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
      System.out.println(resposta);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}