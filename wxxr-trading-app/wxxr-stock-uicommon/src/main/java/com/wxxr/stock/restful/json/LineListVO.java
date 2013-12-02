package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.StockLineVO;

/**
 * 日K线/周k线/月k线
 * @author juyao
 *
 */
@XmlRootElement(name = "list")
public class LineListVO  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XStreamImplicit(itemFieldName="list")
    private List<StockLineVO> list = new ArrayList<StockLineVO>();

    /**
     * 
     */
    public LineListVO() {
        super();
        // TODO Auto-generated constructor stub
    }

   
    public List<StockLineVO> getList() {
        return list;
    }

    public void setList(List<StockLineVO> list) {
        this.list = list;
    }
    public void add(StockLineVO vo) {
        this.list.add(vo);
    }
}
