package FindRoadSignB20Application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static java.lang.Math.abs;
import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;


public class Controller implements Initializable {

    private File[] tableFiles;
    private File actuallImageOutputFile;
    private BufferedImage actuallImage;
    private int counter = 0;
    private Image image;

    @FXML
    private AnchorPane anchorPaneLeft;
    @FXML
    private AnchorPane anchorPaneRight;
    @FXML
    private Button previousImage;
    @FXML
    private Button openPathButton;
    @FXML
    private Button nextImage;
    @FXML
    private Button recognize;
    @FXML
    private ImageView imageRecognizedImageView;
    @FXML
    private TextField pathField;
    @FXML
    private Label nameSignLabel;
    @FXML
    private Label result;
    @FXML
    private ImageView actuallImageView;

    public void openPathOnAction() {
        ImputFileFilter newChooserFile = new ImputFileFilter();
        JFileChooser chooserFile;
        chooserFile = newChooserFile.userFilterChooser();
        int choice = chooserFile.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            for (File file : chooserFile.getSelectedFiles()) {
                if (file != null && (file.getName().toLowerCase().endsWith(".jpeg") || file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png"))) {
                    tableFiles = chooserFile.getSelectedFiles();
                    pathField.setText(tableFiles[0].getAbsolutePath());
                } else {
                    assert file != null;
                    JOptionPane.showMessageDialog(new JFrame(), "Not supported picture format: " + file.getName() + "\nThis file will be skipped.", "Error Picture Format", JOptionPane.ERROR_MESSAGE);
                }
            }
            inputData();
        }
    }

