package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.PlateTaxisVO;

/**
 * 板块排行
 * @author juyao
 *
 */
@XmlRootElement(name = "list")
public class PlateTaxisListVO  implements Serializable{
	@XmlElement(name="list")
	private List<PlateTaxisVO> list = new ArrayList<PlateTaxisVO>();

    /**
     * 
     */
    public PlateTaxisListVO() {
        super();
    }

    
    public List<PlateTaxisVO> getList() {
        return list;
    }

    public void setList(List<PlateTaxisVO> vos) {
        this.list = vos;
    }
    public void add(PlateTaxisVO vo) {
        this.list.add(vo);
    }
}
