package com.bouguerra.dev.services;

import com.bouguerra.dev.dto.UserRegisterDTO;
import com.bouguerra.dev.exceptions.AvatarProcessingException;
import com.bouguerra.dev.exceptions.EmailAlreadyExistsException;
import com.bouguerra.dev.exceptions.EmailSendingException;
import com.bouguerra.dev.exceptions.InvalidTokenException;
import com.bouguerra.dev.exceptions.UserNotFoundException;
import com.bouguerra.dev.exceptions.UsernameAlreadyExistsException;
import com.bouguerra.dev.mappers.PasswordResetTokenMapper;
import com.bouguerra.dev.dto.PasswordResetDTO;
import com.bouguerra.dev.dto.PasswordResetRequestDTO;
import com.bouguerra.dev.dto.PasswordResetTokenDTO;
import com.bouguerra.dev.dto.UserLoginDTO;
import com.bouguerra.dev.models.Mail;
import com.bouguerra.dev.models.PasswordResetToken;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.PasswordResetTokenRepository;
import com.bouguerra.dev.repositories.UserRepository;
import com.bouguerra.dev.config.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

  
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
     @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
     @Autowired
    private PasswordResetTokenMapper tokenMapper; // I

    @Override
    public User registerUser(UserRegisterDTO registerDTO, MultipartFile avatarFile) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }
        
        User newUser = new User();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        newUser.setRole(registerDTO.getRole());
        
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try (InputStream inputStream = avatarFile.getInputStream()) {
                newUser.setPicture(convertInputStreamToByteArray(inputStream));
            } catch (IOException e) {
                throw new AvatarProcessingException("Failed to process avatar file", e);
            }
        }
        
        return userRepository.save(newUser);
    }

    private byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        }
    }
@Override
public Map<String, Object> loginUser(UserLoginDTO userLoginDTO) {
    User user = userRepository.findByUsername(userLoginDTO.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

    if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

 
    String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

 
    Map<String, Object> response = new HashMap<>();
    response.put("token", token);
    response.put("role", user.getRole().name());
    response.put("user", Map.of("id", user.getId(), "username", user.getUsername())); // Ajout de l'ID et du nom d'utilisateur

    return response;
}

@Override
public User findByUsername(String username) {
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
}

  @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public void sendPasswordResetEmail(PasswordResetRequestDTO requestDTO) {
     
 
        Optional<User> optionalUser = userRepository.findByEmail(requestDTO.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
    
 
            Optional<PasswordResetToken> existingToken = tokenRepository.findByUserId(user.getId());
            if (existingToken.isPresent()) {
                tokenRepository.delete(existingToken.get());
             }
    
 
            String token = UUID.randomUUID().toString();
            PasswordResetTokenDTO tokenDTO = new PasswordResetTokenDTO();
            tokenDTO.setToken(token);
            tokenDTO.setExpiryDate(LocalDateTime.now().plusHours(1)); // Expire dans 1 heure
    
 
            PasswordResetToken resetToken = tokenMapper.toEntity(tokenDTO, user);
            tokenRepository.save(resetToken);
     
 
            String resetUrl = "http://localhost:4200/new-password?token=" + token;
    
 
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(requestDTO.getEmail());
            message.setSubject("Réinitialisation du mot de passe");
            message.setText("Veuillez suivre ce lien pour réinitialiser votre mot de passe : " + resetUrl);
    
            try {
                mailSender.send(message);
             } catch (Exception e) {
                 throw new EmailSendingException("Erreur lors de l'envoi de l'email de réinitialisation de mot de passe");
            }
    
        } else {
             throw new UserNotFoundException("Utilisateur non trouvé avec l'email : " + requestDTO.getEmail());
        }
    }
    


    @Override
 	public void sendEmail(Mail mail) 
	{
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setText(mail.getMailContent());
			mailSender.send(mimeMessageHelper.getMimeMessage());
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}
    @Transactional
    @Override
    public void resetPassword(String token, PasswordResetDTO resetDTO) {
        
    
        if (resetDTO.getPassword() == null || resetDTO.getConfirmPassword() == null) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être nul.");
        }
    
        if (!resetDTO.getPassword().equals(resetDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
        }
    
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Jeton invalide ou expiré."));
    
        User user = resetToken.getUser();
        
     
        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé.");
        }
    
        user.setPassword(passwordEncoder.encode(resetDTO.getPassword()));
    
        userRepository.save(user);
    
     
        tokenRepository.delete(resetToken);
    }
    
    @Override
    public String getUsernameById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return user.getUsername();
    }

    
}
