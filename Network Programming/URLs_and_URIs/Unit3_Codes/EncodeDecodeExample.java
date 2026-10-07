import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodeDecodeExample {
    public static void main(String[] args) {
        try {
            String original = "Hello world & i am good";
            
            // StandardCharsets.UTF_8.name() evaluates to "UTF-8"
            String encoded = URLEncoder.encode(original, StandardCharsets.UTF_8.name());
            String decoded=URLDecoder.decode(encoded,StandardCharsets.UTF_8.name());


            System.out.println("Original: " + original);
            System.out.println("Encoded: " + encoded);
            System.out.println("Decoded: " + decoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}