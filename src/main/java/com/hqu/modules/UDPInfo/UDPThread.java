package com.hqu.modules.UDPInfo;

import com.hqu.modules.inventorymanage.billdetailcodes.service.BillDetailCodesService;
import com.hqu.modules.inventorymanage.udpinfo.entity.UdpRecord;
import com.hqu.modules.inventorymanage.udpinfo.service.UdpRecordService;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.UDP.ByteTools;
import com.jeeplus.common.utils.UDP.UdpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

// runable对象，线程运行代码
public class UDPThread implements Runnable{

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public BillDetailCodesService billDetailCodesService = SpringContextHolder.getBean(BillDetailCodesService.class);
    public UdpRecordService udpRecordService = SpringContextHolder.getBean(UdpRecordService.class);

    public static DatagramSocket socket = null;
    public static DatagramPacket packet = null;


    public UDPThread(DatagramSocket socket,DatagramPacket packet) {
        UDPThread.socket = socket;
        UDPThread.packet = packet;
    }

    public static void send(byte[] b)
    {
        try{
        	socket.send(new DatagramPacket(b, b.length, packet.getAddress(), packet.getPort()));
        }catch(IOException e){
        	e.printStackTrace();
        }
    }
    
    public static String getNow(String format){ 
        SimpleDateFormat formatter; 
        formatter = new SimpleDateFormat (format); 
        String ctime = formatter.format(new Date()); 

        return ctime; 
    }

    @Override
    public void run() {

        String info = new String(packet.getData()).trim();
        udpRecordService.saveBWUdpIno(info);
        int len = info.length();
        logger.info("接收到receive："+info.substring(0, len) +" len:"+packet.getLength());
    	
        // 拷贝获取的byte数组
    	byte[] rcv = new byte[packet.getLength()];
        ByteTools.copyBytes(packet.getData(), rcv, packet.getLength());

        if(len < 56){
            logger.info("udp接收到数据长度不符合56位格式"+info);
            return;
        }
        // 第1、2位为出开始标志
        String start = info.substring(0, 2);
        if(!"02".equals(start)){
            logger.info("udp接收到数据开头不符合02格式"+info);
            return;
        }
        // 出入库标志
        // 01入库
    	if(len ==56){
            //结尾标识
            String end = info.substring(52,56);
            if(!"0D0A".equals(end)){
                logger.info("udp接收到数据结尾不符合0D0A格式"+info);
                return;
            }
            // 第3、4位为货区标志
            String container = info.substring(2,4);
            // 第5、6位为出入库标志
            String ioType = info.substring(4,6);
            // 40位二维码
            String barCode = info.substring(6,46);
            // 6位货位码
            String locId = info.substring(46,52);
            //应答成功返回
            String result = UdpUtil.sendSuccUdpInfo(ioType);
            udpRecordService.saveERPUdpIno(result);
            switch (ioType){
                case "01":
                    logger.info("开始记录入库二维码信息");
                    billDetailCodesService.saveUdpData(barCode,container,locId);
                    return;
                case "03":
                    return;
                default:
            }
            /*if(!ioType.equals("01")&&!ioType.equals("03")&&!ioType.equals("05")){
                logger.info("udp接收到数据不是入库数据也不是退料数据更不是超期复验合格入库数据");
                return;
            }*/

    	}else if(len ==63){
            //结尾标识
            String end = info.substring(59,63);
            if(!"0D0A".equals(end)){
                logger.info("udp接收到数据结尾不符合0D0A格式"+info);
                return;
            }
            String ioType = info.substring(2,4);
            String billNum = info.substring(4,19);
            String barCode = info.substring(19,59);
            // 02出库
            String result = UdpUtil.sendSuccUdpInfo(ioType);
            udpRecordService.saveERPUdpIno(result);
            logger.info("开始记录数据出库信息");
            billDetailCodesService.saveUdpData(barCode,billNum);
    	}else if(len == 71){
            logger.info("开始处理退料返回udp信息");
            // 第3、4位为货区标志
            String container = info.substring(2,4);
            // 第5、6位为出入库标志
            String ioType = info.substring(4,6);
            String billNum = info.substring(6,21);
            String barCode = info.substring(21,61);
            String locId = info.substring(61,67);
            String result = UdpUtil.sendSuccUdpInfo(ioType);
            udpRecordService.saveERPUdpIno(result);
            logger.info("开始记录数据退料信息");
            billDetailCodesService.saveOutTreasuryUdpData(barCode,billNum,container,locId);
        }else{

            logger.error("不符合的udp日志信息");
        }


    }

}