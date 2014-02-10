/**
 * 
 */
package com.wxxr.trading.core.storage.account.persistence;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.persistence.service.FindByPrimaryKeyCommand;
import com.wxxr.trading.core.storage.account.bean.AssetAccountInfo;

/**
 * @author neillin
 *
 */
public interface IAssetAccountDAO {
	Long COMPANY_ACCOUNT_ID = new Long(1);
	Long COMPANYCASH_ACCOUNT_ID = new Long(2);

	Long add(AssetAccountInfo asset);
	void update(AssetAccountInfo asset);
	void remove(AssetAccountInfo asset);
	
	@Command(clazz=FindByPrimaryKeyCommand.class)
	AssetAccountInfo findByPrimaryKey(Long id);

//	@Command(clazz=FindLast2TradingAccountsByUser.class)
//	Collection<TradingAccountInfo> findLast2TradingAccountsByUser(String userId);
//	
//	@Command(clazz=VisitAllTradingAccountsByStatusCommand.class)
//	void visitAllTradingAcountsOfStatus(TradingAccountStatus status,IAccountInfoVisitor visitor);
//	
//	@Command(clazz=FindCapitalAccountOfUserCommand.class)
//	UserAccountInfo findUserCapitalAccount(String userId);
//	
//	@Command(clazz=CreateCompanyAccountIfNeccessaryCommand.class)
//	boolean createCompanyAccountIfNecessary();
//	
//	@Command(clazz=FindAllTradingAccountsByUserIdAndTimeCommand.class)
//	Collection<TradingAccountInfo> findAccountsByUserIdAndTimeCommand(String userId,Date beginTime,Date endTime);
//	
//	@Command(clazz=CreateCompanyCashAccountIfNeccessaryCommand.class)
//	boolean createCompanyCashAccountIfNecessary(String type,int virtual);
//	
//	@Command(clazz=CreateUserCashAccountIfNeccessaryCommand.class)
//	boolean createUserCashAccountIfNecessary(String type,int virtual,Long acctID);
//	
//	@Command(clazz=FindTradingAccountsCountByUserIdCommand.class)
//	int findTradingAccountsCountByUser(String userId);
//	
//	@Command(clazz=FindTradingAccountByAcctIdAndUserIdCommand.class)
//	AssetAccountInfo findTradingAccountByAcctIdAndUserId(Long id,String userId);
//	
//	@Command(clazz=FindAllUsersCommand.class)
//	List<UserAccountInfo> fingAllUsers();
//	
//	@Command(clazz=FindTradingAcctByTradingDateCommand.class)
//	List<TradingAccountInfo> findTradingAccountsByDatePeriod(boolean virtual,Date start,Date end);
//	
//	@Command(clazz=CreateCompanyVoucherAccountIfNeccessaryCommand.class)
//	boolean createCompanyVoucherAccountIfNecessary(String type,int virtual);
//	
//	@Command(clazz=FindUserAccountsByPageCommand.class)
//	Collection<TradingAccountInfo> findUserAccountsByPage(String userId,boolean virtual,int start,int limit);
//	
//	@Command(clazz=FindAllUserFrozenMoneyCommand.class)
//	Long findAllUserFrozenMoney();
//
//	@Command(clazz=FindAllUserTotalIncludeFrozenCommand.class)
//	Long findAllUserTotalIncludeFrozen();
//	
//	/**
//	 * 用户交易统计
//	 * @return
//	 */
//	@Command(clazz=FindAllTradeStatCommand.class)
//	TradeStatisticsAssignment findAllTradeStat();
}
