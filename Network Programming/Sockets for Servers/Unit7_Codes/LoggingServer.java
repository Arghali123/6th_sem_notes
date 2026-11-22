import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingServer {
    private static final Logger logger=Logger.getLogger(LoggingServer.class.getName());


    public static void main(String[] args) {
        FileHandler fileHandler=null;

        try{
            //configure file logging
            fileHandler=new FileHandler("server.log",true);

            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            logger.setUseParentHandlers(false);
            logger.setLevel(Level.INFO);
            logger.addHandler(fileHandler);

            logger.info("Starting TCP server...");

            //Start server
            try(ServerSocket server=new ServerSocket(5000))
            {
                logger.info("Server listening on port 5000");

                //Accept one client for this demonstration
                try(Socket client=server.accept())
                {
                    String address=client.getInetAddress().getHostAddress();

                    logger.info("Client connected: "+address);

                    BufferedReader input=new BufferedReader(new InputStreamReader(client.getInputStream()));

                    PrintWriter output=new PrintWriter(client.getOutputStream(),true);

                    String message=input.readLine();

                    if(message != null)
                    {
                        logger.info("Received message of "+message.length()+" characters");
                        output.println("Message received!");
                    }else
                    {
                        logger.warning("Client closed connection without a message");
                    }
                }
            }
        }catch(IOException e)
        {
            logger.log(Level.SEVERE,"Server encountered an I/O EROR "+e);
        }finally
        {
            logger.info("Server shutting down");
            if(fileHandler != null)
            {
                logger.removeHandler(fileHandler);
                fileHandler.close();
            }
        }
    }
}
