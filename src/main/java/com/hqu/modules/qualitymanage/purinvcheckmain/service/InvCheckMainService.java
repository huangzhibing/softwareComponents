/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.hqu.modules.qualitymanage.purinvcheckmain.service;

import com.hqu.modules.basedata.item.entity.Item;
import com.hqu.modules.basedata.item.mapper.ItemMapper;
import com.hqu.modules.inventorymanage.billtype.entity.BillType;
import com.hqu.modules.inventorymanage.billtype.mapper.BillTypeMapper;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractDetail;
import com.hqu.modules.purchasemanage.contractmain.entity.ContractMain;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractDetailMapper;
import com.hqu.modules.purchasemanage.contractmain.mapper.ContractMainMapper;
import com.hqu.modules.purchasemanage.contractmain.service.ContractMainService;
import com.hqu.modules.purchasemanage.purplan.entity.ItemExtend;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanDetail;
import com.hqu.modules.purchasemanage.purplan.entity.PurPlanMain;
import com.hqu.modules.purchasemanage.purplan.mapper.ItemExtendMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanDetailMapper;
import com.hqu.modules.purchasemanage.purplan.mapper.PurPlanMainMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetail;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckDetailCode;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckMain;
import com.hqu.modules.qualitymanage.purinvcheckmain.entity.InvCheckRelations;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailCodeMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckDetailMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckMainMapper;
import com.hqu.modules.qualitymanage.purinvcheckmain.mapper.InvCheckRelationsMapper;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 入库通知单Service
 * @author 张铮
 * @version 2018-04-21
 */
@Service
@Transactional(readOnly = true)
public class InvCheckMainService extends CrudService<InvCheckMainMapper, InvCheckMain> {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ContractMainService contractMainService;
	@Autowired
	private ContractMainMapper contractMainMapper;
	@Autowired
	private ContractDetailMapper contractDetailMapper;
	@Autowired
	private ItemExtendMapper itemExtendMapper;
	@Autowired
	private PurPlanDetailMapper purPlanDetailMapper;
	@Autowired
	private PurPlanMainMapper purPlanMainMapper;
	@Autowired
	private InvCheckDetailMapper invCheckDetailMapper;
	@Autowired
	private InvCheckMainMapper invCheckMainMapper;
	@Autowired
	private InvCheckRelationsMapper invCheckRelationsMapper;
	@Autowired
	private InvCheckDetailCodeMapper invCheckDetailCodeMapper;
	@Autowired
	private BillTypeMapper billTypeMapper;
	
	public InvCheckMain get(String id) {
		InvCheckMain invCheckMain = super.get(id);
		InvCheckDetail invCheckDetail = new InvCheckDetail();
		invCheckDetail.setInvCheckMain(invCheckMain);
		List<InvCheckDetail> listtemp = invCheckDetailMapper.findListbyBillNum(invCheckDetail);
		invCheckMain.setInvCheckDetailList(listtemp);
//		InvCheckDetailCode invCheckDetailCode = new InvCheckDetailCode();
//		invCheckDetailCode.setBillnum(invCheckMain.getBillnum());
		invCheckMain.setInvCheckDetailCodeList(invCheckMainMapper.findListbyBillNum(invCheckMain));
		return invCheckMain;
	}
	
	public List<InvCheckMain> findList(InvCheckMain invCheckMain) {
		return invCheckMainMapper.findList(invCheckMain);
	}
	
	public List<InvCheckMain> findListbyBillStateFlag(InvCheckMain invCheckMain) {
		return invCheckMainMapper.findListbyBillTypeAndState(invCheckMain);
	}

