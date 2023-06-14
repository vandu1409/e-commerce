package com.vandu.controller.site;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vandu.dto.ChangePasswordDto;
import com.vandu.dto.EmailDto;
import com.vandu.dto.RegisterDto;
import com.vandu.dto.UserDto;
import com.vandu.enums.TokenType;
import com.vandu.exception.PasswordMismatchException;
import com.vandu.exception.TokenException;
import com.vandu.exception.UserNotFoundException;
import com.vandu.exception.UsernameAlreadyExistsException;
import com.vandu.helper.AuthenticationType;
import com.vandu.helper.TypeRole;
import com.vandu.helper.UserHelper;
import com.vandu.model.Role;
import com.vandu.model.Token;
import com.vandu.model.User;
import com.vandu.model.UserRole;
import com.vandu.service.EmailService;
import com.vandu.service.RoleService;
import com.vandu.service.TokenService;
import com.vandu.service.UserRoleService;
import com.vandu.service.UserService;

@RestController
public class RegisterApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private TokenService tokenService;

	@PostMapping("register")
	public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

		try {

			if (!registerDto.getPassword().equalsIgnoreCase(registerDto.getRepassword())) {
				throw new PasswordMismatchException("Mật khẩu không trùng khớp!");
			}

			if (userService.findByUsername(registerDto.getUsername()).orElse(null) != null) {
				throw new UsernameAlreadyExistsException("Tên đăng nhập đã tồn tại!");
			}

			User user = new User();
			BeanUtils.copyProperties(registerDto, user);
			user.setAuthenticationType(AuthenticationType.LOCAL);
			user.setCreateDate(new Date());
			User DBUser = userService.save(user);

			UserRole userRole = new UserRole();
			Role role = roleService.findByName(TypeRole.USER).get();

			userRole.setUser(DBUser);
			userRole.setRole(role);

			userRoleService.save(userRole);

			UUID uuid = UUID.randomUUID();
			Token token = new Token();
			token.setToken(uuid.toString());
			token.setCreateDate(new Date());
			token.setExpiredDate(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
			token.setTokenType(TokenType.REGISTER);
			token.setUser(DBUser);

			tokenService.save(token);

			EmailDto emailDto = new EmailDto();
			emailDto.setSubject("Xác thực tài khoản!");
			emailDto.setRecipient(DBUser.getEmail());

			emailService.sendMailRegister(emailDto, token);

			return ResponseEntity.ok("Đăng ký thành công!");

		} catch (PasswordMismatchException e) {
			throw e;
		} catch (UsernameAlreadyExistsException ex) {
			throw ex;
		}

	}

	@PostMapping("forget-password")
	public ResponseEntity<?> forgetPassword(@RequestBody UserDto userDto) {
		System.err.println("Đac anhfhsfhshfbhdsbfhbshbfsfjsb");
		try {
			User user = userService.findByEmailAndUsernameAndAuthenticationType(userDto.getEmail(),
					userDto.getUsername(), AuthenticationType.LOCAL).orElse(null);

			if (user == null) {
				throw new UserNotFoundException("Không tìm thấy tài khoản!");
			}
			System.err.println("dddd");
			UUID uuid = UUID.randomUUID();

			Token token = new Token();
			token.setToken(uuid.toString());
			token.setUser(user);
			token.setCreateDate(new Date());
			token.setExpiredDate(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
			token.setTokenType(TokenType.FORGETPASSWORD);

			tokenService.save(token);
			EmailDto emailDto = new EmailDto();
			emailDto.setSubject("Quên mật khẩu!");

			emailDto.setRecipient(user.getEmail());
			emailService.sendMailForgetPassword(emailDto, token);

			return ResponseEntity.ok("Success");
		} catch (UserNotFoundException e) {
			throw e;
		}

	}

	@PostMapping("change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws TokenException {
		try {
			Token token = tokenService.findByTokenAndTokenType(changePasswordDto.getToken(), TokenType.FORGETPASSWORD)
					.orElseThrow(() -> new TokenException("Không tìm thấy tài khoản!Vui lòng kiểm tra lại"));

			System.err.println(token.getExpiredDate()+ " sjdhfdf"+token.getToken());
			LocalDate now = LocalDate.now();
			LocalDate expiredDate = token.getExpiredDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (expiredDate.isAfter(now)) {
				
				throw new TokenException("Đã hết thời gian đổi mật khẩu!Vui lòng kiểm tra lại!");
			} else {
				if (!changePasswordDto.getPassword().equalsIgnoreCase(changePasswordDto.getRepassword())) {
					throw new PasswordMismatchException("Mật khẩu không trùng khớp!");
				}

				User user = token.getUser();

				if (user != null) {
					user.setPassword(changePasswordDto.getPassword());
					userService.save(user);
					tokenService.delete(token);
				}
				
			}

			return ResponseEntity.ok("Success");
		} catch (TokenException e) {
			throw e;
		}catch(PasswordMismatchException e) {
			throw e;
		}

	}
}
