package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;

/**
 * 行情数据
 * @author juyao
 *
 */
@XmlRootElement(name = "list")
public class QuotationListVO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "list")
    private List<StockQuotationVO> list;

    /**
     * 
     */
    public QuotationListVO() {
        super();
    }

    
    public List<StockQuotationVO> getList() {
        return list;
    }

    public void setList(List<StockQuotationVO> list) {
        this.list = list;
    }
    public void add(StockQuotationVO vo) {
        this.list.add(vo);
    }
    

}