	public List<InvCheckMain> findListbyBillStateFlagAndItemBarCode(InvCheckMain invCheckMain) {
		return invCheckMainMapper.findListbyBillTypeAndStateAndItemBarCode(invCheckMain);
	}
	
//	public List<PurPlanDetail> findPlanDetail(InvCheckMain invCheckMain){
//		List<PurPlanDetail> list = purPlanDetailMapper.findAllDetailList();
//		Iterator<PurPlanDetail> it = list.iterator();
//		//获得当前采购员的计划子表
//		while(it.hasNext()){
//			if(!(it.next().getBuyerId().equals(invCheckMain.getBuyerId()))){
//				it.remove();
//			}
//		}	
//		return list;
//	}
	public List<InvCheckMain> ListToChinese(List<InvCheckMain> list){
		Iterator<InvCheckMain> it = list.iterator();
		while(it.hasNext()){
			InvCheckMain inv = it.next();
			toChinese(inv);
		}
		return list;
		
	}
	
	
	public void toChinese(InvCheckMain invCheckMain){
		if("M".equals(invCheckMain.getBillType())){
			invCheckMain.setBillType("录入");
		}
		else if("C".equals(invCheckMain.getBillType())){
			invCheckMain.setBillType("生成");
		}
		if("Y".equals(invCheckMain.getThFlag())){
			invCheckMain.setThFlag("估价");
		}else if("N".equals(invCheckMain.getThFlag())){
			invCheckMain.setThFlag("实价");
		}
		
		if("W".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("录入完毕");
		}
		else if("E".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("审核通过");
		}
		else if("B".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("审核未通过");
		}else if("A".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("正在录入");
		}
		
		if("是".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("未质检");
		}
		else if("否".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("不质检");
		}
		else if("1".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("已质检");
			
		}else if("0".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("未质检");
		}
		else if("3".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("不质检");
		}
		
		Iterator<InvCheckDetail> it = invCheckMain.getInvCheckDetailList().iterator();
		while(it.hasNext()){
			InvCheckDetail invCheckDetail = it.next();
			if("0".equals(invCheckDetail.getQmsFlag())){
				invCheckDetail.setQmsFlag("不质检");
			}else if("1".equals(invCheckDetail.getQmsFlag())){
				invCheckDetail.setQmsFlag("质检");
			}else{
				invCheckDetail.setQmsFlag("未定义");
			}
		}
		
	}
	public Page<InvCheckMain> findPage(Page<InvCheckMain> page, InvCheckMain invCheckMain) {
		List<InvCheckMain> invCheckList;
    	invCheckList = super.findPage(page, invCheckMain).getList();
		Iterator<InvCheckMain> it = invCheckList.iterator();
		//显示各标志位描述
		while(it.hasNext()){
			InvCheckMain invCheckMainit = it.next();
			toChinese(invCheckMainit);
		}
		return page.setList(invCheckList);
	}
	
	public List<InvCheckMain> findTypingList(InvCheckMain invCheckMain) {
		List<InvCheckMain> invCheckList;
		invCheckMain.setBillStateFlag("A");
		invCheckMain.setBillType("M");
		invCheckList = invCheckMainMapper.findListbyBillTypeAndState(invCheckMain);
		Iterator<InvCheckMain> it = invCheckList.iterator();
		String userNo = UserUtils.getUser().getCurrentUser().getNo(); 
		if(userNo != null){
			while(it.hasNext()){
				InvCheckMain invCheckMainit = it.next();
//				if(invCheckMainit.getMakeEmpid().equals(userNo)){
				toChinese(invCheckMainit);
//				  }else{
//					it.remove();
//				}
			}
		}
		return invCheckList;
	}
	
	public List<InvCheckMain> findCreatedList(InvCheckMain invCheckMain) {
		List<InvCheckMain> invCheckList;
		invCheckMain.setBillStateFlag("A");
		invCheckMain.setBillType("C");
		invCheckList = invCheckMainMapper.findListbyBillTypeAndState(invCheckMain);
		Iterator<InvCheckMain> it = invCheckList.iterator();
		String userNo = UserUtils.getUser().getCurrentUser().getNo();
		if(userNo != null){
			while(it.hasNext()){
				InvCheckMain invCheckMainit = it.next();
				if("1".equals(invCheckMainit.getQmsFlag()) || "0".equals(invCheckMainit.getQmsFlag()) || !(userNo.equals(invCheckMainit.getMakeEmpid()))){
					it.remove();
				}else{
					toChinese(invCheckMainit);
				}
			}
		}
		return invCheckList;
	}
	
