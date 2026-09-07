import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {
  private static ConcurrentHashMap<String, Integer> votos = new ConcurrentHashMap<>();

  public static void main(String[] args) {
    votos.put("Chapa 1", 0);
    votos.put("Chapa 2", 0);

    System.out.println("Servidor de Votação Iniciado na VM1...");

    new Thread(() -> iniciarServidorTCP()).start();

    new Thread(() -> iniciarServidorUDP()).start();
  }

  private static void iniciarServidorTCP() {
    try (ServerSocket serverSocket = new ServerSocket(5000)) {
      System.out.println("Servidor TCP (Votos) aguardando conexões na porta 5000...");
      while (true) {
        Socket socket = serverSocket.accept();
        new Thread(new TrataVotoTCP(socket)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void iniciarServidorUDP() {
    try (DatagramSocket udpSocket = new DatagramSocket(5001)) {
      System.out.println("Servidor UDP (Placar) rodando na porta 5001...");
      byte[] buffer = new byte[256];
      while (true) {
        DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
        udpSocket.receive(pacoteRecebido);

        String placar = "Placar Atual: " + votos.toString();
        byte[] dadosEnvio = placar.getBytes();

        DatagramPacket pacoteEnvio = new DatagramPacket(
          dadosEnvio, dadosEnvio.length, pacoteRecebido.getAddress(), pacoteRecebido.getPort());
        
        udpSocket.send(pacoteEnvio);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class TrataVotoTCP implements Runnable {
    private Socket socket;

    public TrataVotoTCP(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
          
        String voto = in.readLine();
        System.out.println("Processando voto recebido: " + voto);
        
        Thread.sleep(3000); 

        if (votos.containsKey(voto)) {
            votos.put(voto, votos.get(voto) + 1);
            out.println("Sucesso: Voto computado para a " + voto);
        } else {
            out.println("Erro: Candidato invalido.");
        }
        socket.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}