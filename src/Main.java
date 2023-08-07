package src;

import javax.swing.*;
import java.io.IOException;

import static java.lang.System.exit;

public class Main {
    public static void main (String[] args) throws IOException {
        int opcionMenu = -1;
        String[] botones = {" 1. See cats", "2. See favourites","3. Exit"};

        do{
            // main menu
            String opcion = (String) JOptionPane.showInputDialog(
                    null,
                    "Java Cats",
                    "Main Menu",
                    JOptionPane.INFORMATION_MESSAGE,
                    null, botones,botones[0]
            );
            //validamos que opcion selecciona el usuario
            for(int i=0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    opcionMenu = i;
                }
            }

            if (opcionMenu == 0) {
                CatsService.fetchCats ();
            } else {exit(-1);}
        }while(true);

    }

}
