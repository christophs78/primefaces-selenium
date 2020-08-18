package org.primefaces.extensions.selenium.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.primefaces.extensions.selenium.PrimeExpectedConditions;
import org.primefaces.extensions.selenium.PrimeSelenium;
import org.primefaces.extensions.selenium.component.base.AbstractPageableData;
import org.primefaces.extensions.selenium.component.model.data.Page;
import org.primefaces.extensions.selenium.component.model.data.Paginator;
import org.primefaces.extensions.selenium.component.model.datatable.Cell;
import org.primefaces.extensions.selenium.component.model.datatable.Header;
import org.primefaces.extensions.selenium.component.model.datatable.HeaderCell;
import org.primefaces.extensions.selenium.component.model.datatable.Row;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DataTable extends AbstractPageableData {

    private Header header;

    @Override
    public List<WebElement> getRowsWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
    }

    public List<Row> getRows() {
        //rows change after pagination, filter, sort, ... --> do not cache
        return getRowsWebElement().stream().map(rowElt -> {
            List<Cell> cells = rowElt.findElements(By.tagName("td")).stream().map(cellElt -> new Cell(cellElt)).collect(Collectors.toList());
            return new Row(rowElt, cells);
        }).collect(Collectors.toList());
    }

    public Row getRow(int index) {
        return getRows().get(index);
    }

    public WebElement getHeaderWebElement() {
        return findElement(By.tagName("table")).findElement(By.tagName("thead"));
    }

    @Override
    public Paginator getPaginator() {
        //paginator may change after each pagination, filter, sort, ... -> do not cache
        return new Paginator(getPaginatorWebElement());
    }

    public Header getHeader() {
        if (header == null) {
            //header should be stable -> we can cache it
            List<HeaderCell> cells = getHeaderWebElement().findElements(By.tagName("th")).stream()
                    .map(cellElt -> new HeaderCell(cellElt))
                    .collect(Collectors.toList());
            header = new Header(getHeaderWebElement(), cells);
        }

        return header;
    }

    public void selectPage(Page page) {
        page.getWebElement().click();
        PrimeSelenium.waitGui().until(PrimeExpectedConditions.jQueryNotActive());
    }

    public void selectPage(int number) {
        for (Page page : getPaginator().getPages()) {
            if (page.getNumber() == number) {
                selectPage(page);
            }
        }
    }

    public void sort(String headerText) {
        for (Cell cell : getHeader().getCells()) {
            if (cell.getText().equals(headerText)) {
                cell.getWebElement().findElement(By.className("ui-column-title")).click();
                PrimeSelenium.waitGui().until(PrimeExpectedConditions.jQueryNotActive());
            }
        }
    }
}
