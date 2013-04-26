/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orca.engine;

import javax.sip.*;
import javax.sip.message.*;
import javax.sip.header.*;
import javax.sip.address.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.*;

import java.text.ParseException;
import java.util.*;

import orca.*;

import orca.tools.Configuration;
import orca.engine.MessengerEngine;
import orca.tools.SdpInfo;
import orca.tools.SdpManager;

/**
 * 
 * @author prajwalan
 */
public class MessengerEngine implements SipListener {
	private Configuration conf;
	private SipURI myURI;
	private SipFactory mySipFactory;
	private SipStack mySipStack;
	private ListeningPoint myListeningPoint;
	private SipProvider mySipProvider;
	private MessageFactory myMessageFactory;
	private HeaderFactory myHeaderFactory;
	private AddressFactory myAddressFactory;
	private Properties myProperties;
	private OrcaGUI myGUI;
	private ContactHeader myContactHeader;
	private ViaHeader myViaHeader;
	private Address fromAddress;
	private Dialog myDialog;
	private ClientTransaction myClientTransaction;
	private ServerTransaction myServerTransaction;
	public int status;
	private String myIP;
	private SdpManager mySdpManager;
	private SdpInfo answerInfo;
	private SdpInfo offerInfo;
	
	private String longitudeString = "NaN";
	private String latitudeString = "NaN";

	private int myPort;
	private String myServer;
	private int myAudioPort;
	private int myVideoPort;
	private int myAudioCodec;
	private int myVideoCodec;

	static final int YES = 0;
	static final int NO = 1;
	static final int SEND_MESSAGE = 2;

	static final int UNREGISTERED = -2;
	static final int REGISTERING = -1;

	public static final int IDLE = 0;
	public static final int WAIT_PROV = 1;
	public static final int WAIT_FINAL = 2;
	public static final int ESTABLISHED = 4;
	public static final int RINGING = 5;
	public static final int WAIT_ACK = 6;

	private long SUBSCRIBE_COUNT = 1L;
	private long PUBLISH_COUNT = 1L;

	private String tupleID = "";
	private String personID = "";

	private Timer subscribeTimerTask = null;
	private Timer locationTimerTask = null;
	private boolean locationTimerTaskRunning = false;

	class MyTimerTask extends TimerTask {
		MessengerEngine myListener;

		public MyTimerTask(MessengerEngine myListener) {
			this.myListener = myListener;
		}

