package telran.employees;

import telran.net.UdpClient;

public class CompanyNetImplUdp extends CompanyNetImplAbstract {
	private static final long serialVersionUID = 1L;

	public CompanyNetImplUdp(String hostname, int port) throws Exception {
		setNetworkClient(new UdpClient(hostname, port));
	}
}
