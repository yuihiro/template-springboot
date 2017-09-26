package anyclick.wips.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -6502596312985405760L;

	public ServiceException(String message) {
		super(message);
	}
}