	//筛选审核通过的数据
	public List<InvCheckMain> findWareHousingList(InvCheckMain invCheckMain) {
		List<InvCheckMain> invCheckList;
		invCheckMain.setBillStateFlag("E");
		invCheckList = invCheckMainMapper.findListbyBillTypeAndStateAndItemBarCode(invCheckMain);
		Iterator<InvCheckMain> it = invCheckList.iterator();
        while(it.hasNext()) {
            InvCheckMain invCheckMainit = it.next();
            toChinese(invCheckMainit);
        }
		return invCheckList;
	}
//        //		String userNo = UserUtils.getUser().getCurrentUser().getNo();
////		if(userNo != null){
//        while(it.hasNext()){
//            InvCheckMain invCheckMainit = it.next();
////				if(userNo.equals(invCheckMainit.getMakeEmpid())){
//            toChinese(invCheckMainit);
////				}else{
////					it.remove();
////				}
////			}
////		}
	
	    //物料和到货主表检索查询
		public Page<InvCheckMain> findPageService(Page<InvCheckMain> page, InvCheckMain invCheckMain){
			//搜索框中的物料信息不为空
			InvCheckDetail invCheckDetail=new InvCheckDetail();
			invCheckDetail.setItem(invCheckMain.getMainItem());
			invCheckDetail.setItemName(invCheckMain.getMainItemName());
			invCheckDetail.setItemSpecmodel(invCheckMain.getMainItemSpecmodel());	
			//通过物料信息查询子表的数据，得到子表中物料的模糊查询结果
			List<InvCheckDetail> invCheckDetailList=invCheckDetailMapper.findList(invCheckDetail);
			Iterator<InvCheckDetail> it = invCheckDetailList.iterator();
			//得到所有子表查询的外键（主表字段的billNum）
			List <String> idList = new  ArrayList <String>(); 
			while(it.hasNext()){
				InvCheckDetail invCheckDetailit=it.next();	
			    String invCheckMainBillNum=invCheckDetailit.getInvCheckMain().getBillnum();
			    idList.add(invCheckMainBillNum);
			}
			//主表invCheckMain id去重
			List<String> newList = new  ArrayList<String>(); 
			 Set set = new  HashSet(); 
	         set.addAll(idList);
	         newList.addAll(set);
	        //依据主表的id查询，并
	         List <InvCheckMain> resultList=new  ArrayList <InvCheckMain>();
	         
	         //只能查看是制单人本人的合同
	 		 User user=UserUtils.getUser();
	 		 invCheckMain.setMakeEmpid(user.getNo());
	 	 	 //录入的合同
	 		invCheckMain.setBillStateFlag("M");
	 		 //正在录入/修改的合同
	 		invCheckMain.setBillStateFlag("A");
	 		 Page<InvCheckMain> page1=super.findPage(page, invCheckMain);
	         //通过检索框中主表的输入信息模糊检索主表
	         List<InvCheckMain> pageList=page1.getList();
	 		
	         //添加满足物料和主表的查询结果
	         for(InvCheckMain attr:pageList){	
			       if(newList.contains(attr.getBillnum())){
			    	   resultList.add(attr);
			       }	        	 
				}		
	         //将查询结果返回
	         page.setList(resultList);
	         page.setCount(resultList.size());
	         return page;  
		}
	
	@Transactional(readOnly = false)
	public void toFlag(InvCheckMain invCheckMain){
		if("录入".equals(invCheckMain.getBillType())){
			invCheckMain.setBillType("M");
		}else if("生成".equals(invCheckMain.getBillType())){
			invCheckMain.setBillType("C");
		}
		
		if("估价".equals(invCheckMain.getThFlag())){
			invCheckMain.setThFlag("Y");
		}else if("实价".equals(invCheckMain.getThFlag())){
			invCheckMain.setThFlag("N");
		}
		
		if("正在录入".equals(invCheckMain.getBillStateFlag())||"正在修改".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("A");
		}
		else if("录入完毕".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("W");
		}
		else if("审核通过".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("E");
		}
		else if("审核未通过".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("B");
		}
		else if("作废单据".equals(invCheckMain.getBillStateFlag())){
			invCheckMain.setBillStateFlag("V");
		}
		
		if("质检".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("是");
		}
		else if("不质检".equals(invCheckMain.getQmsFlag())){
			if("W".equals(invCheckMain.getBillStateFlag())){
				invCheckMain.setQmsFlag("3");
			}else{
				invCheckMain.setQmsFlag("否");
			}
		}else if("未质检".equals(invCheckMain.getQmsFlag())){
			invCheckMain.setQmsFlag("0");
		}
		
		Iterator<InvCheckDetail> it = invCheckMain.getInvCheckDetailList().iterator();
		while(it.hasNext()){
			InvCheckDetail invCheckDetail = it.next();
			if("不质检".equals(invCheckDetail.getQmsFlag())){
				invCheckDetail.setQmsFlag("0");
			}else if("质检".equals(invCheckDetail.getQmsFlag())){
				invCheckDetail.setQmsFlag("1");
			}else if("未定义".equals(invCheckDetail.getQmsFlag())){
				invCheckDetail.setQmsFlag(null);
			}
		}
		
	}
	
	
	
