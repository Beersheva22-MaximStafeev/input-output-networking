package telran.employees;

import telran.net.TcpClient;

public class CompanyNetImplTcp extends CompanyNetImplAbstract {

	private static final long serialVersionUID = 1L;

	public CompanyNetImplTcp(String hostname, int port) throws Exception {
		setNetworkClient(new TcpClient(hostname, port));
	}
}
