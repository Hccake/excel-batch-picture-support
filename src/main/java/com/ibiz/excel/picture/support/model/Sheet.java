package com.ibiz.excel.picture.support.model;

import com.ibiz.excel.picture.support.constants.WorkbookConstant;
import com.ibiz.excel.picture.support.event.CloseEvent;
import com.ibiz.excel.picture.support.event.InitEvent;
import com.ibiz.excel.picture.support.factory.EventFactory;
import com.ibiz.excel.picture.support.listener.CloseListener;
import com.ibiz.excel.picture.support.listener.FlushListener;
import com.ibiz.excel.picture.support.SheetContext;
import com.ibiz.excel.picture.support.annotation.ExportModel;
import com.ibiz.excel.picture.support.constants.Alias;
import com.ibiz.excel.picture.support.event.FlushEvent;
import com.ibiz.excel.picture.support.factory.ListenerFactory;
import com.ibiz.excel.picture.support.listener.ContentListener;
import com.ibiz.excel.picture.support.listener.InitListener;
import com.ibiz.excel.picture.support.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * 工作表
 * @auther 喻场
 * @date 2020/7/217:36
 */
public class Sheet {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //sheet上下文
    //包括组件文件流,文件模版,
    private void init() {
        SHEET_HANDLER.init();
    }

    private SheetContext sheetContext;
    private int flushSize;
    private Workbook workbook;
    private String sheetName;   //默认sheet1
    private int drawingSequence = 1;	//图片id序号	对应drawing1.xml.rels Id后的数字
    private int sharedStringSequence = -1;	//单元格值序号	sharedString.xml对应<si>标签序号
    private boolean autoMergeCell = Boolean.TRUE;	//是否自动合并单元格
    private boolean hasFlush;   //是否已经执行过flush
    private boolean hasWriteHead; //是否已经写标题
    private final List<String> closeAlias = Arrays.asList(Alias.APP, Alias.WORKBOOK_XML);
    private final SheetHandler SHEET_HANDLER = new SheetHandler();
    private Sheet(int flushSize, String sheetName, Workbook workbook) {
        this.flushSize = flushSize;
        this.sheetName = sheetName;
        this.workbook = workbook;
        init();
    }

    private int writeRow = -1;
    /**行*/
    private List<Row> rows = new ArrayList<>();
    //图片
    private List<Picture> pictures = new ArrayList<>();
    /**需要合并单元格*/
    private LinkedList<MergeCell> mergeCells = new LinkedList<>();
    /**已这列值为标准,对colCells进行合并
     * 0代表A0列 */
    private int mergeCellNumber = 0;	//根据第几列值合并
    /**需要合并单元格的列
     * 对应excel column {"A"}  A列行相同的值要合并*/
    private Set<String> colCells = new HashSet<>();

    /**
     * 单元格辅助类
     */
    private List<ColumnHelper> columnHelpers = new ArrayList<>();

    /**
     * 默认已有fill样式
     * 与对应{@link com.ibiz.excel.picture.support.module.Styles}
     */
    private int fillId = 33;

    /**
     * 默认已有cellStyles样式
     */
    private int s = 4;

    public int getFillId() {
        return fillId;
    }