	@Transactional(readOnly = false)
	public void save(InvCheckMain invCheckMain) {
		if(invCheckMain.getOrderOrContract() != null) {
		    //修改之前把之前的关系数据作废
		    InvCheckRelations conRes=new InvCheckRelations();
			conRes.setAfterNum(invCheckMain.getBillnum());

			List<InvCheckRelations> conRelationList=invCheckRelationsMapper.findList(conRes);
			Iterator<InvCheckRelations> conRelationIt = conRelationList.iterator();  
	        while(conRelationIt.hasNext()){
	        	InvCheckRelations conRelations=conRelationIt.next();

	        	invCheckRelationsMapper.delete(conRelations);
	        }
		List<InvCheckDetail> invCheckDetailList=invCheckMain.getInvCheckDetailList();
		Iterator<InvCheckDetail> invCheckDetailIt = invCheckDetailList.iterator();  
	    Integer serialNum=0;
		while(invCheckDetailIt.hasNext()){
			InvCheckDetail invCheckDetail=invCheckDetailIt.next();
			String  frontBillNum=invCheckDetail.getFrontBillNum();
			String billType=frontBillNum.substring(0, 3);
			if(frontBillNum!=null){
				if("0".equals(invCheckDetail.getDelFlag())){//修改合同信息
					InvCheckRelations conRelations=new InvCheckRelations();
					 serialNum=serialNum+1;
					 invCheckDetail.setSerialnum(serialNum);
					  if("con".equals(billType)){//判定子表单据来源
						  conRelations.setType("CD");
					  }else{
						  conRelations.setType("PD");
					  }
				  conRelations.setAfterId(serialNum);
		    	  conRelations.setAfterNum(invCheckMain.getBillnum());
		    	  conRelations.setFrontNum(invCheckDetail.getFrontBillNum());
		    	  conRelations.setFrontId(invCheckDetail.getSerialnum());
		    	  conRelations.preInsert();
			      //关系表中填写数据
		    	  invCheckRelationsMapper.insert(conRelations);
		    	  
		    	   
//		    		 if("con".equals(billType)){//判定子表单据来源
//		    			//往合同子表中回写到货量
//		    		   	  ContractDetail contractDetail=new ContractDetail();
//		    		      contractDetail.setSerialNum(invCheckDetail.getFrontSerialNum());//前端中获取到的加入到到货子表的合同子表编号
//		    		      ContractMain contractMain=new ContractMain();
//		    		      contractMain.setBillNum(invCheckDetail.getFrontBillNum());
//		    		      contractDetail.setContractMain(contractMain);//前端中获取到的加入到到货子表的合同子表序号
//		    		      List<ContractMain> conMainList = contractMainMapper.findList(contractMain);
//		    		      ContractMain conMain = new ContractMain();
//		    		      for(ContractMain conMainforLoop: conMainList){
//		    		    	  if(conMainforLoop.getBillNum().equals(contractMain.getBillNum())){
//		    		    		  conMain = conMainforLoop;
//		    		    	  }
//		    		      }
//		    		      ContractDetail contractDet = new ContractDetail();
//		    		      contractDet.setContractMain(conMain);
//		    		      List<ContractDetail> conDetailList = contractDetailMapper.findList(contractDet);
//		    		      if(conDetailList != null){
//		    		    	  for(ContractDetail conDetailforLoop: conDetailList){
//		    		    		  if(conDetailforLoop.getSerialNum().equals(contractDetail.getSerialNum())){
//		    		    			  contractDetail = conDetailforLoop;
//		    		    		  }
//		    		    	  }
//		    		      }
//		    			  //contractDetail=contractDetailMapper.getContractDetail(contractDetail);
//			    		//合同子表中所填的到货总量
//			    		 Double checkQty= contractDetail.getCheckQty();
//			    		 if(checkQty==null||checkQty==0){
//			    			 checkQty=0.0;
//			    		 }
//			    		 //该到货子表修改前的到货量
//			    		 Double frontItemQty=invCheckDetail.getFrontQty();
//			    		 if(frontItemQty==null||"".equals(frontItemQty)){
//			    			 frontItemQty=0.0;
//			    		 }
//			    		 //该到货子表修改后的到货量
//			    		 Double checkQtyaf=invCheckDetail.getCheckQty();
//			    		 if(checkQtyaf==null||"".equals(checkQtyaf)){
//			    			 checkQtyaf=0.0;
//			    		 }
//			    		 contractDetail.setCheckQty(checkQty-frontItemQty+checkQtyaf);
//			    		 contractDetailMapper.update(contractDetail);
//					  }else if("pln".equals(billType)){
//						    //往计划子表中回写到货量
//				    		 PurPlanMain purPlanMain=new PurPlanMain();
//				    		 purPlanMain.setBillNum(invCheckDetail.getFrontBillNum());
//						     purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
//				    		 PurPlanDetail purPlanDetail=new PurPlanDetail();
//				    		 purPlanDetail.setBillNum(purPlanMain);
//				    		 purPlanDetail.setSerialNum(invCheckDetail.getFrontSerialNum());
//				    		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
//				    		//计划子表中所填的到货总量
//				    		 Double checkQty= purPlanDetail.getCheckQty();
//				    		 if(checkQty==null||checkQty==0){
//				    			 checkQty=0.0;
//				    		 }
//				    		 //该到货子表修改前的到货量
//				    		 Double frontItemQty=invCheckDetail.getFrontQty();
//				    		 if(frontItemQty==null||"".equals(frontItemQty)){
//				    			 frontItemQty=0.0;
//				    		 }
//				    		 //该到货子表修改后的合同量
//				    		 Double checkQtyaf=invCheckDetail.getCheckQty();
//				    		 if(checkQtyaf==null||"".equals(checkQtyaf)){
//				    			 checkQtyaf=0.0;
//				    		 }
//				    		 purPlanDetail.setCheckQty(checkQty-frontItemQty+checkQtyaf);
//			    			 purPlanDetailMapper.update(purPlanDetail);
//					  }
		    		 
		    	  
				}else{//删除信息
					
					
//					if("con".equals(billType)){//判定子表单据来源
//		    			//往合同子表中回写到货量
//		    		   	  ContractDetail contractDetail=new ContractDetail();
//		    		      contractDetail.setSerialNum(invCheckDetail.getFrontSerialNum());
//		    		      ContractMain contractMain=new ContractMain();
//		    		      contractMain.setBillNum(invCheckDetail.getFrontBillNum());
//		    		      contractDetail.setContractMain(contractMain);
//		    			  contractDetail=contractDetailMapper.getContractDetail(contractDetail);
//			    		//合同子表中所填的合同总量
//			    		 Double conQty= contractDetail.getCheckQty();
//			    		 if(conQty==null||conQty==0){
//			    			 conQty=0.0;
//			    		 }
//			    		 //该到货子表修改前的合同量
//			    		 Double frontItemQty=invCheckDetail.getFrontQty();
//			    		 if(frontItemQty==null||"".equals(frontItemQty)){
//			    			 frontItemQty=0.0;
//			    		 }
//
//			    		 contractDetail.setCheckQty(conQty-frontItemQty);
//			    		 contractDetailMapper.update(contractDetail);
//					  }else{
//						     //往计划子表中回写到货量
//				    		 PurPlanMain purPlanMain=new PurPlanMain();
//				    		 purPlanMain.setBillNum(invCheckDetail.getFrontBillNum());
//						     purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
//				    		 PurPlanDetail purPlanDetail=new PurPlanDetail();
//				    		 purPlanDetail.setBillNum(purPlanMain);
//				    		 purPlanDetail.setSerialNum(invCheckDetail.getFrontSerialNum());
//				    		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
//				    		//计划子表中所填的合同总量
//				    		 Double conQty= purPlanDetail.getConQty();
//				    		 if(conQty==null||conQty==0){
//				    			 conQty=0.0;
//				    		 }
//				    		 //该到货子表修改前的合同量
//				    		 Double frontItemQty=invCheckDetail.getFrontQty();
//				    		 if(frontItemQty==null||"".equals(frontItemQty)){
//				    			 frontItemQty=0.0;
//				    		 }
//
//				    		 purPlanDetail.setCheckQty(conQty-frontItemQty);
//			    			 purPlanDetailMapper.update(purPlanDetail);
//					  }
				}
			}
		}
		
		invCheckMain.setInvCheckDetailList(invCheckDetailList);		
		}
		toFlag(invCheckMain);
		super.save(invCheckMain);
		//保存到货子表
		int i = invCheckMain.getInvCheckDetailList().size();
		InvCheckDetailCode invCheckDetailCode = new InvCheckDetailCode();
		for (InvCheckDetail invCheckDetail : invCheckMain.getInvCheckDetailList()){
			if (invCheckDetail.getId() == null){
				continue;
			}
			if (InvCheckDetail.DEL_FLAG_NORMAL.equals(invCheckDetail.getDelFlag())){

				invCheckDetailCode.setBillnum(invCheckMain.getBillnum());
				
				if (StringUtils.isBlank(invCheckDetail.getId())){
					invCheckDetail.setInvCheckMain(invCheckMain);
					//子表编号排序
					invCheckDetail.setSerialnum(i);
					invCheckDetail.setIsPrint(0);//save时打印标志置0
					invCheckDetail.preInsert();
					invCheckDetailMapper.insert(invCheckDetail);
				}else{
					//子表编号重新排序
					invCheckDetail.setSerialnum(i);
					invCheckDetail.setIsPrint(0);
					invCheckDetail.preUpdate();
					invCheckDetailMapper.update(invCheckDetail);
					
//					//更新物料二维码
//					invCheckDetailCode.setSerialnum(i);
//					invCheckDetailCode.setItemCode(invCheckDetail.getItemCode());
//					String itemBarcode = generateQRCode(invCheckMain.getBillnum(), invCheckDetail.getItemCode());
//					invCheckDetailCode.setItemBarcode(itemBarcode);
//					invCheckDetailCode.preUpdate();
//					invCheckDetailCodeMapper.update(invCheckDetailCode);
					//审核通过后生成物料二维码
					if("E".equals(invCheckMain.getBillStateFlag())){
						if (invCheckMain.getInvCheckDetailCodeList().isEmpty()){
							Double dhcount = invCheckDetail.getCheckQty();
							int count = dhcount.intValue();
							//保存物料二维码
							for(int k = 1 ; k <= count; k ++){
								invCheckDetailCode.setItemName(invCheckDetail.getItemName());
								invCheckDetailCode.setSerialnum(k);
								invCheckDetailCode.setItemCode(invCheckDetail.getItemCode());
								String itemBarcode = generateQRCode(invCheckMain.getBillnum(), invCheckDetail.getItemCode(), k);
								invCheckDetailCode.setItemBarcode(itemBarcode);
								invCheckDetailCode.preInsert();
								invCheckDetailCodeMapper.insert(invCheckDetailCode);
							}
						}else{
							//生成物料二维码时保存扫码的供应商二维码
							for (InvCheckDetailCode invCheckDetailcode : invCheckMain.getInvCheckDetailCodeList()){
								invCheckDetailCodeMapper.update(invCheckDetailcode);
							}
						}
					}
				}
			}else{
				invCheckDetailMapper.delete(invCheckDetail);
				invCheckDetailCodeMapper.delete(invCheckDetailCode);
			}
			i--;
		}
		

	}
	
