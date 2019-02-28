package com.hqu.modules.UDPInfo;

import com.jeeplus.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer extends Thread {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService executorService;//线程池
    private final int POOL_SIZE=50;//单个CPU线程池大小
    
    public boolean runFlag = true;
	
	
	private void service() throws IOException
	{
		logger.info("开始udp端口数据监控");
		String port = Global.getConfig("LOCAL_SERVER_PORT");
		logger.info("udp端口数据监控port:"+port);
        DatagramSocket socket = new DatagramSocket(Integer.valueOf(port));
        DatagramPacket packet = null;

        // 线程池
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);//CPU数*POOL_SIZE.
        byte[] data = null;
        
        try{
	        
	        while(runFlag)
	        {
	            try{
	            	data = new byte[1024];//创建字节数组，指定接收的数据包的大小
		            packet = new DatagramPacket(data, data.length);
		            socket.receive(packet);//此方法在接收到数据报之前会一直阻塞
		            // 线程池响应请求
		            executorService.execute(new UDPThread(socket, packet));
	            }catch(IOException e){
	            	e.printStackTrace();
	            }
	        }
        }catch(Exception e){
        	logger.error("UDPServer Exception:"+e.getMessage());
        }
    }
    
    public void run()
    {
    	try {
			this.service();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("UDPServer run Exception:"+e.getMessage());
		}
    }
}