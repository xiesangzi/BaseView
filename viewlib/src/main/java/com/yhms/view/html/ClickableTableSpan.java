
package com.yhms.view.html;

import android.text.style.ClickableSpan;

public abstract class ClickableTableSpan extends ClickableSpan {
    protected String tableHtml;

    public abstract ClickableTableSpan newInstance();

    public void setTableHtml(String tableHtml) {
        this.tableHtml = tableHtml;
    }

    public String getTableHtml() {
        return tableHtml;
    }
}
