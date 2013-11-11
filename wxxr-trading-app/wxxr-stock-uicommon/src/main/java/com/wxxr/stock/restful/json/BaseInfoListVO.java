package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.StockBaseInfoVO;

@XmlRootElement(name = "list")
public class BaseInfoListVO  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XmlElement(name="list")
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
