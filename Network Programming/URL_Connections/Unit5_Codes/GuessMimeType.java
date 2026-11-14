import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

public class GuessMimeType {
    public static void main(String[] args) {
        //1. Guess MIME type from the file name
        System.out.println("Guessing from filenames:");
        String[] files={
                        "photo.png",
            "document.pdf",
            "index.html",
            "data.json",
            "unknown.xyz"
        };

        for(String file:files)
        {
            String mimeType=URLConnection.guessContentTypeFromName(file);
            System.out.println(file+" -> "+mimeType);
        }

        //2. Guess MIME type from stream bytes
        System.out.println("\nGuessing from stream:");

                // PNG file signature (first 8 bytes)
        byte[] pngSignature = {
            (byte) 0x89,
            0x50, 0x4E, 0x47,
            0x0D, 0x0A, 0x1A, 0x0A
        };

        try(ByteArrayInputStream input=new ByteArrayInputStream(pngSignature))
        {
            if(input.markSupported())
            {
                String mimeType=URLConnection.guessContentTypeFromStream(input);
                System.out.println("Stream MIME type: "+mimeType);
            }
        }catch(IOException e)
        {
            System.out.println("Error: "+e.getMessage());
        }

        //3. Using FileNameMap
        System.out.println("\nUsing FileNameMap:");
        FileNameMap map=URLConnection.getFileNameMap();
        String type=map.getContentTypeFor("report.pdf");
        System.out.println("report.pdf->"+type);
    }
}
