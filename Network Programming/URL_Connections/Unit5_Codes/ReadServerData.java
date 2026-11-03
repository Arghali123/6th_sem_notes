import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ReadServerData {
    public static void main(String[] args) {
        try{
            //Step1: Create the URL object
            URL url=new URL("https://example.com");

            //Step2: Open Connection
            URLConnection connection=url.openConnection();

            //Step3: Obtain Input Stream
            InputStream input=connection.getInputStream();

            //Step4: Convert byte stream into character stream
            InputStreamReader reader=new InputStreamReader(input);

            //Step5: Read effectively using Buffered reader
            BufferedReader br=new BufferedReader(reader);

            //Step6: Read the server response line by line.
            String line;

            while((line = br.readLine()) != null)
            {
                System.out.println(line);
            }

            //Step7: Close resource
            br.close();

        }catch(IOException e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }
}
