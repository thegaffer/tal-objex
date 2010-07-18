package org.tpspencer.tal.objexj.sample.beans.order;

import java.lang.String;

privileged aspect OrderBean_Roo_ToString {
    
    public String OrderBean.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account: ").append(getAccount()).append(", ");
        sb.append("Items: ").append(getItems() == null ? "null" : getItems().size()).append(", ");
        sb.append("Test: ").append(getTest());
        return sb.toString();
    }
    
}
