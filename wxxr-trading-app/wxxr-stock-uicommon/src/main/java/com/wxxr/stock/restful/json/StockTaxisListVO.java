package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;

/**
 * 涨跌排行
 * @author juyao
 *
 */
@XmlRootElement(name = "list")
public class StockTaxisListVO  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XStreamImplicit(itemFieldName="list")
    private List<StockTaxisVO> list = new ArrayList<StockTaxisVO>();

    /**
     * 
     */
    public StockTaxisListVO() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public List<StockTaxisVO> getList() {
        return list;
    }

    public void setList(List<StockTaxisVO> vos) {
        this.list = vos;
    }
    public void add(StockTaxisVO vo) {
        this.list.add(vo) ;
    }
}
