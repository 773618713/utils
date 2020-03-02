package com.scy.ssm.websocket;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * webSocket 测试
 * 需要导入tomcat的lib目录下的jar
 *
 */
@ServerEndpoint("/websocket/{username}")
public class WebSocketTest {
    /**
     * 在线客户端数量
     */
    private static int onlineCount = 0;
    /**
     * 客户端
     */
    private static Map<String, WebSocketTest> clients = new ConcurrentHashMap<>();
    private Session session;
    private String username;

    /**
     * 新建连接
     * @param username
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {

        this.username = username;
        this.session = session;

        addOnlineCount();
        clients.put(username, this);
        System.out.println("已连接");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++){
                    try {
                        System.out.println("输入发送的消息：");
                        clients.get("username").sendMessageAll("来自服务器的消息"+i);
                        Thread.sleep(2000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    /**
     * 客户端连接关闭
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
    }

    /**
     * 接收到的消息
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        System.out.println("接收到的信息"+message);
        JSONObject jsonTo = JSONObject.parseObject(message);
        if (!jsonTo.get("To").equals("All")){
            sendMessageTo("给一个人", jsonTo.get("To").toString());
        }else{
            sendMessageAll("给所有人");
        }
    }

    /**
     * 异常处理
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 发送给某个客户端
     * @param message
     * @param To
     * @throws IOException
     */
    public void sendMessageTo(String message, String To) throws IOException {
        // session.getBasicRemote().sendText(message);
        //session.getAsyncRemote().sendText(message);
        for (WebSocketTest item : clients.values()) {
            if (item.username.equals(To) )
                item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 发送给所有客户端
     * @param message
     * @throws IOException
     */
    public void sendMessageAll(String message) throws IOException {
        for (WebSocketTest item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }


    /**
     * 获取在线客户端数量
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线客户端数量加一
     */
    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    /**
     * 在线客户端数量减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }

    /**
     * 获取所有客户端
     * @return
     */
    public static synchronized Map<String, WebSocketTest> getClients() {
        return clients;
    }

    public static void main(String[] args) {
        try {
            System.out.println("输入发送的消息：");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = br.readLine();

            System.out.println("输入发送的消息："+str);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