	@Transactional(readOnly = false)
	public void delete(InvCheckMain invCheckMain) {
		//子表中的到货量修改
		InvCheckMain invCheck=invCheckMainMapper.get(invCheckMain);
		//List<ContractDetail> contractDetailList=contract.getContractDetailList();
		//查找该合同的所有合同子表信息
		InvCheckDetail invCheckDetail=new InvCheckDetail();
		invCheckDetail.setInvCheckMain(invCheck);
		invCheckDetail.setDelFlag("0");
		List<InvCheckDetail> invCheckDetailList=invCheckDetailMapper.findList(invCheckDetail);
		
		
		Iterator<InvCheckDetail> invCheckDetailIt = invCheckDetailList.iterator();  
        while(invCheckDetailIt.hasNext()){
        	if("M".equals(invCheckMain.getBillType())){
        		break;
        	}
        	
        	InvCheckDetail invDetail=invCheckDetailIt.next();
        	 InvCheckRelations invRes=new InvCheckRelations();
        	 invRes.setAfterNum(invCheck.getBillnum());
        	 invRes.setAfterId(invDetail.getSerialnum());
   			 List<InvCheckRelations> invCheckRelationsList=invCheckRelationsMapper.findList(invRes);

			 if(invCheckRelationsList.size()>0) invRes=invCheckRelationsList.get(0);
			 String billType=invRes.getType();
			    if("CD".equals(billType)){//合同生成到货
			    	  ContractDetail contractDetail=new ContractDetail();
	    		      contractDetail.setSerialNum(invRes.getFrontId());
	    		      ContractMain contractMain=new ContractMain();
	    		      contractMain.setBillNum(invRes.getFrontNum());
	    		      contractDetail.setContractMain(contractMain);
	    			  contractDetail=contractDetailMapper.getContractDetail(contractDetail);
	    			    //合同子表中所填的到货总量
				   		 Double conQty= contractDetail.getCheckQty();
				   		 if(conQty==null||conQty==0){
				   			 conQty=0.0;
				   		 }
				   		 //该合同子表修改后的合同量
				   		 Double checkQty=invDetail.getCheckQty();
				   		 if(checkQty==null||"".equals(checkQty)){
				   			checkQty=0.0;
				   		 }
				   		 contractDetail.setCheckQty(conQty-checkQty);
				   		contractDetailMapper.update(contractDetail);
				   		
				   		invCheckRelationsMapper.delete(invRes);
			    	
			    }else if("PD".equals(billType)){//计划生成到货
			    	 //往计划子表中回写合同量
			   		 PurPlanMain purPlanMain=new PurPlanMain();
			   		 purPlanMain.setBillNum(invRes.getFrontNum());
			   		 purPlanMain=purPlanMainMapper.getPurPlanMain(purPlanMain);
			   		 //依据计划单号和计划子表序号查找计划子表
			   		 PurPlanDetail purPlanDetail=new PurPlanDetail();
			   		 purPlanDetail.setBillNum(purPlanMain);
			   		 purPlanDetail.setSerialNum(invRes.getFrontId());
			   		 purPlanDetail= purPlanDetailMapper.getPurPlanDetail(purPlanDetail);
			   		 //计划子表中所填的合同总量
			   		 Double conQty= purPlanDetail.getConQty();
			   		 if(conQty==null||conQty==0){
			   			 conQty=0.0;
			   		 }
			   		 //该合同子表修改后的合同量
			   		 Double checkQty=invDetail.getCheckQty();
			   		 if(checkQty==null||"".equals(checkQty)){
			   			checkQty=0.0;
			   		 }
			   		 purPlanDetail.setCheckQty(conQty-checkQty);
				     purPlanDetailMapper.update(purPlanDetail);
				     //把之前的关系数据作废
				    // conRes.setState("V");
		        	// conRelationsMapper.update(conRes);
				     invCheckRelationsMapper.delete(invRes);
			    }
        }
				
		super.delete(invCheckMain);
		invCheckDetailMapper.delete(new InvCheckDetail(invCheckMain));
	}
	@Transactional(readOnly = false)
	public void updateQmsFlag(InvCheckMain invCheckMain){
		//invCheckMain.setQmsflag("1");
		invCheckMainMapper.qmsFlagByBillNum(invCheckMain);
	}

