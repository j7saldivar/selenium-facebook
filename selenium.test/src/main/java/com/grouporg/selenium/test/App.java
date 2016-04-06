package com.grouporg.selenium.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {

	public static void sendEmail() {

		Properties props = new Properties();

		InputStream inputStream = App.class
				.getResourceAsStream("resources.properties");

		try {

			props.load(inputStream);
			props.put("mail.smtp.host", props.getProperty("mail.smtp.host"));
			props.put("mail.smtp.socketFactory.port",
					props.getProperty("mail.smtp.socketFactory.port"));
			props.put("mail.smtp.socketFactory.class",
					props.getProperty("mail.smtp.socketFactory.class"));
			props.put("mail.smtp.auth", props.getProperty("mail.smtp.auth"));
			props.put("mail.smtp.port", props.getProperty("mail.smtp.port"));

			final String user = props.getProperty("mail.smtp.user");
			final String password = props.getProperty("mail.smtp.password");

			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(user, password);
						}
					});

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(props
						.getProperty("mail.smtp.from")));
				message.setRecipients(Message.RecipientType.TO, InternetAddress
						.parse(props.getProperty("mail.smtp.to")));
				message.setSubject("Testing Subject");
				message.setText("Hello," + "\n\n Email sent using java mail.");

				Transport.send(message);

				System.out.println("Done");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}

		} catch (IOException e1) {
			System.out.println("Check properties");
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) throws InterruptedException {

		JFrame frame = new JFrame("Username");
		String username = JOptionPane.showInputDialog(frame, "Username: ",
				"Username", JOptionPane.PLAIN_MESSAGE);
		String password = "";

		JPanel panel = new JPanel();
		JLabel label = new JLabel("Password: ");
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);

		String[] options = new String[] { "OK", "Cancel" };

		int option = JOptionPane.showOptionDialog(null, panel, "Password",
				JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]);

		if (option == 0) {
			char[] pwd = pass.getPassword();
			password = new String(pwd);
		}

		System.setProperty("webdriver.chrome.driver",
				"C://Users/jorge.saldivar/Downloads/chromedriver_win32/chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.get("https://www.facebook.com/");
		Thread.sleep(5000);
		WebElement userBox = driver.findElement(By.name("email"));
		userBox.sendKeys(username);
		WebElement passBox = driver.findElement(By.name("pass"));
		passBox.sendKeys(password);
		passBox.submit();
		Thread.sleep(5000);
		driver.quit();

		sendEmail();

	}
}