    public void setFillId(int fillId) {
        this.fillId = fillId;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 创建行,单元格会根据t的属性自动填充
     * @param t 列属性 只会读取com.ibiz.excelx.annotation.Model 注解的属性
     * @param <T>
     * @return Row
     */
    public <T> Row createRow(T t) {
        return SHEET_HANDLER.createRow(t);
    }

    /**
     * 对集合列表进行创建行,单元格会根据t的属性自动填充
     * @param collection 列属性集合 只会读取com.ibiz.excelx.annotation.Model 注解的属性
     * @param <T>
     */
    public <T> void createRow(Collection<T> collection) {
        collection.forEach(this::createRow);
    }

    public Row createRow(int rowNumber) {
        return SHEET_HANDLER.createRow(rowNumber);
    }

    /**
     * 刷新数据到流
     */
    public void flush() {
        sheetContext.getEvents().stream().filter(e -> e instanceof FlushEvent).forEach(e -> e.onEvent(this));
        hasFlush = true;
        clear();
    }

    public boolean hasFlush() {
        return hasFlush;
    }

    /**
     * 关闭
     */
    public void close() {
        flush();
        sheetContext.getEvents().stream().filter(e -> e instanceof CloseEvent).forEach(e -> e.onEvent(this));
    }

    //保证rowList中有一行数据,用作下一行与上一行对比是否合并单元格
    public void clear(){
        SHEET_HANDLER.clear();
    }

    public SheetContext getSheetContext() {
        return sheetContext;
    }

    public static Sheet getInstance(int flushSize, Workbook workbook) {
        return new Sheet(flushSize, "sheet1", workbook);
    }

    public static Sheet getInstance(int flushSize, String sheetName, Workbook workbook) {
        return new Sheet(flushSize, sheetName, workbook);
    }
    public boolean isAutoMergeCell() {
        return autoMergeCell;
    }

    public void setAutoMergeCell(boolean autoMergeCell) {
        this.autoMergeCell = autoMergeCell;
    }

    public LinkedList<MergeCell> getMergeCells() {
        return mergeCells;
    }

    public int getMergeCellNumber() {
        return mergeCellNumber;
    }

    public void setMergeCellNumber(int mergeCellNumber) {
        this.mergeCellNumber = mergeCellNumber;
    }

    public int getSharedStringSequence() {
        return sharedStringSequence;
    }

    public void setSharedStringSequence(int sharedStringSequence) {
        this.sharedStringSequence = sharedStringSequence;
    }

    public Set<String> getColCells() {
        return colCells;
    }

    public void setColCells(Set<String> colCells) {
        this.colCells = colCells;
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getDrawingSequence() {
        return drawingSequence;
    }

    public void setDrawingSequence(int drawingSequence) {
        this.drawingSequence = drawingSequence;
    }
    /**总行数*/
    public int getRowCount() {
        return writeRow + 1;
    }

    class SheetHandler {
        void init() {
            sheetContext = SheetContext.getInstance(Sheet.this);
            ContentListener init = ListenerFactory.getInstance(InitListener.class);
            ContentListener flush = ListenerFactory.getInstance(FlushListener.class);
            ContentListener close = ListenerFactory.getInstance(CloseListener.class);
            sheetContext.getRepositoryHolder().forEach((alias, rep) -> {
                if (!closeAlias.contains(alias)) {
                    //app.xml workbook.xml最后写,考虑getSheetName
                    init.addRepository(rep);
                    flush.addRepository(rep);
                }
                close.addRepository(rep);
            });
            sheetContext.getEvents().add(EventFactory.getInstance(InitEvent.class).registry(init));
            sheetContext.getEvents().add(EventFactory.getInstance(FlushEvent.class).registry(flush));
            sheetContext.getEvents().add(EventFactory.getInstance(CloseEvent.class).registry(close));
            //创建组件文件
            sheetContext.getEvents().stream().filter(e -> e instanceof InitEvent).forEach(e -> e.onEvent(Sheet.this));
        }
        <T> Row create(T t, final boolean isHead) {
            Row row = createRow(++writeRow);
            logger.info("create the "  + writeRow + " row");
            Arrays.stream(t.getClass().getDeclaredFields()).filter(f -> null != f.getAnnotation(ExportModel.class))
                    .sorted(Comparator.comparing(c -> c.getAnnotation(ExportModel.class).sort())).forEach(f -> {
                f.setAccessible(true);
                ExportModel model = f.getAnnotation(ExportModel.class);
                int sort = model.sort();
                boolean isPicture = model.isPicture();
                boolean mergeMaster = model.mergeMaster();
                boolean merge = model.merge();
                String title = model.title();
                Cell cell = new Cell(row.getRowNumber(), sort);
                // 合并 是标题 并且 是要合并的基准列，将元素放在基准列中
                if (isHead && mergeMaster) {
                    mergeCellNumber = sort;
                    autoMergeCell = true;
                    colCells.add(cell.getCol());
                }
                // 合并 是标题 并且 是要合并的列
                if (isHead && merge) {
                    colCells.add(cell.getCol());
                }

                String value = null;
                try {
                    value = null == f.get(t) ? "" : "" + f.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                // 是标题，则将title设置为单元格的值
                if (isHead) {
                    cell.setValue(title);
                } else {
                    cell.setValue(value);
                }
                // 不是标题 并且 是图片 并且 值不为空
                if (!isHead && isPicture && StringUtils.isNotBlank(value)) {
                    //有图片的行,行高设置为100
                    row.setHeight(WorkbookConstant.PICTURE_ROW_HEIGHT);
                    //增加图片
                    pictures.add(new Picture(row.getRowNumber(), cell.getCellNumber(), value));
                }
                // 将组装好的元素项，放到该行的元素项集合中
                row.getCells().add(cell);
            });
            hasWriteHead = writeRow > -1;
            return row;
        }
        <T> Row createRow(T t) {
            if (!hasWriteHead) {
                create(t, true);
            }
            return create(t, false);
        }
        Row createRow(int rowNumber) {
            writeRow = rowNumber;
            if(rows.size() > flushSize && flushSize != -1){
                flush();
            }
            Row row = new Row(rowNumber);
            rows.add(row);
            return row;
        }
        void clear(){
            Row row = null;
            if(rows.size()>1){
                row = rows.get(rows.size()-1);
            }
            List<Row> list = new ArrayList<>();
            list.add(row);
            rows.clear();
            rows.addAll(list);
            pictures.clear();
        }
    }

    /**
     * 设置单元格宽度
     * @param columnIndex 坐标
     * @param width 宽度
     */
    public Sheet setColumnWidth(int columnIndex, int width){
        return this.addColumnHelper(new ColumnHelper(columnIndex, width));
    }


    public Sheet addColumnHelper(ColumnHelper columnHelper){
        return this.addAllColumnHelper(Collections.singletonList(columnHelper));
    }

    public Sheet addAllColumnHelper(List<ColumnHelper> columnHelpers){
        this.columnHelpers.addAll(columnHelpers);
        return this;
    }

    public List<ColumnHelper> getColumnHelpers() {
        return columnHelpers;
    }
}
