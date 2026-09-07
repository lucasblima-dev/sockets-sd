import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteTCP {
  public static void main(String[] args) {
    String host = "192.168.10.10"; // IP da VM1
    int porta = 5000;

    try (Socket socket = new Socket(host, porta);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Scanner scanner = new Scanner(System.in)) {

      System.out.print("Digite seu voto (Ex: Chapa 1 ou Chapa 2): ");
      String voto = scanner.nextLine();

      System.out.println("Enviando voto de forma segura via TCP...");
      out.println(voto);

      String resposta = in.readLine();
      System.out.println("Resposta do Servidor: " + resposta);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}