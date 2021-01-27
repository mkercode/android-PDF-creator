package com.loopbreakr.firstpdf;

public class RowItem {
    private int imageResource;
    private String fileName;

    public RowItem(int img, String stringInput) {
        this.imageResource = img;
        this.fileName = stringInput;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getFileName() {
        return fileName;
    }
}
