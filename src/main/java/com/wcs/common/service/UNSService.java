package com.wcs.common.service;

import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.model.P;

@Stateless
public class UNSService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * web.xml需要配置 <!-- WAS8的jndi的使用,activeMQ,JMS -->
     * 
     * <resource-ref> <res-ref-name>jms/UNSINBOX</res-ref-name> <res-type>javax.jms.Queue</res-type>
     * <res-auth>Container</res-auth> <res-sharing-scope>Shareable</res-sharing-scope> </resource-ref>
     * 
     * <resource-ref> <res-ref-name>jms/ConnectionFactory</res-ref-name> <res-type>javax.jms.ConnectionFactory</res-type>
     * <res-auth>Container</res-auth> <res-sharing-scope>Shareable</res-sharing-scope> </resource-ref>
     */
    
    /**
     * 发送Email的方法体.
     * 
     * @param receiverList 用户信息集合,List<UNSVO类型.使用的VO是UNSVO,List每个对象存储用户的帐号,Email地址,手机号码.
     * @param subject String类型,Email的标题信息.
     * @param body String类型,Email的正文.
     * @return 返回一组String的汉字信息,分为成功和失败两种情况.如果成功,就返回"成功",使用者equals后可以进行发送站内信息和存储数据.如果失败,返回的汉字长居为失败的原因.如果需要可抛出到前台.
     * @throws NamingException 
     * @throws JMSException 
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean sendEmails(P sendTo, String subject, String body) throws JMSException, NamingException {
        String pernr = "";
        String type = "1";
        ResourceBundle rbParam = ResourceBundle.getBundle("uns");
        String cfJNDI = rbParam.getString("WAS8_cfJNDI");
        String queueJNDI = rbParam.getString("WAS8_queueJNDI");
        String sys = rbParam.getString("JMS_sysParam");
        String key = rbParam.getString("JMS_keyParam");
        String aux = rbParam.getString("JMS_auxParam");
        String email = sendTo.getEmail();
        // 循环并读取所有的账户,email,电话
        // msg是要发送的消息.
        if (null != email && !"".equals(email)) {
            String msg = "[{\"sys\":\"sysParam\", \"key\":\"keyParam\", \"type\":\"typeParam\", \"email\":\"emailParam\", \"telno\":\"telnoParam\", \"pernr\":\"pernrParam\", \"subject\":\"subjectParam\", \"body\":\"bodyParam\", \"aux\":\"auxParam\"}]";
            msg = msg.replace("sysParam", sys);
            msg = msg.replace("keyParam", key);
            msg = msg.replace("typeParam", type);
            msg = msg.replace("emailParam",email);
            msg = msg.replace("pernrParam", pernr);
            msg = msg.replace("subjectParam", subject);
            msg = msg.replace("bodyParam", body);
            msg = msg.replace("auxParam", aux);
            logger.info("最终发送邮件的字符串是:" + msg);
            logger.info("开始发送Email...........................");
            return this.getConnectFactory(msg, cfJNDI, queueJNDI);
        }
        return true;
    }

    /**
     * 下面是要从WAS8上获取连接工厂的JNDI和队列地址的JNDI. 然后就是发送信息到服务器端.
     * 
     * @author ZhaoQian
     * @throws JMSException 
     * @throws NamingException 
     */
    private boolean getConnectFactory(String msg, String cfJNDI, String queueJNDI) throws JMSException, NamingException {
    	Connection connection = null;
        MessageProducer producer = null;
        Queue queue = null;
        try {
            InitialContext ic = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) ic.lookup("java:comp/env/jms/ConnectionFactory"); //连接工厂，JMS 用它创建连接 
//           Queue queue = (Queue) ic.lookup("java:comp/env/jms/UNSINBOX");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); //AUTO_ACKNOWLEDGE session将自动地确认收到一则消息
            queue = session.createQueue("unsQueue");
            producer = session.createProducer(queue); 
            TextMessage message = session.createTextMessage();
            message.setText(msg);
            producer.send(message);
            this.close(connection, producer);
        } catch (JMSException e) {
            this.close(connection, producer);
            throw new JMSException(e.getMessage());
        } catch (NamingException e) {
            this.close(connection, producer);
            throw new NamingException(e.getMessage());
        }
        return true;
    }

    private void close(Connection connection, MessageProducer producer) throws JMSException {
        logger.info("关闭时连接对象是：connection = "+connection+" , producer = "+producer);
        try {
            if (null != producer) {
                producer.close();
                logger.info("UNSService.getConnectFactory execute....关闭produce");
            }
            if (null != connection) {
                connection.close();
                logger.info("UNSService.getConnectFactory execute....关闭connection");
            }
        } catch (JMSException e) {
            throw new JMSException(e.getMessage());
        }
    }
}
