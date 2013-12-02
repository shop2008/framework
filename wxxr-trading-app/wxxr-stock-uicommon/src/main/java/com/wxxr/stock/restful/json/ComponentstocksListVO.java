package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.ComponentStocksVO;

/**
 * 成分股
 * @author juyao
 *
 */
@XmlRootElement(name = "list")
public class ComponentstocksListVO  implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @XStreamImplicit(itemFieldName="list")
    private List<ComponentStocksVO> list =  new  ArrayList<ComponentStocksVO>();
    

    /**
     * 
     */
    public ComponentstocksListVO() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public List<ComponentStocksVO> getList() {
        return list;
    }

    public void setList(List<ComponentStocksVO> vos) {
        this.list = vos;
    }
    public void add(ComponentStocksVO vo) {
        this.list.add(vo);
    }

}
