package View;

import model.TaiChinh;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class TaiChinhChartView extends JFrame {
    private List<TaiChinh> allData;
    private JPanel chartContainer;

    public TaiChinhChartView(List<TaiChinh> list) {
        this.allData = list;

        setTitle("Biểu đồ tài chính theo tháng");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Lọc ra các tháng có trong dữ liệu
        Set<String> uniqueMonths = list.stream()
                .map(tc -> new SimpleDateFormat("yyyy-MM").format(tc.getNgay()))
                .collect(Collectors.toCollection(TreeSet::new));

        JComboBox<String> comboThang = new JComboBox<>(uniqueMonths.toArray(new String[0]));
        comboThang.addActionListener(e -> {
            String selectedMonth = (String) comboThang.getSelectedItem();
            updateChart(selectedMonth);
        });

        chartContainer = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());
        add(comboThang, BorderLayout.NORTH);
        add(chartContainer, BorderLayout.CENTER);

        // Hiển thị biểu đồ ban đầu (tháng đầu tiên)
        if (!uniqueMonths.isEmpty()) {
            comboThang.setSelectedIndex(0); // Gọi luôn updateChart
        }
    }

    private void updateChart(String month) {
        List<TaiChinh> filtered = allData.stream()
                .filter(tc -> new SimpleDateFormat("yyyy-MM").format(tc.getNgay()).equals(month))
                .collect(Collectors.toList());

        DefaultCategoryDataset dataset = createDataset(filtered);

        JFreeChart barChart = ChartFactory.createBarChart(
                "Tổng số tiền theo ngày (" + month + ")",
                "Ngày",
                "Số tiền (VND)",
                dataset
        );

        CategoryPlot plot = (CategoryPlot) barChart.getPlot();

        // Xoay nhãn ngày để dễ nhìn
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0)
        );

        // Hiển thị số có dấu phẩy
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(new java.text.DecimalFormat("#,###"));

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartContainer.removeAll();
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private DefaultCategoryDataset createDataset(List<TaiChinh> list) {
        Map<String, Map<String, BigDecimal>> totalByDateAndType = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (TaiChinh tc : list) {
            String ngay = sdf.format(tc.getNgay());
            String loai = tc.getLoaiGiaoDich();
            BigDecimal soTien = tc.getSoTien();

            totalByDateAndType.putIfAbsent(ngay, new TreeMap<>());
            Map<String, BigDecimal> byType = totalByDateAndType.get(ngay);
            byType.put(loai, byType.getOrDefault(loai, BigDecimal.ZERO).add(soTien));
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Map<String, BigDecimal>> entry : totalByDateAndType.entrySet()) {
            String ngay = entry.getKey();
            Map<String, BigDecimal> byType = entry.getValue();

            dataset.addValue(byType.getOrDefault("Thu", BigDecimal.ZERO), "Thu", ngay);
            dataset.addValue(byType.getOrDefault("Chi", BigDecimal.ZERO), "Chi", ngay);
        }

        return dataset;
    }
}
