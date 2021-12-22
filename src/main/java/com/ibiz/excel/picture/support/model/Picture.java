package com.ibiz.excel.picture.support.model;

import com.ibiz.excel.picture.support.constants.WorkbookConstant;

/**
 * @auther 喻场
 * @date 2020/7/217:35
 */
public class Picture {
    private int rembed;	//图片引用(重要)
    private int fromCol;	//图片起始列	Cell.cellNumber
    private int fromRow;	//图片起始行	Row.rowNumber
    private int toCol;	//图片结束列	Cell.cellNumber + 1
    private int toRow;	//图片结束行	Row.rowNumber
    /**当前系统中图片的绝对路径*/
    private String picturePath;	//图片路径

    /**
     * 图片宽度
     */
    private int width = WorkbookConstant.PICTURE_WEIGHT;

    /**
     * 图片高度
     */
    private int height = WorkbookConstant.PICTURE_HEIGHT;

    public Picture(){
    }

    public Picture(int fromRow,int fromCol,String picturePath) {
        super();
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = fromRow;
        this.toCol = fromCol+1;
        this.picturePath = picturePath;
    }

    public Picture(int fromRow,int fromCol,  int width, String picturePath) {
	    super();
	    this.fromRow = fromRow;
	    this.fromCol = fromCol;
	    this.toRow = fromRow;
	    this.toCol = fromCol;
	    this.picturePath = picturePath;
	    this.width = width;
    }

    public Picture(int fromRow,int fromCol,  int width, int height,String picturePath) {
        super();
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = fromRow;
        this.toCol = fromCol;
        this.picturePath = picturePath;
        this.width = width;
        this.height = height;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getFromCol() {
        return fromCol;
    }

    public void setFromCol(int fromCol) {
        this.fromCol = fromCol;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToCol() {
        return toCol;
    }

    public void setToCol(int toCol) {
        this.toCol = toCol;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public int getRembed() {
        return rembed;
    }

    public void setRembed(int rembed) {
        this.rembed = rembed;
    }

    public int getWidth() {
        return width;
    }

    public Picture setWidth(int width) {
        return this;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
