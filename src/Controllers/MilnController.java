package Controllers;

import Exceptions.InvalidDataException;
import MilnMethod.MilnMethod;
import Utils.EquationHandler;
import Utils.Point;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MilnController {

    @FXML
    private Button solveBut;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextArea answerTextArea;

    @FXML
    private TableView<Point> pointsTable;

    @FXML
    private TableColumn<?, ?> xCol;

    @FXML
    private TableColumn<?, ?> yCol;

    @FXML
    private Button test1;

    @FXML
    private Button test2;

    @FXML
    private Button test3;

    @FXML
    private TextArea f_tf;

    @FXML
    private TextArea x0;

    @FXML
    private TextArea y0;

    @FXML
    private TextArea e;

    @FXML
    private TextArea rightBorder;

    ObservableList<Point> points = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        pointsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        xCol.setStyle("-fx-alignment: CENTER;");
        yCol.setStyle("-fx-alignment: CENTER;");

        answerTextArea.setEditable(false);

        f_tf.setWrapText(true);
        x0.setWrapText(true);
        y0.setWrapText(true);
        e.setWrapText(true);
        rightBorder.setWrapText(true);

        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("y"));

        points.addListener(new ListChangeListener<Point>() {
            @Override
            public void onChanged(Change<? extends Point> c) {
                pointsTable.setItems(points);
            }
        });

        solveBut.setOnAction(event -> {
            String eps = e.getText();
            String x = x0.getText();
            String y = y0.getText();
            String f = f_tf.getText();
            String border = rightBorder.getText();

            EquationHandler handler = null;
            if (!eps.equals("") && !f.equals("") && !x.equals("") && !y.equals("") && !border.equals("")) {
                try {
                    handler = new EquationHandler(f, eps, x, y, border);
                } catch (InvalidDataException e) {
                    showAlert("Данные введены некорректно", e.getMessage());
                }
            } else {
                showAlert("Поля не заполнены", "Заполните все необходимые поля.");
            }

            if (handler != null) {
                points.clear();
                MilnMethod solver = handler.getMilnSolver();
                List<Double> yList = solver.getYList();
                List<Double> xList = solver.getXList();

                List<BigDecimal> x1 = new ArrayList<>();
                List<BigDecimal> y1 = new ArrayList<>();

                for (int i = 0; i < xList.size(); i++) {
                    BigDecimal t = new BigDecimal(xList.get(i));
                    BigDecimal k = new BigDecimal(yList.get(i));
                    points.add(new Point(t.setScale(4, RoundingMode.HALF_EVEN), k.setScale(4, RoundingMode.HALF_EVEN)));
                }
                showAnswer(solver.getH(), solver.getLeftBorder(), solver.getRightBorder(), solver.getN());
                drawPlot(xList,yList);
            }

        });

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAnswer(Double h, Double x0, Double xn, int n) {
        answerTextArea.setWrapText(true);
        String ans = "Шаг: " + h + "\n" + "\n" +
                "Количество найденных точек: " + n + "\n" + "\n" +
                "Интервал: [" + x0 + ", " + xn + "]\n";
        answerTextArea.setText(ans);
    }

    private void drawPlot(List<Double> xList, List<Double> yList) {
        if (!lineChart.getData().isEmpty()) {
            while (!lineChart.getData().isEmpty()) {
                lineChart.getData().remove(/*(lineChart.getData().size()-1)*/0);
            }

        }
        XYChart.Series series0 = new XYChart.Series();
        ObservableList<XYChart.Data> datas0 = FXCollections.observableArrayList();
        for (int i = 0; i < xList.size(); i++) {
            datas0.add(new XYChart.Data(xList.get(i), yList.get(i)));
        }
        series0.setData(datas0);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);
        lineChart.getData().add(series0);
    }


}
