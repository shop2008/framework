package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.resource.StockResource;
/**
 * 实时行情
 * @author zhengjincheng
 *
 */
public class StockQuotationLoader extends AbstractEntityLoader<String, StockQuotationBean, StockQuotationVO> {
    public final static String Name = "GetStockQuotationCommand";
    @NetworkConstraint
    public class GetStockQuotationCommand implements ICommand<List<StockQuotationVO>> {
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
        public Class<List<StockQuotationVO>>  getResultType() {
            Class clazz=List.class;
            return clazz;
        }

        @Override
        public void validate() {
        	if (StringUtils.isBlank(market)||StringUtils.isBlank(code)) {
				throw new IllegalArgumentException("Invalid market or code");//fix闪退问题
			}
        }

    }

    @Override
    public ICommand<List<StockQuotationVO>> createCommand(Map<String, Object> params) {
        if (params==null){
            return null;
        }
        GetStockQuotationCommand cmd = new GetStockQuotationCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<StockQuotationVO> result, IReloadableEntityCache<String, StockQuotationBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockQuotationVO vo : result) {
                StockQuotationBean bean=cache.getEntity(vo.getMarket()+vo.getCode());
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(vo.getMarket()+vo.getCode(), bean);
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
    protected List<StockQuotationVO> executeCommand(ICommand<List<StockQuotationVO>> command) throws Exception {
        GetStockQuotationCommand cmd = (GetStockQuotationCommand)command;
        QuotationListVO svos= getRestService(StockResource.class).getQuotation(cmd.getMarket(),cmd.getCode());
        if (svos!=null && svos.getList()!=null){
            return svos.getList();
        }
        return null;
    }
}