    private void inputData() {
        try {
            actuallImage = ImageIO.read(tableFiles[0]);
            actuallImageOutputFile = new File("Test\\resources\\actuallImage.jpg");
            ImageIO.write(actuallImage, "jpg", actuallImageOutputFile);
            image = SwingFXUtils.toFXImage(actuallImage, null);
            actuallImageView.setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void previousImageOnAction() {
        if (tableFiles != null) {
            try {
                if (counter == 0) {
                    actuallImage = ImageIO.read(tableFiles[tableFiles.length - 1]);
                    counter--;
                } else if (-counter == tableFiles.length - 1) {
                    actuallImage = ImageIO.read(tableFiles[0]);
                    counter = 0;
                } else if (counter < 0 && !(-counter == tableFiles.length - 1)) {
                    actuallImage = ImageIO.read(tableFiles[tableFiles.length + (counter - 1)]);
                    counter--;
                } else {
                    actuallImage = ImageIO.read(tableFiles[counter - 1]);
                    counter--;
                }
                actuallImageOutputFile = new File("Test\\resources\\actuallImage.jpg");
                ImageIO.write(actuallImage, "jpg", actuallImageOutputFile);
                pathField.setText(tableFiles[abs(counter)].getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(new JFrame(), "Only one image has been selected.", "Error Only One Input Image", JOptionPane.INFORMATION_MESSAGE);
            }
            image = SwingFXUtils.toFXImage(actuallImage, null);
            actuallImageView.setImage(image);
            System.out.println(counter);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "No photo has been added to recognize the road sign.", "Error No Input Image", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void nextImageOnAction() {
        if (tableFiles != null) {
            try {
                if (counter == 0) {
                    actuallImage = ImageIO.read(tableFiles[counter + 1]);
                    counter++;
                } else if (counter == -1) {
                    actuallImage = ImageIO.read(tableFiles[0]);
                    counter = 0;
                } else if (counter == tableFiles.length - 1) {
                    actuallImage = ImageIO.read(tableFiles[0]);
                    counter = 0;
                } else if (counter < -1) {
                    actuallImage = ImageIO.read(tableFiles[tableFiles.length + counter + 1]);
                    System.out.println(tableFiles.length + counter + 1);
                    counter++;
                } else {
                    actuallImage = ImageIO.read(tableFiles[counter + 1]);
                    counter++;
                }
                actuallImageOutputFile = new File("Test\\resources\\actuallImage.jpg");
                ImageIO.write(actuallImage, "jpg", actuallImageOutputFile);
                pathField.setText(tableFiles[abs(counter)].getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(new JFrame(), "Only one image has been selected.", "Error Only One Input Image", JOptionPane.INFORMATION_MESSAGE);
            }
            image = SwingFXUtils.toFXImage(actuallImage, null);
            actuallImageView.setImage(image);
            System.out.println(counter);
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "No photo has been added to recognize the road sign.", "Error No Input Image", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void recognizeOnAction() {
        try {
            Mat oryginalImage = Imgcodecs.imread(actuallImageOutputFile.getAbsolutePath());
            findSignRedImage(operationOnPixelsImage(setMask(oryginalImage)));
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(new JFrame(), "No photo has been added to recognize the road sign.", "Error No Input Image", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Mat setMask(Mat image) {
        Mat imageHSV = new Mat();
        Mat binaryRed1Mask = new Mat();
        Mat binaryRed2Mask = new Mat();
        Mat binaryRed3Mask = new Mat();
        Mat binarySumRedMask = new Mat();

        Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2HSV);
        inRange(imageHSV, new Scalar(0, 150, 120), new Scalar(20, 255, 255), binaryRed1Mask);
        inRange(imageHSV, new Scalar(160, 80, 0), new Scalar(200, 255, 255), binaryRed2Mask);
        inRange(imageHSV, new Scalar(180, 90, 40), new Scalar(255, 255, 255), binaryRed3Mask);
        Core.add(binaryRed1Mask, binaryRed2Mask, binarySumRedMask);
        Core.add(binarySumRedMask, binaryRed3Mask, binarySumRedMask);
        Imgproc.threshold(binarySumRedMask, binarySumRedMask, 0, 255, THRESH_OTSU);
        return binarySumRedMask;
    }

    public Mat operationOnPixelsImage(Mat image) {
        erode(image, image, new Mat(), new Point(-1, -1), 1);
        dilate(image, image, new Mat(), new Point(-1, 1), 2);
        return image;
    }

    public void findSignRedImage(Mat image) throws Exception {
        Mat oryginalImage = Imgcodecs.imread(actuallImageOutputFile.getAbsolutePath());
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        int largestArea = 0;
        int largestContourIndex;
        Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            MatOfPoint2f approx = new MatOfPoint2f();
            double arcLen = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approx, arcLen, true);
            long count = (int) approx.total();
            double contourArea = Imgproc.contourArea(contours.get(i));
            if (contourArea > largestArea) {
                largestArea = (int) contourArea;
                //przechowuje index największego konturu
                largestContourIndex = i;
                if (count == 8 && contourArea > 200) {
                    Imgproc.drawContours(image, contours, largestContourIndex, new Scalar(0, 255, 0), 5);
                    Rect rectOctagon = Imgproc.boundingRect(contours.get(i));
                    Imgproc.rectangle(image, new Point(rectOctagon.x, rectOctagon.y), new Point(rectOctagon.x + rectOctagon.width, rectOctagon.y + rectOctagon.height), new Scalar(255, 0, 0, 255), 3);
                    Rect bbox = new Rect(rectOctagon.x , rectOctagon.y, rectOctagon.width, rectOctagon.height);
                    Mat croped_image = new Mat(oryginalImage, bbox);
                    Imgproc.resize(croped_image, croped_image, new Size(200, 200));
                    MatOfByte matOfByte = new MatOfByte();
                    Imgcodecs.imencode(".jpg", croped_image, matOfByte);
                    byte[] byteArray = matOfByte.toArray();
                    InputStream in = new ByteArrayInputStream(byteArray);
                    BufferedImage bufImage = ImageIO.read(in);
                    WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
                    imageRecognizedImageView.setImage(writableImage);
                    nameSignLabel.setText("Recognized road sign B-20");
                    result.setVisible(true);
                    imageRecognizedImageView.setVisible(true);
                    nameSignLabel.setVisible(true);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actuallImageView.setPreserveRatio(false);
        imageRecognizedImageView.setPreserveRatio(false);
    }
}


