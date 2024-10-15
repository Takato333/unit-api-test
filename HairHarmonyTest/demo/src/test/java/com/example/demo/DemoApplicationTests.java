package com.example.demo;

import com.example.demo.service.AuthenticationService;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.AccountResponse;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.modelmapper.ModelMapper;
import com.example.demo.service.TokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DemoApplicationTests {

	@InjectMocks
	private AuthenticationService authenticationService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private Authentication authentication;

	@Mock
	private TokenService tokenService;

	@BeforeEach
	void setUp() {
		// Any additional setup can be done here
		ModelMapper realModelMapper = new ModelMapper();
		when(modelMapper.map(any(Account.class), eq(AccountResponse.class)))
				.thenAnswer(invocation -> realModelMapper.map(invocation.getArgument(0), AccountResponse.class));
	}

	@Test
	void testLoginSuccess() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPhone("0862826912");
		loginRequest.setPassword("password123");

		Account account = new Account();
		account.setPhone("0862826912");
		account.setPassword("password123");

		AccountResponse expectedResponse = new AccountResponse();
		expectedResponse.setPhone("0862826912");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(account);
		when(modelMapper.map(account, AccountResponse.class)).thenReturn(expectedResponse);

		// Act
		AccountResponse result = authenticationService.login(loginRequest);

		// Debug
		System.out.println("Authentication result: " + authentication);
		System.out.println("Account from authentication: " + authentication.getPrincipal());
		System.out.println("Mapped response: " + expectedResponse);
		System.out.println("Actual result: " + result);

		// Assert
		assertNotNull(result);
		assertEquals("0862826912", result.getPhone());
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	@Test
	void testLoginFailure() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPhone("1234567890");
		loginRequest.setPassword("wrongpassword");


		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new RuntimeException("Authentication failed"));

		// Act & Assert
		assertThrows(EntityNotFoundException.class, () -> {
			authenticationService.login(loginRequest);
		});
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		System.out.println("Authentication result: " + authentication);
		System.out.println("Account from authentication: " + authentication.getPrincipal());
	}

	@Test
	void contextLoads() {
		// This test ensures that the Spring context loads correctly
		assertTrue(true);
	}

	@Test
	void testLoginWithEmptyCredentials() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPhone("");
		loginRequest.setPassword("");

		// Act & Assert
		assertThrows(EntityNotFoundException.class, () -> {
			authenticationService.login(loginRequest);
		});
		verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	@Test
	void testLoginWithNullCredentials() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPhone(null);
		loginRequest.setPassword(null);

		// Act & Assert
		assertThrows(EntityNotFoundException.class, () -> {
			authenticationService.login(loginRequest);
		});
		verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	@Test
	void testLoginWithInvalidPhoneFormat() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPhone("invalid-phone");
		loginRequest.setPassword("password123");

		// Act & Assert
		assertThrows(EntityNotFoundException.class, () -> {
			authenticationService.login(loginRequest);
		});
		verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}

	@Test
	void testLoginWithShortPassword() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPhone("0862826912");
		loginRequest.setPassword("short");

		// Act & Assert
		assertThrows(EntityNotFoundException.class, () -> {
			authenticationService.login(loginRequest);
		});
		verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
	}
}
