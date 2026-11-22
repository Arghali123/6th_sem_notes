import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Redirector {
    public static void main(String[] args) throws IOException{

        int port=8081;

        try(ServerSocket server=new ServerSocket(port))
        {
            System.out.println("Redirector running at http://localhost:"+port);

            while(true)
            {
                try(Socket client=server.accept())
                {
                    BufferedReader reader=new BufferedReader(new InputStreamReader(client.getInputStream(),StandardCharsets.ISO_8859_1));

                    //Read request line
                    String requestLine=reader.readLine();
                    System.out.println("Request: "+requestLine);

                    //Consume request headers
                    String line;
                    while((line=reader.readLine()) != null && !line.isEmpty())
                    {
                        //No additonal processing required
                    }

                                        String response =
                            "HTTP/1.1 302 Found\r\n" +
                            "Location: https://example.com/\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n";
                    
                    OutputStream output=client.getOutputStream();
                    output.write(response.getBytes(StandardCharsets.ISO_8859_1));
                    output.flush();

                }
            }
        }
        
    }
}
