package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.StockBaseInfoVO;

@XmlRootElement(name = "baseInfos")
public class BaseInfoListVO  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XStreamImplicit(itemFieldName="baseInfo")
    private List<StockBaseInfoVO> list = new ArrayList<StockBaseInfoVO>();

    /**
     * 
     */
    public BaseInfoListVO() {
        super();
    }
    public List<StockBaseInfoVO> getList() {
        return list;
    }
    public void setList(List<StockBaseInfoVO> list) {
        this.list = list;
    }
    
    public void add(StockBaseInfoVO vo) {
        this.list.add(vo);
    }

}
