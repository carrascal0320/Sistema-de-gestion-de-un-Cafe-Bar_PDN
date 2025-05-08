package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  public void sendEmail(String to, String subject, String body) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);

      mailSender.send(message);
      System.out.println("Correo enviado con éxito a " + to); // Log para verificar
    } catch (Exception e) {
      System.err.println("Error al enviar correo a " + to + ": " + e.getMessage());
      e.printStackTrace();
    }
  }
}
