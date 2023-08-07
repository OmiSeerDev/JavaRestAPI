package src;

import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

import static java.lang.System.exit;

public class CatsService {
    public static void fetchCats () throws IOException {
        OkHttpClient client = new OkHttpClient ();

        Request request = new Request.Builder ()
                .url ("https://api.thecatapi.com/v1/images/search")
                .get ()
                .build ();
        Response response = client.newCall (request)
                .execute ();

        assert response.body () != null;
        String jsonRes = response.body ()
                .string ();
        jsonRes = jsonRes.substring (1, jsonRes.length () - 1);

        Gson gson = new Gson ();
        Cats cat = gson.fromJson (jsonRes, Cats.class);

        Image image;
        try {
            URL url = new URL (cat.getUrl ());
            image = ImageIO.read (url);

            ImageIcon catIcon = new ImageIcon (image);
            if(catIcon.getIconWidth() > 800 && catIcon.getIconHeight () > 600){
                //redimensionamos
                Image background = catIcon.getImage();
                Image resized = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                catIcon = new ImageIcon(resized);
            }

            String menu = """
                    Opciones
                    1- Watch another image
                    2- Favorite
                    3 - Back
                    """;
            String[] buttons = {"Watch another", "Fav", "Back"};
            String cat_id = cat.getId ();
            String catOption = (String) JOptionPane.showInputDialog (
                    null,
                    menu,
                    cat_id,
                    JOptionPane.INFORMATION_MESSAGE,
                    catIcon,
                    buttons,
                    buttons[0]
            );
            int selection = -1;
            for (int i = 0; i < buttons.length; i++) {
                if (catOption.equals (buttons[i])) {
                    selection = i;
                }
            }
            switch (selection) {
                case 0 -> fetchCats ();
                case 1 -> favCats (cat);
                case 2 -> exit (-1);
            }
        } catch (IOException ex) {
            throw new RuntimeException (ex);
        }
    }

    public static void favCats (Cats cat) {
        try {
            OkHttpClient client = new OkHttpClient ();
            MediaType mediaType = MediaType.parse ("application/json");
            RequestBody body = RequestBody.create (
                    mediaType, "{\"image_id\":\"" + cat.getId () + "\"}");
            Request request = new Request.Builder ()
                    .url ("https://api.thecatapi.com/v1/favourites")
                    .method ("POST", body)
                    .addHeader ("x-api-key", cat.apiKey)
                    .addHeader ("Content-Type", "application/json")
                    .build ();
            Response response = client.newCall (request)
                    .execute ();
            assert response.body () != null;
            System.out.println (response.body ().string ());
        } catch (IOException ex) {
            throw new RuntimeException (ex);
        }
    }
}
