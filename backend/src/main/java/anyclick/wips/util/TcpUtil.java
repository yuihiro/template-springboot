package anyclick.wips.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpUtil {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public final int MAX_LENGTH = 1024;///<한번 통신할 때 최대 길이, TCP는 항상 1024를 읽는 것은 아니다.
	public final int HEADER_LENGTH = 8;///<Header 길이

	/* Header 내용 */
	protected int length; ///<패킷의 전체 길이
	protected short type; ///<패킷의 메인 타입
	protected byte subType; ///<패킷의 서브 타입
	protected byte ack; ///<패킷의 request(1), ACK(2), NAK(4), must ACK(8)

	protected Socket socket;

	public void connect(String ip, String port) throws IOException {
		connect(ip, port, 3000);
	}

	public void connect(String ip, String port, int timeout) throws NumberFormatException, IOException {
		socket = new Socket();
		socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), timeout);
	}

	public void close() throws IOException, NullPointerException {
		socket.close();
	}

	public void setHeader(int length, int type, int subType, int ack) {
		this.length = length;
		this.type = (short) type;
		this.subType = (byte) subType;
		this.ack = (byte) ack;
	}

	public void setHeader(byte[] receiveData) {
		this.length = getIntFromByte(receiveData, 0);
		this.type = getShortFromByte(receiveData, 4);
		this.subType = receiveData[6];
		this.ack = receiveData[7];
	}

	public String getPacketData(String data) throws Exception {
		DataOutputStream dos = null;

		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}

		String returnValue = "";///<returnValue : 결과값

		try {
			/* send */
			///if : Header 외에 데이타가 있다면 
			if (data != null && data.length() > 0) {
				byte[] bData = data.getBytes();

				sendPacket(dos, bData);
			} else ///else : 패킷이 Header로만 구성되어 있다면
			{
				sendPacket(dos, null);
			}
			/* send */

			returnValue = receivePacket();
		} catch (Exception e) {
		}

		return returnValue;
	}

	public byte[] getRawPacket(String data) throws Exception {
		DataOutputStream dos = null;
		byte[] returnValue = null;

		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {

		}

		try {
			/* send */
			///if : Header 외에 데이타가 있다면 
			if (data != null && data.length() > 0) {
				byte[] bData = data.getBytes();

				sendPacket(dos, bData);
			} else ///else : 패킷이 Header로만 구성되어 있다면
			{
				sendPacket(dos, null);
			}

			returnValue = receiveRawPacket();

		} catch (Exception e) {

		}
		return returnValue;
	}

	public String getPacketDataWithFailLog(String data) throws Exception {
		DataOutputStream dos = null;

		try {
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
		}

		String returnValue = null;///<returnValue : 결과값

		try {
			/* send */
			///if : Header 외에 데이타가 있다면 
			if (data != null && data.length() > 0) {
				byte[] bData = data.getBytes();

				sendPacket(dos, bData);
			} else ///else : 패킷이 Header로만 구성되어 있다면
			{
				sendPacket(dos, null);
			}
			/* send */

			returnValue = receivePacketWithFailLog();
		} catch (Exception e) {

		}
		return returnValue;
	}

	public void sendPacket(DataOutputStream dos, byte[] data) throws IOException {
		///보낼 패킷을 sendBuffer에 저장한다.
		ByteBuffer sendBuffer = null;
		if (data != null && data.length > 0)
			sendBuffer = ByteBuffer.allocate(HEADER_LENGTH + data.length);
		else
			sendBuffer = ByteBuffer.allocate(HEADER_LENGTH);
		sendBuffer.put(convertIntToByte(this.length));
		sendBuffer.put(convertShortToByte(this.type));
		sendBuffer.put(this.subType);
		sendBuffer.put(this.ack);
		if (data != null && data.length > 0)
			sendBuffer.put(data);

		sendBuffer.position(0);

		dos.write(sendBuffer.array(), 0, sendBuffer.array().length);
		dos.flush();
	}

	public void sendPacket(DataOutputStream dos) throws IOException {
		///보낼 패킷을 sendBuffer에 저장한다.
		ByteBuffer sendBuffer = null;
		sendBuffer = ByteBuffer.allocate(HEADER_LENGTH);
		sendBuffer.put(convertIntToByte(this.length));
		sendBuffer.put(convertShortToByte(this.type));
		sendBuffer.put(this.subType);
		sendBuffer.put(this.ack);

		sendBuffer.position(0);

		dos.write(sendBuffer.array(), 0, sendBuffer.array().length);
		dos.flush();
	}

	public String receivePacket() throws Exception {
		String returnValue = ""; // 응답 패킷

		DataInputStream dis = new DataInputStream(socket.getInputStream());

		///전문 검사를 위해 Header만 읽는다.
		byte[] bHeaderData = new byte[HEADER_LENGTH];
		dis.read(bHeaderData);

		///if : cookie, seq, type 값이 보낸 패킷과 동일하면 동일 패킷으로 보고 다음 프로세스를 진행한다.
		if (this.type == getShortFromByte(bHeaderData, 4)) {
			///받은 전문의 헤더를 셋팅한다.
			setHeader(bHeaderData);

			///if : 전문 헤더에 있는 길이와 실제 패킷 길이가 같고, ack 가 ack로 정상적으로 들어오면 데이타를 가져온다.
			if (this.ack == 2) {
				int valueLength = this.length - HEADER_LENGTH;///<valueLength : 패킷 중 실제 data 길이

				if (valueLength != 0) {
					byte[] bTotalData = new byte[valueLength];///<bTotalData 전체 data buffer
					byte[] bTempData = new byte[MAX_LENGTH];///<bTempData MAX_LENGTH길이 만큰의 temp buffer

					int nRead = -1;///stream에서 읽은 length
					int totalRead = 0;

					//stream에서 값을 읽어올 수 있을 때까지 읽는다.
					while ((nRead = dis.read(bTempData, 0, MAX_LENGTH)) != -1) {
						System.arraycopy(bTempData, 0, bTotalData, totalRead, nRead);
						totalRead += nRead;

						if (totalRead == valueLength) // header의 length 만큼 읽으면 빠져나감.
							break;
					}

					returnValue = new String(bTotalData);
				} else {
					returnValue = "";
				}
			} else {
			}
		} else {
		}

		return returnValue;
	}

	public byte[] receiveRawPacket() throws Exception {
		byte[] returnValue = null; // 응답 패킷

		DataInputStream dis = new DataInputStream(socket.getInputStream());

		///전문 검사를 위해 Header만 읽는다.
		byte[] bHeaderData = new byte[HEADER_LENGTH];
		dis.read(bHeaderData);

		///if : cookie, seq, type 값이 보낸 패킷과 동일하면 동일 패킷으로 보고 다음 프로세스를 진행한다.
		if (this.type == getShortFromByte(bHeaderData, 4)) {
			///받은 전문의 헤더를 셋팅한다.
			setHeader(bHeaderData);

			///if : 전문 헤더에 있는 길이와 실제 패킷 길이가 같고, ack 가 ack로 정상적으로 들어오면 데이타를 가져온다.
			if (this.ack == 2) {
				int valueLength = this.length - HEADER_LENGTH;///<valueLength : 패킷 중 실제 data 길이

				if (valueLength != 0) {
					byte[] bTotalData = new byte[valueLength];///<bTotalData 전체 data buffer
					byte[] bTempData = new byte[MAX_LENGTH];///<bTempData MAX_LENGTH길이 만큰의 temp buffer

					int nRead = -1;///stream에서 읽은 length
					int totalRead = 0;

					//stream에서 값을 읽어올 수 있을 때까지 읽는다.
					while ((nRead = dis.read(bTempData, 0, MAX_LENGTH)) != -1) {
						System.arraycopy(bTempData, 0, bTotalData, totalRead, nRead);
						totalRead += nRead;

						if (totalRead == valueLength) // header의 length 만큼 읽으면 빠져나감.
							break;
					}

					returnValue = bTotalData;
				} else {
					returnValue = null;
				}
			} else {
			}
		} else {
		}

		return returnValue;
	}

	public String receivePacketWithFailLog() throws Exception {
		String returnValue = ""; // 응답 패킷

		DataInputStream dis = new DataInputStream(socket.getInputStream());

		///전문 검사를 위해 Header만 읽는다.
		byte[] bHeaderData = new byte[HEADER_LENGTH];
		dis.read(bHeaderData);

		///if : cookie, seq, type 값이 보낸 패킷과 동일하면 동일 패킷으로 보고 다음 프로세스를 진행한다.
		if (this.type == getShortFromByte(bHeaderData, 4)) {
			///받은 전문의 헤더를 셋팅한다.
			setHeader(bHeaderData);

			int valueLength = this.length - HEADER_LENGTH;///<valueLength : 패킷 중 실제 data 길이

			if (valueLength != 0) {
				byte[] bTotalData = new byte[valueLength];///<bTotalData 전체 data buffer
				byte[] bTempData = new byte[MAX_LENGTH];///<bTempData MAX_LENGTH길이 만큰의 temp buffer

				int nRead = -1;///stream에서 읽은 length
				int totalRead = 0;

				//stream에서 값을 읽어올 수 있을 때까지 읽는다.
				while ((nRead = dis.read(bTempData, 0, MAX_LENGTH)) != -1) {
					System.arraycopy(bTempData, 0, bTotalData, totalRead, nRead);
					totalRead += nRead;

					if (totalRead == valueLength) // header의 length 만큼 읽으면 빠져나감.
						break;
				}

				returnValue = new String(bTotalData);

				if (this.ack != 2) {

					throw new Exception("응답이 ACK가 아닙니다.");
				}
			} else {
				returnValue = "";

				if (this.ack != 2) {
					throw new Exception("응답이 ACK가 아닙니다.");
				}
			}
		} else {

			throw new Exception("규약에 어긋난 전문입니다.");
		}

		return returnValue;
	}

	public boolean canConnection() throws Exception {
		DataInputStream dis = null;

		try {
			dis = new DataInputStream(socket.getInputStream());

			byte[] receiveData = new byte[HEADER_LENGTH];
			dis.read(receiveData);

			///받은 전문의 헤더를 셋팅한다.
			setHeader(receiveData);
		} catch (IOException e) {
		}

		///if : 전문 헤더에 있는 길이와 실제 패킷 길이가 같고, ack 가 ack로 정상적으로 들어오면 데이타를 가져온다.
		if (this.ack == 2) {
			return true;
		} else {
			return false;
		}
	}

	public static short getShortFromByte(byte[] value, int idx) {
		return convertByteToShort(getByteFromTotal(value, idx, 2));
	}

	public static int getIntFromByte(byte[] value, int idx) {
		return convertByteToInt(getByteFromTotal(value, idx, 4));
	}

	public static byte[] getByteFromTotal(byte[] value, int idx, int length) {
		byte[] buffer = new byte[length];

		for (int i = 0; i < length; i++) {
			buffer[i] = value[idx + i];
		}

		return buffer;
	}

	public static byte[] convertShortToByte(short value) {
		byte[] buffer = new byte[2];

		buffer[0] = (byte) (value & 0xff);
		buffer[1] = (byte) ((value >> 8) & 0xff);

		return buffer;
	}

	public static byte[] convertIntToByte(int value) {
		byte[] buffer = new byte[4];

		buffer[0] = (byte) (value & 0xff);
		buffer[1] = (byte) ((value >> 8) & 0xff);
		buffer[2] = (byte) ((value >> 16) & 0xff);
		buffer[3] = (byte) ((value >> 24) & 0xff);

		return buffer;
	}

	public static short convertByteToShort(byte[] value) {
		short temp;

		temp = (short) (value[0] & 0x00ff);
		temp |= (short) ((value[1] << 8) & 0xff00);

		return temp;
	}

	public static int convertByteToInt(byte[] value) {
		int temp;

		temp = (int) (value[0] & 0x000000ff);
		temp |= (int) ((value[1] << 8) & 0x0000ff00);
		temp |= (int) ((value[2] << 16) & 0x00ff0000);
		temp |= (int) ((value[3] << 24) & 0xff000000);

		return temp;
	}

}
