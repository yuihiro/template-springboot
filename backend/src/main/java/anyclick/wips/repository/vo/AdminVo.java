package anyclick.wips.repository.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AdminVo {

	public String id;
	public String pwd;
	public String name;
	public String email;
	public String ip;
	public String login_type;
}
