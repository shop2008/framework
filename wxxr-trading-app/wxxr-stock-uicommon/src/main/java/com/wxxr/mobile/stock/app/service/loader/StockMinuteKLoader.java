package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.resource.StockResource;

public class StockMinuteKLoader extends AbstractEntityLoader<String, StockMinuteKBean, StockMinuteKBean> {
    public final static String Name = "GetStockMinuteCommand";
    @NetworkConstraint
    public class GetStockMinuteCommand implements ICommand<List<StockMinuteKBean>> {
        private String code;
        private String market;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }

        @Override
        public String getCommandName() {
            return Name;
        }

        @Override
        public Class<List<StockMinuteKBean>>  getResultType() {
            Class clazz=List.class;
            return clazz;
        }

        @Override
        public void validate() {
        	if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
				throw new IllegalArgumentException("Invalid market or code");
			}
        }

    }

    @Override
    public ICommand<List<StockMinuteKBean>> createCommand(Map<String, Object> params) {
        GetStockMinuteCommand cmd = new GetStockMinuteCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<StockMinuteKBean> result, IReloadableEntityCache<String, StockMinuteKBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockMinuteKBean vo : result) {
                StockMinuteKBean bean=cache.getEntity(vo.getMarket()+vo.getCode());
                if(bean == null) {
                    cache.putEntity(vo.getMarket()+vo.getCode(), vo);
                }else{
                    ConverterUtils.updatefromVOtoBean(bean, vo);
                }
                updated = true;
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return Name;
    }

    @Override
    protected List<StockMinuteKBean> executeCommand(ICommand<List<StockMinuteKBean>> command) throws Exception {
        GetStockMinuteCommand cmd = (GetStockMinuteCommand)command;
        ParamVO p=new ParamVO();
        p.setMarket(cmd.getMarket());
        p.setCode(cmd.getCode());
        StockMinuteKVO vo= getRestService(StockResource.class).getMinuteline(p);
        StockQuotationBean qbean = KUtils.getService(IInfoCenterManagementService.class).getSyncStockQuotation(cmd.getCode(), cmd.getMarket());
        if (vo!=null ){
            StockMinuteKBean bean =ConverterUtils.fromVO(vo);
            bean.setMarket(cmd.getMarket());
            bean.setStop(qbean!=null&&qbean.getStatus()!=null?qbean.getStatus()==2:false);
            bean.setCode(cmd.getCode());
            List<StockMinuteKBean> result= new ArrayList<StockMinuteKBean>();
            result.add(bean);
            return result;
        }
        return null;
    }
}

