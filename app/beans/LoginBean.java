package beans;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class LoginBean implements Serializable {

	@Required
	public String email;

	@Required
	public String password;

	@Override
	public String toString() {
		return "LoginBean [email=" + email + ", password=" + password + "]";
	}

}