	//依据采购员id筛选采购计划
	public Page<PurPlanDetail> selectPlan(Page<PurPlanDetail> page, PurPlanDetail purPlanDetail) {
		     List<PurPlanDetail> purPlanDetails= purPlanDetailMapper.findList(purPlanDetail);
			 Iterator<PurPlanDetail> purPlanIt = purPlanDetails.iterator();
	        
			 List <PurPlanDetail> idList = new  ArrayList <PurPlanDetail>(); 
			 
			 while(purPlanIt.hasNext()){
				 //添加物料数据
	        	 PurPlanDetail purPlan=purPlanIt.next();
	        	 PurPlanMain purPlanMain=purPlanMainMapper.get(purPlan.getBillNum());
	        	 if("C".equals(purPlanMain.getBillStateFlag())){
	        		 Item item=itemMapper.get(purPlan.getItemCode());
	        	//	 ItemExtend item= itemExtendMapper.get(purPlan.getItemCode());
	        		 ItemExtend itemExtend=new ItemExtend();
	        		 itemExtend.setCode(item.getCode());
	        		 itemExtend.setId(item.getId());
	        		 //填写物料材质
		           	 purPlan.setItemTexture(item.getTexture());
		           	 //填写物料编号
		           	 purPlan.setItemCode(itemExtend);
		           	 //填入计划单号
		           	 purPlan.setBillNum(purPlanMain);
		           	 //
		             idList.add(purPlan);
	        	 }
	       	 }
			 page.setList(idList);
		  return page;
	}
	
	
	//根据billNum(contractId)筛选采购合同子表信息
		public Page<ContractDetail> selectContract(Page<ContractDetail> page, ContractMain contractMain, String contractId) {
			contractMain.setBillNum(contractId);
			List<ContractMain> contractMainlist = contractMainService.findList(contractMain);
			List<ContractDetail> contractDetaillist = new ArrayList<ContractDetail>();
			//获得取到的合同主表
			if(contractMainlist!=null){
				Iterator<ContractMain> it = contractMainlist.iterator();
				ContractMain theSelectedContractMain = it.next();
				ContractDetail contractDetail = new ContractDetail();
				contractDetail.setContractMain(theSelectedContractMain);
				contractDetaillist.addAll(contractDetailMapper.findList(contractDetail));
			}
			if(contractDetaillist.size()!=0){
				page.setList(contractDetaillist);
			}
			return page;
		}
	
