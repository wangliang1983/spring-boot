
package cn.linkedcare.springboot.c2s.dto;

/**
 * server的组装的数据
 * 
 * @author wl
 *
 */
public class ServerDto {
	public static final String SPLIT = ":";
	private String connectServer;
	private String password;

	public String getConnectServer() {
		return this.connectServer;
	}

	public String getPassword() {
		return this.password;
	}

	public void setConnectServer(String connectServer) {
		this.connectServer = connectServer;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof ServerDto)) {
			return false;
		} else {
			ServerDto other = (ServerDto) o;
			if (!other.canEqual(this)) {
				return false;
			} else {
				Object this$connectServer = this.getConnectServer();
				Object other$connectServer = other.getConnectServer();
				if (this$connectServer == null) {
					if (other$connectServer != null) {
						return false;
					}
				} else if (!this$connectServer.equals(other$connectServer)) {
					return false;
				}
				Object this$password = this.getPassword();
				Object other$password = other.getPassword();
				if (this$password == null) {
					if (other$password != null) {
						return false;
					}
				} else if (!this$password.equals(other$password)) {
					return false;
				}
				return true;
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof ServerDto;
	}

	public int hashCode() {
		int result = 1;
		Object $connectServer = this.getConnectServer();
		result = result * 59 + ($connectServer == null ? 43 : $connectServer.hashCode());
		Object $password = this.getPassword();
		result = result * 59 + ($password == null ? 43 : $password.hashCode());
		return result;
	}

	public String toString() {
		return "ServerDto(connectServer=" + this.getConnectServer() + ", password=" + this.getPassword() + ")";
	}
}
