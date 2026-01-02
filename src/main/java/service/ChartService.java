package service;

import model.stockPrice;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.util.List;

public class ChartService {

    public File generatePriceChart(String ticker, List<stockPrice> prices) throws Exception {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // dal più vecchio al più recente
        for (int i = prices.size() - 1; i >= 0; i--) {
            stockPrice p = prices.get(i);
            dataset.addValue(p.close, "Close", p.date);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Prezzo azione " + ticker,
                "Data",
                "Prezzo",
                dataset
        );

        File file = File.createTempFile("stock_" + ticker, ".png");
        ChartUtils.saveChartAsPNG(file, chart, 800, 600);

        return file;
    }
}
