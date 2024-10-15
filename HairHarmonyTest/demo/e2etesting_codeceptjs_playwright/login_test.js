Feature('Login');

Scenario('test successful login', ({ I }) => {
    I.amOnPage('/login');  // Visit login page
    I.fillField('username', 'testuser');  // Fill in username
    I.fillField('password', 'password123');  // Fill in password
    I.click('Login');  // Click login button
    I.see('Welcome testuser');  // Verify login success
});

Scenario('test failed login', ({ I }) => {
    I.amOnPage('/login');
    I.fillField('username', 'wronguser');
    I.fillField('password', 'wrongpass');
    I.click('Login');
    I.see('Invalid credentials');  // Verify error message
});