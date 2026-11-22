import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileLoggingExample
{
    public static void main(String[] args) {
        Logger logger=Logger.getLogger("MyServer");

        FileHandler fileHandler=null;

        try{
            //Create or append to a log file
            fileHandler=new FileHandler("server.log",true);

            //Format messages as readable text
            fileHandler.setFormatter(new SimpleFormatter());

            //Allow INFO and higher-priority messages
            fileHandler.setLevel(Level.INFO);

            //configure logger
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.INFO);
            logger.addHandler(fileHandler);

            logger.info("Server started");
            logger.warning("Client disconnected unexpectedly");
            logger.severe("Database connection failed");

            System.out.println("Logs written successfully!");
        }catch(IOException e)
        {
            System.err.println("Unable to create log file");
            e.printStackTrace();
        }finally{
            if(fileHandler != null)
            {
                logger.removeHandler(fileHandler);
                fileHandler.close();
            }
        }
    }
}