		public void run() {
			try {
				Request myBye = myListener.myDialog.createRequest("BYE");
				myBye.addHeader(myListener.myContactHeader);
				myListener.myClientTransaction = myListener.mySipProvider
						.getNewClientTransaction(myBye);
				myListener.myDialog.sendRequest(myListener.myClientTransaction);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	class SubscribeTimerTask extends TimerTask {
		MessengerEngine engine;

		public SubscribeTimerTask(MessengerEngine engine) {
			this.engine = engine;
		}

		public void run() {
			try {
				engine.myGUI.sendSubscribe(null);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public MessengerEngine(Configuration conf, OrcaGUI GUI, String sipserver)
			throws Exception {
		this.conf = conf;

		myServer = sipserver;
		myGUI = GUI;
		// lagency: myIP = InetAddress.getLocalHost().getHostAddress();
		Socket s = new Socket("198.211.102.189", 80);
		myIP = s.getLocalAddress().getHostAddress(); // this what actually works
		s.close();
		//myIP = "129.241.132.174";
		myPort = conf.sipPort;

		mySdpManager = new SdpManager();

		answerInfo = new SdpInfo();
		offerInfo = new SdpInfo();

		tupleID = conf.userID;
		personID = conf.userID;

		mySipFactory = SipFactory.getInstance();
		mySipFactory.setPathName("gov.nist");
		myProperties = new Properties();
		myProperties.setProperty("javax.sip.STACK_NAME", "myStack");
		mySipStack = mySipFactory.createSipStack(myProperties);
		myMessageFactory = mySipFactory.createMessageFactory();
		myHeaderFactory = mySipFactory.createHeaderFactory();
		myAddressFactory = mySipFactory.createAddressFactory();
		myListeningPoint = mySipStack.createListeningPoint(myIP, myPort, "udp");
		mySipProvider = mySipStack.createSipProvider(myListeningPoint);
		mySipProvider.addSipListener(this);

		myURI = myAddressFactory.createSipURI(myGUI.messengerConfiguration.id,
				myGUI.messengerConfiguration.sipProxyIP);
		myURI.setPort(myGUI.messengerConfiguration.sipProxyPort);

		SipURI contactURI = myAddressFactory
				.createSipURI(myURI.getUser(), myIP);
		Address contactAddress = myAddressFactory.createAddress(contactURI);
		myContactHeader = myHeaderFactory.createContactHeader(contactAddress);
		contactURI.setPort(myPort);

		myViaHeader = myHeaderFactory
				.createViaHeader(myIP, myPort, "udp", null);

		SipURI fromURI = myAddressFactory.createSipURI(
				myGUI.messengerConfiguration.id,
				myGUI.messengerConfiguration.sipProxyIP);
		fromAddress = myAddressFactory.createAddress(fromURI);
		fromURI.setPort(myGUI.messengerConfiguration.sipProxyPort);
		myGUI.setStatusTextArea("Initialized at IP " + myIP + ", port "
				+ myPort);

		Address registrarAddress = myAddressFactory.createAddress("sip:"
				+ myServer);
		Address registerToAddress = fromAddress;
		Address registerFromAddress = fromAddress;

		ToHeader myToHeader = myHeaderFactory.createToHeader(registerToAddress,
				null);
		FromHeader myFromHeader = myHeaderFactory.createFromHeader(
				registerFromAddress, "647554");

		// ArrayList myViaHeaders = new ArrayList();
		ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
		myViaHeaders.add(myViaHeader);

		MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory
				.createMaxForwardsHeader(70);
		CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L,
				"REGISTER");
//		ExpiresHeader myExpiresHeader = myHeaderFactory
//				.createExpiresHeader(60000);
		ArrayList<String> myUserAgentStrings = new ArrayList<String>();
		myUserAgentStrings.add("WangXing's SipClient");
		UserAgentHeader myUserAgentHeader = myHeaderFactory.createUserAgentHeader(myUserAgentStrings);
		
		CallIdHeader myCallIDHeader = mySipProvider.getNewCallId();
		javax.sip.address.URI myRequestURI = registrarAddress.getURI();
		// SipURI myRequestURI = (SipURI) registrarAddress.getURI();
		Request myRegisterRequest = myMessageFactory.createRequest(
				myRequestURI, "REGISTER", myCallIDHeader, myCSeqHeader,
				myFromHeader, myToHeader, myViaHeaders, myMaxForwardsHeader);
		myRegisterRequest.addHeader(myContactHeader);
		myRegisterRequest.addHeader(myUserAgentHeader);
//		myRegisterRequest.addHeader(myExpiresHeader);
		
		myClientTransaction = mySipProvider
				.getNewClientTransaction(myRegisterRequest);
		myClientTransaction.sendRequest();

		myGUI.log("[SENT] " + myRegisterRequest.toString());
		setStatus(REGISTERING);
		
		/*
		 * Get and show location
		 */
		testLocation();
		myGUI.setStatusTextArea("Location: " + longitudeString + ", " + latitudeString);
	}

	public void setOff() {
		try {
			killSubscribeTimerTask();
			// killLocationTimerTask();
			mySipProvider.removeSipListener(this);
			mySipProvider.removeListeningPoint(myListeningPoint);
			mySipStack.deleteListeningPoint(myListeningPoint);
			mySipStack.deleteSipProvider(mySipProvider);
			myListeningPoint = null;
			mySipProvider = null;
			mySipStack = null;

			myGUI.showStatus("");
		} catch (Exception e) {
		}
	}

	public void updateConfiguration(Configuration conf) {
		myPort = conf.sipPort;
	}

	public void processRequest(RequestEvent requestReceivedEvent) {
		Request myRequest = requestReceivedEvent.getRequest();
		String method = myRequest.getMethod();
		myGUI.log("[RECEIVED] " + myRequest.toString());
		if (!method.equals("CANCEL")) {
			myServerTransaction = requestReceivedEvent.getServerTransaction();
		}

		try {
			switch (status) {
			case IDLE:
				if (method.equals("INVITE")) {
					processInviteRequest(myRequest);
				} else if (method.equals("MESSAGE")) {
					processMessageRequestWhenIdle(myRequest);
				} else if (method.equals("NOTIFY")) {
					handleNotify(myRequest);
				}

				break;

			case ESTABLISHED:
				if (method.equals("BYE")) {
					processByeRequest(myRequest);
				} else if (method.equals("MESSAGE")) {
					processMessageRequestWhenEstablished(myRequest);
				} else if (method.equals("NOTIFY")) {
					handleNotify(myRequest);
				}
				break;

			case RINGING:
				if (method.equals("CANCEL")) {
					processCancelRequest(requestReceivedEvent, myRequest);
				}
				break;

			case WAIT_ACK:
				if (method.equals("ACK")) {
					setStatus(ESTABLISHED);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processResponse(ResponseEvent responseReceivedEvent) {
		try {
			Response myResponse = responseReceivedEvent.getResponse();
			myGUI.log("[RECEIVED] " + myResponse.toString());
			ClientTransaction thisClientTransaction = responseReceivedEvent
					.getClientTransaction();
			if (!thisClientTransaction.equals(myClientTransaction)) {
				return;
			}
			int myStatusCode = myResponse.getStatusCode();
			CSeqHeader originalCSeq = (CSeqHeader) myClientTransaction
					.getRequest().getHeader(CSeqHeader.NAME);
			long numseq = originalCSeq.getSeqNumber();

			switch (status) {
			case WAIT_PROV:
				if (myStatusCode < 200) // Provisional (1xx)
				{
					processProvisionalResponse(thisClientTransaction);
				} else if (myStatusCode < 300) // Successful (2xx)
				{
					processSuccessfulResponse(thisClientTransaction,
							myResponse, numseq);
				} else // Redirection (3xx), Request Failure (4xx), Server
						// Failure (5xx) Global Failure (6xx)
				{
					processUnSuccessfulResponse(numseq);
				}
				break;

			case WAIT_FINAL:
				if (myStatusCode < 200) // Provisional (1xx)
				{
					processProvisionalResponse(thisClientTransaction);
				} else if (myStatusCode < 300) // Successful (2xx)
				{
					processSuccessfulResponse(thisClientTransaction,
							myResponse, numseq);

				} else // Redirection (3xx), Request Failure (4xx), Server
						// Failure (5xx) Global Failure (6xx)
				{
					processUnSuccessfulFinalResponse(myStatusCode);
				}
				break;

			case REGISTERING:
				if (myStatusCode == 200) // Successful
				{
					processSuccessfulRegisteringResponse();
				} else {
					setStatus(UNREGISTERED);
				}
				break;
			}
		} catch (Exception excep) {
			excep.printStackTrace();
		}
	}

	public void processTimeout(TimeoutEvent timeoutEvent) {
	}

	public void processTransactionTerminated(TransactionTerminatedEvent tevent) {
	}

	public void processDialogTerminated(DialogTerminatedEvent tevent) {
	}

	public void processIOException(IOExceptionEvent tevent) {
	}

	public void processInviteRequest(Request myRequest)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException, ParseException, SipException,
			InvalidArgumentException {
		if (myServerTransaction == null) {
			myServerTransaction = mySipProvider
					.getNewServerTransaction(myRequest);
		}

		byte[] cont = (byte[]) myRequest.getContent();
		offerInfo = mySdpManager.getSdp(cont);

		answerInfo.IpAddress = myIP;
		answerInfo.aport = myAudioPort;
		answerInfo.aformat = offerInfo.aformat;

		if (offerInfo.vport == -1) {
			answerInfo.vport = -1;
		} else if (myVideoPort == -1) {
			answerInfo.vport = 0;
			answerInfo.vformat = offerInfo.vformat;
		} else {
			answerInfo.vport = myVideoPort;
			answerInfo.vformat = offerInfo.vformat;
		}

		Response myResponse = myMessageFactory.createResponse(180, myRequest);
		myResponse.addHeader(myContactHeader);
		ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
		myToHeader.setTag("454326");
		myServerTransaction.sendResponse(myResponse);
		myDialog = myServerTransaction.getDialog();
		myGUI.log("[SENT] " + myResponse.toString());
		setStatus(RINGING);
	}

	public void processMessageRequestWhenIdle(Request myRequest)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException, ParseException, SipException,
			InvalidArgumentException {
		if (myServerTransaction == null) {
			myServerTransaction = mySipProvider
					.getNewServerTransaction(myRequest);
		}
		Response myResponse = myMessageFactory.createResponse(200, myRequest);
		myResponse.addHeader(myContactHeader);
		ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");

		// FromHeader myFromHeader = (FromHeader) myRequest.getHeader("From");
		// javax.sip.address.Address
		// messageFromAddress=myFromHeader.getAddress();

		byte[] myByteContent = myRequest.getRawContent();
		String myContent = new String(myByteContent);

		myToHeader.setTag("454326");
		myServerTransaction.sendResponse(myResponse);

		myGUI.displayMessage(myContent);
		myGUI.log("[SENT] " + myResponse.toString());
	}

	public void processByeRequest(Request myRequest) throws ParseException,
			SipException, InvalidArgumentException {
		Response myResponse = myMessageFactory.createResponse(200, myRequest);
		myResponse.addHeader(myContactHeader);
		myServerTransaction.sendResponse(myResponse);
		myGUI.log("[SENT] " + myResponse.toString());

		setStatus(IDLE);
	}

	public void processMessageRequestWhenEstablished(Request myRequest)
			throws ParseException, SipException, InvalidArgumentException {
		Response myResponse = myMessageFactory.createResponse(200, myRequest);
		myResponse.addHeader(myContactHeader);

		// FromHeader myFromHeader = (FromHeader) myRequest.getHeader("From");
		// javax.sip.address.Address
		// messageFromAddress=myFromHeader.getAddress();

		byte[] myByteContent = myRequest.getRawContent();
		String myContent = new String(myByteContent);

		myServerTransaction.sendResponse(myResponse);
		myGUI.displayMessage(myContent);
		myGUI.log("[SENT] " + myResponse.toString());
	}

	public void processCancelRequest(RequestEvent requestReceivedEvent,
			Request myRequest) throws ParseException, SipException,
			InvalidArgumentException {
		ServerTransaction myCancelServerTransaction = requestReceivedEvent
				.getServerTransaction();
		Request originalRequest = myServerTransaction.getRequest();
		Response myResponse = myMessageFactory.createResponse(487,
				originalRequest);
		myServerTransaction.sendResponse(myResponse);
		Response myCancelResponse = myMessageFactory.createResponse(200,
				myRequest);
		myCancelServerTransaction.sendResponse(myCancelResponse);

		myGUI.log("[SENT] " + myResponse.toString());
		myGUI.log("[SENT] " + myCancelResponse.toString());

		setStatus(IDLE);
	}

	public void processProvisionalResponse(
			ClientTransaction thisClientTransaction) {
		setStatus(WAIT_FINAL);
		myDialog = thisClientTransaction.getDialog();
	}

	public void processSuccessfulResponse(
			ClientTransaction thisClientTransaction, Response myResponse,
			long numseq) throws InvalidArgumentException, SipException {
		setStatus(ESTABLISHED);

		myDialog = thisClientTransaction.getDialog();
		Request myAck = myDialog.createAck(numseq);
		myAck.addHeader(myContactHeader);
		myDialog.sendAck(myAck);
		myGUI.log("[SENT] " + myAck.toString());
	}

	public void processUnSuccessfulResponse(long numseq)
			throws InvalidArgumentException, SipException {
		setStatus(IDLE);
		Request myAck = myDialog.createAck(numseq);
		myAck.addHeader(myContactHeader);
		myDialog.sendAck(myAck);
		myGUI.log("[SENT] " + myAck.toString());

		myGUI.showErrorMessage("Call to "
				+ ((ToHeader) myAck.getHeader("To")).getAddress() + " Failed");
		myGUI.setStatusTextArea("Call to "
				+ ((ToHeader) myAck.getHeader("To")).getAddress() + " Failed");

	}

	public void processUnSuccessfulFinalResponse(int myStatusCode) {
		if (myStatusCode == 600 || myStatusCode == 486) {
			myGUI.setStatusTextArea("Call Rejected or Destination too busy");
			myGUI.showCallRejectedMessage();
		}

		setStatus(IDLE);

	}

	public void processSuccessfulRegisteringResponse() {
		setStatus(IDLE);

		killSubscribeTimerTask();
		subscribeTimerTask = new Timer();
		subscribeTimerTask.schedule(new SubscribeTimerTask(this),
				(conf.subscribeExpire - 10) * 1000,
				(conf.subscribeExpire - 10) * 1000);

		// updateLocationTimer();

		myGUI.setStatusTextArea("Signed in as \n" + conf.userID + "@"
				+ myGUI.messengerConfiguration.sipProxyIP + ":"
				+ myGUI.messengerConfiguration.sipProxyPort);

	}

	public void userInput(int type, String destination, String message) {
		try {
			switch (status) {
			case IDLE:
				if (type == YES) {
					dial(destination);
				} else if (type == SEND_MESSAGE) {
					sendMessageWhenIdle(destination, message);
				}
				break;

			case WAIT_ACK:
				if (type == NO) {
					sendCancel();
				}
				break;
			case WAIT_PROV:
				if (type == NO) {
					sendCancel();
					break;
				}
			case WAIT_FINAL:
				if (type == NO) {
					sendCancel();
					break;
				}
			case ESTABLISHED:
				if (type == NO) {
					sendBye();
				} else if (type == SEND_MESSAGE) {
					sendMessageWhenEstablished(destination, message);
				}
				break;
			case RINGING:
				if (type == NO) {
					sendBusy();
					break;
				} else if (type == YES) {
					acceptCall();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dial(String destination) {
		try {
			System.out.println("destination = " + destination);
			Address toAddress = myAddressFactory.createAddress(destination);
			ToHeader myToHeader = myHeaderFactory.createToHeader(toAddress,
					null);

			FromHeader myFromHeader = myHeaderFactory.createFromHeader(
					fromAddress, "56438");

			myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort, "udp",
					null);
			// ArrayList myViaHeaders = new ArrayList();
			ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
			myViaHeaders.add(myViaHeader);
			MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory
					.createMaxForwardsHeader(70);
			CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L,
					"INVITE");
			CallIdHeader myCallIDHeader = mySipProvider.getNewCallId();
			javax.sip.address.URI myRequestURI = toAddress.getURI();
			Request myRequest = myMessageFactory.createRequest(myRequestURI,
					"INVITE", myCallIDHeader, myCSeqHeader, myFromHeader,
					myToHeader, myViaHeaders, myMaxForwardsHeader);

			// myRequest.addFirst(myRouteHeader);
			myRequest.addHeader(myContactHeader);

			// HERE GOES SDP AND MEDIA TOOL (SEND OFFER)

			offerInfo = new SdpInfo();
			offerInfo.IpAddress = myIP;
			offerInfo.aport = myAudioPort;
			offerInfo.aformat = myAudioCodec;
			offerInfo.vport = myVideoPort;
			offerInfo.vformat = myVideoCodec;

			ContentTypeHeader contentTypeHeader = myHeaderFactory
					.createContentTypeHeader("application", "sdp");
			byte[] content = mySdpManager.createSdp(offerInfo);
			myRequest.setContent(content, contentTypeHeader);

			// ****************************************************

			myClientTransaction = mySipProvider
					.getNewClientTransaction(myRequest);
			// String bid=myClientTransaction.getBranchId();

			myClientTransaction.sendRequest();
			myDialog = myClientTransaction.getDialog();
			myGUI.log("[SENT] " + myRequest.toString());

			setStatus(WAIT_PROV);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendCancel() {
		try {
			Request myCancelRequest = myClientTransaction.createCancel();
			ClientTransaction myCancelClientTransaction = mySipProvider
					.getNewClientTransaction(myCancelRequest);
			myCancelClientTransaction.sendRequest();
			myGUI.log("[SENT] " + myCancelRequest.toString());

			setStatus(IDLE);
		} catch (Exception e) {

		}
	}

	public void sendBye() {
		try {
			Request myBye = myDialog.createRequest("BYE");
			myBye.addHeader(myContactHeader);
			myClientTransaction = mySipProvider.getNewClientTransaction(myBye);
			myDialog.sendRequest(myClientTransaction);
			myGUI.log("[SENT] " + myBye.toString());

			setStatus(IDLE);
		} catch (Exception e) {

		}

	}

	public void sendMessageWhenEstablished(String destination, String message) {
		try {
			Request myMessage = myDialog.createRequest("MESSAGE");

			myMessage.addHeader(myContactHeader);
			ContentTypeHeader myContentTypeHeader = myHeaderFactory
					.createContentTypeHeader("text", "plain");

			message = destination + ": " + message;
			byte[] contents = message.getBytes();
			myMessage.setContent(contents, myContentTypeHeader);

			myClientTransaction = mySipProvider
					.getNewClientTransaction(myMessage);
			myDialog.sendRequest(myClientTransaction);

			myGUI.displayMessage(message);
			myGUI.log("[SENT] " + myMessage.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessageWhenIdle(String destination, String message) {
		try {
			Address toAddress = myAddressFactory.createAddress(destination);
			ToHeader myToHeader = myHeaderFactory.createToHeader(toAddress,
					null);

			FromHeader myFromHeader = myHeaderFactory.createFromHeader(
					fromAddress, "685354");

			ViaHeader myViaHeader = myHeaderFactory.createViaHeader(myIP,
					myPort, "udp", null);
			// ArrayList myViaHeaders = new ArrayList();
			ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
			myViaHeaders.add(myViaHeader);
			MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory
					.createMaxForwardsHeader(70);
			CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L,
					"MESSAGE");
			CallIdHeader myCallIDHeader = mySipProvider.getNewCallId();
			javax.sip.address.URI myRequestURI = toAddress.getURI();
			Request myRequest = myMessageFactory.createRequest(myRequestURI,
					"MESSAGE", myCallIDHeader, myCSeqHeader, myFromHeader,
					myToHeader, myViaHeaders, myMaxForwardsHeader);
			// myRequest.addFirst(myRouteHeader);
			myRequest.addHeader(myContactHeader);

			ContentTypeHeader myContentTypeHeader = myHeaderFactory
					.createContentTypeHeader("text", "plain");

			byte[] contents = message.getBytes();
			myRequest.setContent(contents, myContentTypeHeader);

			myClientTransaction = mySipProvider
					.getNewClientTransaction(myRequest);
			// String bid=myClientTransaction.getBranchId();

			myClientTransaction.sendRequest();

			String name = fromAddress.getDisplayName();

			myGUI.displayMessage(name + ":  " + message);

			myGUI.log("[SENT] " + myRequest.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendBusy() {
		try {
			Request originalRequest = myServerTransaction.getRequest();
			// 600 -> Busy EVERYWHERE
			// 486 -> Busy Here
			Response myResponse = myMessageFactory.createResponse(600,
					originalRequest);
			myServerTransaction.sendResponse(myResponse);
			myGUI.log("[SENT] " + myResponse.toString());

			setStatus(IDLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void acceptCall() {
		try {
			Request originalRequest = myServerTransaction.getRequest();
			Response myResponse = myMessageFactory.createResponse(200,
					originalRequest);
			ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
			myToHeader.setTag("454326");
			myResponse.addHeader(myContactHeader);

			// SEND ANSWER SDP

			ContentTypeHeader contentTypeHeader = myHeaderFactory
					.createContentTypeHeader("application", "sdp");
			byte[] content = mySdpManager.createSdp(answerInfo);
			myResponse.setContent(content, contentTypeHeader);

			myServerTransaction.sendResponse(myResponse);
			myDialog = myServerTransaction.getDialog();

			new Timer().schedule(new MyTimerTask(this), 500000);
			myGUI.log("[SENT] " + myResponse.toString());
			setStatus(WAIT_ACK);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void killSubscribeTimerTask() {
		if (subscribeTimerTask != null) {
			subscribeTimerTask.cancel();
			subscribeTimerTask = null;
		}

	}
	
	public void sendSubscribe(String subscriberAddress) {
		/*
		 * subscriberAddress is a string with the format bob@11.111.11.111:5060,
		 * So you need to make SipURI out of it.
		 * 
		 * Use myHeaderFactory to create any necessary headers Use
		 * myMessageFactory to create any Request messages Finally create a new
		 * clienttransaction and send the request.
		 */

		if (subscriberAddress == null) {
			System.out
					.println("[Debug] MessengerEngine#sendSubscribe(null) invoked");
			return;
		} else {
			System.out.println("[Debug] MessengerEngine#sendSubscribe("
					+ subscriberAddress + ") invoked");
		}

		/*
		 * parse subscriberAddress
		 */
		String destinationID = null;
		String destinationProxyIP = null;
		String destinationPortStr = null;
		
		destinationID = subscriberAddress.split("@")[0];
		destinationProxyIP = subscriberAddress.split("@")[1].split(":")[0];
		if (subscriberAddress.split("@")[1].split(":").length > 1) {
			destinationPortStr = subscriberAddress.split("@")[1].split(":")[1];
		}
		
		/*
		 * Construct Request and Send it
		 */
		try {
			SipURI destinationURI = myAddressFactory.createSipURI(
						destinationID, 
						destinationProxyIP);
			if (destinationPortStr != null) {
				destinationURI.setPort(Integer.parseInt(destinationPortStr));
			}
	
			Address destinationAddress = myAddressFactory.createAddress(destinationURI);
			
			//Create an array list for viaHeader
			ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
			
			//Create viaViaHeader from HeaderFactory object
			ViaHeader myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,
						"udp", null);
			
			// Add your viaHeader to the array list
			viaHeaders.add(myViaHeader);
			
			// Create MaxForwardsHeader using HeaderFactory object
			MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory.createMaxForwardsHeader(70);
			
			// Make CallID Header
			CallIdHeader myCallIdHeader = mySipProvider.getNewCallId();
			
			// Create CSeqHeader using HeaderFactory object
			CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L, "SUBSCRIBE");
			
			// Create From Address and From Header
			Address fromAddress = myAddressFactory.createAddress(myURI);
			FromHeader myFromHeader = myHeaderFactory.createFromHeader(fromAddress, "56438");
	
			// Create To Header
			ToHeader myToHeader = myHeaderFactory.createToHeader(destinationAddress, null);
			
			// Create SUBSCRIBE request
			Request myRequest = myMessageFactory.createRequest(destinationURI,
					"SUBSCRIBE", myCallIdHeader, myCSeqHeader, myFromHeader,
					myToHeader, viaHeaders, myMaxForwardsHeader);
			
			// Create ContactHeader and Add it
			// Create: nothing, because it is already constructed: MessengerEngine#myContactHeader
			myRequest.addHeader(myContactHeader);
			
			// Create Expire Header and add it
			myRequest.addFirst(myHeaderFactory.createExpiresHeader(300));
			
			// Create Event Header and add it
			myRequest.addFirst(myHeaderFactory.createEventHeader("Presence"));
			
			// Send the message
			ClientTransaction myClientTransaction = mySipProvider.getNewClientTransaction(myRequest);
			myClientTransaction.sendRequest();
			myGUI.log("[SENT] " + myRequest.toString());
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		}
	}

	public void sendPublish(String statusid, String status) {
		// TODO: Fill up this method ...
		/*
		 * Use myHeaderFactory to create any necessary headers Use
		 * myMessageFactory to create any Request messages You must call
		 * appropriate method to set the XML content in the Message Make sure
		 * you also set the content type. Finally create a new clienttransaction
		 * and send the request.
		 */
		
		if (statusid == null || status == null) {
			System.out
					.println("[Debug] MessengerEngine#sendPublish(contains null value) invoked");
			return;
		} else {
			System.out.println("[Debug] MessengerEngine#sendPublish("
					+ statusid + ", " + status + ") invoked");
		}
		
		/*
		 * Construct Request and Send it
		 */
		try {
			SipURI destinationURI = myURI;
	
			Address destinationAddress = myAddressFactory.createAddress(destinationURI);
			
			//Create an array list for viaHeader
			ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
			
			//Create viaViaHeader from HeaderFactory object
			ViaHeader myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,
						"udp", null);
			
			// Add your viaHeader to the array list
			viaHeaders.add(myViaHeader);
			
			// Create MaxForwardsHeader using HeaderFactory object
			MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory.createMaxForwardsHeader(70);
			
			// Make CallID Header
			CallIdHeader myCallIdHeader = mySipProvider.getNewCallId();
			
			// Create CSeqHeader using HeaderFactory object
			CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L, "PUBLISH");
			
			// Create From Address and From Header
			Address fromAddress = myAddressFactory.createAddress(myURI);
			FromHeader myFromHeader = myHeaderFactory.createFromHeader(fromAddress, "56438");
	
			// Create To Header
			ToHeader myToHeader = myHeaderFactory.createToHeader(destinationAddress, null);
			
			// Create SUBSCRIBE request
			Request myRequest = myMessageFactory.createRequest(myURI,
					"PUBLISH", myCallIdHeader, myCSeqHeader, myFromHeader,
					myToHeader, viaHeaders, myMaxForwardsHeader);
			
			// Create ContactHeader and Add it
			// Create: nothing, because it is already constructed: MessengerEngine#myContactHeader
			myRequest.addHeader(myContactHeader);
			
			// Create Expire Header and add it
			myRequest.addHeader(myHeaderFactory.createExpiresHeader(300));
			
			// Create Event Header and add it
			myRequest.addHeader(myHeaderFactory.createEventHeader("Presence"));
			
			// Create Allow Header and add it
			myRequest.addHeader(myHeaderFactory.createAllowHeader(
					"INVITE, ACK, CANCEL, OPTIONS, BYE, " +
					"REFER, NOTIFY, MESSAGE, SUBSCRIBE, INFO"));
			
			// Create ContentTypeHeader
			ContentTypeHeader myContentTypeHeader = 
					myHeaderFactory.createContentTypeHeader("application", "pidf+xml");
			
			// Create Content
			String content = null;
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version='1.0' encoding='UTF-8'?>" +
					"<presence xmlns='urn:ietf:params:xml:ns:pidf' " +
					"xmlns:dm='urn:ietf:params:xml:ns:pidf:data-model' " +
					"xmlns:rpid='urn:ietf:params:xml:ns:pidf:rpid' " +
					"xmlns:c='urn:ietf:params:xml:ns:pidf:cipid' entity='");
			String pureAddress = fromAddress.toString().replace("<", "").replace(">", "").split(":5060")[0];
			sb.append(pureAddress);
			sb.append("'>");
			sb.append("<tuple id='");
			sb.append(myGUI.messengerConfiguration.id);
			sb.append("'>");
			sb.append("<status>");
			sb.append("<basic>");
			sb.append("open");
			sb.append("</basic>");
//			if (myGUI.messengerConfiguration.sendLocationWithPresence) {
//				sb.append("<geoLongitude>" + longitudeString + "</geoLongitude>");
//				sb.append("<geoLatitude>" + latitudeString + "</geoLatitude>");
//			}
			sb.append("</status>");
			sb.append("</tuple><dm:person id='");
			sb.append(myGUI.messengerConfiguration.id);
			sb.append("'>");
			sb.append("<rpid:activities><rpid:");
			sb.append(statusid);
			sb.append("/>");
			sb.append("</rpid:activities><dm:note>");
			sb.append(statusid);
			sb.append("</dm:note>");
			sb.append("</dm:person></presence>");
			content = sb.toString();
			
			// Add content and content type
			myRequest.setContent(content, myContentTypeHeader);
			
			// Send the message
			ClientTransaction myClientTransaction = mySipProvider.getNewClientTransaction(myRequest);
			myClientTransaction.sendRequest();
			myGUI.log("[SENT] " + myRequest.toString());
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		}

	}

	private void handleNotify(Request myRequest) {
		// TODO: Fill up this method ...
		/*
		 * Use myMessageFactory for creating Request or Response messages Use
		 * myServerTransaction to send Responses
		 * 
		 * Call following method to update the GUI's buddy list with new
		 * presence status myGUI.updateListWithStatus(<entity>,
		 * <presencestatus>);
		 */
		
		try {
			/*
			 * Send OK to server
			 */
			Response myResponse = myMessageFactory.createResponse(200, myRequest);
			myResponse.addHeader(myContactHeader);
			myServerTransaction.sendResponse(myResponse);
			myGUI.log("[SENT] " + myResponse.toString());
			
			/*
			 * Fetch location information
			 */
			String xmlStr = new String(myRequest.getRawContent());
			String destinationSipAddress = 
					xmlStr.split("\"><tuple")[0]
							.split("entity=\"")[1];
			System.out.println("Header From: " + destinationSipAddress);	// DEBUG
			System.out.println("xmlStr: ");	// DEBUG
			System.out.println(xmlStr);		// DEBUG
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(xmlStr));
	        Document doc =  builder.parse(is);
	        String status = doc.getElementsByTagName("basic").item(0).getFirstChild().getNodeValue();
	        
	        // Show location
			if (myGUI.messengerConfiguration.receiveLocationWithPresence) {
				String longitudeStr = doc.getElementsByTagName("geoLongitude").item(0).getFirstChild().getNodeValue();
		        String latitudeStr = doc.getElementsByTagName("geoLatitude").item(0).getFirstChild().getNodeValue();
		        
		        // update GUI
		        myGUI.updateListWithStatus(destinationSipAddress, 
		        		status + " (" + longitudeStr + ", " + latitudeStr + ")");
			} else {
				// update GUI
				myGUI.updateListWithStatus(destinationSipAddress, status);
			}

		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStatus(int status) {
		this.status = status;

		switch (this.status) {
		case UNREGISTERED:
			myGUI.showStatus("Status: UNREGISTERED");
			break;
		case REGISTERING:
			myGUI.showStatus("Status: REGISTERING");
			break;
		case IDLE:
			myGUI.showStatus("Status: IDLE");
			myGUI.toggleChatView(false);
			myGUI.enableDisableWhileCalling(true);
			break;
		case WAIT_PROV:
			myGUI.showStatus("Status: TRYING TO CONNECT");
			break;
		case WAIT_FINAL:
			myGUI.showStatus("Status: ALERTING");
			break;
		case ESTABLISHED:
			myGUI.showStatus("Status: ESTABLISHED");
			myGUI.toggleChatView(true);
			break;
		case RINGING:
			myGUI.showStatus("Status: RINGING");
			myGUI.enableDisableWhileRinging();
			break;
		case WAIT_ACK:
			myGUI.showStatus("Status: WAITING ACK");
			break;
		}

		myGUI.setDialCaption();
	}

//	public void testLocation() {
//		// Create an instance of HttpClient.
//		HttpClient client = new HttpClient();
//
//		// Create a method instance.
//		GetMethod method = new GetMethod("http://www.item.ntnu.no/fag/ttm4130/locate/");
//
//		// Provide custom retry handler is necessary
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//				new DefaultHttpMethodRetryHandler(3, false));
//
//		try {
//			// Execute the method.
//			int statusCode = client.executeMethod(method);
//
//			if (statusCode != HttpStatus.SC_OK) {
//				System.err.println("Method failed: " + method.getStatusLine());
//			}
//
//			// Read the response body.
//			byte[] responseBody = method.getResponseBody();
//
//			// Deal with the response.
//			// Use caution: ensure correct character encoding and is not binary
//			// data
//			String xmlStr = new String(responseBody);
//			
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//	        DocumentBuilder builder = factory.newDocumentBuilder();
//	        InputSource is = new InputSource(new StringReader(xmlStr));
//	        Document doc =  builder.parse(is);
//	        
//	        String longitudeStr = doc.getElementsByTagName("geoLongitude").item(0).getFirstChild().getNodeValue();
//	        String latitudeStr = doc.getElementsByTagName("geoLatitude").item(0).getFirstChild().getNodeValue();
//	        
//	        // set instance variable
//	        longitudeString = longitudeStr;
//	        latitudeString = latitudeStr;
//	        
//		} catch (HttpException e) {
//			System.err.println("Fatal protocol violation: " + e.getMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.err.println("Fatal transport error: " + e.getMessage());
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} finally {
//			// Release the connection.
//			method.releaseConnection();
//		}
//	}
	
	// Mock Method to speed up development
	public void testLocation() {
		longitudeString = "99.99";
        latitudeString = "88.88";
	}

}
