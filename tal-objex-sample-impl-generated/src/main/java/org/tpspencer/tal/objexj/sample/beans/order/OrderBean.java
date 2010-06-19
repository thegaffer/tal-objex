package org.tpspencer.tal.objexj.sample.beans.order;

import java.util.List;

import org.tpspencer.tal.objexj.annotations.ObjexStateBean;

@ObjexStateBean(name="Order")
public class OrderBean {
    private static final long serialVersionUID = 1L;

    private long account;
    private String[] items;
    private String test;
    private List<String> listRefs;
    
    
}
