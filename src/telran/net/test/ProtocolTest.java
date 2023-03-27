package telran.net.test;

import telran.net.*;

public class ProtocolTest implements Protocol {

	@Override
	public Response getResponse(Request request) {
		Response res = new Response(ResponseCode.OK, null);
		switch (request.type) {
			case "reverse":
				try {
					res.data = new StringBuffer(((String)request.data)).reverse().toString();
				} catch (Exception e) {
					res.code = ResponseCode.WRONG_DATA;
				}
				break;
			case "length":
				try {
					res.data = ((String)request.data).length(); 
				} catch (Exception e) {
					res.code = ResponseCode.WRONG_DATA;
				}
				break;
			default: 
				res.code = ResponseCode.WRONG_REQUEST;
				res.data = request.type + " wrong request";
		}
		return res;
	}

}
