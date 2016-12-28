package com.device.inspect.common.util.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.device.inspect.Application;
import com.device.inspect.common.model.device.Device;
import com.device.inspect.common.model.device.DeviceInspect;
import com.device.inspect.common.model.device.InspectData;
import com.device.inspect.common.model.device.InspectType;
import com.device.inspect.common.repository.device.DeviceInspectRepository;
import com.device.inspect.common.repository.device.DeviceRepository;
import com.device.inspect.common.repository.device.InspectDataRepository;
import com.device.inspect.common.repository.device.InspectTypeRepository;
import com.device.inspect.common.restful.RestResponse;
import com.device.inspect.common.restful.device.RestInspectData;
import com.device.inspect.common.util.transefer.ByteAndHex;
import com.device.inspect.common.util.transefer.StringDate;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/23.
 */

public class SocketServerThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(SocketServerThread.class);

    private Socket sock;
//    PrintWriter out = null;
//    DataOutputStream dos = null;
    DataOutputStream outData = null;
    DataInputStream dins = null;
    public SocketServerThread(Socket s)
    {
        this.sock =s;
    }

    public void run(){
        try {

            InputStream ins = sock.getInputStream();
            dins = new DataInputStream(ins);
            //服务端解包过程
            boolean flag = false;
            while(!flag) {
//                out = new PrintWriter(sock.getOutputStream());
                outData = new DataOutputStream(sock.getOutputStream());
                byte[] data = new byte[512];
                dins.read(data);
                String result = ByteAndHex.bytesToHexString(data);
                LOGGER.info(Thread.currentThread().getName()+"发来的内容是:" + result);
//                String flagString = result.substring(2,4);
                String response = "";
                if (!result.startsWith("ef")){
                    flag = true;
                }else {
                    response = get(result);
                }
                if (null==response){
                    flag = true;
                    break;
                }

                byte[] bytes = ByteAndHex.hexStringToBytes(response);
                outData.write(bytes,0,bytes.length);
                outData.flush();

                flag = true;
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                dins.close();
                outData.close();
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String get(String message) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://localhost:8998/api/rest/socket/insert/data?result="+
        message);

        client.executeMethod(method);
        //打印服务器返回的状态
        System.out.println(method.getStatusLine());
        InputStream stream = method.getResponseBodyAsStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuffer buf = new StringBuffer();
        String line;
        while (null != (line = br.readLine())) {
            buf.append(line).append("\n");
        }
        System.out.println(buf.toString());
        String response = buf.toString();
        JSONObject jsonObject = JSON.parseObject(response);
        String result = null;
        if (null!=jsonObject.get("data"))
            result = jsonObject.get("data").toString();

        //释放连接
        method.releaseConnection();
        LOGGER.info(Thread.currentThread().getName()+"当前返回数据:" + result);
        return result;
    }


}
