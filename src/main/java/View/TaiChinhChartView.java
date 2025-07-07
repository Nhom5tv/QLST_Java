package View;

import model.TaiChinh;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;


public class TaiChinhChartView extends JFrame {

   public TaiChinhChartView(List<TaiChinh> list) {
    setTitle("Biểu đồ tài chính theo ngày");
    setSize(800, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    DefaultCategoryDataset dataset = createDataset(list);
    JFreeChart barChart = ChartFactory.createBarChart(
            "Tổng số tiền theo ngày (Thu & Chi)",
            "Ngày",
            "Số tiền (VND)",
            dataset
    );
    // Tùy chỉnh định dạng số tiền trên trục Y
CategoryPlot plot = (CategoryPlot) barChart.getPlot();
NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
rangeAxis.setNumberFormatOverride(new java.text.DecimalFormat("#,###")); // Hiển thị với dấu phẩy

    ChartPanel chartPanel = new ChartPanel(barChart);
    chartPanel.setPreferredSize(new Dimension(760, 520));
    setContentPane(chartPanel);
}


    private DefaultCategoryDataset createDataset(List<TaiChinh> list) {
    // Map ngày -> loại giao dịch -> tổng tiền
    Map<String, Map<String, BigDecimal>> totalByDateAndType = new TreeMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    for (TaiChinh tc : list) {
        String ngay = sdf.format(tc.getNgay());
        String loai = tc.getLoaiGiaoDich(); // "Thu" hoặc "Chi"
        BigDecimal soTien = tc.getSoTien();

        totalByDateAndType.putIfAbsent(ngay, new TreeMap<>());
        Map<String, BigDecimal> byType = totalByDateAndType.get(ngay);
        byType.put(loai, byType.getOrDefault(loai, BigDecimal.ZERO).add(soTien));
    }

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (Map.Entry<String, Map<String, BigDecimal>> entry : totalByDateAndType.entrySet()) {
        String ngay = entry.getKey();
        Map<String, BigDecimal> byType = entry.getValue();

        // Thêm giá trị "Thu"
        BigDecimal thu = byType.getOrDefault("Thu", BigDecimal.ZERO);
        dataset.addValue(thu, "Thu", ngay);

        // Thêm giá trị "Chi"
        BigDecimal chi = byType.getOrDefault("Chi", BigDecimal.ZERO);
        dataset.addValue(chi, "Chi", ngay);
    }

    return dataset;
}

}
