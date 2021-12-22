package com.svalero.aasandramultihilo;

import com.svalero.aasandramultihilo.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {
    public TextField tfUrl;
    public TabPane tpDownloads;
    public String directorio="descargas";

    private Map<String, DownloadController> allDownloads;

    public AppController() {
        allDownloads = new HashMap<>();
    }

    @FXML
    public void launchDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();

        launchDownload(urlText);
    }

    private void launchDownload(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("download.fxml"));

            DownloadController downloadController = new DownloadController(url);
            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1); //nombre que aparece en la pestaña
            tpDownloads.getTabs().add(new Tab(filename, downloadBox)); //se genera en la sección del panel tpDownloads

            allDownloads.put(url, downloadController);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    //Metodo para importar descargas desde un fichero elegido por el usuario y ponerlas en cola:
    @FXML
    public void imports() {

        //Se pide el fichero al usuario:
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(directorio));
        File file = fileChooser.showOpenDialog(null);

        try {
            List<String> urls = Files.readAllLines(file.toPath()); // Lee el fichero y carga linea a linea en un List
            urls.forEach(url -> launchDownload(url)); //Se recoge linea a linea del archivo y los lanza como descarga
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }
}
