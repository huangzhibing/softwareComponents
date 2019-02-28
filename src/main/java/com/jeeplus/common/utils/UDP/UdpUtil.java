package com.jeeplus.common.utils.UDP;

import com.hqu.modules.inventorymanage.udpinfo.service.UdpRecordService;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Description: <br>
 *
 * @author huang.youcai<br>
 * #date 2018年08月03日<br>
 * @since V1.0<br>
 */
public class UdpUtil {

    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(UdpUtil.class);
    public static UdpRecordService udpRecordService = SpringContextHolder.getBean(UdpRecordService.class);

    /**
     * 发送消息接收成功返回udp消息
     * @param type 出入库类型 01入库/02出库
     * @param resultCode 返回成功失败信息 00失败/01成功
     */
    public static String sendUdpInfo(String type,String resultCode){
        String udpInfo = "02"+type+resultCode+"0D0A";
        logger.info("发送udp信息："+udpInfo);
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            // 创建一个数据报，每一个数据报不能大于64k，都记录着数据信息，发送端的IP、端口号，以及要发送到
            // 的接收端的IP、端口号。
            byte[] b = udpInfo.getBytes();
            DatagramPacket pack = new DatagramPacket(b, 0, b.length,
                    InetAddress.getByName(Global.getConfig("BW_SERVER_IP")), Integer.parseInt(Global.getConfig("BW_SERVER_PORT")));
            ds.send(pack);
            logger.info("发送成功指令"+udpInfo);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("指令发送失败"+udpInfo+e.getMessage());
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        udpRecordService.saveERPUdpIno(udpInfo);
        return udpInfo;
    }

    /**
     * 发送成功报文
     * @param type 出入库类型 01入库/02出库
     */
    public static String sendSuccUdpInfo(String type){
        logger.info("发送成功报文");
        return sendUdpInfo(type,"02");
    }
    /**
     * 发送失败报文
     * @param type 出入库类型 01入库/02出库
     */
    public static String sendFailUdpInfo(String type){
        logger.info("发送失败报文");
        return sendUdpInfo(type,"01");
    }

    /**
     * 发送出库指令
     * @param billNum 单据号15位
     * @param itemCode 物料代码12位
     * @param num 数量
     */
    public static void outTreasury(String billNum,String itemCode,int num){
        if(billNum.length() > 15){
            logger.info("单据号长度不能超过15位");
            return;
        }
        if(itemCode.length() > 12){
            logger.info("物料代码长度不能超过12位");
            return;
        }
        if(num > 9999){
            logger.info("数量不能超过4位数");
            return;
        }

        //itemCode = String.format("%012d", itemCode);
        String numStr = String.format("%04d", num);
        sendUdpInfo("02",billNum+itemCode+numStr);
    }

    /**
     * 发送退料指令指令
     * @param billNum 单据号15位
     * @param itemCode 物料代码12位
     * @param num 数量 负数
     */
    public static void backTreasury(String billNum,String itemCode,int num){
        if(billNum.length() > 15){
            logger.info("单据号长度不能超过15位");
            return;
        }
        if(itemCode.length() > 12){
            logger.info("物料代码长度不能超过12位");
            return;
        }
        if(num > 9999){
            logger.info("数量不能超过4位数");
            return;
        }

        //itemCode = String.format("%012d", itemCode);
        String numStr = String.format("%04d", num);
        sendUdpInfo("03",billNum+itemCode+numStr);
    }
    /**
     * 发送超期复验出库指令指令
     * @param qrCode 二维码
     */
    public static void recheckOutTreasury(String qrCode){
        if(qrCode.length() != 40){
            logger.info("单据号长度最少为40位");
            throw new RuntimeException("二维码喂40位字符");
        }
        sendUdpInfo("04",qrCode);
    }

    /**
     * 发送超期复验合格入库指令指令
     * @param qrCode 二维码
     */
    public static void recheckInTreasury(String qrCode){
        if(qrCode.length() != 40){
            logger.info("单据号长度最少为40位");
            throw new RuntimeException("二维码喂40位字符");
        }
        sendUdpInfo("05",qrCode);
    }
    public static void main(String[] args) {
    	outTreasury("123456789012345","123456789012",12);
	}

}
