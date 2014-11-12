package beans.android;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class LoginBean implements Serializable {

	public String email;


	public String password;

	@Override
	public String toString() {
		return "LoginBean [email=" + this.email + ", password=" + this.password + "]";
	}

}