	public String generateQRCode(String billnum, String itemCode, int k)	{
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Integer k1 = k;
		int len = k1.toString().length();
		String flow = "";
		for(int i = len; i < 5; i++){
			flow += "0";
		}
		flow += k1.toString();
//		Random rand = new Random();
//		int n = rand.nextInt(90000) + 10000;
		//15位单据编号+12位物料编码+8位日期+5位流水
		String QRCode = billnum + itemCode + df.format(date.getTime()) + flow;
		return QRCode;
	}
	
	//根据子表退货标志筛选退货信息
	public List<InvCheckMain> selectBack(InvCheckDetail invCheckDetail) {
//		invCheckDetail.setBackFlag("1");
//		List<InvCheckDetail> invChecklDetailist = invCheckDetailMapper.findList(invCheckDetail);
//		InvCheckMain invCheckMain = new InvCheckMain();
//		List<InvCheckMain> invCheckMainListforLoop = findList(invCheckMain);
//		List<InvCheckMain> invCheckMainList = new ArrayList<InvCheckMain>();
//		Iterator<InvCheckDetail> it = invChecklDetailist.iterator();
//		while(it.hasNext()){
//			InvCheckDetail invCheckDetailit = it.next();
//			String billnum = invCheckDetailit.getInvCheckMain().getBillnum();
//			for(InvCheckMain invCheckMainforLoop : invCheckMainListforLoop){
//				if(billnum.equals(invCheckMainforLoop.getBillnum())){
//					toChinese(invCheckMainforLoop);
//					invCheckMainList.add(invCheckMainforLoop);
//				}
//			}
//		}
		List<InvCheckMain> invCheckMainList = new ArrayList<InvCheckMain>();
		invCheckMainList = invCheckMainMapper.findBackList();
		Iterator<InvCheckMain> it = invCheckMainList.iterator();
		while(it.hasNext()){
			InvCheckMain invMainDetailit = it.next();
			toChinese(invMainDetailit);
		}
		return invCheckMainList;
	}
	
	//设置默认入库类型
	public InvCheckMain setDefultIO(InvCheckMain invCheckMain) {
		BillType billType = new BillType();
		String defultdesc = "外购件入库";
		String defulttype = "WI01";
		billType.setIoDesc(defultdesc);
		List<BillType> btl = billTypeMapper.findList(billType);
		if(btl.isEmpty()){
			invCheckMain.setIoDesc(defultdesc);
			invCheckMain.setIoType(defulttype);
		}else{
			billType = btl.get(0);
			invCheckMain.setIoDesc(billType.getIoDesc());
			invCheckMain.setIoType(billType.getIoType());
		}
		return invCheckMain;
	}

}