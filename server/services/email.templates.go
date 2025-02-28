package services

import "fmt"

func GetNewUserEmailTemplate(baseURL string, verificationToken string) string {
	return fmt.Sprintf(`
		<p>
			Hello and Welcome to OpenZooSim! Please verify your account by 
			<a href="%v/api/auth/verify?token=%v">Clicking Here!</a>
		</p>
	`, baseURL, verificationToken)
}

func GetLoginEmailTemplate(baseURL string, verificationToken string) string {
	return fmt.Sprintf(`
		<p>
			Hello! You can login to your account by 
			<a href="%v/api/auth/login-with-email?token=%v">Clicking Here!</a>
		</p>

		<p>
			If you did not request this email, please ignore it.
		</p>
	`, baseURL, verificationToken)
}
