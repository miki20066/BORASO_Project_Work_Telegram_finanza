package model;

import java.io.File;

public class StockResult {

    public final String message;
    public final File chart;

    public StockResult(String message, File chart) {
        this.message = message;
        this.chart = chart;
    }
}
