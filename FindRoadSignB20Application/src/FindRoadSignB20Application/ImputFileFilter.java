package FindRoadSignB20Application;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImputFileFilter {

    //zmienna informująca o rozszerzeniach plików zdjęc, które są obsługiwane przez program
    private final String[] EXPANDERS = new String[]{".jpeg", ".jpg", ".png"};

    public JFileChooser userFilterChooser() {
        //JFileChooser zapewnia użytkownikowi prosty mechanizm wyboru pliku. Aby uzyskać informacje na temat używania JFileChooser, zobacz sekcję Jak korzystać z wyboru plików w samouczku dotyczącym języka Java .
        //Poniższy kod uruchamia selektor plików dla katalogu domowego użytkownika.
        JFileChooser chooserFile = new JFileChooser();
        chooserFile.setCurrentDirectory(new File("Test\\resources"));

        //włączenie opcji zaznaczenia wielu plików
        chooserFile.setMultiSelectionEnabled(true);

        //włączenie widoczności naszych filtrów na liście filtrów do wyboru
        chooserFile.setAcceptAllFileFilterUsed(true);

        //Poniższy fragment to stworzony własny filtr dla plików obrazów, pozwalający wyświetlić w oknie wyboru jedynie obrazy o rozszeżeniach zawartych w polu EXPANDERS z pominięciem katalogów
        chooserFile.setFileFilter(new Filter("Images", EXPANDERS));

        //Poniższy fragment to filtr pozwalający wyświetlić w oknie wyboru jedynie katalogi
        chooserFile.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.isDirectory();
            }

            @Override
            public String getDescription()
            {
                return "Directories";
            }
        });
        return chooserFile;
    }

    /**
     * Clasa <code> Filter </code> pozwalająca tworzyć filtry plików dla użytkownika. Clasa posiada dwa pola w których przechwuje nazwę filtra oraz tablicę string zawierającą oczekiwane rozszeżenia plików.
     */
    private class Filter extends FileFilter {
        private String description;
        private String[] expand;

        public Filter (String description, String[] expand) {
            this.description = description;
            this.expand = expand;
        }

        /**
         * Metoda <code> accept </code> przyjmująca jeden parametr wejściowy - obraz. Sprawdza, czy nazwa argumentu kończy się wymaganym rozszerzeniem EXPANDERS
         * .jpeg, .jpg lub .png oraz dodatkowo sprawdza, czy argument nie jest przypadkiem folderem. Zwraca prawdę lub fałsz.
         */
        @Override
        public boolean accept(File picture) {
            for (int i = 0; i < this.expand.length; i++){
                if (picture.getName().toLowerCase().endsWith(this.expand[i]) && !picture.isDirectory())
                    return true;
            }
            return false;
        }

        // getter zwracający nazwę filtra
        @Override
        public String getDescription() {
            return description;
        }
    }
}
