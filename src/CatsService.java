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

    public static void showFavouriteCats (String api_key) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/favourites").get()
                .addHeader("x-api-key", api_key).build();

        Response response = client.newCall(request).execute();
        System.out.println("Mostrando favoritos");

        String respJson = response.body().string();

        // Gson

        Gson gson = new Gson();

        FavCats[] catsArray = gson.fromJson(respJson, FavCats[].class);

        if (catsArray.length > 0) {
            for (FavCats cat : catsArray) {

                // Crear una variable para almacenar la imagen
                Image img;

                try {
                    // Crear un objeto URL a partir de la URL de la imagen en la respuesta JSON
                    URL url = new URL(cat.getUrl());

                    // Leer la imagen desde la URL
                    img = ImageIO.read(url);

                    // Crear un ImageIcon con la imagen obtenida
                    ImageIcon catBackground = new ImageIcon(img);

                    // Si el ancho de la imagen es mayor a 800, redimensionarla
                    if (catBackground.getIconWidth() > 800) {
                        Image background = catBackground.getImage();
                        Image newBackground = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                        catBackground = new ImageIcon(newBackground);
                    }

                    String menu = "Opciones:\n" + "1- Ver otra imagen\n" + "2- Eliminar favorito\n" + "3- Volver";

                    String[] botones = { "ver otra imagen", "eliminar de favoritos", "volver" };

                    String idCat = cat.getId();

                    String opcion = (String) JOptionPane.showInputDialog(null, menu, idCat,
                            JOptionPane.INFORMATION_MESSAGE, catBackground, botones, botones[0]);

                    int seleccion = -1;

                    for (int i = 0; i < botones.length; i++) {
                        if (opcion.equals(botones[i])) {
                            seleccion = i;
                        }
                    }

                    switch (seleccion) {
                        case 0:
                            showFavouriteCats (Secrets.API_KEY);
                            break;
                        case 1:
                            //delFavoriteCat(cat);
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("Opcion no valida");
                            break;
                    }
                } catch (Exception e) {
                    // Imprimir un mensaje de error si hay algún problema al formatear la imagen
                    System.out.println("Error al formatear imagen");
                }

            }

        }
    }

    }