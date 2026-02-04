package com.fib.uqcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import cn.hutool.core.util.ObjectUtil;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

public class MailUtil {
	public static boolean send(MailInfo mailInfo) {
		boolean retFlag = true;
		Properties props = new Properties();
		// 设置发送邮件的邮件服务器的属性
		props.put("mail.smtp.host", mailInfo.host());
		// 需要经过授权，也就是有户名和密码的校验
		if (ObjectUtil.isEmpty(mailInfo.validate())) {
			props.put("mail.smtp.auth", "false");
		} else {
			props.put("mail,smtp,auth", mailInfo.validate());
		}

		// 1.创建Session
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);
		// 2.通过Session获取Transport
		Transport transport;
		try {
			transport = session.getTransport("smtp");
			// 3连接邮箱服务器
			transport.connect(mailInfo.host(), mailInfo.username(), mailInfo.password());
			// 4.封装邮件
			Message message = createAttachMail(session, mailInfo);
			// 5.发送邮件
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			retFlag = false;
		}
		return retFlag;
	}

	private static Message createAttachMail(Session session, MailInfo mailInfo) throws FileNotFoundException, UnsupportedEncodingException {
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(mailInfo.fromAddress()));

			InternetAddress[] sendTo = new InternetAddress[mailInfo.sendAddress().length];
			if (sendTo.length == 0) {
				throw new AddressException("未设置收件人地址");
			}
			for (int i = 0, len = mailInfo.sendAddress().length; i < len; i++) {
				sendTo[i] = new InternetAddress(mailInfo.sendAddress()[i]);
			}
			message.setRecipients(Message.RecipientType.TO, sendTo);

			if (mailInfo.ccAddress().length > 0) {
				sendTo = new InternetAddress[mailInfo.ccAddress().length];
				for (int i = 0, len = mailInfo.ccAddress().length; i < len; i++) {
					sendTo[i] = new InternetAddress(mailInfo.ccAddress()[i]);
				}
				message.setRecipients(Message.RecipientType.CC, sendTo);
			}

			message.setSubject(mailInfo.subject());
			message.setSentDate(mailInfo.sentDate());

			Multipart multiparty = new MimeMultipart();
			BodyPart contentParty = new MimeBodyPart();

			contentParty.setContent("<meta http-equiv=Content-Type content=text/html;charset=UTP-8>" + mailInfo.content(), "text/html;charset=UTF-8");
			multiparty.addBodyPart(contentParty);

			// 邮件附件
			if (ObjectUtil.isNotEmpty(mailInfo.filePath())) {
				for (int i = 0; i < mailInfo.filePath().length; i++) {
					BodyPart messageBodyPart = new MimeBodyPart();
					String fpath = mailInfo.filePath()[i];
					if (ObjectUtil.isNotEmpty(fpath)) {

						if (!new File(fpath).exists()) {
							throw new FileNotFoundException("附件不存在");
						}
						DataSource source = new FileDataSource(fpath);
						DataHandler dh = new DataHandler(source);
						messageBodyPart.setDataHandler(dh);

						String fileName = fpath.substring(fpath.lastIndexOf(File.separator) + 1);
						messageBodyPart.setFileName(MimeUtility.encodeText(fileName));
						multiparty.addBodyPart(messageBodyPart);
					}
				}
			}

			message.setContent(multiparty);
			message.saveChanges();
			return message;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
