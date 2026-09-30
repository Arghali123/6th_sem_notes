import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WebServerLogProcessor {

    public static void main(String[] args) {
        String fileName="log.txt";

        Map<Integer,Integer> statusCount=new HashMap<>();

        try(
            FileInputStream fis=new FileInputStream(fileName);
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr)

        )
        {
            String line;

            while((line = br.readLine()) != null)
            {
                String[] parts=line.trim().split("\\s+");

                if(parts.length >= 5 )
                {
                    int statusCode=Integer.parseInt(parts[parts.length-1]);

                    statusCount.put(
                        statusCode
                        , statusCount.getOrDefault(statusCode, 0)+1);
                }
            }

            System.out.println("Status Code Counts:");

            for(Map.Entry<Integer,Integer> entry:statusCount.entrySet())
            {
                System.out.println(entry.getKey()+": "+entry.getValue());
            }

        }catch(Exception e)
        {
         System.out.println("Error: "+e.getMessage());
        }
    }
}