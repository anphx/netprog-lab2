package se.ltu.netprog.javaprog.sma;

public class DateClient {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: DateClient host port");
		}
		String host = args[0];
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch(Exception e) {
			port = DateService.DATE_SERVICE_PORT;
		}
		MessageClient conn;
		try {
			conn = new MessageClient(host,port);
		} catch(Exception e) {
			System.err.println(e);
			return;
		}

		sendSomeMsg(conn);
		sendAnotherMsg(conn);

//		Message m = new Message();
////		m.setType(DateService.DATE_SERVICE_MESSAGE);
////		m.setParam("person","george");
////		m = conn.call(m);
////		System.out.println("Date " + m.getParam("date"));
////		m.setType(75);
////		m = conn.call(m);
////		System.out.println("Bad reply " + m);
		// AnP: 4th package sent
		conn.disconnect();
	}

	static void sendAnotherMsg(MessageClient conn) {
		Message m = new Message();
		m.setType(DateService.DATE_SERVICE_MESSAGE);

		// AnP: 3rd package sent
		m = conn.call(m);
		System.out.println("Message after call" + m);
		System.out.println("Date " + m.getParam("date"));
	}

	static void sendSomeMsg(MessageClient conn) {
		Message m = new Message();
		m.setType(DateService.DATE_SERVICE_MESSAGE);
		m.setParam("person","george");

		// AnP: 1st package sent
		m = conn.call(m);
		System.out.println("Date " + m.getParam("date"));
		m.setType(75);

		// AnP: 2nd package sent
		m = conn.call(m);
		System.out.println("Bad reply " + m);
	}
}